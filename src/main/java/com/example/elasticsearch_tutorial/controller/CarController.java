package com.example.elasticsearch_tutorial.controller;

import com.example.elasticsearch_tutorial.model.CarModel;
import com.example.elasticsearch_tutorial.repo.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarRepository carRepository;

    @PostMapping
    public void save(@RequestBody CarModel car) {
        carRepository.save(car);
    }

    @GetMapping("/{id}")
    public CarModel findById(@PathVariable String id) {
        return carRepository.findById(id).orElse(null);
    }

    @GetMapping
    public Iterable<CarModel> findAll() {
        return carRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        carRepository.deleteById(id);
    }

    @PutMapping
    public void update(@RequestBody CarModel car) {
        carRepository.save(car);
    }

    @GetMapping("/find")
    public List<CarModel> findByBrand(@RequestParam String brand) {
        return carRepository.findAllByBrand(brand, Sort.by("model").ascending());
    }
}
