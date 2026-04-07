package com.example.hms.dto;

import lombok.Data;

@Data
public class PatientDto {

    private Long id;
    private String name;
    private int age;
    private String gender;
    private String disease;
    private String address;
    private String phoneNumber;

}