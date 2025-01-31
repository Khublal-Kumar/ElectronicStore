package com.red.ElectronicStore.Enumeration;

public enum RoleName {
    ADMIN,
    USER,
    MANAGER,
    MODERATOR;

    @Override
    public String toString() {
        return this.name().toUpperCase();
    }
}
