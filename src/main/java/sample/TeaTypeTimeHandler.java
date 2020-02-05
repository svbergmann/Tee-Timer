package sample;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeaTypeTimeHandler implements Serializable {

    private static final Gson gson = new Gson();
    private ArrayList<String> teaTypesArrayList = new ArrayList<>();
    private ObservableList<String> teaTypes;
    private Map<String, Integer> teaTypeTimeMap = new HashMap<>();
    private File teasAndTimes = new File(System.getProperty("user.home") + "\\TeasAndTimes.txt");

    public TeaTypeTimeHandler() throws IOException {
        if (fileExists()) {
            teaTypeTimeMap = readFile();
        } else {
            teaTypeTimeMap.put("Feinster Grüner Tee (Meßmer)", 3);
            teaTypeTimeMap.put("Ingwer Holunderblüte (Lord Nelson)", 6);
            teaTypesArrayList.add("Feinster Grüner Tee (Meßmer)");
            teaTypesArrayList.add("Ingwer Holunderblüte (Lord Nelson)");
            teaTypes = FXCollections.observableArrayList(teaTypesArrayList);
            writeFile();
        }
    }

    private boolean fileExists() {
        return teasAndTimes.exists();
    }

    private void writeFile() throws IOException {
        gson.toJson(teaTypeTimeMap, new FileWriter(teasAndTimes));
    }

    private HashMap<String, Integer> readFile() throws FileNotFoundException {
        return gson.fromJson(new FileReader(teasAndTimes), (Type) teaTypeTimeMap);
    }

    public ObservableList<String> getTeaTypes() {
        return teaTypes;
    }

    public Map<String, Integer> getTeaTypeTimeMap() {
        return teaTypeTimeMap;
    }

}
