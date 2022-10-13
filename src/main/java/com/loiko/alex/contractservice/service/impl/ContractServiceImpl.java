package com.loiko.alex.contractservice.service.impl;

import com.loiko.alex.contractservice.config.ServiceConfig;
import com.loiko.alex.contractservice.model.Car;
import com.loiko.alex.contractservice.model.Contract;
import com.loiko.alex.contractservice.repository.ContractRepository;
import com.loiko.alex.contractservice.service.ContractService;
import com.loiko.alex.contractservice.service.client.*;
import com.loiko.alex.contractservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class ContractServiceImpl implements ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Autowired
    private MessageSource messages;

    @Autowired
    private ContractRepository repository;

    @Autowired
    ServiceConfig config;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    CarRestTemplateClient carRestTemplateClient;

    @Autowired
    CarDiscoveryClient carDiscoveryClient;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @Autowired
    CustomerRestTemplate customerRestTemplate;

    @Autowired
    CustomerDiscoveryClient customerDiscoveryClient;

    @Override
    public Contract findById(String id) {
        Optional<Contract> maybeContract = repository.findById(id);
        if (maybeContract.isEmpty()) {
            throw new IllegalArgumentException(String.format(messages.getMessage("contract.search.error.message", null, null), id));
        }
        return maybeContract.get();
    }

    @Override
    public Contract findByContractIdAndCarId(String contractId, String carId, String clientType) {
        Contract contract = repository.getContractByContractIdAndCarId(contractId, carId);
        if (contract == null) {
            throw new IllegalArgumentException(String.format(messages.getMessage("contract.search.error.message", null, null), contractId, carId));
        }
        Car car = retrieveCarInfo(carId, clientType);
        if (car != null) {
            contract.setCarModel(car.getModel());
            contract.setCarColor(car.getColor());
            contract.setCarEngineVolume(car.getEngineVolume());
            contract.setCarYear(car.getYear());
        }
        return contract.withComment(config.getProperty());
    }

    private Car retrieveCarInfo(String carId, String clientType) {
        Car car = null;
        switch (clientType) {
            case "discovery":
                car = carDiscoveryClient.getCar(carId);
                break;
                //TODO FIX status 503 reading CarFeignClient#getCar(String)
            case "feign":
                car = carFeignClient.getCar(carId);
                break;
                //TODO FIX I/O error on GET request for \"http://car-service/v1/car/{carId}\": car-service; nested exception is java.net.UnknownHostException: car-service
            case "rest":
            default:
                car = carRestTemplateClient.getCar(carId);
                break;
        }
        return car;
    }

    @Override
    public Contract save(Contract contract) {
        contract.setContractId(UUID.randomUUID().toString());
        return update(contract);
    }

    @Override
    public Contract update(Contract contract) {
        repository.save(contract);
        return contract.withComment(config.getProperty());
    }

    @Override
    public String delete(String id) {
        var contract = new Contract();
        contract.setContractId(id);
        repository.delete(contract);
        return String.format(messages.getMessage("contract.delete.message", null, null), id);
    }

    @Override
    @CircuitBreaker(name = "contractService", fallbackMethod = "buildFallbackContractList")
    @RateLimiter(name = "contractService", fallbackMethod = "buildFallbackContractList")
    @Retry(name = "retryContractService", fallbackMethod = "buildFallbackContractList")
    @Bulkhead(name = "bulkheadContractService", type= Bulkhead.Type.THREADPOOL, fallbackMethod = "buildFallbackContractList")
    public List<Contract> getContractsByCarId(String carId) throws TimeoutException {
        logger.debug("getContractsByCarId Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return repository.getContractsByCarId(carId);
    }

    @SuppressWarnings("unused")
    private List<Contract> buildFallbackContractList(String carId, Throwable t){
        List<Contract> fallbackList = new ArrayList<>();
        Contract contract = new Contract();
        contract.setContractId("0000000-00-00000");
        contract.setCarId(carId);
        contract.setCarModel("Sorry no contract information currently available");
        fallbackList.add(contract);
        return fallbackList;
    }

    private void randomlyRunLong() throws TimeoutException{
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException{
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    @CircuitBreaker(name = "carService")
    private Car getCar(String carId) {
        return carRestTemplateClient.getCar(carId);
    }
}