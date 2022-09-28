package com.volodymyrvasylyshyn.audi_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    @OneToMany(targetEntity = Car.class,mappedBy = "audiModel",fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Car> cars;

}
