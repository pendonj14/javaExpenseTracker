import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(ImageIcon icon) {
        setIcon(icon);
        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        return this;
    }
}
