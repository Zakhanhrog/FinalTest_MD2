package com.codegym.contactmanagement.model;

public class Contact {
    private String phoneNumber;
    private String group;
    private String fullName;
    private String gender;
    private String address;
    private String birthDate;
    private String email;

    public Contact(String phoneNumber, String group, String fullName, String gender, String address, String birthDate, String email) {
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.fullName = fullName;
        this.gender = gender;
        this.address = address;
        this.birthDate = birthDate;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toCsvString() {
        return String.join(",", phoneNumber, group, fullName, gender, address, birthDate, email);
    }

    @Override
    public String toString() {
        return "SĐT: " + phoneNumber + ", Nhóm: " + group + ", Tên: " + fullName +
                ", Giới tính: " + gender + ", Địa chỉ: " + address +
                ", Ngày sinh: " + birthDate + ", Email: " + email;
    }

    public String toShortString() {
        return "SĐT: " + phoneNumber + ", Nhóm: " + group + ", Tên: " + fullName +
                ", Giới tính: " + gender + ", Địa chỉ: " + address;
    }
}