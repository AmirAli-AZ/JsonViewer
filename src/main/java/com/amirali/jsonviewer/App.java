package com.amirali.jsonviewer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Map;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JsonViewer");
        stage.setScene(new Scene(createContent(), 900, 600));
        stage.show();
    }

    private Parent createContent() {
        var root = new BorderPane();

        var treeView = new TreeView<Map<String, Object>>();
        treeView.setCellFactory(jsonItemTreeView -> new TreeItemCell());

        var fileMenu = new Menu("File");

        var loadMenuItem = new MenuItem("Load Json File");
        loadMenuItem.setOnAction(actionEvent -> {
            var fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Json");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            var file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (file == null)
                return;

            try(var bufferedReader = new BufferedReader(new FileReader(file))){
                var json = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    json.append(line).append('\n');
                }

                treeView.setRoot(TreeUtils.getInstance().createTree(json.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var saveMenuItem = new MenuItem("Save");
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveMenuItem.setOnAction(actionEvent -> {
            var fileChooser = new FileChooser();
            fileChooser.setTitle("Save Json");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            var file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if (file == null)
                return;

            try (var bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                if (TreeUtils.getInstance().getMainJsonArray() != null)
                    bufferedWriter.write(TreeUtils.getInstance().getMainJsonArray().toString(4));
                else if (TreeUtils.getInstance().getMainJsonObject() != null)
                    bufferedWriter.write(TreeUtils.getInstance().getMainJsonObject().toString(4));
                bufferedWriter.flush();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });

        var clearMenuItem = new MenuItem("Clear");
        clearMenuItem.setOnAction(actionEvent -> treeView.setRoot(null));

        fileMenu.getItems().addAll(loadMenuItem, saveMenuItem, clearMenuItem);
        var menuBar = new MenuBar(fileMenu);

        root.setTop(menuBar);
        root.setCenter(treeView);

        return root;
    }

}
