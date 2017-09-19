package com.edgardrake.flameseeker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Edgar Drake on 05-Jul-17.
 */

public class User {
    @Expose
    private String id;
    @Expose
    private String email;
    @Expose
    private String username;
    @Expose
    private String name;

    public String getID() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.username = builder.username;
        this.name = builder.name;
    }

    public static class Builder {
        private String id;
        private String email;
        private String username;
        private String name;

        public Builder(String id) {
            this.id = id;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
