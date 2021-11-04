package com.riggy.example.cakes.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CAKE", uniqueConstraints = {@UniqueConstraint(columnNames = "CAKE_ID"), @UniqueConstraint(columnNames = "TITLE")})
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Cake implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAKE_ID", unique = true, nullable = false)
    private Integer cakeId;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "DESC", unique = false, nullable = false, length = 100)
    private String desc;

    @Column(name = "IMAGE", unique = false, nullable = false, length = 300)
    private String image;
    
    @Column(name="INGREDIENTS")
    @ElementCollection
    private List<String> ingredients;
    
    @Column(name = "PRICE", unique = false, nullable = false, length = 7)
    private BigDecimal price;

}