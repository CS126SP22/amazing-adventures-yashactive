package student.adventure;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;

import static javafx.application.Platform.exit;


public class AdventureGame {
    private static Layout gameLayout;
    private static Room currentRoom;
    public static void deserialize(String myJson) throws IOException {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(myJson));
            gameLayout = gson.fromJson(reader, Layout.class);
            //System.out.print(gameLayout);
        } catch (Exception e) {
            System.out.println("invalid json format");
        }
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(command);
        AdventureGame.deserialize("src/main/resources/adventureMap.json");
        Room[] rooms = gameLayout.getRooms();
        currentRoom = gameLayout.getStaringRoomObj();
        ArrayList<String> pickedUpItems = new ArrayList<String>();

        while (true) {
            System.out.print("> ");
            Scanner scan = new Scanner(System.in);
            String input = "";
            input = scan.nextLine();
            String formattedInput = input.trim().toLowerCase().replaceAll(" +", " ");
            System.out.println(formattedInput);
            String[] splitFormattedInput = formattedInput.split("\\s");
            String command = splitFormattedInput[0];
//        ArrayList<String> availableItems = new ArrayList<String>();
//        //System.out.println(command);
//        Room[] rooms = gameLayout.getRooms();
//        currentRoom = gameLayout.getStaringRoomObj();
            //System.out.println(currentRoom.getName());
            //displayRoomInformation(currentRoom);
            if (command.equals("quit") || command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }

            if (!command.equals("quit") && !command.equals("exit") && !command.equals("go") && !command.equals("drop") && !command.equals("take") && !command.equals("examine")) {
                System.out.println("I don't understand " + formattedInput + "!");
            }

            if (command.equals("go")) {
                changeRoom(currentRoom, splitFormattedInput);
                displayRoomInformation(currentRoom);

            }
            if (command.equals("take") || command.equals("drop")) {

            }
            if (command.equals("examine")) {
                displayRoomInformation(currentRoom);
            }
        }



    }


    public static void displayRoomInformation(Room currentRooms) {
        Room currentRoomObj = currentRooms;
        if (currentRoomObj.getName().equals(gameLayout.getEndingRoom())) {
            System.out.println("YOU WIN SUCKER");
            System.exit(0);
        }
        String[] itemsList = currentRooms.getItems();
        Direction[] availableDirections = currentRoomObj.getDirections();
        String[] directionNames = new String[availableDirections.length];
        int counter = 0;
        for (Direction availableOnes : availableDirections) {
            directionNames[counter] = availableOnes.getDirectionName();
            counter++;
        }
        String availableDirectionNames = Arrays.toString(directionNames);
        availableDirectionNames = availableDirectionNames.substring(1, availableDirectionNames.length()-1);
        System.out.println("\n"+ currentRoomObj.getDescription());
        System.out.println("From here, you can go: "+ availableDirectionNames);
        String itemsInRoom = Arrays.toString(itemsList);
        itemsInRoom = itemsInRoom.substring(1, itemsInRoom.length() -1);
        System.out.println("Items Visible: " + itemsInRoom);
    }

    public static void changeRoom(Room currentRooms, String[] userInput) {
        if (userInput.length != 2) {
            System.out.println("I can't go " + Arrays.toString(userInput));
        }
        Direction[] canGoDirections = currentRooms.getDirections();
        int counter = canGoDirections.length;
        Room[] rooms = gameLayout.getRooms();
        while (counter != 0) {
           if (canGoDirections[counter - 1].getDirectionName().equalsIgnoreCase(userInput[1])) {
               Direction newDirection = canGoDirections[counter - 1];
               String nextRoomName = newDirection.getNextRoomName();
               System.out.println(nextRoomName);
               for (Room m : rooms) {
                   if (m.getName().equalsIgnoreCase(nextRoomName)) {
                       currentRoom = m;
                   }
               }
               return;
           }
           counter--;
        }
        System.out.println("I can't go " + userInput[1]);
    }















}
