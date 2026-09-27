package org.example.productjdbc.Controller;

import org.example.productjdbc.Model.*;
import org.example.productjdbc.Service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AppController {

    @Autowired
    private AppService service;

    // =========================================================================
    // 1. CONTACT MASTER (CITIZEN, CONTRACTOR, UTILITY)
    // =========================================================================
    @GetMapping("/contacts")
    public List<Contact> getAllContacts(@RequestParam(required = false) String type) {
        if (type != null && !type.isEmpty()) {
            return service.getContactsByType(type);
        }
        return service.getAllContacts();
    }

    @PostMapping("/citizens")
    public ResponseEntity<Map<String, Object>> registerCitizen(@RequestBody Contact contact) {
        contact.setContactType("CITIZEN");
        int id = service.createContact(contact);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Citizen registered successfully");
        resp.put("citizenId", id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/contacts")
    public ResponseEntity<Map<String, Object>> createContact(@RequestBody Contact contact) {
        int id = service.createContact(contact);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Contact created successfully");
        resp.put("contactId", id);
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 2. DEPARTMENT MASTER (ANALYTIC ACCOUNTS)
    // =========================================================================
    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return service.getAllDepartments();
    }

    @PostMapping("/departments")
    public ResponseEntity<Map<String, Object>> createDepartment(@RequestBody Department dept) {
        int id = service.createDepartment(dept);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Department created successfully");
        resp.put("departmentId", id);
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 3. PRODUCT & SERVICE MASTER
    // =========================================================================
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product prod) {
        int id = service.createProduct(prod);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Product/Service created successfully");
        resp.put("productId", id);
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 4. CHART OF ACCOUNTS & JOURNALS
    // =========================================================================
    @GetMapping("/accounts")
    public List<ChartOfAccount> getAllAccounts() {
        return service.getAllAccounts();
    }

    @GetMapping("/journals")
    public List<Journal> getAllJournals() {
        return service.getAllJournals();
    }

    @GetMapping("/journal-entries")
    public List<JournalEntry> getAllJournalEntries() {
        return service.getAllJournalEntries();
    }

    // =========================================================================
    // 5. GRIEVANCES & SLA ESCALATION
    // =========================================================================
    @GetMapping("/grievances")
    public List<Grievance> getAllGrievances() {
        return service.getAllGrievances();
    }

    @PostMapping("/grievances")
    public ResponseEntity<Map<String, Object>> lodgeGrievance(@RequestBody Grievance grievance) {
        int id = service.lodgeGrievance(grievance);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Grievance ticket lodged successfully. SLA 72-hour window initiated.");
        resp.put("grievanceId", id);
        resp.put("ticketNumber", grievance.getTicketNumber());
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/grievances/{id}/status")
    public ResponseEntity<Map<String, Object>> updateGrievanceStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        String notes = body.getOrDefault("notes", "");
        service.updateGrievanceStatus(id, status, notes);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Grievance status updated to " + status);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/grievances/escalate-check")
    public ResponseEntity<Map<String, Object>> triggerSlaCheck() {
        int count = service.runSlaEscalationCheck();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("escalatedCount", count);
        resp.put("message", count + " grievances escalated past 72-hour SLA window");
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 6. PURCHASE ORDERS
    // =========================================================================
    @GetMapping("/purchase-orders")
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return service.getAllPurchaseOrders();
    }

    @PostMapping("/purchase-orders")
    public ResponseEntity<Map<String, Object>> createPurchaseOrder(@RequestBody PurchaseOrder po) {
        int id = service.createPurchaseOrder(po);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Repair Purchase Order created successfully");
        resp.put("poId", id);
        resp.put("poNumber", po.getPoNumber());
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 7. VENDOR BILLS
    // =========================================================================
    @GetMapping("/vendor-bills")
    public List<VendorBill> getAllVendorBills() {
        return service.getAllVendorBills();
    }

    @PostMapping("/purchase-orders/{id}/convert-to-bill")
    public ResponseEntity<Map<String, Object>> convertToBill(@PathVariable int id) {
        int billId = service.convertPoToVendorBill(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "PO converted to Vendor Bill and repair expenses ledger posted");
        resp.put("billId", billId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/vendor-bills/{id}/pay")
    public ResponseEntity<Map<String, Object>> payBill(
            @PathVariable int id,
            @RequestBody(required = false) Map<String, String> body) {
        String method = (body != null && body.containsKey("paymentMethod")) ? body.get("paymentMethod") : "Bank Transfer";
        int paymentId = service.payVendorBill(id, method);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Vendor Bill paid via Bank and journal entries posted");
        resp.put("paymentId", paymentId);
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 8. CUSTOMER INVOICES / PERMIT FEES
    // =========================================================================
    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return service.getAllInvoices();
    }

    @PostMapping("/invoices")
    public ResponseEntity<Map<String, Object>> createInvoice(@RequestBody Invoice inv) {
        int id = service.createInvoice(inv);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Civic Fee Invoice created successfully");
        resp.put("invoiceId", id);
        resp.put("invoiceNumber", inv.getInvoiceNumber());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<Map<String, Object>> payInvoice(
            @PathVariable int id,
            @RequestBody(required = false) Map<String, String> body) {
        String method = (body != null && body.containsKey("paymentMethod")) ? body.get("paymentMethod") : "Online Gateway";
        int paymentId = service.collectInvoicePayment(id, method);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Permit Fee payment collected and revenue ledger posted");
        resp.put("paymentId", paymentId);
        return ResponseEntity.ok(resp);
    }

    // =========================================================================
    // 9. PAYMENTS
    // =========================================================================
    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return service.getAllPayments();
    }

    // =========================================================================
    // 10. FINANCIAL & BUDGET REPORTS
    // =========================================================================
    @GetMapping("/reports/balance-sheet")
    public ReportDTO.BalanceSheet getBalanceSheet() {
        return service.getBalanceSheet();
    }

    @GetMapping("/reports/profit-and-loss")
    public ReportDTO.ProfitAndLoss getProfitAndLoss() {
        return service.getProfitAndLoss();
    }

    @GetMapping("/reports/budget")
    public List<ReportDTO.DepartmentBudgetReport> getDepartmentBudgetReports() {
        return service.getDepartmentBudgetReports();
    }
}
