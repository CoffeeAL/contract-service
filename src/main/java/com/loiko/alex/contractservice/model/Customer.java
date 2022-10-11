package com.loiko.alex.contractservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
public class Customer extends RepresentationModel<Customer> {

    String customerId;
    String firstName;
    String lastName;
    String email;
    String comment;
}