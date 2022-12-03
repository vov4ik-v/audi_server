package com.volodymyrvasylyshyn.audi_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "friends")
@NoArgsConstructor
@Data
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_date")
    private Date createdDate;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "first_user_id", referencedColumnName = "id")
    User firstUser;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "second_user_id", referencedColumnName = "id")
    User secondUser;


}
