package com.loiko.alex.contractservice.controller;

import com.loiko.alex.contractservice.model.Contract;
import com.loiko.alex.contractservice.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/car/{carId}/contract")
public class ContractController {

    @Autowired
    private ContractService service;

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> findContract(@PathVariable("carId") String carId,
                                                 @PathVariable("contractId") String contractId) {
        Contract contract = service.findByContractIdAndCarId(contractId, carId, "discovery");
        contract.add(
                linkTo(methodOn(ContractController.class).findContract(contract.getCarId(), contract.getCarId())).withSelfRel(),
                linkTo(methodOn(ContractController.class).saveContract(contract)).withRel("saveContract"),
                linkTo(methodOn(ContractController.class).updateContract(contract)).withRel("updateContract"),
                linkTo(methodOn(ContractController.class).deleteContract(contract.getCarId())).withRel("deleteContract"));
        return ResponseEntity.ok(contract);
    }

    @GetMapping("/{contractId}/{clientType}")
    public Contract getContractWithClient(@PathVariable("contractId") String contractId,
                                          @PathVariable("carId") String carId,
                                          @PathVariable("clientType") String clientType) {
        return service.findByContractIdAndCarId(contractId, carId, clientType);
    }

    @PostMapping
    public ResponseEntity<Contract> saveContract(@RequestBody Contract request) {
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping
    public ResponseEntity<Contract> updateContract(@RequestBody Contract request) {
        return ResponseEntity.ok(service.update(request));
    }

    @DeleteMapping("/{contractId}")
    public ResponseEntity<String> deleteContract(@PathVariable("contractId") String carId) {
        return ResponseEntity.ok(service.delete(carId));
    }
}