package com.chokhaniyash.fooddeliveryappapi.controller;

import com.chokhaniyash.fooddeliveryappapi.io.FoodRequest;
import com.chokhaniyash.fooddeliveryappapi.io.FoodResponse;
import com.chokhaniyash.fooddeliveryappapi.service.FoodService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {
    private final FoodService foodService;
    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString, @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            request = objectMapper.readValue(foodString,FoodRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON format");
        }
        FoodResponse response = foodService.addFood(request,file);
        return response;
    }

    @GetMapping
    public List<FoodResponse> readFoods(){
        return foodService.readFoods();
    }
    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){
        return foodService.readFood(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable("id") String id){
        foodService.deleteFood(id);
    }
}
