package com.example.elasticsearch_tutorial.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

import static com.example.elasticsearch_tutorial.helper.Indices.CAR;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Document(indexName = CAR, createIndex = false)//I want index was created every time application starts
public class CarModel {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "model")
    private String model;

    @Field(type = FieldType.Integer, name = "year")
    private Integer yearOfManufacture;

    @Field(type = FieldType.Text, name = "brand")
    private String brand;

    @Field(type = FieldType.Nested, name = "owners")
    private List<OwnerModel> owners;
}
