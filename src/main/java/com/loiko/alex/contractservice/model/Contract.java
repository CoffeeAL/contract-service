package com.loiko.alex.contractservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contract extends RepresentationModel<Contract> {

    @Id
    @Column(name = "contract_id", nullable = false)
    private String contractId;

    @Column(name = "car_id", nullable = false)
    private String carId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Column(name = "comment")
    private String comment;

    @Transient
    private String carModel;

    @Transient
    private String carYear;

    @Transient
    private String carColor;

    @Transient
    private String carEngineVolume;

    @Transient
    private String firstName;

    @Transient
    private String lastName;

    @Transient
    private String email;

    public Contract withComment(String comment) {
        setComment(comment);
        return this;
    }
}