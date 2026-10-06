package com.github.felipeschwartz.parkingsystem.model.enums;

import java.util.HashSet;
import java.util.Set;

public enum UserProfile {
    USER,
    PARKING,
    PARKING_MANAGER,
    ADMIN;

    public Set<String> roles() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_" + name());
        if (this == ADMIN) {
            roles.add("ROLE_USER");
        }
        return roles;
    }
}
