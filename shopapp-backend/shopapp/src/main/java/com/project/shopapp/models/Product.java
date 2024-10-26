package com.project.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //max length =350 char
    @Column(name="name", nullable = false, length = 350)
    private String name;
    private Float price;
    @Column(name ="thumnail", length = 300)
    private String thumnail;

    @Column(name="description")
    private String description;


    @ManyToOne
    @JoinColumn(name= "category_id")
    private Category category;
}
