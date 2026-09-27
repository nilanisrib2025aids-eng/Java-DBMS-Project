package org.example.productjdbc.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class JournalEntry {
    private Integer id;
    private String entryNumber;
    private Integer journalId;
    private String journalName;
    private Integer accountId;
    private String accountName;
    private String accountCode;
    private BigDecimal debit;
    private BigDecimal credit;
    private String reference;
    private String description;
    private Timestamp entryDate;

    public JournalEntry() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEntryNumber() { return entryNumber; }
    public void setEntryNumber(String entryNumber) { this.entryNumber = entryNumber; }

    public Integer getJournalId() { return journalId; }
    public void setJournalId(Integer journalId) { this.journalId = journalId; }

    public String getJournalName() { return journalName; }
    public void setJournalName(String journalName) { this.journalName = journalName; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }

    public BigDecimal getDebit() { return debit; }
    public void setDebit(BigDecimal debit) { this.debit = debit; }

    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getEntryDate() { return entryDate; }
    public void setEntryDate(Timestamp entryDate) { this.entryDate = entryDate; }
}
