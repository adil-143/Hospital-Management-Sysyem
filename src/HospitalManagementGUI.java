import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// ---------- DB HELPER CLASS ----------
class DBHelper {
    // TODO: change username and password according to your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";      // your MySQL username
    private static final String PASS = "your password";  // your MySQL password


    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "MySQL JDBC Driver not found.\nAdd mysql-connector-j jar to classpath.",
                    "Driver Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

// ---------- MAIN GUI CLASS ----------
public class HospitalManagementGUI extends JFrame {

    private JTextField txtId, txtName, txtAge, txtDisease, txtPhone;
    private JTextField txtSearchId, txtDischargeId;
    private JTable table;
    private DefaultTableModel tableModel;

    public HospitalManagementGUI() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // center screen

        initComponents();
        loadAllPatients(); // show data on start
    }

    // Initialize UI components
    private void initComponents() {
        // --------- TOP PANEL: Patient Form ---------
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Patient"));

        txtId      = new JTextField();
        txtName    = new JTextField();
        txtAge     = new JTextField();
        txtDisease = new JTextField();
        txtPhone   = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Age:"));
        formPanel.add(new JLabel("Disease:"));
        formPanel.add(new JLabel("Phone:"));

        formPanel.add(txtId);
        formPanel.add(txtName);
        formPanel.add(txtAge);
        formPanel.add(txtDisease);
        formPanel.add(txtPhone);

        // --------- MIDDLE PANEL: Buttons ---------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAdd      = new JButton("Add Patient");
        JButton btnViewAll  = new JButton("View All");
        JButton btnSearch   = new JButton("Search by ID");
        JButton btnDischarge = new JButton("Discharge by ID");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDischarge);

        // --------- SEARCH / DISCHARGE FIELDS ---------
        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search / Discharge"));

        txtSearchId    = new JTextField();
        txtDischargeId = new JTextField();

        searchPanel.add(new JLabel("Search Patient ID:"));
        searchPanel.add(txtSearchId);
        searchPanel.add(new JLabel("Discharge Patient ID:"));
        searchPanel.add(txtDischargeId);

        // --------- TABLE ---------
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age", "Disease", "Phone", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table read-only
            }
        };

        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Patients"));

        // --------- ADD EVERYTHING TO FRAME ---------
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        // --------- BUTTON ACTIONS ---------
        btnAdd.addActionListener(e -> addPatient());
        btnViewAll.addActionListener(e -> loadAllPatients());
        btnSearch.addActionListener(e -> searchPatient());
        btnDischarge.addActionListener(e -> dischargePatient());
    }

    // Add a new patient to DB
    private void addPatient() {
        String idStr = txtId.getText().trim();
        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String disease = txtDisease.getText().trim();
        String phone = txtPhone.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || ageStr.isEmpty() || disease.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id, age;
        try {
            id = Integer.parseInt(idStr);
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO patients (id, name, age, disease, phone, admitted) VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, disease);
            ps.setString(5, phone);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully!");

            clearForm();
            loadAllPatients();

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Patient with this ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error while adding patient.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Load all patients into table
    private void loadAllPatients() {
        String sql = "SELECT * FROM patients";

        tableModel.setRowCount(0); // clear table

        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id        = rs.getInt("id");
                String name   = rs.getString("name");
                int age       = rs.getInt("age");
                String disease= rs.getString("disease");
                String phone  = rs.getString("phone");
                boolean admitted = rs.getInt("admitted") == 1;

                String status = admitted ? "Admitted" : "Discharged";

                tableModel.addRow(new Object[]{id, name, age, disease, phone, status});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error while loading patients.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Search patient by ID and show only that in table
    private void searchPatient() {
        String idStr = txtSearchId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Patient ID to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Patient ID must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM patients WHERE id = ?";

        tableModel.setRowCount(0); // clear table

        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int pid       = rs.getInt("id");
                    String name   = rs.getString("name");
                    int age       = rs.getInt("age");
                    String disease= rs.getString("disease");
                    String phone  = rs.getString("phone");
                    boolean admitted = rs.getInt("admitted") == 1;
                    String status = admitted ? "Admitted" : "Discharged";

                    tableModel.addRow(new Object[]{pid, name, age, disease, phone, status});
                } else {
                    JOptionPane.showMessageDialog(this, "No patient found with ID " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error while searching patient.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Discharge patient (set admitted = 0)
    private void dischargePatient() {
        String idStr = txtDischargeId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Patient ID to discharge.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Patient ID must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkSql = "SELECT * FROM patients WHERE id = ?";
        String updateSql = "UPDATE patients SET admitted = 0 WHERE id = ?";

        try (Connection con = DBHelper.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setInt(1, id);

            try (ResultSet rs = checkPs.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "No patient found with ID " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                updatePs.setInt(1, id);
                int rows = updatePs.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Patient discharged successfully!");
                    loadAllPatients();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to discharge patient.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error while discharging patient.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Clear add form fields
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtDisease.setText("");
        txtPhone.setText("");
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalManagementGUI().setVisible(true);
        });
    }
}
