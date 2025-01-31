package com.red.ElectronicStore.Enumeration;

public enum RoleName {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_MODERATOR;

    @Override
    public String toString() {
        return this.name().toUpperCase();
    }
}
