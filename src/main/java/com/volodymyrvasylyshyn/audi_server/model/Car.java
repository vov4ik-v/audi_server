package com.volodymyrvasylyshyn.audi_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "audiModel_id")
    private AudiModel audiModel;

    private String modelName;
    private double price;
    private String imageURL;
    private String smallImageUrl;
    private String descriptionImageUrl;
    private String topViewImageUrl;
    private String frontViewImageUrl;
    private String sideViewImageURL;
    private String crashTestMovie;
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
