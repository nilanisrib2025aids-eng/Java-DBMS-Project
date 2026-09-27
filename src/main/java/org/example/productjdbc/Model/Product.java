package org.example.productjdbc.Model;

import java.math.BigDecimal;

public class Product {
    private Integer id;
    private String name;
    private String productType; // SERVICE, GOODS
    private BigDecimal standardRate;
    private Integer departmentId;
    private String departmentName;

    public Product() {}

    public Product(Integer id, String name, String productType, BigDecimal standardRate, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.productType = productType;
        this.standardRate = standardRate;
        this.departmentId = departmentId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getStandardRate() { return standardRate; }
    public void setStandardRate(BigDecimal standardRate) { this.standardRate = standardRate; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
