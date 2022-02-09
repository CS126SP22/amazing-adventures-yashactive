package student.adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static student.adventure.AdventureGame.changeRoom;

import com.google.gson.Gson;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class AdventureTest {
    private static Layout gameLayout;
    private static AdventureGame newGame;
    private static Room room;
    private static Direction directions;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));

    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Before
    public void setUp() {
        // This is run before every test.
        Gson gson = new Gson();
        newGame = new AdventureGame();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/adventureMap.json"));
            gameLayout = gson.fromJson(reader, Layout.class);
        } catch (Exception e) {
            System.out.println("invalid json format");
        }
    }
    @Test
    public void testChangeRoom() throws IOException {
        setUp();
        Room[] rooms = gameLayout.getRooms();
        Room currentRoom = gameLayout.getStaringRoomObj();
        System.out.println(currentRoom.getName());
        String[] input = {"Go", "North"};
        newGame.changeRoom(currentRoom,input);
        assertEquals(currentRoom.getName(), "LivingArea");
    }
    @Test
    public void testChangeRoomWithInvalidDirection() throws IOException {
        setUp();
        Room[] rooms = gameLayout.getRooms();
        Room currentRoom = gameLayout.getStaringRoomObj();
        System.out.println(currentRoom.getName());
        String[] input = {"Go", "South"};
        newGame.changeRoom(currentRoom,input);
        assertEquals(outContent.toString(), "I can't go South");
    }
    @Test
    public void testGetStartingRoomObject() {
        Room currentRoom = gameLayout.getStaringRoomObj();
        assertEquals(currentRoom.getName(), gameLayout.getStartingRoom());
    }
    @Test
    public void testGetRoomName() {
        Room[] rooms = gameLayout.getRooms();
        assertEquals(rooms[2].getName(), "Veranda");
    }
    @Test
    public void testAvailableDirections() {
        Room[] rooms = gameLayout.getRooms();
        Direction[] idealDirections = rooms[0].getDirections();
        String[] directionNames = new String[idealDirections.length];
        int counter = 0;
        for (Direction availableOnes : idealDirections) {
            directionNames[counter] = availableOnes.getDirectionName();
            counter++;
        }
        String[] ideaiInput = {"North", "West"};
        assertEquals(directionNames, ideaiInput);
    }
    @Test
    public void testGetDirectionName() {
        Room[] rooms = gameLayout.getRooms();
        Direction[] directions = rooms[1].getDirections();
        assertEquals("South", directions[2].getDirectionName());
    }
    @Test
    public void testDisplayRoomInformation() {
        //private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Room currentRoom = gameLayout.getStaringRoomObj();
        String idealOutput = "You are currently at the front door. This is the pathway to enter the house.\nEnter at your peril cause pathway to victory is dangerous. Find Yash in his room and you win\nFrom here, you can go: North, West\nItems Visible: shoes, water";
        newGame.displayRoomInformation(currentRoom);
        assertEquals(idealOutput,outContent.toString());
    }
    @Test(expected = NullPointerException.class)
    public void testDisplayRoomInformationNullRoom() throws NullPointerException {
        newGame.displayRoomInformation(null);
    }
    @Test
    public void testGetRoomDescripton() {
        Room[] rooms = gameLayout.getRooms();
        String descriptions = rooms[1].getDescription();
        assertEquals("You are in the Living room with sofas and maybe a few family members holding discussions.\nThey are very protective of Yash so try to be discreet as you move through this area", descriptions);
    }
    @Test
    public void testgetItems() {
        Room[] rooms = gameLayout.getRooms();
        String[] idealOutput = {"shoes", "water"};
        assertEquals(rooms[0].getItems(), idealOutput);
    }
    @Test
    public void testgetStartingRoom() {
        assertEquals("MainGate", gameLayout.getStartingRoom());
    }
    @Test
    public void testgetEndingRoom() {
        assertEquals("YashRoom", gameLayout.getEndingRoom());
    }
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void getNonExistentRoomName() {
        gameLayout.getRooms()[10].getName();
    }
    @Test
    public void testGetRooms() {
        int size = gameLayout.getRooms().length;
        assertEquals(9,size );
    }
    @Test
    public void testTakeItemFromRoom() {
        Room currRoom = gameLayout.getStaringRoomObj();
        ArrayList<String> newList = new ArrayList<>();
        String input = "Take shoes";
        newGame.takeItemFromRoom(currRoom,input,newList);
        assertEquals(newList.get(0), "shoes");

    }
    @Test
    public void testTakeItemFromRoomWhichIsNotInRoom() {
        Room currRoom = gameLayout.getStaringRoomObj();
        ArrayList<String> newList = new ArrayList<>();
        String input = "Take hail";
        newGame.takeItemFromRoom(currRoom,input,newList);
        assertEquals("There is no item hail in the room", outContent.toString());
    }
    @Test
    public void testTakeItemFromRoomWhichIsNotStartingRoom() {
        Room[] rooms = gameLayout.getRooms();
        Room currRoom = rooms[2];
        ArrayList<String> newList = new ArrayList<>();
        String input = "Take money";
        newGame.takeItemFromRoom(currRoom,input,newList);
        assertEquals(newList.get(0), "money");
    }
    @Test
    public void testTakeItemFromRoomWithEmptyCommand() {
        Room currRoom = gameLayout.getStaringRoomObj();
        ArrayList<String> newList = new ArrayList<>();
        String input = "Take";
        newGame.takeItemFromRoom(currRoom,input,newList);
        assertEquals("There is no item inputted to be taken", outContent.toString());
    }

    @Test
    public void testDropItemInRoom() {
        Room[] rooms = gameLayout.getRooms();
        Room currRoom = rooms[2];
        Room dropRoom = rooms[1];
        ArrayList<String> newList = new ArrayList<>();
        String inputTake = "Take money";
        String inputDrop = "Drop money";
        newGame.takeItemFromRoom(currRoom,inputTake,newList);
        newGame.dropItemInRoom(dropRoom, inputDrop, newList);
        String [] newString = dropRoom.getItems();
        assertEquals("money", newString[3]);
    }
    @Test
    public void testDropItemInRoomNotInList() {
        Room[] rooms = gameLayout.getRooms();
        Room dropRoom = rooms[1];
        ArrayList<String> newList = new ArrayList<>();
        String inputDrop = "Drop money";
        newGame.dropItemInRoom(dropRoom, inputDrop, newList);
        assertEquals("You don't have money!", outContent.toString());
    }
    @Test
    public void testDropItemWithEmptyCommand() {
        Room[] rooms = gameLayout.getRooms();
        Room dropRoom = rooms[1];
        ArrayList<String> newList = new ArrayList<>();
        String inputDrop = "Drop";
        newGame.dropItemInRoom(dropRoom, inputDrop, newList);
        assertEquals("You didn't input an item to be dropped", outContent.toString());
    }



}