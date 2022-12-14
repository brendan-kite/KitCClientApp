/**
 * @author Brendan Kite
 * Description: Class that serves as the controller for sign-up.fxml
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

// -- Class Declaration
public class SignUpController implements Initializable {
    // -- Elements created in sign-up.fxml
    @FXML
    private Button button_sign_up;
    @FXML
    private Button button_log_in;
    @FXML
    private TextField tf_first_name;
    @FXML
    private TextField tf_last_name;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField tf_password;
    @FXML
    private PasswordField tf_confirm_password;

    // -- Public variables storing user data to be accessed by other classes
    //public static String vCode;
    public String fName;
    public String email;

    public String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // -- When signup button is clicked, change the scene to the pass the user info to the signup method in DBUtils
        // -- to be added to the database, send the user an email with a verification code, and change the scene to verification.fxml
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // -- Checks if the user input for first name, last name, email and password are not empty
                if (!tf_first_name.getText().trim().isEmpty() && !tf_last_name.getText().trim().isEmpty()
                        && !tf_email.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
                    // -- Checks if the password matches the
                    if (tf_password.getText().trim().equals(tf_confirm_password.getText().trim())) {
                        // -- Stores the first name, verification code generated by the method in DBUtils, and email in public
                        // -- variables, so they can be accessed by other classes
                        fName = tf_first_name.getText().trim();
                        //vCode = DBUtils.verificationCode();
                        email = tf_email.getText().trim();
                        password = tf_password.getText().trim();

                        // -- Sends the email to the user with the generated verification code
                        //EmailSender.sendMail(email, fName, vCode);

                        // -- Passes the user info to the signup method in DBUtils and adds it to the MySQL database
                        DBUtils.signUp(actionEvent, fName, tf_last_name.getText().trim(), email, tf_password.getText().trim());

                        // -- Changes the scene to verification.fxml
                        //DBUtils.changeScene(actionEvent, "verification.fxml", "Email Verification", null);

                    // -- If the passwords the user filled in do not match, display an alert
                    } else {
                        // -- For testing can be removed
                        System.out.println("Passwords do not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Passwords do not match");
                        alert.show();
                    }

                // -- If the user did not fill in any/all information, display an alert
                } else {
                    // -- For testing can be removed
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up");
                    alert.show();
                }
            }
        });

        // -- When login button is clicked, change the scene back to the login/main page
        button_log_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "hello-view.fxml", "KitC", null);
            }
        });
    }
}