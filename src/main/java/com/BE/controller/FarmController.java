package com.BE.controller;

import com.BE.model.entity.Response;
import com.BE.model.request.FarmRequestDTO;
import com.BE.service.FarmService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name ="api")
@RequestMapping("/api/farm")
public class FarmController {
    @Autowired
    private FarmService farmService;

    @GetMapping
    public ResponseEntity<List<FarmRequestDTO>> getAll() {
        return ResponseEntity.ok(farmService.getAllFarm());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody FarmRequestDTO farmRequestDTO) {
        FarmRequestDTO newFarmEntity = farmService.add(farmRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFarmEntity);
    }



    @PutMapping("{id}")
    public ResponseEntity updateFarm(@PathVariable long id, @RequestBody FarmRequestDTO farmRequestDTO) {
        FarmRequestDTO updateFarmEntity = farmService.update(id, farmRequestDTO);
        return ResponseEntity.ok(updateFarmEntity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity update(@PathVariable long id) {
        boolean deleteFarm = farmService.deleteFarm(id);
        return ResponseEntity.ok(deleteFarm);
    }

    @GetMapping("{id}")
    public ResponseEntity<FarmRequestDTO> get(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(farmService.getFarm(id));
    }
}
