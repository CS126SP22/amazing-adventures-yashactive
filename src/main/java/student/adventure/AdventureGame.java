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

    /**
     * The function deserializes the JSON file into POJOS using gson
     * @param myJson The string that describes the path to the JSON file
     */
    public static void deserialize(String myJson) throws IOException {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(myJson));
            gameLayout = gson.fromJson(reader, Layout.class);
        } catch (Exception e) {
            System.out.println("invalid json format");
        }
    }

    public static void main(String[] args) throws IOException {
        AdventureGame.deserialize("src/main/resources/adventureMap.json");
        Room[] rooms = gameLayout.getRooms();
        currentRoom = gameLayout.getStaringRoomObj();
        ArrayList<String> pickedUpItems = new ArrayList<String>();
        System.out.println("You are venturing into the NB residence. Danger and thrill awaits. \nThe real question is if you have the courage to reach Yash's Room. The ride is Bumpy ahead, tread carefully.");
        System.out.println("\nBelow is the list of commands");
        System.out.println("Go <direction> takes you to a room in that direction");
        System.out.println("Quit or Exit to exit this danger");
        System.out.println("Take <item> name to pick up the item from the room");
        System.out.println("Drop <item> to drop item in the room");
        System.out.println("Examine to examine your current location and situation");
        while (true) {
            System.out.print("> ");
            Scanner scan = new Scanner(System.in);
            String input = "";
            input = scan.nextLine();
            String formattedInput = input.trim().toLowerCase().replaceAll(" +", " ");
            String[] splitFormattedInput = formattedInput.split("\\s");
            String command = splitFormattedInput[0];
            if (command.equals("quit") || command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            else if (command.equals("go")) {
                changeRoom(currentRoom, splitFormattedInput);
                displayRoomInformation(currentRoom);
            }
            else if (command.equals("take")) {
                takeItemFromRoom(currentRoom, formattedInput, pickedUpItems);
            }
            else if (command.equals("drop")) {
                dropItemInRoom(currentRoom, formattedInput, pickedUpItems);
            }
            else if (command.equals("examine")) {
                if (splitFormattedInput.length != 1) {
                    System.out.println("I don't understand "+ formattedInput);
                    continue;
                }
                displayRoomInformation(currentRoom);
            }
            else if (command.equals("print")) {
                printPickedUpItemsList(pickedUpItems);
            } else {
                System.out.println("I don't understand " + formattedInput + "!");

            }
        }
    }

    /**
     * The function adds the item taken by the user to the itemslist while removing it from the array of items in the room
     * @param currentRooms The Room Object representing the current Room the user is in
     * @param userInput The String with the input from the user
     * @param itemList The ArrayList that keeps track of the items picked up by the user
     */
    public static void takeItemFromRoom(Room currentRooms, String userInput, ArrayList<String> itemList) {
        if (userInput.length() < 5) {
            System.out.println("There is no item inputted to be taken");
            return;
        }
        String input = userInput.substring(5);
        String[] availableItemsInRoom = currentRooms.getItems();
        for (int index = 0; index < availableItemsInRoom.length; index++) {
            if (input.equalsIgnoreCase(availableItemsInRoom[index])) {
                itemList.add(availableItemsInRoom[index]);
                List<String> list = new ArrayList<String>(Arrays.asList(availableItemsInRoom));
                list.remove(input);
                availableItemsInRoom = list.toArray(new String[0]);
                currentRooms.setItems(availableItemsInRoom);
                return;
            }
        }
        System.out.println("There is no item " + input + " in the room");
    }

    /**
     * The function enables the user to drop an item from his list into the room
     * @param currentRooms The Room Object representing the current Room the user is in
     * @param userInput The String with the input from the user
     * @param itemList The ArrayList that keeps track of the items picked up by the user
     */
    public static void dropItemInRoom(Room currentRooms, String userInput, ArrayList<String> itemList) {
        if (userInput.length() < 5) {
            System.out.println("You didn't input an item to be dropped");
            return;
        }
        String input = userInput.substring(5);
        String[] currItemList = currentRooms.getItems();
        String[] newItemRoomList = new String[currItemList.length + 1];
        System.arraycopy(currItemList,0, newItemRoomList,0, currItemList.length);
        if (itemList.indexOf(input) != -1) {
            int index = itemList.indexOf(input);
            newItemRoomList[currItemList.length] = itemList.get(index);
            itemList.remove(input);
        } else {
            System.out.println("You don't have " + input + "!");
            return;
        }
        currentRooms.setItems(newItemRoomList);

    }

    /**
     * The function prints out information like Room name, description, list of available items and available directions from the room.
     * @param currentRooms The room object representing the current room the user is in.
     */
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

    /**
     * The function changes the current room of the user to a new room based on the user input.
     * @param currentRooms The room object representing the current room the user is in.
     * @param userInput The String array represents the input from the user
     */
    public static void changeRoom(Room currentRooms, String[] userInput) {
        if (userInput.length != 2) {
            String input = String.join(" ",userInput);
            input = input.substring(3, input.length());
            System.out.println("I can't go " + input);
            return;
        }
        Direction[] canGoDirections = currentRooms.getDirections();
        int counter = canGoDirections.length;
        Room[] rooms = gameLayout.getRooms();
        while (counter != 0) {
           if (canGoDirections[counter - 1].getDirectionName().equalsIgnoreCase(userInput[1])) {
               Direction newDirection = canGoDirections[counter - 1];
               String nextRoomName = newDirection.getNextRoomName();
               System.out.println(nextRoomName);
               for (Room nextRoom : rooms) {
                   if (nextRoom.getName().equalsIgnoreCase(nextRoomName)) {
                       currentRoom = nextRoom;
                   }
               }
               return;
           }
           counter--;
        }
        System.out.println("I can't go " + userInput[1]);
    }

    /**
     * The function prints the items taken by the user from the room.
     * @param items List of items taken by the user
     */
    public static void printPickedUpItemsList(ArrayList<String> items) {
       for (int i = 0 ; i < items.size(); i++) {
           System.out.println(items.get(i));
       }
    }















}
