package main;

import com.asprise.ocr.Ocr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {


    private static String path;
    private static List<String> foundList = new ArrayList<String>();

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Main.path = path;
    }

    public static List<String> getFoundList() {
        return foundList;
    }


    public static void findFiles(String pathToCheck, String textToFind, Boolean isInsideCatalog, String speedOption) throws IOException {
        foundList.clear();
        File folder = new File(pathToCheck);
        File[] listofFiles = folder.listFiles();
        for (int i = 0; i < listofFiles.length; i++) {
            System.out.println("Checking: " + listofFiles[i].toString());
            String insidePath = listofFiles[i].toString().toLowerCase();
            if (listofFiles[i].isFile() && (insidePath.endsWith(".jpg") || insidePath.endsWith(".jpeg") || insidePath.endsWith(".png"))) {
                String findResult = insideFind(listofFiles[i], textToFind, speedOption);
                if (!findResult.equals("")){
                    foundList.add(findResult);
                }
            } else if (listofFiles[i].isDirectory() && isInsideCatalog) {
                findFiles(listofFiles[i].toString(), textToFind, isInsideCatalog, speedOption);
            }
        }
    }

    public static String insideFind(File file, String textToFind, String speedOption){
        Ocr.setUp();
        Ocr ocr = new Ocr();
        System.out.println("Speed option choosed: " + speedOption);
        ocr.startEngine("eng", speedOption);
        String plainTextFile = ocr.recognize(new File[]{new File(file.toString())}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        if (plainTextFile.toLowerCase().contains(textToFind.toLowerCase())) {
            return file.toString();
        }
        return "";
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/layout.fxml"));
        primaryStage.setTitle("Pictures scanner");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
