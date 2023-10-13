package com.relex.relex_social.entity;

public enum AllowedToSend {
    ALL("ALL"),
    FRIENDS("FRIENDS"),
    NONE("NONE");

    private final String name;

    AllowedToSend(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
