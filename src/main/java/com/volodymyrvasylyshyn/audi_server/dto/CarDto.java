package com.volodymyrvasylyshyn.audi_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Integer id;
    private Integer audiModelId;
    private String modelName;
    private double price;
    private String imageURL;
    private String descriptionImageUrl;
    private String topViewImageUrl;
    private String crashTestMovie;
    private String frontViewImageUrl;
    private String sideViewImageURL;
    //DESCRIPTION
    //Engine
    private String engineType;
    //Transmission
    private String driveType;
    //Mass
    private String emptyWeight ;
    private String grossWeight ;
    private String trunkVolume;
    //Capacity
    private String maxSpeed;
    private String from0to100;
    private String averageFuelConsumption;
    //Dimensions
    private String length;
    private String width;
    private String height;
    private String wheelBase;

}
