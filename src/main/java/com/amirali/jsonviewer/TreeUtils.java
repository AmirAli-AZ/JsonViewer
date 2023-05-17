package com.amirali.jsonviewer;

import javafx.scene.control.TreeItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class TreeUtils {

    private static TreeUtils instance;

    private JSONArray mainJsonArray;

    private JSONObject mainJsonObject;

    private TreeUtils() {

    }

    public static TreeUtils getInstance() {
        if (instance == null)
            instance = new TreeUtils();
        return instance;
    }

    public TreeItem<Map<String, Object>> createTree(String json) {
        var mainTreeItem = new TreeItem<Map<String, Object>>();

        try {
            mainJsonArray = new JSONArray(json);
            mainTreeItem.getChildren().add(createArray(mainJsonArray, ""));
        }catch (JSONException e) {
            mainJsonObject = new JSONObject(json);
            mainTreeItem.getChildren().add(createObject(mainJsonObject, ""));
        }catch (Exception e) {
            e.printStackTrace();
        }

        return mainTreeItem;
    }

    public JSONArray getMainJsonArray() {
        return mainJsonArray;
    }

    public JSONObject getMainJsonObject() {
        return mainJsonObject;
    }

    private TreeItem<Map<String, Object>> createObject(JSONObject jsonObject, String name) {
        var header = new HashMap<String, Object>();
        header.put("key", "NO_KEY");
        header.put("value", name);
        header.put("type", Type.HEADER);

        var treeItem = new TreeItem<Map<String, Object>>(header);

        var keys = jsonObject.keys();
        while (keys.hasNext()) {
            var key = keys.next();

            if (jsonObject.get(key) instanceof JSONArray jsonArray) {
                treeItem.getChildren().add(createArray(jsonArray, key));
            }else if (jsonObject.get(key) instanceof JSONObject jsonObject1){
                treeItem.getChildren().add(createObject(jsonObject1, key));
            }else {
                var field = new HashMap<String, Object>();
                field.put("key", key);
                field.put("value", jsonObject.get(key));
                field.put("type", Type.OBJECT_FIELD);
                field.put("json_object", jsonObject);

                treeItem.getChildren().add(new TreeItem<>(field));
            }
        }

        return treeItem;
    }

    private TreeItem<Map<String, Object>> createArray(JSONArray jsonArray, String name) {
        var header = new HashMap<String, Object>();
        header.put("key", "NO_KEY");
        header.put("value", name);
        header.put("type", Type.HEADER);

        var treeArray = new TreeItem<Map<String, Object>>(header);

        for (int i = 0; i < jsonArray.length(); i++) {
            var object = jsonArray.get(i);
            if (object instanceof JSONObject jsonObject1) {
                var objectTree = createObject(jsonObject1, "");
                treeArray.getChildren().add(objectTree);
            }else {
                var field = new HashMap<String, Object>();
                field.put("key", "EMPTY_KEY");
                field.put("value", object);
                field.put("type", Type.ARRAY_FIELD);
                field.put("json_array", jsonArray);
                field.put("field_index", i);

                treeArray.getChildren().add(new TreeItem<>(field));
            }
        }

        return treeArray;
    }

}
