package com.pequla.ticket.service;

import com.pequla.ticket.model.FlightModel;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebService {
    private final RestTemplate restTemplate;

    public WebService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public FlightModel getFlightById(Integer id) {
        String url = "https://flight.pequla.com/api/flight/{id}";
        return this.restTemplate.getForObject(url, FlightModel.class, id);
    }
}
