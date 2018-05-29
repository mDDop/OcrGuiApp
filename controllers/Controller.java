package main.controllers;

import com.asprise.ocr.Ocr;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button findButton;
    @FXML
    CheckBox checkBox;
    @FXML
    Button choseButton;
    @FXML
    TextField textField;
    @FXML
    ListView listView;
    @FXML
    TableView<String> tableTable;
    @FXML
    TableColumn<String, String> textFound= new TableColumn("Found in: ");
    @FXML
    ToggleGroup group;
    @FXML
    RadioButton optionFast;
    @FXML
    RadioButton optionMedium;
    @FXML
    RadioButton optionSlow;


    ObservableList<String> listFinal = FXCollections.observableArrayList();

    DirectoryChooser directoryChooser = new DirectoryChooser();
    Stage stage = new Stage();

    public void findFiles() throws IOException {
        tableTable.getItems().clear();
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        String value = selectedRadioButton.getText();
        String option = Ocr.SPEED_FASTEST;
        if (value.equals(optionMedium.getText())){
            option = Ocr.SPEED_FAST;
        } else if (value.equals(optionSlow.getText())) {
            option = Ocr.SPEED_SLOW;
        }
        Main.findFiles(Main.getPath(), textField.getText(), checkBox.selectedProperty().getValue(), option);
        for (String s : Main.getFoundList()) {
            System.out.println(s);
            listFinal.add(s);
        }
        tableTable.setItems(listFinal);
        textFound.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(cellData.getValue()));
        textFound.prefWidthProperty().bind(tableTable.widthProperty());
        tableTable.getColumns().setAll(textFound);

    }
    public void loadPath(ActionEvent actionEvent) {
        File file = directoryChooser.showDialog(stage);
        if (file != null){
            String s = file.getAbsolutePath();
            Main.setPath(s);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        optionFast.setSelected(true);
        tableTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount()==2){
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + tableTable.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
