package ucf.assignments;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ucf.assignments.LoadSave.Type.*;

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
    private TextField searchField;
    @FXML
    private Button addItemButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelUpdateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private MenuItem openItem;
    @FXML
    private MenuItem TSVItem;
    @FXML
    private MenuItem HTMLItem;
    @FXML
    private MenuItem JSONItem;
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


    public ObservableList<Inventory> items = FXCollections.observableArrayList();

    //public FileChooser fc = new FileChooser(); //for saving and opening files
    public String fname = "";
    public int index;
    public boolean isopened = false;


    @FXML
    public void initialize()
    {
        //String path = System.getProperty("user.dir");
        //fc.setInitialDirectory(new File(path));
        toggleButtons(false);

        item_value.setCellValueFactory(new PropertyValueFactory<>("dollars"));

        item_serial.setCellValueFactory(new PropertyValueFactory<>("serial_number"));

        item_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        FilteredList<Inventory> filteredData = new FilteredList<>(items, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(inventory -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (inventory.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                {
                    return true; // Filter matches name.
                }
                else if (inventory.getSerial_number().toLowerCase().indexOf(lowerCaseFilter) != -1)
                {
                    return true; // Filter matches serial number.
                }
                else
                    return false; // Does not match.
            });
        });


        SortedList<Inventory> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);

        tableview.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                if(tableview.getSelectionModel().getSelectedItem() != null)
                {
                    updateButton.setDisable(false);
                    addItemButton.setDisable(true);
                    index = tableview.getSelectionModel().getSelectedIndex();

                    String name = items.get(index).getName();
                    nameText.setText(name);

                    String serial = items.get(index).getSerial_number();
                    serialText.setText(serial);

                    double money = items.get(index).getValue();
                    String money_str = String.valueOf(money);
                    moneyText.setText(money_str);
                }
            }


        });


        /*
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                if(isopened == true) {
                    try {
                        saveItemData(fname);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
     */

        tableview.getSelectionModel().clearSelection();


    }

    @FXML
    public void saveClicked(ActionEvent actionEvent)
    {
        Object src = actionEvent.getSource();

        if (src == TSVItem)
        {
            Save_as(TSV);
        }

        else if (src == HTMLItem)
        {
            Save_as(HTML);
        }

        else if (src == JSONItem)
        {
            Save_as(JSON);
        }
    }

    private void Save_as(LoadSave.Type type)
    {
        FileChooser chooser = new FileChooser();

        FileChooser.ExtensionFilter filter = getExtensionFilter(type);
        chooser.getExtensionFilters().add(filter);
        File filename = chooser.showSaveDialog(InventoryTracker.getMainWindow());

        ArrayList<Inventory> list = (ArrayList<Inventory>) items.stream().collect(Collectors.toList());
        AppData data = new AppData(list);

        if (filename == null)
            return;

        try
        {
            LoadSave.Save_As(filename, type, data.getList());
        }
        catch (Exception e)
        {
            showAlert("Save Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void openClicked(ActionEvent actionEvent)
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(getExtensionFilter(TSV), getExtensionFilter(HTML), getExtensionFilter(JSON));
        File filename = chooser.showOpenDialog(InventoryTracker.getMainWindow());

        if (filename == null)
            return;

        try
        {
            ArrayList<Inventory> list = LoadSave.Open(filename);

            AppData data = new AppData(list);
            items.setAll(data.getList());
            tableview.setItems(items);

        }

        catch (Exception e)
        {
            e.printStackTrace();
            showAlert("File Open Error", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

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


        else if(isDouble(moneyText.getText()) == false)
        {
            printError("Please enter the monetary value in the correct format");
            return false;
        }

        else if(isSerial(serialText.getText()) == false)
        {
            printError("Please enter the serial number in the correct format");
            return false;
        }

        else if(nameText.getText().length() < 2)
        {
            printError("Inventory item name less than 2 characters");
            return false;
        }

        else if(nameText.getText().length() > 256)
        {
            printError("Inventory item name cannot exceed 256 characters");
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

        toggleButtons(items.isEmpty());

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

    private static boolean isStringUpperCase(String str)
    {
        char currentCharacter;
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";

        for (int i = 0; i < str.length(); i++)
        {
            currentCharacter = str.charAt(i);
            if (!Character.isDigit(currentCharacter) && !specialChars.contains(String.valueOf(currentCharacter)))
            {
                if(!Character.isUpperCase(currentCharacter))
                {
                    return false;
                }

            }
        }

        return true;
    }

    private boolean isSerial(String str)
    {

        if ( isStringUpperCase(str) && (str.length() == 10) && str.matches("^[a-zA-Z0-9]*$")) {
            return true;
        }

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
        tableview.getSelectionModel().clearSelection();

        nameText.setText("");
        moneyText.setText("");
        serialText.setText("");
        errorLabel.setText("");

        addItemButton.setDisable(false);
        updateButton.setDisable(false);
        cancelUpdateButton.setDisable(false);
        deleteButton.setDisable(false);
    }


    @FXML
    public void updateItemClicked(ActionEvent actionEvent) throws IOException, InterruptedException  //when update button is clicked
    {

        if(tableview.getSelectionModel().getSelectedItem() != null) {

            items.remove(index);

            double value = Double.parseDouble(moneyText.getText());
            items.add(index, new Inventory(value, serialText.getText(), nameText.getText()));

            tableview.setItems(items);
            tableview.getSelectionModel().clearSelection();
            nameText.setText("");
            moneyText.setText("");
            serialText.setText("");
            errorLabel.setText("");


            toggleButtons(items.isEmpty());
        }

        else
        {
            printError("Please Select An Inventory Item To Update");
            //Thread.sleep(2000);
            //errorLabel.setText("");

        }



        /*
        new FileOutputStream(fname).close();
        saveItemData(fname);

         */

    }

    @FXML
    public void cancelUpdateClicked(ActionEvent actionEvent) throws IOException, InterruptedException  //when update button is clicked
    {
        if(tableview.getSelectionModel().getSelectedItem() != null) {
            tableview.getSelectionModel().clearSelection();
            nameText.setText("");
            moneyText.setText("");
            serialText.setText("");
            errorLabel.setText("");


            toggleButtons(items.isEmpty());
        }

        else
        {
            printError("Please Don't Click On Cancel Without Selecting An Item");
            //Thread.sleep(2000);
            //errorLabel.setText("");
        }

    }


        @FXML
    public void sortName(ActionEvent actionEvent)
    {
        Collections.sort(items, new Comparator<Inventory>()
        {
            @Override
            public int compare(Inventory t1, Inventory t2)
            {

                if( t1.getName().compareTo(t2.getName()) > 0 || t1.getName().compareTo(t2.getName()) == 0 )
                {
                    return 1;
                }
                if( t1.getName().compareTo(t2.getName()) < 0)
                {
                    return -1;
                }
                return 0;
            }
        });

        tableview.setItems(items);
    }


    @FXML
    public void sortSerialNumber(ActionEvent actionEvent)
    {
        Collections.sort(items, new Comparator<Inventory>()
        {
            @Override
            public int compare(Inventory t1, Inventory t2)
            {

                if( t1.getSerial_number().compareTo(t2.getSerial_number()) > 0 || t1.getSerial_number().compareTo(t2.getSerial_number()) == 0 )
                {
                    return 1;
                }
                if( t1.getSerial_number().compareTo(t2.getSerial_number()) < 0)
                {
                    return -1;
                }
                return 0;
            }
        });

        tableview.setItems(items);
    }

    @FXML
    public void sortValue(ActionEvent actionEvent)
    {
        Collections.sort(items, new Comparator<Inventory>()
        {
            @Override
            public int compare(Inventory t1, Inventory t2)
            {

                if( t1.getValue() > t2.getValue() || t1.getValue() == t2.getValue() )
                {
                    return 1;
                }
                if( t1.getValue() < t2.getValue())
                {
                    return -1;
                }
                return 0;
            }
        });

        tableview.setItems(items);
    }

    private void toggleButtons(boolean listsEmpty)
    {
        deleteButton.setDisable(listsEmpty);
        addItemButton.setDisable(listsEmpty);
    }

    private void printError(String text)
    {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.RED);
    }

    private void showAlert(String title, String message, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }

    private FileChooser.ExtensionFilter getExtensionFilter(LoadSave.Type type)
    {
        if (type == TSV)
        {
            return new FileChooser.ExtensionFilter(type.getFilename(),type.getExtension());
        }
        else if (type == HTML)
        {
            return new FileChooser.ExtensionFilter(type.getFilename(),type.getExtension());
        }
        else
        {
            return new FileChooser.ExtensionFilter(type.getFilename(),type.getExtension());
        }

    }

}

