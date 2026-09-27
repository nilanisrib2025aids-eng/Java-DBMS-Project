package org.example.productjdbc.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VendorBill {
    private Integer id;
    private String billNumber;
    private Integer poId;
    private String poNumber;
    private Integer contractorId;
    private String contractorName;
    private BigDecimal amount;
    private String status;
    private Timestamp createdAt;

    public VendorBill() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public Integer getPoId() { return poId; }
    public void setPoId(Integer poId) { this.poId = poId; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public Integer getContractorId() { return contractorId; }
    public void setContractorId(Integer contractorId) { this.contractorId = contractorId; }

    public String getContractorName() { return contractorName; }
    public void setContractorName(String contractorName) { this.contractorName = contractorName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
