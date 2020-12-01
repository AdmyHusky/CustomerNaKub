package com.digitalacademy.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @JsonProperty("firstName")
    @Size(min = 1, max = 100, message = "Please enter your first name between 1-100 ")
    private String first_name;

    @NotNull
    @JsonProperty("lastName")
    @Size(min = 1, max = 100, message = "Please enter your last name between 1-100 ")
    private String last_name;

    @Email(message = "Please type valid email")
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("phoneNo")
    private String phoneNo;

    @JsonProperty("age")
    private Integer age;


}
