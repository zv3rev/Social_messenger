package com.relex.relex_social.entity;

public enum ProfileStatus {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    private final String name;

    ProfileStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
