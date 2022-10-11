package com.loiko.alex.contractservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
public class Car extends RepresentationModel<Car> {

    String carId;
    String model;
    String year;
    String color;
    String engineVolume;
    String comment;
}