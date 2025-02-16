package com.example.elasticsearch_tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@SpringBootApplication
public class ElasticsearchTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchTutorialApplication.class, args);
    }

}
