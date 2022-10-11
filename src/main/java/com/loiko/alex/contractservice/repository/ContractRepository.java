package com.loiko.alex.contractservice.repository;

import com.loiko.alex.contractservice.model.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends CrudRepository<Contract, String> {

    List<Contract> getContractsByCarId(String carId);

    Contract getContractByContractIdAndCarId(String contractId, String carId);
}