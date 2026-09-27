package org.example.productjdbc.Model;

public class Journal {
    private Integer id;
    private String journalCode;
    private String name;
    private String journalType; // PURCHASE, FINES, BANK, GENERAL

    public Journal() {}

    public Journal(Integer id, String journalCode, String name, String journalType) {
        this.id = id;
        this.journalCode = journalCode;
        this.name = name;
        this.journalType = journalType;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getJournalCode() { return journalCode; }
    public void setJournalCode(String journalCode) { this.journalCode = journalCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getJournalType() { return journalType; }
    public void setJournalType(String journalType) { this.journalType = journalType; }
}
