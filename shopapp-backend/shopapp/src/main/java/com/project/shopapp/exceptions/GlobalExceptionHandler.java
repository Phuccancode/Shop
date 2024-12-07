//package com.project.shopapp.exceptions;
//
//
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalException {
//    @ExceptionHandler(value = {
//            UsernameNotFoundException.class,
//            BadCredentialsException.class,
//            InvalidException.class,
//            UnAuthorizedException.class
//    })
//    public ResponseEntity<RestResponse<Object>> handleException(Exception e) {
//        RestResponse<Object> res = new RestResponse<Object>();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setError(e.getMessage());
//        res.setMessage("Bad request");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//    }
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        RestResponse<Object> res = new RestResponse<Object>();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setError(e.getBody().getDetail());
//        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
//        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//    }
//}