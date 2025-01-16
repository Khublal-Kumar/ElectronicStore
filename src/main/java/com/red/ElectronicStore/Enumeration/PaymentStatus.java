package com.red.ElectronicStore.Enumeration;

public enum PaymentStatus {
    PENDING,       // Payment has not been completed yet
    COMPLETED,     // Payment was successfully completed
    FAILED,        // Payment attempt failed
    REFUNDED,      // Payment was refunded
    PARTIALLY_PAID // Part of the payment has been made
}
