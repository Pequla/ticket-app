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
        String url = "http://82.208.22.205:7000/api/flight/{id}";
        return this.restTemplate.getForObject(url, FlightModel.class, id);
    }
}
