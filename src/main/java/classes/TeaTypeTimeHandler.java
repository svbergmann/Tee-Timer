package classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class TeaTimeTypeHandler for saving the Teas and Times in a HashMap and writing the HashMap in a JSON file.
 *
 * @author ProfSchmergmann
 */
public class TeaTypeTimeHandler implements Serializable {

    private static TeaTypeTimeHandler self = new TeaTypeTimeHandler();

    private final Gson gson = new Gson();
    private File teasAndTimes = new File(System.getProperty("user.home") + "\\TeasAndTimes.gson");
    private HashMap<String, Double> teaTypesAndTimesHashMap;

    private boolean successfulWrote;
    private boolean successfulRead;

    public static TeaTypeTimeHandler getInstance() {
        return self;
    }

    /**
     * Constructor for reading an existing file or creating a new file if there is none.
     */
    private TeaTypeTimeHandler() {
        if (teasAndTimes.exists()) {
            teaTypesAndTimesHashMap = readFile(teasAndTimes);
        } else {
            URL resource = getClass().getResource("/files/TeaTypes.json");
            teaTypesAndTimesHashMap = readFile(new File(resource.getPath()));
            writeFile();
        }
    }

    /**
     * Method for a wrong formatted file.
     */
    public void wasWrongFormat() {
        teasAndTimes.delete();
        URL resource = getClass().getResource("/files/TeaTypes.json");
        teaTypesAndTimesHashMap = readFile(new File(resource.getPath()));
        writeFile();
    }

    public boolean isSuccessfulRead() {
        return successfulRead;
    }

    /**
     * Writes the new HashMap converted as JSON format in the file.
     */
    public void writeFile() {
        try {
            String json = gson.toJson(teaTypesAndTimesHashMap);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(teasAndTimes));
            bufferedWriter.write(json);
            bufferedWriter.close();
            successfulWrote = true;
        } catch (IOException e) {
            successfulWrote = false;
        }
    }

    /**
     * Reads the file.
     *
     * @param file the file.
     * @return a HashMap.
     * @throws ClassCastException if the file isn't formatted in JSON.
     */
    private HashMap<String, Double> readFile(File file) throws ClassCastException {
        HashMap<String, Double> temp = null;
        try {
            temp = gson.fromJson(new FileReader(file), new TypeToken<HashMap<String, Double>>() {
            }.getType());
            successfulRead = true;
        } catch (Throwable e) {
            e.printStackTrace();
            successfulRead = false;
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