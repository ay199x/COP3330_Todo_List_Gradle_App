package ucf.assignments;

public class Inventory {
    private double value;
    private String serial_number;
    private String name;

    /*
    @Override
    public String toString() {
        return "Inventory{" +
                "value=" + value +
                ", serial_number='" + serial_number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
*/
    public Inventory(double value, String serial_number, String name) {
        this.value = value;
        this.serial_number = serial_number;
        this.name = name;
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
}
