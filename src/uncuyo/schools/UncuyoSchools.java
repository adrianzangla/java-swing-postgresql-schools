package uncuyo.schools;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UncuyoSchools {

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/uncuyo",
                "adrian",
                "adrian")) {
            ResultSet result = connection
                    .createStatement()
                    .executeQuery("SELECT * FROM uncuyo.schools");
            ResultSetMetaData metaData = result.getMetaData();
            DefaultTableModel model = new DefaultTableModel();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                model.addColumn(metaData.getColumnName(i + 1));
            }
            while (result.next()) {
                Object[] row = new Object[metaData.getColumnCount()];
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    row[i] = result.getObject(i + 1);
                }
                model.addRow(row);
            }
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            JFrame frame = new JFrame();
            frame.add(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
