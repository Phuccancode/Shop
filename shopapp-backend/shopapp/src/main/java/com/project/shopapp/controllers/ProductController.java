package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
//                                                @ModelAttribute MultipartFile files,
                                                BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // saved in createProduct
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable long id,
            @ModelAttribute List<MultipartFile> files){
        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>(): files;
            List<ProductImage> productImages = new ArrayList<>();
            for(MultipartFile file:files) {
                if (file.getSize() == 0) continue;
                //check the size and format of file
                if (file.getSize() > 10 * 1024 * 1024) {
//                throw new ResponseStatusException(
//                        HttpStatus.PAYLOAD_TOO_LARGE,"File is too large! Max size is 10MB");
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Max size is 10MB");
                }
                //Get the format of file
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");

                }
                String filename = storeFile(file);
                ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Add UUID in forward of file name to make it unique
        String uniqueFilename = UUID.randomUUID().toString() +" "+ filename;
        //Path to folder storing file
        java.nio.file.Path uploadDir = Paths.get("upload");
        //Check and create folder if it doesnt exist
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //path to destination file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(new ProductResponse(updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //http://localhost:8088/api/v1/products?page=1&limit=10
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> products = productService.getAllProducts(pageRequest);
        int totalPages = products.getTotalPages();
        List<ProductResponse> productList = products.getContent();
        ProductListResponse productListResponse = new ProductListResponse(productList, totalPages);
        return ResponseEntity.ok(productListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") long productId){
        try {
            ProductResponse existingProduct = new ProductResponse(productService.getProductById(productId));
            return ResponseEntity.ok(existingProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        //giong voi ResponseEntity.ok()
        //return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        //Dung cai binh thuong hay hon
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted with id " + id);
    }
    @PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i =0; i<1_000_000;i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName((productName))){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90_000_000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,4))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products generated successfully");
    }
}