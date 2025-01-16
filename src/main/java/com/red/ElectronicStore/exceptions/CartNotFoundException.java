package com.red.ElectronicStore.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String msg) {
        super(msg);
    }
}
