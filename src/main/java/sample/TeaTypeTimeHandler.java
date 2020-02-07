package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TeaTypeTimeHandler implements Serializable {

    private static final Gson gson = new Gson();
    private File teasAndTimes = new File(System.getProperty("user.home") + "\\TeasAndTimes.gson");

    private HashMap<String, Double> teaTypesAndTimesHashMap;

    public TeaTypeTimeHandler() throws IOException {
        if (fileExists()) {
            teaTypesAndTimesHashMap = readFile(teasAndTimes);
            System.out.println(teasAndTimes.toString());
        } else {
            teaTypesAndTimesHashMap = readFile(new File("src/main/resources/TeaTypes.json"));
            writeFile();
        }
    }

    private boolean fileExists() {
        return teasAndTimes.exists();
    }

    public void writeFile() throws IOException {
        String json = gson.toJson(teaTypesAndTimesHashMap);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(teasAndTimes));
        bufferedWriter.write(json);
        bufferedWriter.close();
    }

    private HashMap<String, Double> readFile(File file) throws ClassCastException {
        HashMap<String, Double> temp;
        try {
            temp = gson.fromJson(new FileReader(file), new TypeToken<HashMap<String, Double>>() {
            }.getType());
        } catch (Throwable e) {
            throw new ClassCastException("Wrong formatted file!");
        }
        return temp;
    }

    public ObservableList<String> getTeaTypesAsObservableList() {
        ArrayList<String> temp = new ArrayList<>(teaTypesAndTimesHashMap.keySet());
        return FXCollections.observableList(temp);
    }

    public void addTeaType(String tea, double time) {
        teaTypesAndTimesHashMap.put(tea, time);
    }

    public double getTeaTimeInSeconds(String tea) {
        return teaTypesAndTimesHashMap.get(tea) * 60;
    }


}
