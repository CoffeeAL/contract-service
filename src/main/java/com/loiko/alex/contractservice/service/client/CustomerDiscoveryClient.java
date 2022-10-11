package com.loiko.alex.contractservice.service.client;

import com.loiko.alex.contractservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CustomerDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public Customer getCustomer(String customerId) {
        RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");
        if (instances.isEmpty()) {
            return null;
        }
        String uri = String.format("%s/v1/customer/%s", instances.get(0).getUri().toString(), customerId);
        ResponseEntity<Customer> exchange = template.exchange(uri, HttpMethod.GET, null, Customer.class, customerId);
        return exchange.getBody();
    }
}