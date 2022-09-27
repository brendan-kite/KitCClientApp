/**
 * @author Brendan Kite
 * Description: Class that serves as the controller for verification.fxml
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.kitcclientapp.DBUtils.verification_code;

// -- Class Declaration
public class VerificationController extends SignUpController implements Initializable {
    // -- Elements created in verification.fxml
    @FXML
    private Button button_submit;
    @FXML
    private Button button_resend;
    @FXML
    private TextField tf_verify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // -- When submit button is clicked, verify the user inputted confirmation code with the one stored in the MySQL database
        button_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // -- For testing can be removed
                System.out.println(tf_verify.getText());
                DBUtils.verify(actionEvent, tf_verify.getText().trim());
            }
        });

        // -- When resend button is clicked, resend the email to the user
        button_resend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                EmailSender.sendMail(email, fName, verification_code);
            }
        });

    }
}
