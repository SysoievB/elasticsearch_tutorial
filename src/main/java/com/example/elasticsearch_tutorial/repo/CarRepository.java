package com.example.elasticsearch_tutorial.repo;

import com.example.elasticsearch_tutorial.model.CarModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CarRepository extends ElasticsearchRepository<CarModel, String> {

    List<CarModel> findAllByBrand(String brand, Sort sort);

    List<CarModel> findByBrandContaining(String model);

    List<CarModel> findByBrandIn(List<String> brands);

    //The @Highlight annotation allows us to highlight matching search terms in the search results.
    @Highlight(fields = {
            @HighlightField(name = "brand"),
            @HighlightField(name = "model")
    })
    SearchHits<CarModel> findByBrandContainingOrModelContaining(String brand, String model);

    //The @SourceFilters annotation is used to retrieve only specific fields from Elasticsearch instead of the entire document.
    @SourceFilters(includes = "model")
    SearchHits<CarModel> findByModel(String model);
}
