package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ayush Pindoria
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Ayush Pindoria
 */
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestBasicFunctionality
{
    private static TestHarness app;
    private static Random random;

    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        app = new TestHarness();
        random = new Random();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        app = null;
    }

    @BeforeEach
    void setUp() throws Exception
    {
        app.addNewItem(new Inventory(45.00,"A6D7G3T5H2","RAZER MOUSE"));
        app.addNewItem(new Inventory(65.99,"C6D7G3T5H2","PROTEIN PLUS"));
        app.addNewItem(new Inventory(3.49,"D6D7G3T5H2","WRIGLEY'S GUM"));
        app.addNewItem(new Inventory(300.00,"B6D7G3T5H2","SAMSUNG GEAR S3"));
        app.addNewItem(new Inventory(800.99,"G6D7G3T5H2","SAMSUNG S21 PLUS"));
    }

    @AfterEach
    void tearDown() throws Exception
    {
        app.reset();
    }

    @Test
    void testAddItems()
    {
        System.out.println("testAddTasks start");

        app.reset();
        assertEquals(0, app.getList().size());
        app.addNewItem(new Inventory(45.00,"A6D7G3T5HX","LOGITECH MOUSE"));
        app.addNewItem(new Inventory(65.99,"C6D7G3T5HX","KETO PLUS"));
        app.addNewItem(new Inventory(6.78,"D6D7G3T5HX","COBALT GUM"));
        app.addNewItem(new Inventory(100.00,"B6D7G3T5HX","SAMSUNG GEAR S2"));
        app.addNewItem(new Inventory(900.99,"G6D7G3T5HX","SAMSUNG S24 PLUS"));
        assertEquals(5, app.getList().size());

        System.out.println("testAddTasks end");
    }

    @Test
    void testRemoveItems()
    {
        System.out.println("testRemoveTasks start");

        assertEquals(5, app.getList().size());

        app.deleteItem(random.nextInt(app.getList().size()), true);
        app.deleteItem(random.nextInt(app.getList().size()), true);
        app.deleteItem(random.nextInt(app.getList().size()), true);
        app.deleteItem(random.nextInt(app.getList().size()), true);

        assertEquals(1, app.getList().size());

        System.out.println("testRemoveTasks end");
    }



    @Test
    void testAddNullItems()
    {
        System.out.println("testAddNullTask start");

        assertTrue(app.getErrorLabel().equals(""));
        assertEquals(5, app.getList().size());

        app.addNewItem(new Inventory(56.67, "XXXXX12345", ""));

        assertTrue(app.getErrorLabel().equals("Cannot create an empty inventory item"));
        assertEquals(5, app.getList().size());

        app.addNewItem(new Inventory(0.00, "XXXXX12345", "XBOX"));

        assertTrue(app.getErrorLabel().equals("Cannot create an inventory item with no monetary value"));
        assertEquals(5, app.getList().size());

        app.addNewItem(new Inventory(56.67, "", "XBOX"));

        assertTrue(app.getErrorLabel().equals("Cannot create an inventory item with no serial number"));
        assertEquals(5, app.getList().size());


        System.out.println("testAddNullTask end");
    }


    @Test
    void testAddDuplicateItem()
    {
        System.out.println("testAddDuplicateTask start");
        app.reset();

        assertTrue(app.getErrorLabel().equals(""));
        assertEquals(0, app.getList().size());

        app.addNewItem(new Inventory(45.00,"A6D7G3T5H2","LOGITECH MOUSE"));

        assertEquals(1, app.getList().size());
        assertTrue(app.getErrorLabel().equals(""));

        app.addNewItem(new Inventory(670.9,"A6D7G3T5H2","RAZER MOUSE"));

        assertEquals(1, app.getList().size());
        assertTrue(app.getErrorLabel().equals("Cannot create an inventory item with duplicate serial number"));

        app.addNewItem(new Inventory(670.9,"B6D7G3T5H2","RAZER MOUSE"));

        assertEquals(2, app.getList().size());
        assertTrue(app.getErrorLabel().equals(""));

        System.out.println("testAddDuplicateTask end");
    }


    @Test
    void testCheckValueSorting()
    {
        System.out.println("testCheckSorting start");

        app.reset();
        assertEquals(0, app.getList().size());

        app.addNewItem(new Inventory(450.00,"A6D7G3T5H0","LOGITECH MOUSE"));
        app.addNewItem(new Inventory(658.99,"C6D7G3T5H0","KETO PLUS"));
        app.addNewItem(new Inventory(78.78,"D6D7G3T5H0","COBALT GUM"));
        app.addNewItem(new Inventory(10.00,"B6D7G3T5H0","SAMSUNG GEAR S2"));
        app.addNewItem(new Inventory(457.99,"G6D7G3T5H0","SAMSUNG S24 PLUS"));
        app.sortValue(app.getList());

        for(int i = 0 ; i < app.getList().size() ; i++)
        {
            if(i == 0)
            {
                continue;
            }
            assertFalse(app.getList().get(i).getValue() <= app.getList().get(i-1).getValue());
        }

        System.out.println("testCheckSorting end");
    }

    @Test
    void testCheckSerialNoSorting()
    {
        System.out.println("testCheckSorting start");

        app.reset();
        assertEquals(0, app.getList().size());

        app.addNewItem(new Inventory(45.00,"A6D7G3T5H2","LOGITECH MOUSE"));
        app.addNewItem(new Inventory(65.99,"C6D7G3T5H2","KETO PLUS"));
        app.addNewItem(new Inventory(6.78,"D6D7G3T5H2","COBALT GUM"));
        app.addNewItem(new Inventory(100.00,"B6D7G3T5H2","SAMSUNG GEAR S2"));
        app.addNewItem(new Inventory(900.99,"G6D7G3T5H2","SAMSUNG S24 PLUS"));
        app.sortSerialNumber(app.getList());

        for(int i = 0 ; i < app.getList().size() ; i++)
        {
            if(i == 0)
            {
                continue;
            }
            assertFalse(app.getList().get(i).getSerial_number().compareTo(app.getList().get(i-1).getSerial_number()) < 0);
        }

        System.out.println("testCheckSorting end");
    }

    @Test
    void testCheckNameSorting()
    {
        System.out.println("testCheckSorting start");

        app.reset();
        assertEquals(0, app.getList().size());

        app.addNewItem(new Inventory(45.00,"A6D7G3T5H2","LOGITECH MOUSE"));
        app.addNewItem(new Inventory(65.99,"C6D7G3T5H2","KETO PLUS"));
        app.addNewItem(new Inventory(6.78,"D6D7G3T5H2","COBALT GUM"));
        app.addNewItem(new Inventory(100.00,"B6D7G3T5H2","AAMSUNG GEAR S2"));
        app.addNewItem(new Inventory(900.99,"G6D7G3T5H2","EAMSUNG S24 PLUS"));
        app.sortName(app.getList());

        for(int i = 0 ; i < app.getList().size() ; i++)
        {
            if(i == 0)
            {
                continue;
            }
            assertFalse(app.getList().get(i).getName().compareTo(app.getList().get(i-1).getName()) < 0);
        }

        System.out.println("testCheckSorting end");
    }
}