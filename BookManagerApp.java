import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookManagerApp extends JFrame {
    private JTextField tfBookID, tfTitle, tfAuthor, tfYear;
    private JButton btnAdd, btnDelete, btnRefresh;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public BookManagerApp() {
        setTitle("Library Management System");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initializing components
        tfBookID = new JTextField(10);
        tfTitle = new JTextField(20);
        tfAuthor = new JTextField(20);
        tfYear = new JTextField(4);

        btnAdd = new JButton("Add Book");
        btnDelete = new JButton("Delete Book");
        btnRefresh = new JButton("Refresh");

        tableModel = new DefaultTableModel();
        bookTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        
        JPanel formPanel = new JPanel(new FlowLayout());
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(tfBookID);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(tfTitle);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(tfAuthor);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(tfYear);
        formPanel.add(btnAdd);
        formPanel.add(btnDelete);
        formPanel.add(btnRefresh);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess:\\C:\\Users\\Usser\\Desktop\\Coursework 1\\LibraryManagementSystem\\Database");
            statement = connection.createStatement();
        } catch (SQLException e) {
        }

        // Add Book button
        btnAdd.addActionListener(e -> {
            String bookID = tfBookID.getText();
            String title = tfTitle.getText();
            String author = tfAuthor.getText();
            String year = tfYear.getText();

            try {
                String query = "INSERT INTO Books (BookID, Title, Author, Year) VALUES ('" + bookID + "', '" + title + "', '" + author + "', '" + year + "')";
                statement.executeUpdate(query);
                refreshBookList();
            } catch (SQLException ex) {
            }
        });

        // Delete Book button 
        btnDelete.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                String bookID = (String) tableModel.getValueAt(selectedRow, 0);
                try {
                    String query = "DELETE FROM Books WHERE BookID='" + bookID + "'";
                    statement.executeUpdate(query);
                    refreshBookList();
                } catch (SQLException ex) {
                }
            }
        });

        // Refresh button 
        btnRefresh.addActionListener(e -> refreshBookList());

        // Display the application
        setVisible(true);
    }

    private void refreshBookList() {
        tableModel.setRowCount(0); // Clear table
        try {
            resultSet = statement.executeQuery("SELECT * FROM Books");
            while (resultSet.next()) {
                String bookID = resultSet.getString("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String year = resultSet.getString("Year");
                tableModel.addRow(new String[]{bookID, title, author, year});
            }
        } catch (SQLException e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookManagerApp::new);
    }
}
