import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.*;

public class MainFrame {

    private JFrame frame;
    private JPanel loginPanel;
    private JPanel quizPanel;
    private JPanel registerPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    public MainFrame() {
        // Frame setup
        frame = new JFrame("Quiz App");
        frame.setSize(800, 600); // Increased size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setLayout(new CardLayout());

        // Initialize panels
        loginPanel = new JPanel();
        quizPanel = new JPanel();
        registerPanel = new JPanel();

        createLoginScreen();

        createRegisterScreen();

        frame.add(loginPanel, "LoginScreen");
        frame.add(quizPanel, "QuizScreen");
        frame.add(registerPanel, "RegisterScreen");

        frame.setVisible(true);
    }

    private void createLoginScreen() {
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome To");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 50, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(welcomeLabel, gbc);

        JLabel quizBeeLabel = new JLabel("Quiz Bee");
        quizBeeLabel.setFont(new Font("Showcard Gothic", Font.BOLD, 34));
        quizBeeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        loginPanel.add(quizBeeLabel, gbc);

        // Username Field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,10,10,10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        loginPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try (Connection conn = DatabaseHelper.getConnection()) {
                    String query = "SELECT password FROM userregister WHERE username = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, username);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String storedPassword = rs.getString("password");
                                if (storedPassword.equals(password)) {
                                    JOptionPane.showMessageDialog(frame, "Welcome to Quiz Bee" + username + "!");

                                    showQuizScreen();
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Incorrect password.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "User not found. Please register.");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Questions().setVisible(true);
            }
        });

        // Register Button
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterScreen();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(buttonPanel, gbc);
    }

    private void createRegisterScreen() {
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();

        // Registration Form
        JLabel registerLabel = new JLabel("Register New User");
        registerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        registerLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10,10,10,10);
        registerPanel.add(registerLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,10,10,10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        registerPanel.add(usernameLabel, gbc);

        JTextField regUsernameField = new JTextField(20);
        gbc.gridx = 1;
        registerPanel.add(regUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        registerPanel.add(emailLabel, gbc);

        JTextField regEmailField = new JTextField(20);
        gbc.gridx = 1;
        registerPanel.add(regEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        registerPanel.add(passwordLabel, gbc);

        JPasswordField regPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        registerPanel.add(regPasswordField, gbc);

        JButton submitButton = new JButton("Register");
        styleButton(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = regUsernameField.getText();
                String email = regEmailField.getText();
                String password = new String(regPasswordField.getPassword());

                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(email);

                if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(frame, "Invalid email format.");
                    return;
                }

                try (Connection conn = DatabaseHelper.getConnection()) {
                    String query = "INSERT INTO userregister (username, email, password) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, username);
                        stmt.setString(2, email);
                        stmt.setString(3, password);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Registration successful!");
                        showLoginScreen();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
                }
            }
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginScreen();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        registerPanel.add(buttonPanel, gbc);
    }





    private void styleButton(JButton button) {
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void showLoginScreen() {
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), "LoginScreen");
    }

    private void showRegisterScreen() {
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), "RegisterScreen");
    }

    private void showQuizScreen() {
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), "QuizScreen");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Quiz";
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "Hema@595Madhuri"; // Replace with your DB password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
