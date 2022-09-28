package com.volodymyrvasylyshyn.audi_server.controller;

import com.volodymyrvasylyshyn.audi_server.common.ApiResponse;
import com.volodymyrvasylyshyn.audi_server.dto.CarDto;
import com.volodymyrvasylyshyn.audi_server.model.AudiModel;
import com.volodymyrvasylyshyn.audi_server.model.Car;
import com.volodymyrvasylyshyn.audi_server.repository.AudiModelRepository;
import com.volodymyrvasylyshyn.audi_server.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/car")
@CrossOrigin
public class CarController {
    private final CarService carService;
    private final AudiModelRepository audiModelRepository;

    public CarController(CarService carService, AudiModelRepository audiModelRepository) {
        this.carService = carService;
        this.audiModelRepository = audiModelRepository;
    }
    @GetMapping("/listCar")
    public ResponseEntity<List<CarDto>> getAllCars(){
        List<CarDto> carList = carService.getAllCars();
        return new ResponseEntity<>(carList,HttpStatus.OK);

    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<Car> getOne(@PathVariable("id") Integer carId){
        Car car = carService.getOne(carId);
        return new ResponseEntity<>(car,HttpStatus.OK);
    }
    @PostMapping("/addCar")
    public ResponseEntity<ApiResponse> addCar(@RequestBody CarDto carDto){
        Optional<AudiModel> audiModelOptional = audiModelRepository.findById(carDto.getAudiModelId());
        if (audiModelOptional.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(true,"Audi Model does not exists"), HttpStatus.NOT_FOUND);

        }
        carService.createCar(carDto,audiModelOptional.get());
        return new ResponseEntity<>(new ApiResponse(true,"Added the car"), HttpStatus.CREATED);

    }
    @PutMapping("/editCar/{id}")
    public ResponseEntity<ApiResponse> editCar(@PathVariable("id") Integer carId,@RequestBody CarDto updatedCar){
        carService.editCar(carId,updatedCar);
        return new ResponseEntity<>(new ApiResponse(true,"Edited the car"), HttpStatus.OK);


    }
    @DeleteMapping("/deleteCar/{id}")
    public ResponseEntity<ApiResponse> deleteCar(@PathVariable("id") Integer carId){
        carService.deleteCar(carId);
        return new ResponseEntity<>(new ApiResponse(true,"Deleted the car"), HttpStatus.OK);

    }
}
