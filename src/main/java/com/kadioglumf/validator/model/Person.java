package com.kadioglumf.validator.model;

import com.kadioglumf.validator.minvalidator.CustomMin;

import java.math.BigDecimal;

public class Person {

    private String id;
    private String name;

    @CustomMin(value = 20, message = "sdsffs")
    private int age;

    @CustomMin(value = 168)
    private BigDecimal amount;

    public Person() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}