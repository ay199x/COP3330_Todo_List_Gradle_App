package ucf.assignments;

import java.util.ArrayList;

public class AppData
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
