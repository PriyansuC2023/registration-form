import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class form extends JDialog {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public form(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);




        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(tfPassword.getPassword());
        String confirmPassword = String.valueOf(tfConfirmPassword.getPassword());


        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = addUserToDatabase(name, email, phone, address, password);
        if (user != null) {
            dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    public user user;

    private user addUserToDatabase(String name, String email, String phone, String address, String password) {
        user user = null;
        final String DBurl = "jdbc:mysql://localhost:3306/users";
        final String USERNAME = "root";
        final String PASSWORD = "babai@2002";

        try {
            Connection conn = DriverManager.getConnection(DBurl, USERNAME, PASSWORD);
            String sql = "INSERT INTO users (name, email, phone, address, password)" + "VALUES(?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new user();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;

            }

            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;

    }

    public static void main(String[] args) {
        form myForm = new form(null);

        user user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        } else {
            System.out.println("Registration cancelled");
        }


    }
}