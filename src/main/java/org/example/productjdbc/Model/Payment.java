package org.example.productjdbc.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private Integer id;
    private String paymentNumber;
    private String paymentType; // PAYMENT_OUT, PAYMENT_IN
    private Integer referenceBillId;
    private Integer referenceInvoiceId;
    private Integer contactId;
    private String contactName;
    private BigDecimal amount;
    private Timestamp paymentDate;
    private String paymentMethod;

    public Payment() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPaymentNumber() { return paymentNumber; }
    public void setPaymentNumber(String paymentNumber) { this.paymentNumber = paymentNumber; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public Integer getReferenceBillId() { return referenceBillId; }
    public void setReferenceBillId(Integer referenceBillId) { this.referenceBillId = referenceBillId; }

    public Integer getReferenceInvoiceId() { return referenceInvoiceId; }
    public void setReferenceInvoiceId(Integer referenceInvoiceId) { this.referenceInvoiceId = referenceInvoiceId; }

    public Integer getContactId() { return contactId; }
    public void setContactId(Integer contactId) { this.contactId = contactId; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
