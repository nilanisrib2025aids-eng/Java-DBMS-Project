package org.example.productjdbc.Model;

import java.math.BigDecimal;

public class Department {
    private Integer id;
    private String name;
    private String code;
    private String headOfficer;
    private BigDecimal allocatedBudget;

    public Department() {}

    public Department(Integer id, String name, String code, String headOfficer, BigDecimal allocatedBudget) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.headOfficer = headOfficer;
        this.allocatedBudget = allocatedBudget;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getHeadOfficer() { return headOfficer; }
    public void setHeadOfficer(String headOfficer) { this.headOfficer = headOfficer; }

    public BigDecimal getAllocatedBudget() { return allocatedBudget; }
    public void setAllocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; }
}
