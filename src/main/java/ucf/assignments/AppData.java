package ucf.assignments;

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
