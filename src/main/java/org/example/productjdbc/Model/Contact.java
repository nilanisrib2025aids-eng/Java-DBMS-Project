package org.example.productjdbc.Model;

import java.sql.Timestamp;

public class Contact {
    private Integer id;
    private String name;
    private String contactType; // CITIZEN, CONTRACTOR, UTILITY
    private String email;
    private String phone;
    private String address;
    private Timestamp createdAt;

    public Contact() {}

    public Contact(Integer id, String name, String contactType, String email, String phone, String address, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.contactType = contactType;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
