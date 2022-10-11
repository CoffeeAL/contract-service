package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerRestTemplate {

    @Autowired
    RestTemplate restTemplate;

    public Customer getCustomer(String customerId) {
        ResponseEntity<Customer> exchange = restTemplate.exchange("http://customer-service/v1/customer/{customerId}", HttpMethod.GET, null, Customer.class, customerId);
        return exchange.getBody();
    }
}