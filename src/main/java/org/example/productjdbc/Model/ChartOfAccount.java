package org.example.productjdbc.Model;

import java.math.BigDecimal;

public class ChartOfAccount {
    private Integer id;
    private String code;
    private String name;
    private String accountType; // ASSET, LIABILITY, INCOME, EXPENSE
    private BigDecimal balance;

    public ChartOfAccount() {}

    public ChartOfAccount(Integer id, String code, String name, String accountType, BigDecimal balance) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
