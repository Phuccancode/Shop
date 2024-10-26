package com.project.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

//Xác định nó là thực thể
@Entity
//Bảng trong db là categories mà class chúng ta lại là Category
// --> dùng @Table để ánh xạ
@Table(name="social_accounts")
@Data //toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {
    @Id //primary key
    // No same instance --> when add a new instance --> auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", length = 50)
    private String providerId;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length = 150)
    private String email;

}
