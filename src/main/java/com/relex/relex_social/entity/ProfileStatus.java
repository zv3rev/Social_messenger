package com.relex.relex_social.entity;

public enum ProfileStatus {
    UNCONFIRMED("UNCONFIRMED"),
    CONFIRMED("CONFIRMED"),
    DELETED("DELETED");

    private final String name;

    ProfileStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
