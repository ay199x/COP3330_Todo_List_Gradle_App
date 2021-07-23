package ucf.assignments;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Inventory_GUI_Controller
{
    @FXML
    private SplitPane mainPane;
    @FXML
    private TextField nameText;
    @FXML
    private TextField moneyText;
    @FXML
    private TextField serialText;
    @FXML
    private TextField searchText;
    @FXML
    private Button addItemButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<Inventory> tableview;
    @FXML
    private TableColumn<Inventory, String> item_value;
    @FXML
    private TableColumn<Inventory, String> item_serial;
    @FXML
    private TableColumn<Inventory, String> item_name;
    @FXML
    private MenuButton sortButton;
    @FXML
    private MenuButton openButton;
    @FXML
    private MenuButton saveButton;

    public ObservableList<Inventory> items = FXCollections.observableArrayList();
    public FileChooser fc = new FileChooser(); //for saving and opening files
    public String fname = "";
    public int index;
    public boolean isopened = false;

    @FXML
    void addNewItem(ActionEvent event)
    {
        if(addItemValidate())
        {
            addItemCommit();
        }
    }

    private boolean addItemValidate()
    {

        if( nameText.getText().equals(""))
        {
            printError("Cannot create an empty inventory item");
            return false;
        }
        else if( moneyText.getText().equals(""))
        {
            printError("Cannot create an inventory item with no monetary value");
            return false;
        }
        else if(serialText.getText().equals(""))
        {
            printError("Cannot create an inventory item with no serial number");
            return false;
        }
       else if(isDouble(moneyText.getText()) == false && isSerial(serialText.getText()) == false && (nameText.getText().length() < 2 || nameText.getText().length() > 256))
        {
            printError("Please enter the inventory item data in the correct format");
            return false;
        }

        if(isDuplicate())
        {
            printError("Cannot create an inventory item with duplicate serial number");
            return false;
        }

        return true;
    }

    private void addItemCommit()
    {
        double value = Double.parseDouble(moneyText.getText());
        items.add(new Inventory(value, serialText.getText(), nameText.getText()));

        tableview.setItems(items);
        nameText.setText("");
        moneyText.setText("");
        serialText.setText("");
        errorLabel.setText("");

        //toggleButtons(items.isEmpty());

    }

    private boolean isDouble(String str)
    {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isSerial(String str)
    {
        if (str.equals(str.toUpperCase()) && str.length() == 10)
            return true;

        else
            return false;
    }

    private boolean isDuplicate()
    {
        for(int i = 0; i < items.size(); i++) {
            if (serialText.getText().equals(items.get(i).getSerial_number()))
                return true;
        }

        return false;
    }

    @FXML
    void deleteItem(ActionEvent event)
    {
        items.remove(tableview.getSelectionModel().getSelectedItem());
        tableview.refresh();
        //toggleButtons(items.isEmpty());
        tableview.getSelectionModel().clearSelection();
    }

    private void printError(String text)
    {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.RED);
    }


}

