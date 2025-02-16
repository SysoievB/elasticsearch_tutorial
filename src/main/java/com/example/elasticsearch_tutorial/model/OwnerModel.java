package com.example.elasticsearch_tutorial.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OwnerModel {

    @Field(type = FieldType.Text)
    String name;

    @Field(type = FieldType.Integer)
    Integer age;

    @Field(type = FieldType.Boolean, name = "isActive")
    Boolean isActive;
}
