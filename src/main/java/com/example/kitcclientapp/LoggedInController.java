/**
 * @author Brendan Kite
 * Description: Class that serves as the controller for logged-in.fxml
 */

// -- Package
package com.example.kitcclientapp;

// -- Import Statements
import com.jcraft.jsch.SftpException;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

// -- Class Declaration
public class LoggedInController implements Initializable {

    // -- Logout button created in logged-in.fxml
    @FXML
    private AnchorPane drawerPane;
    @FXML
    private AnchorPane opacityPane;
    @FXML
    private Button button_log_out;
    // -- Welcome label created in logged-in.fxml
    @FXML
    private Label label_welcome;
    @FXML
    private Label drawerImage;
    @FXML
    public TreeView<String> treeView;
    @FXML
    private ListView listView;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem newFolder;
    @FXML
    private MenuItem upload;
    @FXML
    private MenuItem download;
    @FXML
    private MenuItem rename;
    @FXML
    private MenuItem delete;
    @FXML
    private ContextMenu contextMenu1;
    @FXML
    private MenuItem newFolder1;
    @FXML
    private MenuItem upload1;
    @FXML
    private MenuItem download1;
    @FXML
    private MenuItem rename1;
    @FXML
    private MenuItem delete1;
    @FXML
    private Label label_server_status;
    @FXML
    private ImageView image_status;
    public static String string;
    private String fN;
    String itemSelected;
    String status = KitCConnection.isConnected();
    List<String> path = new ArrayList<>();
    String monitor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // -- When logout button is clicked, change the scene to the login/main page
        button_log_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "hello-view.fxml", "Log in!", null);
                if (status.equals("true")) {
                    KitCConnection.disconnect();
                }
            }
        });

        //Want to customize further
        //Gets user input on file name
        newFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //String select = selectItem();
                //KitCConnection.changeDirectories(select);
                //System.out.println(select);
                TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setHeaderText("Please name your files");
                textInputDialog.setTitle("Name your Files!");
                textInputDialog.setGraphic(new ImageView(new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/com/example/kitcclientapp/Images/Folder_25x25.png")))));
                textInputDialog.showAndWait();
                String pls = textInputDialog.getEditor().getText();
                System.out.println(pls);
                //addToTree(pls, rootItem);
                //KitCConnection.displayDirectoryContents(currentDir, rootItem);
            }
        });

        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (status.equals("true")) {
                    //KitCConnection.changeDirectories(fN);
                    FileChooser fileChooser = new FileChooser();
                    List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);
                    if (selectedFile != null) {
                        for (File file : selectedFile) {
//                            if (file.isDirectory()) {
//                                KitCConnection.changeDirectories(file.getName());
//                            }

                            //KitCConnection.changeDirectories(file.getName());
                            KitCConnection.Upload(file);
                            listView.getItems().add(file.getName());
                            //KitCConnection.changeDirectories("..");
                            treeView.refresh();
//                            try {
//                                setUserInfo(fN, LocalTime.now());
//                            } catch (SftpException | IOException e) {
//                                throw new RuntimeException(e);
//                            }

                        }
                    } else {
                        System.out.println("File is not valid");
                        //KitCConnection.changeDirectories("..");
                    }
                } else {
                    // -- For testing can be removed
                    System.out.println("Please connect to server");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please Connect to Server");
                    alert.show();
                }
            }
        });

        upload1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (status.equals("true")) {
                    KitCConnection.changeDirectories(fN);
                    FileChooser fileChooser = new FileChooser();
                    List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);
                    if (selectedFile != null) {
                        for (File file : selectedFile) {
//                            if (file.isDirectory()) {
//                                KitCConnection.changeDirectories(file.getName());
//                            }
                            //KitCConnection.changeDirectories(file.getName());
                            KitCConnection.Upload(file);
                            listView.getItems().add(file.getName());
                            //KitCConnection.changeDirectories("..");
                            try {
                                setUserInfo(fN, LocalTime.now());
                            } catch (SftpException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        System.out.println("File is not valid");
                        //KitCConnection.changeDirectories("..");
                    }
                } else {
                    // -- For testing can be removed
                    System.out.println("Please connect to server");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please Connect to Server");
                    alert.show();
                }
            }
        });

        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (status.equals("true")) {
//                    FileChooser fileChooser = new FileChooser();
//                    fileChooser.setTitle("Save");
//                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
//                    fileChooser.showSaveDialog(null);
                    KitCConnection.Download(new File(itemSelected));
                } else {
                    // -- For testing can be removed
                    System.out.println("Please connect to server");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please Connect to Server");
                    alert.show();
                }
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                KitCConnection.Delete(itemSelected, fN);
                treeView.refresh();
                listView.refresh();
                try {
                    setUserInfo(fN, LocalTime.now());
                } catch (SftpException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        opacityPane.setVisible(false);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        // -- Moves the sidebar pane back by 600 so when the menu button is clicked it appears and disappears rather than going to the other side of the screen
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnMouseClicked(event -> {
            opacityPane.setVisible(true);

            // -- This makes the rest of the screen dark when I click the menu button
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            // -- This moves the slider pane onto the screen
            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {
            // -- This gets rid of the opacityPane, so you can click on things again
            // -- The opacityPane covers the entire screen so clicking anywhere other than the slider will trigger this action
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            // -- Once the transition is finished set the opacityPane visibility to false
            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });

            // -- This moves the slider back off the screen
            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
    }

    public String[] directories;
    // -- Method that sets the welcome text that is displayed when the user logs in
    public void setUserInfo(String first_name, LocalTime timeOfDay) throws SftpException, IOException {
        // -- String to store whether it is morning, afternoon, or evening
        String localTime;

        fN = first_name;

        if (status.equals("true")) {
            File currentDir = new File("/" + first_name);
            TreeItem<String> rootItem = new TreeItem<>(currentDir.getName(), new ImageView(new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/com/example/kitcclientapp/Images/Folder_25x25.png")))));
            treeView.setRoot(rootItem);
            System.out.println(currentDir);
            //KitCConnection.displayDirectoryContents(currentDir, rootItem, listView);
            KitCConnection.getDirectories(first_name, rootItem);
        }
       // fN = first_name;

        //KitCConnection.getDirectories(first_name, rootItem);

        //System.out.println(Arrays.toString(directories));
        //buildFileSystemBrowser(first_name);
//        TreeItem<Path> rootItem = new TreeItem<Path>(Paths.get("D:/TestFolder/" + first_name));
//        rootItem.setExpanded(true);
//        createTree(rootItem);
//
//        treeView = new TreeView<Path>(rootItem);
//        treeView.setRoot(rootItem);
//        StackPane root = new StackPane();
//        root.getChildren().add(treeView);

        //treeView = buildFileSystemBrowser(first_name);
//        List<String> path = new ArrayList<>();
        //populateTree(first_name);
        //treeView = KitCConnection.populateTree(first_name, path);

        //KitCConnection.getDirectories(first_name, rootItem);
        //KitCConnection.listDirectory(first_name, path, rootItem);
        // -- If the local time is before or after a certain time, set timeOfDay to the respective time of day
        // -- 12 AM and 12 PM
        if (timeOfDay.isAfter(LocalTime.MIDNIGHT) && timeOfDay.isBefore(LocalTime.NOON)) {
            localTime = "Morning";
        // -- 12 PM to 6 PM
        } else if (timeOfDay.isAfter(LocalTime.NOON) && timeOfDay.isBefore(LocalTime.of(18,0))) {
            localTime = "Afternoon";
        // -- 6PM to 12 AM
        } else {
            localTime = "Evening";
        }

        // -- Sets the welcome label in the login page
        label_welcome.setText("Good " + localTime + " " + first_name);
    }



    public void addToTree(String name, TreeItem treeItem) {


        //KitCConnection.changeDirectories(name);
        KitCConnection.MakeDirectory(name);
        TreeItem<String> branchItem = new TreeItem<>(name);
        treeItem.getChildren().add(branchItem);

    }
    public void setStatus() throws FileNotFoundException {


        //KitCConnection.isConnected();
        if (status.equals("false")) {
            InputStream stream = new FileInputStream("src/main/resources/com/example/kitcclientapp/Images/Offline.png");
            image_status.setImage(new Image(stream));
            System.out.println("Not");
            label_server_status.setText("Offline");
        } else {
            InputStream stream = new FileInputStream("src/main/resources/com/example/kitcclientapp/Images/Online.png");
            image_status.setImage(new Image(stream));
            System.out.println("Yes");
            label_server_status.setText("Online");
        }

    }

    /**
     * Recursively create the tree
     * @throws IOException
     */
    public static void createTree(TreeItem<Path> rootItem) throws IOException {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        }
    }

    public void populateTree(String first_name) {
        List<String> path = new ArrayList<>();
        //KitCConnection.listDirectory(first_name, path);
        TreeItem<String> rootItem = new TreeItem<>(first_name);
//        treeView.setRoot(rootItem);


        //KitCConnection.getDirectories(first_name, rootItem);
        //KitCConnection.listDirectory(first_name, path);
//
//        for (int i = 2; i < directories.length; i++) {
//            TreeItem<String> branchItem = new TreeItem<>(directories[i]);
//            rootItem.getChildren().add(branchItem);
//            for (int j = 0; j < directories[i].length(); j++) {
//                KitCConnection.changeDirectories(directories[i]);
//                TreeItem<String> branch = new TreeItem<>(directories[i]);
//                branchItem.getChildren().add(branch);
//            }
//        }
    }


    //Will use this to get the directory new folders will go in
    public void selectItem() throws SftpException {

        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            itemSelected = item.getValue();

            File file = new File(itemSelected);

            System.out.println("FILE: " + file);
            System.out.println(itemSelected);

            if (item.getParent() != null) {
                String test = item.getParent().getValue();
//                if (!item.getValue().equals(KitCConnection.currentDirectory())) {
//
//                    KitCConnection.changeDirectories(itemSelected);
//                }

//                if (item.equals(item.getParent())) {
//                    KitCConnection.changeDirectories("..");
//                }
                System.out.println("PARENT: " + test);
                //KitCConnection.changeDirectories(itemSelected);
//                if (file.isDirectory()) {
//                    KitCConnection.changeDirectories(itemSelected);
//                }
            }




            //If item is already selected, dont change directories
            //if (item.is)


        }

        //return String.valueOf(item);
    }
}