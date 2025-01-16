package com.red.ElectronicStore.exceptions;

public class BadApiRequest extends RuntimeException {
    public BadApiRequest(String s) {
        super(s);
    }
}
