package com.example.elasticsearch_tutorial.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.example.elasticsearch_tutorial.model.CarModel;
import com.example.elasticsearch_tutorial.repo.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarSearchService {
    /**
     * <h2>IndexOperations</h2>
     * <p>Defines actions on index level like creating or deleting an index.</p>
     *
     * <h2>DocumentOperations</h2>
     * <p>Defines actions to store, update, and retrieve entities based on their ID.</p>
     *
     * <h2>SearchOperations</h2>
     * <p>Defines the actions to search for multiple entities using queries.</p>
     *
     * <h2>ElasticsearchOperations</h2>
     * <p>Combines the <code>DocumentOperations</code> and <code>SearchOperations</code> interfaces.</p>
     */
    private final ElasticsearchOperations elasticsearchOperations;
    private final CarRepository carRepository;

    public List<CarModel> searchCarsByBrand(String keyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("brand")
                                .query(keyword))
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find cars whose brand OR model matches a keyword.
    public List<CarModel> searchCarsByBrandOrModel(String keyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("brand", "model")
                                .query(keyword))
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find cars whose brand AND year matches a keyword.
    public List<CarModel> searchCarsByBrandAndYear(String brand, int year) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .match(mt -> mt.field("brand").query(brand))
                                )
                                .must(m -> m
                                        .match(mt -> mt.field("year").query(year))
                                )
                        )
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find cars whose brand is brandKeyword OR year is yearKeyword
    public List<CarModel> searchCarsByTwoFieldsBrandOrYear(String brandKeyword, int yearKeyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(m -> m
                                        .match(mt -> mt.field("brand").query(brandKeyword))
                                )
                                .should(m -> m
                                        .match(mt -> mt.field("year").query(yearKeyword))
                                )
                        )
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find cars where brand is NOT brandKeyword
    public List<CarModel> searchCarsByNotThisBrand(String brandKeyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b.mustNot(m -> m
                                        .match(mt -> mt.field("brand").query(brandKeyword))
                                )
                        )
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find how many cars exist for each brand
    public List<CarModel> searchCountEachBrandsCars() {
        NativeQuery query = NativeQuery.builder()
                .withAggregation("brandCounts", Aggregation.of(a -> a
                        .terms(ta -> ta
                                .field("brand.keyword")  // Use .keyword for aggregations
                                .size(3)
                        )
                ))
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find max and min cars years
    public List<CarModel> findMaxAndMinYears() {
        NativeQuery query = NativeQuery.builder()
                .withAggregation("maxYear", Aggregation.of(a -> a
                        .max(ma -> ma.field("year"))
                ))
                .withAggregation("minYear", Aggregation.of(a -> a
                        .min(mi -> mi.field("year"))
                ))
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    //Find max and min cars years
    public List<CarModel> combineSearchByBrandAndGroupByModel(String brandKeyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("brand")
                                .query(brandKeyword)
                        )
                )
                .withAggregation("models", Aggregation.of(a -> a
                        .terms(ta -> ta.field("model").size(5))
                ))
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    public List<CarModel> searchCarsByBrandSorted(String keyword, String sortField, boolean ascending) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("brand")
                                .query(keyword))
                )
                .withSort(s -> s
                        .field(f -> f
                                .field(sortField)
                                .order(ascending ? SortOrder.Asc : SortOrder.Desc))
                )
                .build();

        SearchHits<CarModel> searchHits = elasticsearchOperations.search(query, CarModel.class);
        return searchHits.stream().map(SearchHit::getContent).toList();
    }

    // Highlighted search by brand or model
    /*"highlight": {
            "model": [
                "<em>Model</em> 3"
            ]
        }*/
    public List<Map<String, Object>> searchCarsWithHighlight(String brand, String model) {
        SearchHits<CarModel> searchHits = carRepository.findByBrandContainingOrModelContaining(brand, model);

        return searchHits.stream().map(hit -> {
            Map<String, Object> carData = new HashMap<>();
            carData.put("car", hit.getContent());
            carData.put("highlight", hit.getHighlightFields());
            return carData;
        }).toList();
    }

    // Fetch only the model field using @SourceFilters
    /*[
    "Model S",
    "Model 3",
    "Model X",
    "Model 3"
     ]*/
    public List<String> searchCarModels(String model) {
        SearchHits<CarModel> searchHits = carRepository.findByModel(model);
        return searchHits.stream()
                .map(hit -> hit.getContent().getModel()) // Only return the model field
                .toList();
    }
}
