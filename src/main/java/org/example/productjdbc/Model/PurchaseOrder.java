package org.example.productjdbc.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PurchaseOrder {
    private Integer id;
    private String poNumber;
    private Integer grievanceId;
    private String grievanceTicket;
    private Integer contractorId;
    private String contractorName;
    private Integer departmentId;
    private String departmentName;
    private Integer productId;
    private String productName;
    private BigDecimal amount;
    private String status;
    private Timestamp createdAt;

    public PurchaseOrder() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public Integer getGrievanceId() { return grievanceId; }
    public void setGrievanceId(Integer grievanceId) { this.grievanceId = grievanceId; }

    public String getGrievanceTicket() { return grievanceTicket; }
    public void setGrievanceTicket(String grievanceTicket) { this.grievanceTicket = grievanceTicket; }

    public Integer getContractorId() { return contractorId; }
    public void setContractorId(Integer contractorId) { this.contractorId = contractorId; }

    public String getContractorName() { return contractorName; }
    public void setContractorName(String contractorName) { this.contractorName = contractorName; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
