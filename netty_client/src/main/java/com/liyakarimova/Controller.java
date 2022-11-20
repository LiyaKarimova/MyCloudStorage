package com.liyakarimova;

import com.liyakarimova.commands.Command;
import com.liyakarimova.commands.FileMessageCommand;
import com.liyakarimova.commands.FileRequestCommand;
import com.liyakarimova.commands.ListRequestCommand;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


@Slf4j
public class Controller implements Initializable {

    private static String ROOT_DIR = "netty_client/root";
    private static String CLOUD_DIR = "netty_server/root";
    private static byte[] buffer = new byte[1024];

    private String currentClientDir;

    @FXML
    private TreeView <String> clientFileTree;

    @FXML
    private TreeView <String> cloudTreeView;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;

    @FXML
    private TextField clientPath;

    @FXML
    private TextField cloudPath;

    @FXML
    private Button sendToCloudButton;

    @FXML
    private Button clientUpButton;

    @FXML
    public void send(ActionEvent actionEvent) throws Exception {
        //String fileName = input.getText();
        //input.clear();
        //sendFile(fileName);
    }

    private void sendFile(String fileName) throws IOException {
        System.err.println("From client "+ fileName);
        Path file = Paths.get(ROOT_DIR, fileName);
        os.writeObject(new FileMessage(file));
        os.flush();
    }


    @FXML
    private void onListRequestButtonClicked () {
        try {
            Command listRequestCommand = new ListRequestCommand();
            os.writeObject(listRequestCommand);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadClientPart(ROOT_DIR);
            loadCloudPart(CLOUD_DIR);
            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());
            Thread daemon = new Thread(() -> {
                try {
                    while (true) {
//                        String msg = is.readUTF();
//                        Platform.runLater(() ->
//                                input.setText(msg));
                        //ListRequestCommand msg = (ListRequestCommand) is.readObject();
                        Command msg = (Command) is.readObject();
                        System.err.println("Server command type: " + msg.getType());
//                        // TODO: 23.09.2021 Разработка системы команд
                        switch (msg.getType()) {
                            case LIST_RESPONSE -> {
//                                String list = ((ListResponseCommand) msg).getFileList();
//                                Platform.runLater(() ->
                                //input.setText("Список файлов в папке root: " + list));
                            }
                            case FILE_MESSAGE -> {

                            }

                            case FILE_REQUEST -> {
                                log.info("Client received FILE REQUEST COMMAND");
                                FileRequestCommand fileRequestCommand = (FileRequestCommand)msg;
                                String alertMessage;
                                if (fileRequestCommand.isFileMovedCorrect()) {
                                    alertMessage = "Файл успешно добавлен в облако";
                                } else {
                                    alertMessage = "Ошибка!Файл не добавлен";
                                }
                                Platform.runLater(() -> {
                                    loadCloudPart(CLOUD_DIR);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText(alertMessage);
                                    alert.showAndWait();
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("exception while read from input stream");
                }
            });
            daemon.setDaemon(true);
            daemon.start();
        } catch (IOException ioException) {
            log.error("e=", ioException);
        }
    }

    private void loadClientPart (String rootPath) {
        loadTree(rootPath, clientFileTree,clientPath);
    }

    private void loadCloudPart (String rootPath) {
        loadTree(rootPath, cloudTreeView, cloudPath);
    }

    private void loadTree (String rootPath, TreeView <String> tree, TextField currentPath) {
        TreeItem <String> root = new TreeItem<>(Paths.get(rootPath).getFileName().toString());

        tree.setRoot(root);
        findChildren (root,rootPath);
        root.setExpanded(true);
        currentPath.setText(rootPath);


        tree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if (tree.getSelectionModel().getSelectedItem() != null) {
                    String file = tree.getSelectionModel().getSelectedItem().getValue();
                    if (Files.isDirectory(Paths.get(currentPath.getText(), file))) {
                        loadTree(Paths.get(currentPath.getText(), file).toString(),tree,currentPath);
                    }
                }
            }
        });
    }

    private void loadClientTree(String rootPath) throws IOException {
        TreeItem <String> root = new TreeItem<>(Paths.get(rootPath).getFileName().toString());
        clientFileTree.setRoot(root);
        findChildren (root,rootPath);

        clientFileTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                System.err.println("Gfgrf");
            }
        });
//        Files.list (Paths.get(ROOT_DIR)).forEach(f -> {
//            TreeItem <String> file = new TreeItem<>(f.getFileName().toString());
//            root.getChildren().add(file);
//        });
//        Files.list(ROOT_DIR)
//        listView.getItems().addAll(
//                Files.list(Paths.get(ROOT_DIR))
//                    .map(p -> p.getFileName().toString())
//                    .collect(Collectors.toList())
//        );
//        listView.setOnMouseClicked(e -> {
//            if (e.getClickCount() == 2) {
//                String item = listView.getSelectionModel().getSelectedItem();
//                input.setText(item);
//            }
//        });
    }

    private void findChildren (TreeItem <String> treeItem, String path)  {
        try {
            Files.list (Paths.get(path)).forEach(f -> {
                TreeItem <String> file = new TreeItem<>(f.getFileName().toString());
                treeItem.getChildren().add(file);
                if (Files.isDirectory(f)) {
                    findChildren(file,f.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTreeClicked () {

    }

    @FXML
    private void onSendToCloudButtonClicked () {
        try {
            String file = clientFileTree.getSelectionModel().getSelectedItem().getValue();
            Command fileMessageCommand = new FileMessageCommand((Paths.get(ROOT_DIR,file)).toString(), Paths.get(CLOUD_DIR).toString());
            os.writeObject(fileMessageCommand);
            os.flush();
            log.info("Client created File message command. File: " + Paths.get(ROOT_DIR,file) + " to dir: " + Paths.get(CLOUD_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoadFromCloudButtonClicked () {

    }

    @FXML
    private void onClientUpButtonClicked () throws IOException {
        if (!Paths.get(clientPath.getText()).toString().equals(ROOT_DIR)) {
            loadClientPart(Paths.get(clientPath.getText()).getParent().toString());
        }


    }

    @FXML
    private void onUpCloudButtonClicked () {
        if (!Paths.get(cloudPath.getText()).toString().equals(CLOUD_DIR)) {
            loadCloudPart(Paths.get(cloudPath.getText()).getParent().toString());
        }
    }
}
