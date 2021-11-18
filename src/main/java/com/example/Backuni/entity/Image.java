package com.example.Backuni.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    private Building building;

    @ManyToOne
    private Cabinet cabinet;
}
