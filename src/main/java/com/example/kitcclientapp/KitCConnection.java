package com.example.kitcclientapp;

import com.jcraft.jsch.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitCConnection {
    private static ChannelSftp channelSftp;

    private static String directory;
    private static Session session;
    //public static String[] directories;

    //static String privateKey = "/src/main/resources/com/example/kitcclientapp/KitCPublicKey";

   //static List<String> tempPath = new ArrayList<>();
    public static void connect() {
        try {
            String user = "test";
            String password = "password";
            //session.setConfig("PreferredAuthentications", "publickey, keyboard-interactive, password");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            String host = "192.81.128.99";
            int port = 2222;

            JSch jSch = new JSch();
            //jSch.addIdentity(privateKey);
            session = jSch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.getHome();
            //channelSftp.cd(directory);

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    // -- Upload
    public static void Upload(File file) {
        try {
            channelSftp.put(file.getAbsolutePath(), file.getName());
            System.out.println("FILE PATH: " + file.getAbsolutePath());
            System.out.println("FILE NAME: " + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -- Download
    public static void Download(File file) {
        System.out.println(file);
        try {
            String home = System.getProperty("user.home");
            File dest = new File(home + "/Downloads/" + file);
            channelSftp.get(file.toString(), dest.toString());
            System.out.println("Downloaded");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("File Downloaded! Please check your downloads");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -- Create New Directory
    public static void MakeDirectory(String dirName) {
        try {
            channelSftp.mkdir(dirName);
            System.out.println("D CREATED" + dirName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -- Rename
    public static void RenameFile(String oldPath, String newPath) throws SftpException {
        try {
            channelSftp.rename(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Delete(String path, String firstName) {
        try {
            channelSftp.rm(firstName + "/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String isConnected() {
        if (!session.isConnected()) {
            System.out.println("Failed to connect to server");
            return "false";
        }
        else {
            System.out.println("Connected to server!");
            return "true";
        }
    }

    public static void displayDirectoryContents(File dir, TreeItem<String> treeItem, ListView listView) {
        ArrayList<String> direct= new ArrayList<>(List.of(dir.toString().split("\\\\")));

        int num = direct.size();
        System.out.println(num);

        String test = direct.get(num - 1);
        System.out.println(test);

        //String testString = listDirectories(test);
        File[] files = new File(test).listFiles();
        System.out.println(Arrays.toString(files));
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    TreeItem<String> branchItem = new TreeItem<>(file.getName(), new ImageView(new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/com/example/kitcclientapp/Images/Folder_25x25.png")))));
                    treeItem.getChildren().add(branchItem);
                    //System.out.println("directory:" + file.getCanonicalPath());
                    displayDirectoryContents(file, branchItem, listView);
                } else {
                    //System.out.println("     file:" + file.getCanonicalPath());
                    TreeItem<String> branchItem = new TreeItem<>(file.getName(), new ImageView(new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/com/example/kitcclientapp/Images/Files_25x25.png")))));
                    treeItem.getChildren().add(branchItem);
                    listView.getItems().add(file.getName());

                }
            }
        }

        //System.out.println("FILES: " + Arrays.toString(files));

    }

    static String[] directories;

    // -- Method to get the directories inside a path
    public static void getDirectories(String currentDirectory, TreeItem treeItem) throws SftpException {


        directory = currentDirectory;
        System.out.println("Current Directory" + currentDirectory);


        if (!Objects.equals(currentDirectory, fn)) {


            //channelSftp.lcd("/" + currentDirectory);
            try {
                // -- Assigns the directories in the current directory to a Vector called fileList
                //Vector fileList = channelSftp.ls("TestFolder/" + firstName);


                Vector fileList = channelSftp.ls(currentDirectory);
                //System.out.println("FILE LIST: " + fileList);
                // -- Initializes directories by setting the length of the array to the size of the Vector
                directories = new String[fileList.size() - 2];
                //System.out.println("FILELIST: " + fileList.size());

                String path;
                // -- Loops through fileList and gets the file name which is then stored in the directories array
                for (int i = 0; i < fileList.size() - 2; i++) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) fileList.get(i + 2);
                    //System.out.println("LSENTRY: " + lsEntry);

                    directories[i] = lsEntry.getFilename();

                    path = directories[i];


                    TreeItem<String> branchItem = new TreeItem<>(directories[i]);
                    treeItem.getChildren().add(branchItem);


                    //getDirectories(directories[i], branchItem);


                    // -- For testing can be removed
                    //System.out.println(directories[i] + i);
                    //System.out.println(lsEntry.getFilename());
                }

                boolean flag = true;
                try {
                    channelSftp.cd("/" + currentDirectory);
                    for (int j = 0; j < directories.length; j++) {

                    }
                    System.out.println(flag);
                } catch (Exception e) {
                    flag = false;
                    System.out.println(flag);
                }

                //listDirectory(firstName, directories, )

            } catch (Exception e) {
                e.printStackTrace();
            }

//
//        boolean flag = true;
//        try {
//            channelSftp.cd("/" + currentDirectory);
//            System.out.println(flag);
//        }catch (Exception e) {
//            flag = false;
//            System.out.println(flag);
//        }

//        for (int j = 0; j < directories.length; j++) {
//            String temp = directories[j];
//            System.out.println(directories[j] + ", " + temp);
//            //for (int k = 0; k < directories[j].length(); k++) {
//            boolean flag2 = true;
//            try {
//                channelSftp.cd("/" + temp);
//                System.out.println(flag2);
//            } catch (Exception e) {
//                flag2 = false;
//                System.out.println(flag2);
//            }
            //}

            //channelSftp.cd(directories[j]);
            //getDirectories(temp, branchItem);
            //System.out.println("Current Directory: " + currentDirectory);
            //System.out.println("J: " + directories[j]);
            //getDirectories(directories[j], treeItem);

        }
    }



//        String test = ;
//        getDirectories();
        //channelSftp.cd("Test");



    public static void listDirectories(String path) {
        try {
            //System.out.println("Entries in " + path + " directory: ");
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(path);
            for (ChannelSftp.LsEntry entry : entries) {
                System.out.println(entry.getFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static String fn;
    public static void changeDirectories(String change) {
        fn = change;
        try {

            System.out.println("Entries in " + change + " directory: ");
            channelSftp.cd(change);
            listDirectories(".");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String currentDirectory() throws SftpException {
        return channelSftp.pwd();
    }


    public static void disconnect() {

        channelSftp.disconnect();
        session.disconnect();
    }
}
