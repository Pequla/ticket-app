package com.pequla.ticket.repository;

import com.pequla.ticket.entity.AppUser;
import com.pequla.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findByIdAndUser(Integer id, AppUser user);

    void deleteByIdAndUser(Integer id, AppUser user);

    List<Ticket> findAllByUserAndUsedAtIsNull(AppUser user);

    List<Ticket> findAllByUserAndUsedAtIsNotNull(AppUser user);
}
