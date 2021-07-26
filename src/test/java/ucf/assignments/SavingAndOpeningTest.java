package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ayush Pindoria
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingAndOpeningTest
{
    private ArrayList<Inventory> list = new ArrayList<>();

    @BeforeEach
    public void start()
    {
        list.add(new Inventory(900.00,"A1B2C3D4E5","SAMSUNG S21 PLUS"));
        list.add(new Inventory(400.00,"B1B2C3D4E5","SAMSUNG GEAR S3"));
        list.add(new Inventory(600.00,"C3D4E5F6G7","APPLE WATCH"));
    }


    @ParameterizedTest
    @MethodSource
    public void Open(String test, File file) throws Exception
    {
        ArrayList<Inventory> opened = LoadSave.Open(file);
        assertEquals(list,opened,test);
    }

    public static Stream<Arguments> Open()
    {
        return Stream.of(Arguments.of("TSV Import", new File("src\\test\\resources\\ucf\\assignments\\test.txt")), Arguments.of("HTML Import", new File("src\\test\\resources\\ucf\\assignments\\test.html")), Arguments.of("HTML Import", new File("src\\test\\resources\\ucf\\assignments\\test.json")));
    }
}
