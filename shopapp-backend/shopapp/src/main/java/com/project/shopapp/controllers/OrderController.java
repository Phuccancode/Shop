package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,
                                         BindingResult result){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
        return ResponseEntity.ok("create order successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrders(@PathVariable("user_id") Long userId){
        try{
            return ResponseEntity.ok("get order "+userId+" successfully");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    //admin works
    public ResponseEntity<?> updateOrder(@PathVariable Long id,
                                         @Valid @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok("update successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // soft delete -> active = false
        return ResponseEntity.ok("delete order successfully");
    }
    //TO DO Get: get list order from user id
    //method getOders
    //TO Do Put: update info of a order from user id
    //method: updateOrder
    // admin work
    //To do Delete from user id
    //method: deleteOrder
    //Xóa mềm active =false
}
