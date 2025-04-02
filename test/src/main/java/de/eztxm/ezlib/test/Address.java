package de.eztxm.ezlib.test;

public class Address {
    private int postCode;
    private String city;
    private String street;
    private String number;

    public Address() {
    }

    public Address(int postCode, String city, String street, String number) {
        this.postCode = postCode;
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public int getPostCode() {
        return postCode;
    }
}
