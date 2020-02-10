package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class TeaTimeTypeHandler for saving the Teas and Times in a HashMap and writing the HashMap in a JSON file.
 *
 * @author Prof Schmergmann
 */
public class TeaTypeTimeHandler implements Serializable {

    private static final Gson gson = new Gson();
    private File teasAndTimes = new File(System.getProperty("user.home") + "\\TeasAndTimes.gson");
    private HashMap<String, Double> teaTypesAndTimesHashMap;

    /**
     * Constructor for reading an existing file or creating a new file if there is none.
     *
     * @throws IOException if something is wrong with reading or writing the file.
     */
    public TeaTypeTimeHandler() throws IOException {
        if (teasAndTimes.exists()) {
            teaTypesAndTimesHashMap = readFile(teasAndTimes);
            System.out.println(teasAndTimes.toString());
        } else {
            teaTypesAndTimesHashMap = readFile(new File("src/main/resources/TeaTypes.json"));
            writeFile();
        }
    }

    /**
     * Writes the new HashMap converted as JSON format in the file.
     *
     * @throws IOException if something is wrong with writing the file.
     */
    public void writeFile() throws IOException {
        String json = gson.toJson(teaTypesAndTimesHashMap);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(teasAndTimes));
        bufferedWriter.write(json);
        bufferedWriter.close();
    }

    /**
     * Reads the file.
     *
     * @param file the file.
     * @return a HashMap.
     * @throws ClassCastException if the file isn't formatted in JSON.
     */
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

    /**
     * Creates and returns an ObservableList for the ComboBox.
     *
     * @return an ObservableList.
     */
    public ObservableList<String> getTeaTypesAsObservableList() {
        ArrayList<String> temp = new ArrayList<>(teaTypesAndTimesHashMap.keySet());
        return FXCollections.observableList(temp);
    }

    /**
     * Adds a tea type to the HashMap.
     *
     * @param tea  a String.
     * @param time an int.
     */
    public void addTeaType(String tea, double time) {
        teaTypesAndTimesHashMap.put(tea, time);
    }

    /**
     * Returns the saved time for a tea in seconds.
     *
     * @param tea the key for the HashMap.
     * @return the teatime in seconds.
     */
    public double getTeaTimeInSeconds(String tea) {
        return teaTypesAndTimesHashMap.get(tea) * 60;
    }
}