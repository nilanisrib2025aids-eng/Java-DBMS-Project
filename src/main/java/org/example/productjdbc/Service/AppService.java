package org.example.productjdbc.Service;

import org.example.productjdbc.Model.*;
import org.example.productjdbc.Repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class AppService {

    @Autowired
    private AppRepository repository;

    // =========================================================================
    // MASTER DATA OPERATIONS
    // =========================================================================
    public List<Contact> getAllContacts() { return repository.findAllContacts(); }
    public List<Contact> getContactsByType(String type) { return repository.findContactsByType(type); }
    public Contact getContactById(int id) { return repository.findContactById(id); }
    public int createContact(Contact contact) { return repository.saveContact(contact); }

    public List<Department> getAllDepartments() { return repository.findAllDepartments(); }
    public Department getDepartmentById(int id) { return repository.findDepartmentById(id); }
    public int createDepartment(Department department) { return repository.saveDepartment(department); }

    public List<Product> getAllProducts() { return repository.findAllProducts(); }
    public Product getProductById(int id) { return repository.findProductById(id); }
    public int createProduct(Product product) { return repository.saveProduct(product); }

    public List<ChartOfAccount> getAllAccounts() { return repository.findAllAccounts(); }
    public List<Journal> getAllJournals() { return repository.findAllJournals(); }
    public List<JournalEntry> getAllJournalEntries() { return repository.findAllJournalEntries(); }

    // =========================================================================
    // GRIEVANCE REDRESSAL & SLA ESCALATION WORKFLOW
    // =========================================================================
    public List<Grievance> getAllGrievances() { return repository.findAllGrievances(); }
    public Grievance getGrievanceById(int id) { return repository.findGrievanceById(id); }
    public int lodgeGrievance(Grievance grievance) { return repository.saveGrievance(grievance); }
    public void updateGrievanceStatus(int id, String status, String notes) {
        repository.updateGrievanceStatus(id, status, notes);
    }

    // SLA Escalation Engine: runs automatically every 60 seconds (also on-demand callable)
    @Scheduled(fixedRate = 60000)
    public int runSlaEscalationCheck() {
        int escalatedCount = repository.autoEscalateExpiredGrievances();
        if (escalatedCount > 0) {
            System.out.println("[SLA ESCALATION ENGINE] Automatically escalated " + escalatedCount + " unaddressed grievances exceeding 72h SLA.");
        }
        return escalatedCount;
    }

    // =========================================================================
    // TRANSACTION FLOW: PURCHASE ORDER -> VENDOR BILL -> PAYMENT & LEDGER
    // =========================================================================
    public List<PurchaseOrder> getAllPurchaseOrders() { return repository.findAllPurchaseOrders(); }
    public PurchaseOrder getPurchaseOrderById(int id) { return repository.findPurchaseOrderById(id); }

    public int createPurchaseOrder(PurchaseOrder po) {
        return repository.savePurchaseOrder(po);
    }

    @Transactional
    public int convertPoToVendorBill(int poId) {
        PurchaseOrder po = repository.findPurchaseOrderById(poId);
        if (po == null) {
            throw new IllegalArgumentException("Purchase Order not found: " + poId);
        }

        // 1. Create Vendor Bill
        VendorBill bill = new VendorBill();
        bill.setPoId(po.getId());
        bill.setContractorId(po.getContractorId());
        bill.setAmount(po.getAmount());
        int billId = repository.saveVendorBill(bill);

        // 2. Mark PO as BILLED
        repository.updatePurchaseOrderStatus(poId, "BILLED");

        // 3. Post double-entry Journal Entry for Vendor Bill:
        // Debit: Civic Repair & Maintenance Expenses (5001)
        // Credit: Contractor Payables (2001)
        ChartOfAccount expenseAccount = repository.findAccountByCode("5001");
        ChartOfAccount liabilityAccount = repository.findAccountByCode("2001");

        if (expenseAccount != null && liabilityAccount != null) {
            // Journal 1 = Purchase Journal
            repository.saveJournalEntry(1, expenseAccount.getId(), po.getAmount(), BigDecimal.ZERO,
                    "BILL-" + billId, "Contractor Repair Cost accrued for PO " + po.getPoNumber());
            repository.saveJournalEntry(1, liabilityAccount.getId(), BigDecimal.ZERO, po.getAmount(),
                    "BILL-" + billId, "Payable accrued for Contractor: " + po.getContractorName());

            // Update Account Balances
            repository.updateAccountBalance(expenseAccount.getId(), po.getAmount());
            repository.updateAccountBalance(liabilityAccount.getId(), po.getAmount());
        }

        return billId;
    }

    public List<VendorBill> getAllVendorBills() { return repository.findAllVendorBills(); }

    @Transactional
    public int payVendorBill(int billId, String paymentMethod) {
        VendorBill bill = repository.findVendorBillById(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Vendor Bill not found: " + billId);
        }
        if ("PAID".equalsIgnoreCase(bill.getStatus())) {
            throw new IllegalStateException("Vendor Bill is already paid.");
        }

        // 1. Record Payment
        Payment p = new Payment();
        p.setPaymentType("PAYMENT_OUT");
        p.setReferenceBillId(bill.getId());
        p.setContactId(bill.getContractorId());
        p.setAmount(bill.getAmount());
        p.setPaymentMethod(paymentMethod != null ? paymentMethod : "Bank Transfer");
        int paymentId = repository.savePayment(p);

        // 2. Update Vendor Bill Status
        repository.updateVendorBillStatus(billId, "PAID");

        // 3. Post double-entry Journal Entry:
        // Debit: Contractor Payables (2001)
        // Credit: Municipal Treasury Fund (Bank) (1001)
        ChartOfAccount liabilityAccount = repository.findAccountByCode("2001");
        ChartOfAccount bankAccount = repository.findAccountByCode("1001");

        if (liabilityAccount != null && bankAccount != null) {
            // Journal 3 = Bank Journal
            repository.saveJournalEntry(3, liabilityAccount.getId(), bill.getAmount(), BigDecimal.ZERO,
                    "PAY-" + paymentId, "Payable liquidated for Vendor Bill " + bill.getBillNumber());
            repository.saveJournalEntry(3, bankAccount.getId(), BigDecimal.ZERO, bill.getAmount(),
                    "PAY-" + paymentId, "Disbursement from Treasury Bank for " + bill.getContractorName());

            // Update Account Balances
            repository.updateAccountBalance(liabilityAccount.getId(), bill.getAmount().negate());
            repository.updateAccountBalance(bankAccount.getId(), bill.getAmount().negate());
        }

        return paymentId;
    }

    // =========================================================================
    // TRANSACTION FLOW: CIVIC PERMIT FEE -> INVOICE -> PAYMENT COLLECTION
    // =========================================================================
    public List<Invoice> getAllInvoices() { return repository.findAllInvoices(); }

    public int createInvoice(Invoice inv) {
        return repository.saveInvoice(inv);
    }

    @Transactional
    public int collectInvoicePayment(int invoiceId, String paymentMethod) {
        Invoice inv = repository.findInvoiceById(invoiceId);
        if (inv == null) {
            throw new IllegalArgumentException("Invoice not found: " + invoiceId);
        }
        if ("PAID".equalsIgnoreCase(inv.getStatus())) {
            throw new IllegalStateException("Invoice is already paid.");
        }

        // 1. Record Payment
        Payment p = new Payment();
        p.setPaymentType("PAYMENT_IN");
        p.setReferenceInvoiceId(inv.getId());
        p.setContactId(inv.getCitizenId());
        p.setAmount(inv.getAmount());
        p.setPaymentMethod(paymentMethod != null ? paymentMethod : "Online Gateway");
        int paymentId = repository.savePayment(p);

        // 2. Mark Invoice as PAID
        repository.updateInvoiceStatus(invoiceId, "PAID");

        // 3. Post double-entry Journal Entry:
        // Debit: Municipal Treasury Fund (Bank) (1001)
        // Credit: Civic Fines & Permit Revenue (4001)
        ChartOfAccount bankAccount = repository.findAccountByCode("1001");
        ChartOfAccount revenueAccount = repository.findAccountByCode("4001");

        if (bankAccount != null && revenueAccount != null) {
            // Journal 2 = Fines/Revenue Journal
            repository.saveJournalEntry(2, bankAccount.getId(), inv.getAmount(), BigDecimal.ZERO,
                    "PAY-" + paymentId, "Civic Permit Fee collection deposited into Treasury Bank");
            repository.saveJournalEntry(2, revenueAccount.getId(), BigDecimal.ZERO, inv.getAmount(),
                    "PAY-" + paymentId, "Civic Fee Income recognized from Citizen ID: " + inv.getCitizenId());

            // Update Account Balances
            repository.updateAccountBalance(bankAccount.getId(), inv.getAmount());
            repository.updateAccountBalance(revenueAccount.getId(), inv.getAmount());
        }

        return paymentId;
    }

    public List<Payment> getAllPayments() { return repository.findAllPayments(); }

    // =========================================================================
    // FINANCIAL & BUDGET REPORTING
    // =========================================================================
    public ReportDTO.BalanceSheet getBalanceSheet() {
        List<ChartOfAccount> accounts = repository.findAllAccounts();
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        List<Map<String, Object>> assetList = new ArrayList<>();
        List<Map<String, Object>> liabilityList = new ArrayList<>();

        for (ChartOfAccount acc : accounts) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", acc.getCode());
            map.put("name", acc.getName());
            map.put("balance", acc.getBalance());

            if ("ASSET".equalsIgnoreCase(acc.getAccountType())) {
                totalAssets = totalAssets.add(acc.getBalance());
                assetList.add(map);
            } else if ("LIABILITY".equalsIgnoreCase(acc.getAccountType())) {
                totalLiabilities = totalLiabilities.add(acc.getBalance());
                liabilityList.add(map);
            }
        }

        ReportDTO.BalanceSheet bs = new ReportDTO.BalanceSheet();
        bs.setTotalAssets(totalAssets);
        bs.setTotalLiabilities(totalLiabilities);
        bs.setNetMunicipalEquity(totalAssets.subtract(totalLiabilities));
        bs.setAssetAccounts(assetList);
        bs.setLiabilityAccounts(liabilityList);
        return bs;
    }

    public ReportDTO.ProfitAndLoss getProfitAndLoss() {
        List<ChartOfAccount> accounts = repository.findAllAccounts();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        List<Map<String, Object>> incomeList = new ArrayList<>();
        List<Map<String, Object>> expenseList = new ArrayList<>();

        for (ChartOfAccount acc : accounts) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", acc.getCode());
            map.put("name", acc.getName());
            map.put("balance", acc.getBalance());

            if ("INCOME".equalsIgnoreCase(acc.getAccountType())) {
                totalIncome = totalIncome.add(acc.getBalance());
                incomeList.add(map);
            } else if ("EXPENSE".equalsIgnoreCase(acc.getAccountType())) {
                totalExpenses = totalExpenses.add(acc.getBalance());
                expenseList.add(map);
            }
        }

        ReportDTO.ProfitAndLoss pnl = new ReportDTO.ProfitAndLoss();
        pnl.setTotalIncome(totalIncome);
        pnl.setTotalExpenses(totalExpenses);
        pnl.setNetSurplusOrDeficit(totalIncome.subtract(totalExpenses));
        pnl.setIncomeItems(incomeList);
        pnl.setExpenseItems(expenseList);
        return pnl;
    }

    public List<ReportDTO.DepartmentBudgetReport> getDepartmentBudgetReports() {
        List<Department> departments = repository.findAllDepartments();
        List<PurchaseOrder> pos = repository.findAllPurchaseOrders();
        List<Grievance> grievances = repository.findAllGrievances();

        List<ReportDTO.DepartmentBudgetReport> list = new ArrayList<>();

        for (Department d : departments) {
            ReportDTO.DepartmentBudgetReport r = new ReportDTO.DepartmentBudgetReport();
            r.setDepartmentId(d.getId());
            r.setDepartmentName(d.getName());
            r.setDepartmentCode(d.getCode());
            r.setHeadOfficer(d.getHeadOfficer());
            r.setAllocatedBudget(d.getAllocatedBudget() != null ? d.getAllocatedBudget() : BigDecimal.ZERO);

            BigDecimal spent = BigDecimal.ZERO;
            for (PurchaseOrder po : pos) {
                if (Objects.equals(po.getDepartmentId(), d.getId()) && !"CANCELLED".equalsIgnoreCase(po.getStatus())) {
                    spent = spent.add(po.getAmount());
                }
            }
            r.setActualSpent(spent);
            r.setRemainingBudget(r.getAllocatedBudget().subtract(spent));

            if (r.getAllocatedBudget().compareTo(BigDecimal.ZERO) > 0) {
                double pct = spent.divide(r.getAllocatedBudget(), 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
                r.setBudgetUtilizationPct(Math.round(pct * 100.0) / 100.0);
            } else {
                r.setBudgetUtilizationPct(0.0);
            }

            int total = 0;
            int resolved = 0;
            int escalated = 0;
            for (Grievance g : grievances) {
                if (Objects.equals(g.getDepartmentId(), d.getId())) {
                    total++;
                    if ("RESOLVED".equalsIgnoreCase(g.getStatus()) || "CLOSED".equalsIgnoreCase(g.getStatus())) {
                        resolved++;
                    }
                    if ("ESCALATED".equalsIgnoreCase(g.getEscalationStatus())) {
                        escalated++;
                    }
                }
            }
            r.setTotalGrievances(total);
            r.setResolvedGrievances(resolved);
            r.setEscalatedGrievances(escalated);

            list.add(r);
        }
        return list;
    }
}
