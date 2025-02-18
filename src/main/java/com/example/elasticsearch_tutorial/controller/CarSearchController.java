package com.example.elasticsearch_tutorial.controller;

import com.example.elasticsearch_tutorial.model.CarModel;
import com.example.elasticsearch_tutorial.service.CarSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class CarSearchController {
    private final CarSearchService carSearchService;

    @GetMapping("/brand")
    public List<CarModel> searchCarsByBrand(@RequestParam String brand) {
        return carSearchService.searchCarsByBrand(brand);
    }

    @GetMapping("/brandOrModel")
    public List<CarModel> searchCarsByBrandOrModel(@RequestParam String keyword) {
        return carSearchService.searchCarsByBrandOrModel(keyword);
    }

    @GetMapping("/brandAndYear")
    public List<CarModel> searchCarsByBrandAndYear(
            @RequestParam String brand,
            @RequestParam int year) {
        return carSearchService.searchCarsByBrandAndYear(brand, year);
    }

    @GetMapping("/countEachBrand")
    public List<CarModel> searchCountEachBrandsCars() {
        return carSearchService.searchCountEachBrandsCars();
    }

    @GetMapping("/searchCarsByBrandSorted")
    public List<CarModel> searchCarsByBrandSorted(
            @RequestParam String keyword,
            @RequestParam String sortField,
            @RequestParam boolean ascending) {
        return carSearchService.searchCarsByBrandSorted(keyword, sortField, ascending);
    }

    @GetMapping("/highlightSearch")
    public List<Map<String, Object>> searchCarsWithHighlight(
            @RequestParam String brand,
            @RequestParam String model) {
        return carSearchService.searchCarsWithHighlight(brand, model);
    }

    @GetMapping("/models")
    public List<String> searchCarModels(@RequestParam String model) {
        return carSearchService.searchCarModels(model);
    }
}
