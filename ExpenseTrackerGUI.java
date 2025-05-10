import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private final ExpenseManager manager;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel totalLabel;

    public ExpenseTrackerGUI() {
        manager = new ExpenseManager();

        setTitle("Expense Tracker");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Expense Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Improved Icons
        ImageIcon editIcon = loadIcon("images/edit.png");
        ImageIcon deleteIcon = loadIcon("images/delete.png");

        // Table setup
        tableModel = new DefaultTableModel(new String[] { "ID", "Category", "Amount", "Description", "Date", "", "" },
                0) {
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);

        // Enable sorting
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);

        // Sort amount numerically
        sorter.setComparator(2, (a, b) -> {
            double valA = Double.parseDouble(a.toString().replace("₱", "").replace(",", ""));
            double valB = Double.parseDouble(b.toString().replace("₱", "").replace(",", ""));
            return Double.compare(valA, valB);
        });

        // Sort date as LocalDate
        sorter.setComparator(4, (o1, o2) -> {
            LocalDate d1 = LocalDate.parse(o1.toString());
            LocalDate d2 = LocalDate.parse(o2.toString());
            return d1.compareTo(d2);
        });

        table.setRowSorter(sorter);

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Category
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Amount
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Description
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Date

        // Buttons
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(editIcon));
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), this, "edit", editIcon));
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(deleteIcon));
        table.getColumnModel().getColumn(6)
                .setCellEditor(new ButtonEditor(new JCheckBox(), this, "delete", deleteIcon));
        table.getColumnModel().getColumn(5).setHeaderValue("");
        table.getColumnModel().getColumn(6).setHeaderValue("");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom controls
        JPanel controlPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(totalLabel);

        JButton addButton = new JButton("Add Expense");
        JButton deleteAllButton = new JButton("Delete All");
        addButton.setBackground(Color.LIGHT_GRAY);
        deleteAllButton.setBackground(Color.LIGHT_GRAY);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(addButton);
        rightPanel.add(deleteAllButton);

        controlPanel.add(leftPanel, BorderLayout.WEST);
        controlPanel.add(rightPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> showExpenseDialog(null));
        deleteAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete all expenses?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.clearAll();
                refreshTable();
            }
        });

        refreshTable();
        setVisible(true);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Expense e : manager.getExpenses()) {
            tableModel.addRow(new Object[] {
                    e.getId(),
                    e.getCategory(),
                    String.format("₱%.2f", e.getAmount()),
                    e.getDescription(),
                    e.getDate().toString(),
                    "Edit",
                    "Delete"
            });
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = manager.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText("Total: " + String.format("₱%.2f", total));
    }

    public void showExpenseDialog(Expense existingExpense) {
        JComboBox<String> categoryCombo = new JComboBox<>(
                new String[] { "Food", "Transportation", "Bills", "Entertainment" });
        JTextField amountField = new JTextField();
        JTextField descField = new JTextField();

        if (existingExpense != null) {
            categoryCombo.setSelectedItem(existingExpense.getCategory());
            amountField.setText(String.valueOf(existingExpense.getAmount()));
            descField.setText(existingExpense.getDescription());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Category:"));
        panel.add(categoryCombo);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                existingExpense == null ? "Add Expense" : "Edit Expense",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String category = (String) categoryCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                String description = descField.getText();

                if (existingExpense == null) {
                    manager.addExpense(category, amount, description);
                } else {
                    manager.updateExpense(existingExpense.getId(), category, amount, description);
                }
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deleteExpense(int id) {
        manager.deleteExpense(id);
        refreshTable();
    }

    public JTable getTable() {
        return table;
    }

    public Expense getExpenseById(int id) {
        return manager.getExpenses().stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    private ImageIcon loadIcon(String path) {
        Image img = new ImageIcon(path).getImage();
        return new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
