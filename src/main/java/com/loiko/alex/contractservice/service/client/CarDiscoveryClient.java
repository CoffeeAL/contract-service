package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CarDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public Car getCar(String carId) {
        RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("car-service");
        if (instances.isEmpty()) {
            return null;
        }
        String uri = String.format("%s/v1/car/%s", instances.get(0).getUri().toString(), carId);
        ResponseEntity<Car> exchange = template.exchange(uri, HttpMethod.GET, null, Car.class, carId);
        return exchange.getBody();
    }
}