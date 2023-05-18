package com.amirali.jsonviewer;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class TreeItemCell extends TreeCell<Map<String, Object>> {

    private HBox root;

    private Label keyLabel;

    private TextField valueTextField;

    @Override
    protected void updateItem(Map<String, Object> map, boolean b) {
        super.updateItem(map, b);

        setText(null);
        if (map == null || b) {
            setGraphic(null);
        }else {
            if (root == null)
                createContent();

            if (map.get("key").toString().equals("NO_KEY")) {
                keyLabel.setText(map.get("value").toString());
                valueTextField.setVisible(false);
            } else {
                keyLabel.setText(map.get("key") + " : ");
                valueTextField.setVisible(true);
                valueTextField.setText(map.get("value").toString());
            }

            setGraphic(root);
        }
    }

    private Parent createContent() {
        keyLabel = new Label();
        keyLabel.setFont(Font.font(15));

        valueTextField = new TextField();
        valueTextField.setFont(Font.font(15));
        valueTextField.setOnAction(actionEvent -> {
            var item = getItem();
            var newValue = valueTextField.getText();

            if (item.get("type").equals(Type.OBJECT_FIELD)) {
                var jsonObject = ((JSONObject) item.get("json_object"));

                if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false"))
                    jsonObject.put(item.get("key").toString(), newValue.equalsIgnoreCase("true"));
                else {
                    try {
                        jsonObject.put(item.get("key").toString(), Integer.parseInt(newValue));
                    }catch (NumberFormatException e1) {
                        try {
                            jsonObject.put(item.get("key").toString(), Long.parseLong(newValue));
                        }catch (NumberFormatException e2) {
                            try {
                                jsonObject.put(item.get("key").toString(), Float.parseFloat(newValue));
                            }catch (NumberFormatException e3) {
                                try {
                                    jsonObject.put(item.get("key").toString(), Double.parseDouble(newValue));
                                }catch (NumberFormatException e4) {
                                    jsonObject.put(item.get("key").toString(), newValue);
                                }
                            }
                        }
                    }
                }
            }else if (item.get("type").equals(Type.ARRAY_FIELD)) {
                var jsonArray = ((JSONArray) item.get("json_array"));

                if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false"))
                    jsonArray.put(Integer.parseInt(item.get("key").toString()), newValue.equalsIgnoreCase("true"));
                else {
                    try {
                        jsonArray.put(Integer.parseInt(item.get("key").toString()), Integer.parseInt(newValue));
                    }catch (NumberFormatException e1) {
                        try {
                            jsonArray.put(Integer.parseInt(item.get("key").toString()), Long.parseLong(newValue));
                        }catch (NumberFormatException e2) {
                            try {
                                jsonArray.put(Integer.parseInt(item.get("key").toString()), Float.parseFloat(newValue));
                            }catch (NumberFormatException e3) {
                                try {
                                    jsonArray.put(Integer.parseInt(item.get("key").toString()), Double.parseDouble(newValue));
                                }catch (NumberFormatException e4) {
                                    jsonArray.put(Integer.parseInt(item.get("key").toString()), newValue);
                                }
                            }
                        }
                    }
                }
            }
        });

        root = new HBox(keyLabel, valueTextField);
        return root;
    }
}
