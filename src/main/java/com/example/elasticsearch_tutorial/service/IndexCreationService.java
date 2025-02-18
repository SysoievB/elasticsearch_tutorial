package com.example.elasticsearch_tutorial.service;

import com.example.elasticsearch_tutorial.model.CarModel;
import com.example.elasticsearch_tutorial.model.OwnerModel;
import com.example.elasticsearch_tutorial.repo.CarRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexCreationService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final CarRepository repository;

    @PostConstruct
    public void init() {
        createIndex();
        populateData();
    }

    @PreDestroy
    public void destroy() {
        elasticsearchOperations.indexOps(CarModel.class).delete();
        log.info("----------------- Index 'carindex' removed! -----------------");
    }

    private void createIndex() {
        IndexCoordinates index = IndexCoordinates.of("carindex");

        if (elasticsearchOperations.indexOps(index).exists()) {
            elasticsearchOperations.indexOps(index).delete();
            log.info("----------------- Existing index 'carindex' deleted -----------------");
        }

        elasticsearchOperations.indexOps(CarModel.class).create();
        log.info("----------------- Index 'carindex' created! -----------------");
    }

    private void populateData() {
        List<CarModel> cars = List.of(
                new CarModel(UUID.randomUUID().toString(), "Model S", 2022, "Tesla", List.of(new OwnerModel("Alice", 30, true))),
                new CarModel(UUID.randomUUID().toString(), "Model 3", 2023, "Tesla", List.of(new OwnerModel("Bob", 25, false))),
                new CarModel(UUID.randomUUID().toString(), "Model X", 2021, "Tesla", List.of(new OwnerModel("Charlie", 40, true))),
                new CarModel(UUID.randomUUID().toString(), "Mustang", 2019, "Ford", List.of(new OwnerModel("Bob", 45, false))),
                new CarModel(UUID.randomUUID().toString(), "Civic", 2018, "Honda", List.of(new OwnerModel("Charlie", 27, true))),
                new CarModel(UUID.randomUUID().toString(), "Camry", 2021, "Toyota", List.of(new OwnerModel("David", 34, true))),
                new CarModel(UUID.randomUUID().toString(), "Corolla", 2020, "Toyota", List.of(new OwnerModel("Emily", 28, false))),
                new CarModel(UUID.randomUUID().toString(), "RAV4", 2022, "Toyota", List.of(new OwnerModel("Frank", 45, true))),
                new CarModel(UUID.randomUUID().toString(), "Highlander", 2023, "Toyota", List.of(new OwnerModel("Grace", 50, true))),
                new CarModel(UUID.randomUUID().toString(), "Prius", 2019, "Toyota", List.of(new OwnerModel("Henry", 32, false))),
                new CarModel(UUID.randomUUID().toString(), "X5", 2020, "BMW", List.of(new OwnerModel("Eve", 29, false))),
                new CarModel(UUID.randomUUID().toString(), "A4", 2017, "Audi", List.of(new OwnerModel("Frank", 50, true))),
                new CarModel(UUID.randomUUID().toString(), "Golf", 2023, "Volkswagen", List.of(new OwnerModel("Grace", 38, false))),
                new CarModel(UUID.randomUUID().toString(), "Porsche 911", 2019, "Porsche", List.of(new OwnerModel("Hank", 40, true))),
                new CarModel(UUID.randomUUID().toString(), "Corolla", 2022, "Toyota", List.of(new OwnerModel("Ivy", 31, true))),
                new CarModel(UUID.randomUUID().toString(), "Model 3", 2021, "Tesla", List.of(new OwnerModel("Jack", 44, false)))
        );

        repository.saveAll(cars);
        log.info("----------------- {} Car Documents Added! -----------------", cars.size());
    }
}
