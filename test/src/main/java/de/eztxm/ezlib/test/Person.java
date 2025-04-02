package de.eztxm.ezlib.test;

import de.eztxm.ezlib.config.annotation.JsonConfig;
import de.eztxm.ezlib.config.annotation.JsonIgnore;
import de.eztxm.ezlib.config.annotation.JsonValue;

@JsonConfig
public class Person {
    @JsonValue(name = "firstname")
    private String firstName;
    @JsonValue(name = "lastname")
    private String lastName;
    @JsonValue(name = "age")
    private int age;
    @JsonValue(name = "address")
    private Address address;
    @JsonIgnore
    private String role;

    public Person() {
    }

    public Person(String firstName, String lastName, int age, Address address, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
