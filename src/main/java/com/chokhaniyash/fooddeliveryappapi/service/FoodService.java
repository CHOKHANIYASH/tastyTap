package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.io.FoodRequest;
import com.chokhaniyash.fooddeliveryappapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();
}
