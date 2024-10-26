package com.project.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

//Xác định nó là thực thể
@Entity
//Bảng trong db là categories mà class chúng ta lại là Category
// --> dùng @Table để ánh xạ
@Table(name="categories")
@Data //toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
