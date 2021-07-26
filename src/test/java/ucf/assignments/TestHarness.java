package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ayush Pindoria
 */
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TestHarness
{
    private ArrayList<Inventory> list;
    private String errorLabel;

    public TestHarness()
    {
        reset();
    }

    public void reset()
    {
        list = new ArrayList<Inventory>();
        errorLabel = "";

    }

    public void addNewItem(Inventory task)
    {
        if(addItemValidate(task))
        {
            addItemCommit(task);
        }
    }

    private boolean addItemValidate(Inventory task)
    {

        if( task.getName().equals(""))
        {
            errorLabel = "Cannot create an empty inventory item";
            return false;
        }
        else if( task.getValue() <= 0.00)
        {
            errorLabel = "Cannot create an inventory item with no monetary value";
            return false;
        }
        else if(task.getSerial_number().equals(""))
        {
            errorLabel = "Cannot create an inventory item with no serial number";
            return false;
        }


        else if(isSerial(task.getSerial_number()) == false)
        {
            errorLabel = "Invalid Serial Number";
            return false;
        }
//
        else if(task.getName().length() < 2)
        {
            errorLabel = "Please Enter More than 1 Character";
            return false;
        }

        else if(task.getName().length() > 256)
        {
            errorLabel = "Please Enter Less Than 257 Characters";
            return false;
        }

        if(isDuplicate(task))
        {
            errorLabel = "Cannot create an inventory item with duplicate serial number";
            return false;
        }

        return true;
    }

    private void addItemCommit(Inventory task)
    {
        list.add(task);
        errorLabel = "";
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

    private boolean isDuplicate(Inventory task)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(task.getSerial_number().equals(list.get(i).getSerial_number()))
            {
                return true;
            }
        }
        return false;
    }


    public void deleteItem(int position, boolean deleteTask)
    {
        if(deleteTask)
        {
            if(list.size() == 0)
            {
                return;
            }

            list.remove(position);
        }
    }

    public void sortValue(ArrayList<Inventory> list)
    {
        Collections.sort(list, new Comparator<Inventory>()
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

    }

    public void sortSerialNumber(ArrayList<Inventory> list)
    {
        Collections.sort(list, new Comparator<Inventory>()
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

    }

    public void sortName(ArrayList<Inventory> list)
    {
        Collections.sort(list, new Comparator<Inventory>()
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

    }

    public ArrayList<Inventory> getList() {
        return list;
    }

    public void setList(ArrayList<Inventory> list) {
        this.list = list;
    }

    public String getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }



}

