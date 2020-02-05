package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TeaTypeTimeHandler implements Serializable {

    protected static ArrayList<String> teaTypesArrayList = new ArrayList<>();
    protected static ObservableList<String> teaTypes;
    protected static HashMap<String, Integer> teaTypeTimeMap = new HashMap<>();

    public static void readAndSave(File file) throws IOException {
        ArrayList<String> lines = (ArrayList<String>) new BufferedReader(new FileReader(file)).lines().collect(Collectors.toList());
        for (String string : lines) {
            String[] teaPlusTime = string.split(" : ");
            teaTypesArrayList.add(teaPlusTime[0]);
            teaTypeTimeMap.put(teaPlusTime[0], Integer.parseInt(teaPlusTime[1]));
        }
        teaTypes = FXCollections.observableArrayList(teaTypesArrayList);
        System.out.println(teaTypeTimeMap.entrySet().toString());
    }

    public static void writeAndSave() throws IOException {
        File teasAndTimes = new File(System.getProperty("user.home") + "\\TeasAndTimes.txt");
        if (teasAndTimes.createNewFile()) {
            try {
                JSONObject jsonObject = new JSONObject(teaTypeTimeMap);
                FileWriter fileWriter = new FileWriter(teasAndTimes);
                fileWriter.write(jsonObject.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Datei einlesen
        }
    }


    public static ObservableList<String> getTeaTypes() {
        return teaTypes;
    }

    public static HashMap<String, Integer> getTeaTypeTimeMap() {
        return teaTypeTimeMap;
    }

}
