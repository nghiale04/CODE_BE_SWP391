package com.BE.controller;

import com.BE.model.entity.KoiFishEntity;
import com.BE.model.request.KoiRequestDTO;
import com.BE.service.KoiFishService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/koifish")
@SecurityRequirement(name ="api")
public class KoiFishController {
    @Autowired
    private KoiFishService koiFishService;

    @GetMapping
    public ResponseEntity<List<KoiRequestDTO>> getAll() {
        return ResponseEntity.ok(koiFishService.getAllKoiFish());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody KoiRequestDTO koiRequestDTO) {
        KoiFishEntity koiReq = koiFishService.add(koiRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(koiReq);
    }
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable int id, @RequestBody KoiRequestDTO koiRequestDTO) {
        boolean updateKoiFish = koiFishService.update(id, koiRequestDTO);
        return ResponseEntity.ok(updateKoiFish);
    }

    @DeleteMapping("{id}")
    public ResponseEntity update(@PathVariable long id) {
        boolean deleteKoiFish = koiFishService.delete(id);
        return ResponseEntity.ok(deleteKoiFish);
    }

    @GetMapping("{id}")
    public ResponseEntity<KoiRequestDTO> get(@PathVariable long id) {
       return ResponseEntity.status(HttpStatus.OK).body(koiFishService.findById(id));
    }

}
