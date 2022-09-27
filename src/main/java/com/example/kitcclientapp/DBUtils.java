/**
 * @author Brendan Kite
 * Description: Class that is responsible for the changing of scenes, inputting user data into a MySQL database,
 * crossreferencing user inputted data with data saved in a MySQL database, generating verification codes, and checking
 * if the user inputted code matches the one in the MySQL database
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import com.jcraft.jsch.SftpException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

// -- Class Declaration
public class DBUtils {

    // -- String object to store email passed in from signUP() to be used in verify()
    public static String tempEmail;
    public static String fName;

    public static String verification_code;


    // -- Method to change the scene within the JavaFX stage
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String firstName) {
        Parent root = null;

        // -- If the user included a first name
        if (firstName != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();

                LoggedInController loggedInController = loader.getController();

                loggedInController.setUserInfo(firstName, LocalTime.now());
                loggedInController.setStatus();
            } catch (IOException | SftpException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    // -- Method to input user data into a database
    public static void signUp(ActionEvent actionEvent, String firstName, String lastName, String email, String password) {
        Connection connection = null;
        PreparedStatement insert = null;
        PreparedStatement checkUser = null;
        ResultSet resultSet = null;

        // -- Stores the user email in tempEmail, so it can be accessed by the other methods
        tempEmail = email;
        fName = firstName;

        verification_code = DBUtils.verificationCode();
        try {
            // -- Establishes the connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://192.81.128.99:3306/KitC", "bkite", "ILoveKatie11.19.21");
            checkUser = connection.prepareStatement("SELECT * FROM user_data WHERE email = ?");
            checkUser.setString(1, email);
            resultSet = checkUser.executeQuery();

            // -- If account already exists display alert
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this email");
                alert.show();

            // -- If account does not exist yet inserts into database
            } else {
                insert = connection.prepareStatement("INSERT INTO user_data (first_name, last_name, email, password, verification_code) VALUES (?, ?, ?, ?, ?)");
                insert.setString(1, firstName);
                insert.setString(2, lastName);
                insert.setString(3, email);
                insert.setString(4, password);
                insert.setString(5, verification_code);
                insert.executeUpdate();
                changeScene(actionEvent, "verification.fxml", "Email Verification", null);
                EmailSender.sendMail(email, fName, verification_code);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        // -- Terminate the database connection
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (checkUser != null) {
                try {
                    checkUser.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insert != null) {
                try {
                    insert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // -- Method to check user credentials stored in a database
    public static void login(ActionEvent actionEvent, String email, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // -- root, C00p3r_H4s4_Cut3_Butt789
        try {
            // -- Establishes the connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://192.81.128.99:3306/KitC", "bkite", "ILoveKatie11.19.21");
            preparedStatement = connection.prepareStatement("SELECT first_name, password FROM user_data WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            // -- If the account doesn't exist in the database display an alert
            if (!resultSet.isBeforeFirst()) {
                // -- For testing can be deleted
                System.out.println("User Not Found in the Database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();

            } else {
                // -- Extracts the password and first name associated with the users email
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("password");
                    String retrieveFName = resultSet.getString("first_name");

                    // -- If the passwords match change the scene to the logged in page
                    if (retrievePassword.equals(password)) {
                        KitCConnection.connect();
                        //KitCConnection.changeDirectories(retrieveFName);
                        changeScene(actionEvent, "logged-in.fxml", "Welcome!", retrieveFName);
                        //KitCConnection.getDirectories(retrieveFName);
                        //LoggedInController.populateTree(fName);
                        //LoggedInController.setStatus();

                    // -- If the passwords don't match display an alert
                    } else {
                        // -- For testing can be deleted
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        // -- Terminate the database connection
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // -- Method that generates a random 6 digit number between 0 and 999999
    public static String verificationCode() {
        // -- Creates a new random number generator
        Random rnd = new Random();

        // -- Creates a random number stored as an integer
        int number = rnd.nextInt(999999);

        // -- Converts the number to a 6 digit string
        String vCode = String.format("%06d", number);

        // -- For testing, can be removed
        System.out.println("VCODE: " + vCode);

        return vCode;
    }

    // -- Method to check if the verification code the user inputted matches the one associated with their email in the database
    public static void verify(ActionEvent actionEvent, String verificationCode) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // -- For testing can be removed
        System.out.println("HEY " + tempEmail);

        try {
            // -- Establishes the connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://192.81.128.99:3306/KitC", "bkite", "ILoveKatie11.19.21");
            preparedStatement = connection.prepareStatement("SELECT first_name, verification_code FROM user_data WHERE email = ?");
            preparedStatement.setString(1, tempEmail);
            resultSet = preparedStatement.executeQuery();

            // -- Checks if the verification code is 6 characters long
            if (verificationCode.length() == 6) {
                // -- For testing can be removed
                System.out.println("VCODE LENGTH: " + verificationCode.length());

                // -- Extracts the verification code and first name associated with the users email
                while (resultSet.next()) {
                    String retrieveVCode = resultSet.getString("verification_code");

                    // -- For testing can be removed
                    System.out.println("LOOK " + retrieveVCode);

                    String retrieveFName = resultSet.getString("first_name");

                    // -- For testing can be removed
                    System.out.println("RETRIEVE FNAME " + retrieveFName);

                    // -- If the verification codes match change the scene to the logged in page
                    if (retrieveVCode.equals(verificationCode)) {
                        KitCConnection.connect();
                        KitCConnection.MakeDirectory(fName);
                        //KitCConnection.changeDirectories(fName);
                        changeScene(actionEvent, "logged-in.fxml", "Welcome!", retrieveFName);
                        //LoggedInController.populateTree(fName);

                    // -- If the verification codes don't match display an alert
                    } else {
                        // -- For testing can be removed
                        System.out.println("Incorrect Verification Code");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect Verification Code");
                        alert.show();
                    }
                }
            // -- If the user hits submit and the code isn't 6 characters long display an alert
            } else {
                // -- For testing can be deleted
                System.out.println("Please fill in all information");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all information to sign up");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        // -- Terminate the database connection
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
