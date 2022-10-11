package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Car;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarRestTemplateClient {

    public Car getCar(String carId) {
        var restTemplate = new RestTemplate();
        ResponseEntity<Car> exchange = restTemplate.exchange("http://car-service/v1/car/{carId}", HttpMethod.GET, null, Car.class, carId);
        return exchange.getBody();
    }
}