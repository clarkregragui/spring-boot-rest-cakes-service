package com.riggy.example.cakes.data;

import org.springframework.data.repository.CrudRepository;

import com.riggy.example.cakes.model.entity.Cake;

public interface CakeRepository extends CrudRepository<Cake,Integer> {

}
