package org.example.productjdbc.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReportDTO {

    // Balance Sheet Report DTO
    public static class BalanceSheet {
        private BigDecimal totalAssets;
        private BigDecimal totalLiabilities;
        private BigDecimal netMunicipalEquity;
        private List<Map<String, Object>> assetAccounts;
        private List<Map<String, Object>> liabilityAccounts;

        public BigDecimal getTotalAssets() { return totalAssets; }
        public void setTotalAssets(BigDecimal totalAssets) { this.totalAssets = totalAssets; }

        public BigDecimal getTotalLiabilities() { return totalLiabilities; }
        public void setTotalLiabilities(BigDecimal totalLiabilities) { this.totalLiabilities = totalLiabilities; }

        public BigDecimal getNetMunicipalEquity() { return netMunicipalEquity; }
        public void setNetMunicipalEquity(BigDecimal netMunicipalEquity) { this.netMunicipalEquity = netMunicipalEquity; }

        public List<Map<String, Object>> getAssetAccounts() { return assetAccounts; }
        public void setAssetAccounts(List<Map<String, Object>> assetAccounts) { this.assetAccounts = assetAccounts; }

        public List<Map<String, Object>> getLiabilityAccounts() { return liabilityAccounts; }
        public void setLiabilityAccounts(List<Map<String, Object>> liabilityAccounts) { this.liabilityAccounts = liabilityAccounts; }
    }

    // Profit & Loss Report DTO
    public static class ProfitAndLoss {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal netSurplusOrDeficit;
        private List<Map<String, Object>> incomeItems;
        private List<Map<String, Object>> expenseItems;

        public BigDecimal getTotalIncome() { return totalIncome; }
        public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

        public BigDecimal getTotalExpenses() { return totalExpenses; }
        public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }

        public BigDecimal getNetSurplusOrDeficit() { return netSurplusOrDeficit; }
        public void setNetSurplusOrDeficit(BigDecimal netSurplusOrDeficit) { this.netSurplusOrDeficit = netSurplusOrDeficit; }

        public List<Map<String, Object>> getIncomeItems() { return incomeItems; }
        public void setIncomeItems(List<Map<String, Object>> incomeItems) { this.incomeItems = incomeItems; }

        public List<Map<String, Object>> getExpenseItems() { return expenseItems; }
        public void setExpenseItems(List<Map<String, Object>> expenseItems) { this.expenseItems = expenseItems; }
    }

    // Departmental Budget & SLA Report DTO
    public static class DepartmentBudgetReport {
        private Integer departmentId;
        private String departmentName;
        private String departmentCode;
        private String headOfficer;
        private BigDecimal allocatedBudget;
        private BigDecimal actualSpent;
        private BigDecimal remainingBudget;
        private double budgetUtilizationPct;
        private int totalGrievances;
        private int resolvedGrievances;
        private int escalatedGrievances;

        public Integer getDepartmentId() { return departmentId; }
        public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

        public String getDepartmentCode() { return departmentCode; }
        public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

        public String getHeadOfficer() { return headOfficer; }
        public void setHeadOfficer(String headOfficer) { this.headOfficer = headOfficer; }

        public BigDecimal getAllocatedBudget() { return allocatedBudget; }
        public void setAllocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; }

        public BigDecimal getActualSpent() { return actualSpent; }
        public void setActualSpent(BigDecimal actualSpent) { this.actualSpent = actualSpent; }

        public BigDecimal getRemainingBudget() { return remainingBudget; }
        public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }

        public double getBudgetUtilizationPct() { return budgetUtilizationPct; }
        public void setBudgetUtilizationPct(double budgetUtilizationPct) { this.budgetUtilizationPct = budgetUtilizationPct; }

        public int getTotalGrievances() { return totalGrievances; }
        public void setTotalGrievances(int totalGrievances) { this.totalGrievances = totalGrievances; }

        public int getResolvedGrievances() { return resolvedGrievances; }
        public void setResolvedGrievances(int resolvedGrievances) { this.resolvedGrievances = resolvedGrievances; }

        public int getEscalatedGrievances() { return escalatedGrievances; }
        public void setEscalatedGrievances(int escalatedGrievances) { this.escalatedGrievances = escalatedGrievances; }
    }
}
