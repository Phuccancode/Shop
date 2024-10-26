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
public class Category {

    @Id //primary key
    // No same instance --> when add a new instance --> auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //nullable == false --> not null
    @Column(name="name", nullable = false)
    private String name;

}
