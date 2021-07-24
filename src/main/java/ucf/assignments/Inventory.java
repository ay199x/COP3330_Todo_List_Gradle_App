package ucf.assignments;

import java.io.Serializable;

public class Inventory implements Serializable
{
    private double value;
    private String serial_number;
    private String name;
    private String dollars;

    /*
    @Override
    public String toString() {
        return "$";
    }
    */


    public Inventory(double value, String serial_number, String name) {
        this.value = value;
        this.serial_number = serial_number;
        this.name = name;

        this.dollars = "$" + String.valueOf(value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public String getName() {
        return name;
    }

    public String getDollars(){
        return dollars;
    }


}
