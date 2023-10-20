package com.relex.relex_social.entity;

public enum JwtType {
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String name;

    JwtType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
