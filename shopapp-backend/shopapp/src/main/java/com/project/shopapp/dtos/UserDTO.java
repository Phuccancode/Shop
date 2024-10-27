package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;

@Data

//User user = User.builder()
//        .username("john_doe")
//        .email("john.doe@example.com")
//        .age(25)
//        .build();
//Bạn có thể chỉ định thứ tự bất kỳ cho các thuộc tính mà không phải lo lắng về thứ tự của chúng trong constructor.
//Không cần phải truyền tất cả các tham số; bạn chỉ cần truyền các tham số mà bạn muốn.
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO
{
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password is required")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    //yyyy-mm-dd
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private Long facebookAccountId;

    @JsonProperty("google_account_id")
    private Long googleAccountId;

    @JsonProperty("role_id")
    @NotNull(message = "Role ID is required")
    private Long roleId;

}
