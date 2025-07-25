package com.chokhaniyash.fooddeliveryappapi.repository;

import com.chokhaniyash.fooddeliveryappapi.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<FoodEntity,String> {
}
