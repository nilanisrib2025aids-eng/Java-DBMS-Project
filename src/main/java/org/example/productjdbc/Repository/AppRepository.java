package org.example.productjdbc.Repository;

import org.example.productjdbc.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Repository
public class AppRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =========================================================================
    // CONTACT MASTER REPOSITORY
    // =========================================================================
    private final RowMapper<Contact> contactRowMapper = (rs, rowNum) -> {
        Contact c = new Contact();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setContactType(rs.getString("contact_type"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    };

    public List<Contact> findAllContacts() {
        return jdbcTemplate.query("SELECT * FROM contacts ORDER BY id DESC", contactRowMapper);
    }

    public List<Contact> findContactsByType(String type) {
        return jdbcTemplate.query("SELECT * FROM contacts WHERE contact_type = ? ORDER BY name ASC", contactRowMapper, type);
    }

    public Contact findContactById(int id) {
        List<Contact> list = jdbcTemplate.query("SELECT * FROM contacts WHERE id = ?", contactRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, contact_type, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getContactType());
            ps.setString(3, contact.getEmail());
            ps.setString(4, contact.getPhone());
            ps.setString(5, contact.getAddress());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    // =========================================================================
    // DEPARTMENT MASTER REPOSITORY
    // =========================================================================
    private final RowMapper<Department> departmentRowMapper = (rs, rowNum) -> {
        Department d = new Department();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setCode(rs.getString("code"));
        d.setHeadOfficer(rs.getString("head_officer"));
        d.setAllocatedBudget(rs.getBigDecimal("allocated_budget"));
        return d;
    };

    public List<Department> findAllDepartments() {
        return jdbcTemplate.query("SELECT * FROM departments ORDER BY id ASC", departmentRowMapper);
    }

    public Department findDepartmentById(int id) {
        List<Department> list = jdbcTemplate.query("SELECT * FROM departments WHERE id = ?", departmentRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveDepartment(Department d) {
        String sql = "INSERT INTO departments (name, code, head_officer, allocated_budget) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getName());
            ps.setString(2, d.getCode());
            ps.setString(3, d.getHeadOfficer());
            ps.setBigDecimal(4, d.getAllocatedBudget() != null ? d.getAllocatedBudget() : BigDecimal.ZERO);
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    // =========================================================================
    // PRODUCT / SERVICE MASTER REPOSITORY
    // =========================================================================
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setProductType(rs.getString("product_type"));
        p.setStandardRate(rs.getBigDecimal("standard_rate"));
        p.setDepartmentId(rs.getInt("department_id"));
        p.setDepartmentName(rs.getString("dept_name"));
        return p;
    };

    public List<Product> findAllProducts() {
        String sql = "SELECT p.*, d.name AS dept_name FROM products p " +
                     "LEFT JOIN departments d ON p.department_id = d.id ORDER BY p.id ASC";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Product findProductById(int id) {
        String sql = "SELECT p.*, d.name AS dept_name FROM products p " +
                     "LEFT JOIN departments d ON p.department_id = d.id WHERE p.id = ?";
        List<Product> list = jdbcTemplate.query(sql, productRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveProduct(Product p) {
        String sql = "INSERT INTO products (name, product_type, standard_rate, department_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getProductType());
            ps.setBigDecimal(3, p.getStandardRate() != null ? p.getStandardRate() : BigDecimal.ZERO);
            if (p.getDepartmentId() != null) {
                ps.setInt(4, p.getDepartmentId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    // =========================================================================
    // CHART OF ACCOUNTS REPOSITORY
    // =========================================================================
    private final RowMapper<ChartOfAccount> coaRowMapper = (rs, rowNum) -> {
        ChartOfAccount c = new ChartOfAccount();
        c.setId(rs.getInt("id"));
        c.setCode(rs.getString("code"));
        c.setName(rs.getString("name"));
        c.setAccountType(rs.getString("account_type"));
        c.setBalance(rs.getBigDecimal("balance"));
        return c;
    };

    public List<ChartOfAccount> findAllAccounts() {
        return jdbcTemplate.query("SELECT * FROM chart_of_accounts ORDER BY code ASC", coaRowMapper);
    }

    public ChartOfAccount findAccountById(int id) {
        List<ChartOfAccount> list = jdbcTemplate.query("SELECT * FROM chart_of_accounts WHERE id = ?", coaRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public ChartOfAccount findAccountByCode(String code) {
        List<ChartOfAccount> list = jdbcTemplate.query("SELECT * FROM chart_of_accounts WHERE code = ?", coaRowMapper, code);
        return list.isEmpty() ? null : list.get(0);
    }

    public void updateAccountBalance(int accountId, BigDecimal balanceDelta) {
        jdbcTemplate.update("UPDATE chart_of_accounts SET balance = balance + ? WHERE id = ?", balanceDelta, accountId);
    }

    // =========================================================================
    // GRIEVANCES REPOSITORY & SLA ESCALATION
    // =========================================================================
    private final RowMapper<Grievance> grievanceRowMapper = (rs, rowNum) -> {
        Grievance g = new Grievance();
        g.setId(rs.getInt("id"));
        g.setTicketNumber(rs.getString("ticket_number"));
        g.setCitizenId(rs.getInt("citizen_id"));
        g.setCitizenName(rs.getString("citizen_name"));
        g.setDepartmentId(rs.getInt("department_id"));
        g.setDepartmentName(rs.getString("department_name"));
        g.setProductId(rs.getInt("product_id"));
        g.setProductName(rs.getString("product_name"));
        g.setTitle(rs.getString("title"));
        g.setDescription(rs.getString("description"));
        g.setLocation(rs.getString("location"));
        g.setStatus(rs.getString("status"));
        g.setEscalationStatus(rs.getString("escalation_status"));
        g.setCreatedAt(rs.getTimestamp("created_at"));
        g.setSlaDeadline(rs.getTimestamp("sla_deadline"));
        g.setResolvedAt(rs.getTimestamp("resolved_at"));
        g.setResolutionNotes(rs.getString("resolution_notes"));
        return g;
    };

    public List<Grievance> findAllGrievances() {
        String sql = "SELECT g.*, c.name AS citizen_name, d.name AS department_name, p.name AS product_name " +
                     "FROM grievances g " +
                     "JOIN contacts c ON g.citizen_id = c.id " +
                     "JOIN departments d ON g.department_id = d.id " +
                     "LEFT JOIN products p ON g.product_id = p.id " +
                     "ORDER BY g.id DESC";
        return jdbcTemplate.query(sql, grievanceRowMapper);
    }

    public Grievance findGrievanceById(int id) {
        String sql = "SELECT g.*, c.name AS citizen_name, d.name AS department_name, p.name AS product_name " +
                     "FROM grievances g " +
                     "JOIN contacts c ON g.citizen_id = c.id " +
                     "JOIN departments d ON g.department_id = d.id " +
                     "LEFT JOIN products p ON g.product_id = p.id " +
                     "WHERE g.id = ?";
        List<Grievance> list = jdbcTemplate.query(sql, grievanceRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveGrievance(Grievance g) {
        String ticketNumber = "GRV-" + System.currentTimeMillis();
        g.setTicketNumber(ticketNumber);
        String sql = "INSERT INTO grievances (ticket_number, citizen_id, department_id, product_id, title, description, location, status, escalation_status, sla_deadline) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'SUBMITTED', 'NORMAL', DATE_ADD(NOW(), INTERVAL 72 HOUR))";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, g.getTicketNumber());
            ps.setInt(2, g.getCitizenId());
            ps.setInt(3, g.getDepartmentId());
            if (g.getProductId() != null) {
                ps.setInt(4, g.getProductId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, g.getTitle());
            ps.setString(6, g.getDescription());
            ps.setString(7, g.getLocation());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    public void updateGrievanceStatus(int id, String status, String notes) {
        if ("RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status)) {
            jdbcTemplate.update("UPDATE grievances SET status = ?, resolution_notes = ?, resolved_at = NOW() WHERE id = ?",
                    status, notes, id);
        } else {
            jdbcTemplate.update("UPDATE grievances SET status = ?, resolution_notes = ? WHERE id = ?",
                    status, notes, id);
        }
    }

    public int autoEscalateExpiredGrievances() {
        String sql = "UPDATE grievances SET escalation_status = 'ESCALATED' " +
                     "WHERE status IN ('SUBMITTED', 'IN_PROGRESS') " +
                     "AND escalation_status = 'NORMAL' " +
                     "AND NOW() > sla_deadline";
        return jdbcTemplate.update(sql);
    }

    // =========================================================================
    // PURCHASE ORDERS REPOSITORY
    // =========================================================================
    private final RowMapper<PurchaseOrder> poRowMapper = (rs, rowNum) -> {
        PurchaseOrder po = new PurchaseOrder();
        po.setId(rs.getInt("id"));
        po.setPoNumber(rs.getString("po_number"));
        po.setGrievanceId(rs.getInt("grievance_id"));
        po.setGrievanceTicket(rs.getString("grievance_ticket"));
        po.setContractorId(rs.getInt("contractor_id"));
        po.setContractorName(rs.getString("contractor_name"));
        po.setDepartmentId(rs.getInt("department_id"));
        po.setDepartmentName(rs.getString("dept_name"));
        po.setProductId(rs.getInt("product_id"));
        po.setProductName(rs.getString("product_name"));
        po.setAmount(rs.getBigDecimal("amount"));
        po.setStatus(rs.getString("status"));
        po.setCreatedAt(rs.getTimestamp("created_at"));
        return po;
    };

    public List<PurchaseOrder> findAllPurchaseOrders() {
        String sql = "SELECT po.*, g.ticket_number AS grievance_ticket, c.name AS contractor_name, " +
                     "d.name AS dept_name, p.name AS product_name " +
                     "FROM purchase_orders po " +
                     "LEFT JOIN grievances g ON po.grievance_id = g.id " +
                     "JOIN contacts c ON po.contractor_id = c.id " +
                     "JOIN departments d ON po.department_id = d.id " +
                     "LEFT JOIN products p ON po.product_id = p.id " +
                     "ORDER BY po.id DESC";
        return jdbcTemplate.query(sql, poRowMapper);
    }

    public PurchaseOrder findPurchaseOrderById(int id) {
        String sql = "SELECT po.*, g.ticket_number AS grievance_ticket, c.name AS contractor_name, " +
                     "d.name AS dept_name, p.name AS product_name " +
                     "FROM purchase_orders po " +
                     "LEFT JOIN grievances g ON po.grievance_id = g.id " +
                     "JOIN contacts c ON po.contractor_id = c.id " +
                     "JOIN departments d ON po.department_id = d.id " +
                     "LEFT JOIN products p ON po.product_id = p.id " +
                     "WHERE po.id = ?";
        List<PurchaseOrder> list = jdbcTemplate.query(sql, poRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int savePurchaseOrder(PurchaseOrder po) {
        String poNumber = "PO-" + System.currentTimeMillis();
        po.setPoNumber(poNumber);
        String sql = "INSERT INTO purchase_orders (po_number, grievance_id, contractor_id, department_id, product_id, amount, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'CONFIRMED')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, po.getPoNumber());
            if (po.getGrievanceId() != null) ps.setInt(2, po.getGrievanceId()); else ps.setNull(2, Types.INTEGER);
            ps.setInt(3, po.getContractorId());
            ps.setInt(4, po.getDepartmentId());
            if (po.getProductId() != null) ps.setInt(5, po.getProductId()); else ps.setNull(5, Types.INTEGER);
            ps.setBigDecimal(6, po.getAmount());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    public void updatePurchaseOrderStatus(int id, String status) {
        jdbcTemplate.update("UPDATE purchase_orders SET status = ? WHERE id = ?", status, id);
    }

    // =========================================================================
    // VENDOR BILLS REPOSITORY
    // =========================================================================
    private final RowMapper<VendorBill> vendorBillRowMapper = (rs, rowNum) -> {
        VendorBill vb = new VendorBill();
        vb.setId(rs.getInt("id"));
        vb.setBillNumber(rs.getString("bill_number"));
        vb.setPoId(rs.getInt("po_id"));
        vb.setPoNumber(rs.getString("po_number"));
        vb.setContractorId(rs.getInt("contractor_id"));
        vb.setContractorName(rs.getString("contractor_name"));
        vb.setAmount(rs.getBigDecimal("amount"));
        vb.setStatus(rs.getString("status"));
        vb.setCreatedAt(rs.getTimestamp("created_at"));
        return vb;
    };

    public List<VendorBill> findAllVendorBills() {
        String sql = "SELECT vb.*, po.po_number, c.name AS contractor_name " +
                     "FROM vendor_bills vb " +
                     "JOIN purchase_orders po ON vb.po_id = po.id " +
                     "JOIN contacts c ON vb.contractor_id = c.id " +
                     "ORDER BY vb.id DESC";
        return jdbcTemplate.query(sql, vendorBillRowMapper);
    }

    public VendorBill findVendorBillById(int id) {
        String sql = "SELECT vb.*, po.po_number, c.name AS contractor_name " +
                     "FROM vendor_bills vb " +
                     "JOIN purchase_orders po ON vb.po_id = po.id " +
                     "JOIN contacts c ON vb.contractor_id = c.id " +
                     "WHERE vb.id = ?";
        List<VendorBill> list = jdbcTemplate.query(sql, vendorBillRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveVendorBill(VendorBill bill) {
        String billNumber = "BILL-" + System.currentTimeMillis();
        bill.setBillNumber(billNumber);
        String sql = "INSERT INTO vendor_bills (bill_number, po_id, contractor_id, amount, status) VALUES (?, ?, ?, ?, 'POSTED')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bill.getBillNumber());
            ps.setInt(2, bill.getPoId());
            ps.setInt(3, bill.getContractorId());
            ps.setBigDecimal(4, bill.getAmount());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    public void updateVendorBillStatus(int id, String status) {
        jdbcTemplate.update("UPDATE vendor_bills SET status = ? WHERE id = ?", status, id);
    }

    // =========================================================================
    // INVOICES (PERMIT FEES) REPOSITORY
    // =========================================================================
    private final RowMapper<Invoice> invoiceRowMapper = (rs, rowNum) -> {
        Invoice inv = new Invoice();
        inv.setId(rs.getInt("id"));
        inv.setInvoiceNumber(rs.getString("invoice_number"));
        inv.setCitizenId(rs.getInt("citizen_id"));
        inv.setCitizenName(rs.getString("citizen_name"));
        inv.setDepartmentId(rs.getInt("department_id"));
        inv.setDepartmentName(rs.getString("dept_name"));
        inv.setFeeType(rs.getString("fee_type"));
        inv.setAmount(rs.getBigDecimal("amount"));
        inv.setStatus(rs.getString("status"));
        inv.setCreatedAt(rs.getTimestamp("created_at"));
        return inv;
    };

    public List<Invoice> findAllInvoices() {
        String sql = "SELECT inv.*, c.name AS citizen_name, d.name AS dept_name " +
                     "FROM invoices inv " +
                     "JOIN contacts c ON inv.citizen_id = c.id " +
                     "JOIN departments d ON inv.department_id = d.id " +
                     "ORDER BY inv.id DESC";
        return jdbcTemplate.query(sql, invoiceRowMapper);
    }

    public Invoice findInvoiceById(int id) {
        String sql = "SELECT inv.*, c.name AS citizen_name, d.name AS dept_name " +
                     "FROM invoices inv " +
                     "JOIN contacts c ON inv.citizen_id = c.id " +
                     "JOIN departments d ON inv.department_id = d.id " +
                     "WHERE inv.id = ?";
        List<Invoice> list = jdbcTemplate.query(sql, invoiceRowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int saveInvoice(Invoice inv) {
        String invNumber = "INV-" + System.currentTimeMillis();
        inv.setInvoiceNumber(invNumber);
        String sql = "INSERT INTO invoices (invoice_number, citizen_id, department_id, fee_type, amount, status) VALUES (?, ?, ?, ?, ?, 'UNPAID')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, inv.getInvoiceNumber());
            ps.setInt(2, inv.getCitizenId());
            ps.setInt(3, inv.getDepartmentId());
            ps.setString(4, inv.getFeeType());
            ps.setBigDecimal(5, inv.getAmount());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    public void updateInvoiceStatus(int id, String status) {
        jdbcTemplate.update("UPDATE invoices SET status = ? WHERE id = ?", status, id);
    }

    // =========================================================================
    // PAYMENTS REPOSITORY
    // =========================================================================
    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setPaymentNumber(rs.getString("payment_number"));
        p.setPaymentType(rs.getString("payment_type"));
        p.setReferenceBillId(rs.getInt("reference_bill_id"));
        p.setReferenceInvoiceId(rs.getInt("reference_invoice_id"));
        p.setContactId(rs.getInt("contact_id"));
        p.setContactName(rs.getString("contact_name"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentDate(rs.getTimestamp("payment_date"));
        p.setPaymentMethod(rs.getString("payment_method"));
        return p;
    };

    public List<Payment> findAllPayments() {
        String sql = "SELECT p.*, c.name AS contact_name FROM payments p " +
                     "JOIN contacts c ON p.contact_id = c.id " +
                     "ORDER BY p.id DESC";
        return jdbcTemplate.query(sql, paymentRowMapper);
    }

    public int savePayment(Payment payment) {
        String pNumber = "PAY-" + System.currentTimeMillis();
        payment.setPaymentNumber(pNumber);
        String sql = "INSERT INTO payments (payment_number, payment_type, reference_bill_id, reference_invoice_id, contact_id, amount, payment_method) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, payment.getPaymentNumber());
            ps.setString(2, payment.getPaymentType());
            if (payment.getReferenceBillId() != null) ps.setInt(3, payment.getReferenceBillId()); else ps.setNull(3, Types.INTEGER);
            if (payment.getReferenceInvoiceId() != null) ps.setInt(4, payment.getReferenceInvoiceId()); else ps.setNull(4, Types.INTEGER);
            ps.setInt(5, payment.getContactId());
            ps.setBigDecimal(6, payment.getAmount());
            ps.setString(7, payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "Bank Transfer");
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    // =========================================================================
    // JOURNALS & JOURNAL ENTRIES REPOSITORY
    // =========================================================================
    private final RowMapper<JournalEntry> journalEntryRowMapper = (rs, rowNum) -> {
        JournalEntry je = new JournalEntry();
        je.setId(rs.getInt("id"));
        je.setEntryNumber(rs.getString("entry_number"));
        je.setJournalId(rs.getInt("journal_id"));
        je.setJournalName(rs.getString("journal_name"));
        je.setAccountId(rs.getInt("account_id"));
        je.setAccountName(rs.getString("account_name"));
        je.setAccountCode(rs.getString("account_code"));
        je.setDebit(rs.getBigDecimal("debit"));
        je.setCredit(rs.getBigDecimal("credit"));
        je.setReference(rs.getString("reference"));
        je.setDescription(rs.getString("description"));
        je.setEntryDate(rs.getTimestamp("entry_date"));
        return je;
    };

    public List<JournalEntry> findAllJournalEntries() {
        String sql = "SELECT je.*, j.name AS journal_name, ca.name AS account_name, ca.code AS account_code " +
                     "FROM journal_entries je " +
                     "JOIN journals j ON je.journal_id = j.id " +
                     "JOIN chart_of_accounts ca ON je.account_id = ca.id " +
                     "ORDER BY je.id DESC";
        return jdbcTemplate.query(sql, journalEntryRowMapper);
    }

    public void saveJournalEntry(int journalId, int accountId, BigDecimal debit, BigDecimal credit, String reference, String description) {
        String entryNumber = "JE-" + System.currentTimeMillis();
        String sql = "INSERT INTO journal_entries (entry_number, journal_id, account_id, debit, credit, reference, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, entryNumber, journalId, accountId, debit, credit, reference, description);
    }

    public List<Journal> findAllJournals() {
        return jdbcTemplate.query("SELECT * FROM journals ORDER BY id ASC", (rs, rowNum) ->
            new Journal(rs.getInt("id"), rs.getString("journal_code"), rs.getString("name"), rs.getString("journal_type"))
        );
    }
}
