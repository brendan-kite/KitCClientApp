/**
 * @author Brendan Kite
 * Description: Class that serves as the controller for hello-view.fxml
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

// -- Class Declaration
public class HelloController implements Initializable {
    @FXML
    private TextField tf_email;

    @FXML
    private PasswordField tf_password;

    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.login(actionEvent, tf_email.getText(), tf_password.getText());
            }
        });
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "sign-up.fxml", "Sign up", null);
            }
        });
        tf_email.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    tf_password.requestFocus();
                }
            }
        });
    }

    public void onEnter(ActionEvent actionEvent) {
        DBUtils.login(actionEvent, tf_email.getText(), tf_password.getText());
    }
}