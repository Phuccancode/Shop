package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID >0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Image URL must be between 3 and 300 characters")
    private String imageUrl;
}
