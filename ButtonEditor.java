import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private final ExpenseTrackerGUI gui;
    private final String action;
    private int currentRow;

    public ButtonEditor(JCheckBox checkBox, ExpenseTrackerGUI gui, String action, ImageIcon icon) {
        super(checkBox);
        this.gui = gui;
        this.action = action;

        button = new JButton(icon);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        button.addActionListener(e -> {
            int id = (int) gui.getTable().getValueAt(currentRow, 0);
            if (action.equals("edit")) {
                gui.showExpenseDialog(gui.getExpenseById(id));
            } else if (action.equals("delete")) {
                gui.deleteExpense(id);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
