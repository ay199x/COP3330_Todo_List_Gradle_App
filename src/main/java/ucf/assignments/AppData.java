package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ayush Pindoria
 */
import java.io.Serializable;
import java.util.ArrayList;

public class AppData implements Serializable
{
    private ArrayList<Inventory> list;

    public AppData(ArrayList<Inventory> list)
    {
        this.list = list;

    }

    public ArrayList<Inventory> getList()
    {

        return list;
    }

}
