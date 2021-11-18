package com.example.Backuni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListCabinets {

    private  Long id;

    private Long number;

    private String description;

    private List<String> url;
}
