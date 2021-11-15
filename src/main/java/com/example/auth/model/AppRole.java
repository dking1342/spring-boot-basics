package com.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "App_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppRole {

    @Id
    @SequenceGenerator(
            name = "App_role_sequence",
            sequenceName = "App_role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "App_role_sequence"
    )
    private Long id;
    private String name;


}
