package com.BE.controller;

import com.BE.model.entity.Response;
import com.BE.model.request.FindTourRequestDTO;
import com.BE.model.request.TourRequestDTO;
import com.BE.model.response.TourResponseDTO;
import com.BE.service.TourService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/tour")
@SecurityRequirement(name ="api")
public class TourController {
    @Autowired
    private TourService tourService;

    @GetMapping("/search")
    public ResponseEntity<Page<TourResponseDTO>> getAllTours(@ModelAttribute FindTourRequestDTO findTourRequestDTO) {
        try {
            Page<TourResponseDTO> listTour = tourService.getAllTour(findTourRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(listTour);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<TourRequestDTO>> getAllTours() {
        List<TourRequestDTO> getAllTour = tourService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(getAllTour);
    }

    @GetMapping("{id}")
    public ResponseEntity<TourRequestDTO> getTourById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tourService.getTourById(id));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody TourRequestDTO tourRequestDTO) {
        TourRequestDTO responeTour = tourService.add(tourRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responeTour);
    }
    @PutMapping("{id}")
    public ResponseEntity update(@RequestBody TourRequestDTO tourRequestDTO, @PathVariable long id) {
        TourRequestDTO responeTour = tourService.update(id, tourRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responeTour);
    }
    @DeleteMapping("{id}")
    public Response delete(@PathVariable long id) {
        Boolean tourDeleted = tourService.delete(id);
        if(tourDeleted){
            return new Response(true,"Delete Tour Successfully!", null);
        }else{
            return new Response(false,"Delete Tour Failed!", null);
        }
    }



}
