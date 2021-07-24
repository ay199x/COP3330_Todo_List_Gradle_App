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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private TextField searchField;
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
    public void initialize()
    {
        String path = System.getProperty("user.dir");
        fc.setInitialDirectory(new File(path));
        toggleButtons(false);

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

    private boolean isSerial(String str)
    {

        if (str.equals(str.toUpperCase()) && str.length() == 10 && str.matches("^[a-zA-Z0-9]*$"))
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
        toggleButtons(items.isEmpty());
        tableview.getSelectionModel().clearSelection();
    }

    @FXML
    void addItemClicked(MouseEvent event)
    {
        tableview.getSelectionModel().clearSelection();

        Scene scene = (Scene) mainPane.getScene();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                enterPressed();
            }
        });
    }

    private void enterPressed()
    {
        if(nameText.isFocused())
        {
            addNewItem(null);
        }
    }

    @FXML
    void itemsTableClicked(MouseEvent event)
    {
        if(!items.isEmpty())
        {
            addItemButton.setDisable(true);
        }


        deleteButton.setDisable(items.isEmpty());
        addItemButton.setDisable(false);
        updateButton.setDisable(true);
        tableview.getSelectionModel().clearSelection();

        if(tableview.getSelectionModel().getSelectedIndex() >= 0)
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

    @FXML
    public void updateItemClicked(ActionEvent actionEvent) throws IOException  //when update button is clicked
    {

        if(addItemValidate()) {
            items.remove(index);
            double value = Double.parseDouble(moneyText.getText());
            items.add(new Inventory(value, serialText.getText(), nameText.getText()));

            tableview.setItems(items);
            tableview.setItems(items);
            nameText.setText("");
            moneyText.setText("");
            serialText.setText("");
            errorLabel.setText("");

            toggleButtons(items.isEmpty());
        }

        /*
        new FileOutputStream(fname).close();
        saveItemData(fname);

         */

    }

    @FXML
    public void sortName(ActionEvent actionEvent)
    {
        Collections.sort(items, new Comparator<Inventory>()
        {
            @Override
            public int compare(Inventory t1, Inventory t2)
            {

                if( t1.getName().compareTo(t2.getName()) == 1 || t1.getName().compareTo(t2.getName()) == 0 )
                {
                    return 1;
                }
                if( t1.getName().compareTo(t2.getName()) == -1)
                {
                    return -1;
                }
                return 0;
            }
        });

        tableview.setItems(items);
    }

    int getSortOrder(String s)
    {   Pattern p = Pattern.compile("^([0-9]+)([a-z]?)$");
        Matcher m = p.matcher(s);
        if(!m.matches()) return 0;
        int major = Integer.parseInt(m.group(1));
        int minor = m.group(2).isEmpty() ? 0 : m.group(2).charAt(0);
        return (major << 8) | minor;
    }

    @FXML
    public void sortSerialNumber(ActionEvent actionEvent)
    {
        Collections.sort(items, new Comparator<Inventory>()
        {
            @Override
            public int compare(Inventory t1, Inventory t2)
            {

                return getSortOrder(t1.getSerial_number()) - getSortOrder(t2.getSerial_number());
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




}

