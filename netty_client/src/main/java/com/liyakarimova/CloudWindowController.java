package com.liyakarimova;

import com.liyakarimova.commands.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;


@Slf4j
public class CloudWindowController  implements Initializable {

    //корневая директория клиента
    private static String ROOT_DIR = "netty_client/root";

    //текущая директория облака
    private String currentCloudDir;

    //текущая директория клиента
    private Path currentClientDir;

    @FXML
    private TreeView <Path> clientFileTree;

//    @FXML
//    private TreeView <String> cloudTreeView;
    @FXML
    private ListView <CloudItem> cloudListView;
    @FXML
    private TextField clientPath;
    @FXML
    private TextField cloudPath;
    @FXML
    private Button sendToCloudButton;
    @FXML
    private Button clientUpButton;

    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currentClientDir = Paths.get(ROOT_DIR);
            loadClientPart(currentClientDir);

            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());
            Thread daemon = new Thread(() -> {
                try {
                    while (true) {
                        Command msg = (Command) is.readObject();
                        System.err.println("Client command type: " + msg.getType());
                        switch (msg.getType()) {
                            case LIST_RESPONSE -> {
                                Platform.runLater(() -> {
                                    loadCloudPart(((ListResponseCommand) msg).getFilesList());
                                });
                            }
                            case FILE_MESSAGE -> {
                                FileMessageCommand fileMessageCommand = (FileMessageCommand)msg;
                                System.err.println(currentClientDir.resolve(fileMessageCommand.getName()).toString());
                                Files.write(currentClientDir.resolve(fileMessageCommand.getName()),fileMessageCommand.getBytes());
                                Platform.runLater(() -> {
                                    loadClientPart(currentClientDir);
                                });

                            }

                            case FILE_REQUEST -> {
                                log.info("Client received FILE REQUEST COMMAND");
                                //FileRequestCommand fileRequestCommand = (FileRequestCommand)msg;
                                Platform.runLater(() -> {
                                    updateCloudPart();
                                });
                            }


                            case PATH_RESPONSE -> {
                                PathResponseCommand pathResponseCommand = (PathResponseCommand) msg;
                                currentCloudDir = pathResponseCommand.getCurrentPath();
                                log.info("PATH RESPONSE COMMAND: " + currentCloudDir);
                                Platform.runLater(() -> {
                                    cloudPath.setText(currentCloudDir);
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




    private void requestCurrentCloudDir () {
        Command pathRequestCommand = new PathRequestCommand();
        try {
            os.writeObject(pathRequestCommand);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void requestList () {
        try {
            Command listRequestCommand = new ListRequestCommand();
            os.writeObject(listRequestCommand);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClientPart (Path rootPath) {
        loadClientTree(rootPath, clientFileTree,clientPath);
    }

    private void updateCloudPart () {
    }

    private void loadCloudPart (List <CloudItem> files) {
        cloudListView.getItems().clear();
        for (CloudItem c: files) {
            cloudListView.getItems().add (c);
        }


        cloudListView.setCellFactory(new Callback<ListView<CloudItem>, ListCell<CloudItem>>() {
            @Override
            public ListCell<CloudItem> call(ListView<CloudItem> param) {
                ListCell <CloudItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(CloudItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getName() == null) {
                            setText("");
                            setGraphic(null);
                        } else {
                            if (item.isDir()) {
                                setGraphic(new ImageView(new Image("/icons/folder.png",15,15,false,false)));
                            }
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });

        cloudListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && cloudListView.getSelectionModel().getSelectedItem() != null) {
                try {
                    os.writeObject(new PathInRequestCommand(cloudListView.getSelectionModel().getSelectedItem().getName()));
                    os.flush();
                } catch (IOException e) {
                    log.info("Не удалось отправить PathInRequestCommand", e);
                }
            }
        });

    }

    private void loadClientTree(Path rootPath, TreeView <Path> tree, TextField currentPath) {
        tree.setCellFactory(new Callback<TreeView<Path>, TreeCell<Path>>() {
            @Override
            public TreeCell<Path> call(TreeView<Path> param) {
                TreeCell <Path> cell = new TreeCell<>() {
                    @Override
                    protected void updateItem(Path item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getFileName() == null) {
                            setText("");
                        } else {
                            setText(item.getFileName().toString());
                        }
                    }
                };
                return cell;
            }



        });
        TreeItem <Path> root = new TreeItem<>(rootPath);
        tree.setRoot(root);
        findChildren (root,rootPath);
        root.setExpanded(true);
        currentPath.setText(rootPath.toString());

        tree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if (tree.getSelectionModel().getSelectedItem() != null) {
                    Path file = tree.getSelectionModel().getSelectedItem().getValue();
                    if (Files.isDirectory(file)) {
                        currentClientDir = file;
                        loadClientTree(file,tree,currentPath);
                    }
                }
            }
        });
    }

    private void findChildren (TreeItem <Path> treeItem, Path path)  {
        try {
            Files.list (path).forEach(f -> {
                TreeItem <Path> file = new TreeItem<>(f);
                treeItem.getChildren().add(file);
                if (Files.isDirectory(f)) {
                    findChildren(file,f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void onSendToCloudButtonClicked () {
        try {
            Path filePath = clientFileTree.getSelectionModel().getSelectedItem().getValue();
            Command fileMessageCommand = new FileMessageCommand(filePath.getFileName().toString(),Files.readAllBytes(filePath) );
            os.writeObject(fileMessageCommand);
            os.flush();
            log.info("Client created File message command.");
        } catch (IOException e) {
            log.info("Не смог отправить файл", e);
        }
    }

    @FXML
    private void onLoadFromCloudButtonClicked () {
        try {
            os.writeObject(new FileRequestCommand(cloudListView.getSelectionModel().getSelectedItem().getName()));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onClientUpButtonClicked () throws IOException {
        if (!Paths.get(clientPath.getText()).toString().equals(ROOT_DIR)) {
            currentClientDir = Paths.get(clientPath.getText()).getParent();
            loadClientPart(currentClientDir);
        }
    }

    @FXML
    private void onUpCloudButtonClicked () {
        try {
            os.writeObject(new PathUpRequestCommand());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
