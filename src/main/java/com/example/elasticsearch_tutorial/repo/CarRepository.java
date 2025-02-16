package com.example.elasticsearch_tutorial.repo;

import com.example.elasticsearch_tutorial.model.CarModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CarRepository extends ElasticsearchRepository<CarModel, String> {

    List<CarModel> findAllByBrand(String brand);

    List<CarModel> findByBrandContaining(String model);

    List<CarModel> findByBrandIn(List<String> brands);

    List<CarModel> saveAll(List<CarModel> cars);
}
