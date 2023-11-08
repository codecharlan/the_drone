package com.codecharlan.thedrone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Name can only contain letters, numbers, ‘-‘, ‘_’")
    private String name;

    @Max(value = 500, message = "Weight Cannot exceed 500 gram")
    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code must contain only upper case letters, underscore and numbers")
    private String code;

    @Lob
    private byte[] image;

    @JsonIgnore
    @ManyToOne
    private Drone drone;
}
