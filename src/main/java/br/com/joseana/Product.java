package br.com.joseana;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

@Entity
@Cacheable
public class Product extends PanacheEntity {

    public String name;
    public int quantity;
    public String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate expiry_date;

}