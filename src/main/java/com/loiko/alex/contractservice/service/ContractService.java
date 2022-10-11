package com.loiko.alex.contractservice.service;

import com.loiko.alex.contractservice.model.Contract;

import java.util.List;

public interface ContractService {

    Contract findById(String id);

    List<Contract> getContractsByCarId(String carId);

    Contract findByContractIdAndCarId(String contractId, String carId, String clientType);

    Contract save(Contract contract);

    Contract update(Contract contract);

    String delete(String id);
}