package com.gamezone.model;

public class Customer extends Person {
    private String email;

    public Customer(String name, String id, String phone, String email) {
        super(name, id, phone);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getRoleDescription() {
        return "Cliente - Email: " + email;
    }
}