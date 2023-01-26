package com.pequla.ticket.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pequla.ticket.entity.AppUser;
import com.pequla.ticket.error.UserRejectedException;
import com.pequla.ticket.model.CreateModel;
import com.pequla.ticket.model.LoginModel;
import com.pequla.ticket.model.TokenModel;
import com.pequla.ticket.model.UserModel;
import com.pequla.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final MailService service;
    private final ObjectMapper mapper;

    @Autowired
    public UserService(UserRepository repository, MailService service) {
        this.repository = repository;
        this.service = service;
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public HashMap<String, String> login(LoginModel model) throws JsonProcessingException {
        AppUser user = getUserByEmail(model.getEmail());
        if (user.getVerifyToken() != null) {
            throw new UserRejectedException(UserRejectedException.Type.NOT_VERIFIED_EMAIL);
        }

        user.setLastLoginAt(LocalDateTime.now());
        repository.save(user);

        BCrypt.Result result = BCrypt.verifyer().verify(model.getPassword().toCharArray(), user.getPassword());
        if (!result.verified) {
            throw new UserRejectedException(UserRejectedException.Type.BAD_PASSWORD);
        }

        String token = mapper.writeValueAsString(TokenModel.builder()
                .issuedAt(LocalDateTime.now())
                .user(makeModel(user))
                .build());

        HashMap<String, String> map = new HashMap<>();
        map.put("token", Base64Utils.encodeToString(token.getBytes()));
        return map;
    }

    public UserModel getSelfUser(String token) throws IOException {
        return makeModel(getUserFromToken(token));
    }

    public UserModel updateUser(String token, UserModel model) throws IOException {
        AppUser user = getUserFromToken(token);
        user.setName(model.getName());
        user.setUpdatedAt(LocalDateTime.now());
        service.send(user.getEmail(),
                "You have successfully updated your profile",
                "Profile details updated");
        return makeModel(repository.save(user));
    }

    public UserModel createUser(CreateModel model) {
        String hashed = BCrypt.withDefaults().hashToString(12, model.getPassword().toCharArray());
        AppUser newUser = new AppUser();
        newUser.setEmail(model.getEmail());
        newUser.setPassword(hashed);
        newUser.setName(model.getName());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setVerifyToken(UUID.randomUUID().toString());

        AppUser savedUser = repository.save(newUser);
        service.send(savedUser.getEmail(),
                "Follow this link to verify email: https://ticketapp.pequla.com/action/verify?token=" + savedUser.getVerifyToken(),
                "Verify email");
        return makeModel(savedUser);
    }

    public UserModel verifyUser(String token) {
        Optional<AppUser> optional = repository.findByVerifyToken(token);
        if (optional.isEmpty()) {
            throw new UserRejectedException(UserRejectedException.Type.NOT_FOUND);
        }
        AppUser user = optional.get();
        user.setVerifyToken(null);
        return makeModel(repository.save(user));
    }

    private AppUser getUserByEmail(String email) {
        Optional<AppUser> optional = repository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new UserRejectedException(UserRejectedException.Type.NOT_FOUND);
        }
        return optional.get();
    }

    private TokenModel verifyToken(String token) throws IOException {
        TokenModel model = mapper.readValue(Base64Utils.decodeFromString(token), TokenModel.class);
        if (model.getIssuedAt().plusDays(30).isBefore(LocalDateTime.now())) {
            throw new UserRejectedException(UserRejectedException.Type.TOKEN_EXPIRED);
        }
        return model;
    }

    public AppUser getUserFromToken(String token) throws IOException {
        return getUserByEmail(verifyToken(token).getUser().getEmail());
    }

    private UserModel makeModel(AppUser user) {
        return UserModel.builder()
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
