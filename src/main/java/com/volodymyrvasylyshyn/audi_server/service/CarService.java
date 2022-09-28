package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.dto.CarDto;
import com.volodymyrvasylyshyn.audi_server.exeptions.CarNotFoundExeption;
import com.volodymyrvasylyshyn.audi_server.model.AudiModel;
import com.volodymyrvasylyshyn.audi_server.model.Car;
import com.volodymyrvasylyshyn.audi_server.repository.AudiModelRepository;
import com.volodymyrvasylyshyn.audi_server.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final AudiModelRepository audiModelRepository;

    private CarDto getCarDTO(Car car){
        CarDto carDto = new CarDto();
        carDto.setId(car.getId());
        carDto.setAudiModelId(car.getAudiModel().getId());
        carDto.setAverageFuelConsumption(car.getAverageFuelConsumption());
        carDto.setDescriptionImageUrl(car.getDescriptionImageUrl());
        carDto.setDriveType(car.getDriveType());
        carDto.setEmptyWeight(car.getEmptyWeight());
        carDto.setEngineType(car.getEngineType());
        carDto.setFrom0to100(car.getFrom0to100());
        carDto.setFrontViewImageUrl(car.getFrontViewImageUrl());
        carDto.setGrossWeight(car.getGrossWeight());
        carDto.setCrashTestMovie(car.getCrashTestMovie());
        carDto.setHeight(car.getHeight());
        carDto.setImageURL(car.getImageURL());
        carDto.setLength(car.getLength());
        carDto.setMaxSpeed(car.getMaxSpeed());
        carDto.setPrice(car.getPrice());
        carDto.setModelName(car.getModelName());
        carDto.setSideViewImageURL(car.getSideViewImageURL());
        carDto.setSmallImageUrl(car.getSmallImageUrl());
        carDto.setTopViewImageUrl(car.getTopViewImageUrl());
        carDto.setTrunkVolume(car.getTrunkVolume());
        carDto.setWheelBase(car.getWheelBase());
        carDto.setWidth(car.getWidth());
        return carDto;
    }
    private Car getCarWithDTO(Car car,CarDto carDto){
        car.setAudiModel(audiModelRepository.findById(carDto.getAudiModelId()).orElse(null));
        car.setAverageFuelConsumption(carDto.getAverageFuelConsumption());
        car.setDescriptionImageUrl(carDto.getDescriptionImageUrl());
        car.setDriveType(carDto.getDriveType());
        car.setEmptyWeight(carDto.getEmptyWeight());
        car.setEngineType(carDto.getEngineType());
        car.setFrom0to100(carDto.getFrom0to100());
        car.setFrontViewImageUrl(carDto.getFrontViewImageUrl());
        car.setGrossWeight(carDto.getGrossWeight());
        car.setHeight(carDto.getHeight());
        car.setCrashTestMovie(carDto.getCrashTestMovie()
        );
        car.setId(carDto.getId());
        car.setImageURL(carDto.getImageURL());
        car.setLength(carDto.getLength());
        car.setMaxSpeed(carDto.getMaxSpeed());
        car.setPrice(carDto.getPrice());
        car.setModelName(carDto.getModelName());
        car.setSideViewImageURL(carDto.getSideViewImageURL());
        car.setSmallImageUrl(carDto.getSmallImageUrl());
        car.setTopViewImageUrl(carDto.getTopViewImageUrl());
        car.setTrunkVolume(carDto.getTrunkVolume());
        car.setWheelBase(carDto.getWheelBase());
        car.setWidth(carDto.getWidth());
        return car;
    }

    public CarService(CarRepository carRepository, AudiModelRepository audiModelRepository) {
        this.carRepository = carRepository;
        this.audiModelRepository = audiModelRepository;
    }

    public void createCar(CarDto carDto, AudiModel audiModel) {
        Car car = new Car();
        getCarWithDTO(car,carDto);
        car.setAudiModel(audiModel);
        carRepository.save(car);

    }

    public void editCar(Integer carId, CarDto carDto) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()){
            throw new CarNotFoundExeption("Car with id " + carId +" not found");
        }
        Car car = optionalCar.get();
        getCarWithDTO(car,carDto);
        car.setId(carId);
        carRepository.save(car);
    }

    public void deleteCar(Integer carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()){
            throw new CarNotFoundExeption("Car with id " + carId +" not found");
        }
        Car car = optionalCar.get();
        carRepository.delete(car);
    }

    public List<CarDto> getAllCars() {
        List<Car> cars = carRepository.findAll();
        List<CarDto> carDtos = new ArrayList<>();
        for (Car car : cars){
            carDtos.add(getCarDTO(car));

        }
        return carDtos;
    }



    public Car getOne(Integer carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()){
            throw new CarNotFoundExeption("Car with id " + carId +" not found");
        }
        return optionalCar.get();
    }
}
