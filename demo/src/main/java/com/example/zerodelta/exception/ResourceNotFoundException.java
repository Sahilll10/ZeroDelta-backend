package com.example.zerodelta.exception;


public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
