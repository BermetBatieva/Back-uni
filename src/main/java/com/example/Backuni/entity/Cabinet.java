package com.example.Backuni.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cabinet")
public class Cabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;

    private String name;

    private byte[] image;

    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @ManyToOne
    private  Building building;


}
