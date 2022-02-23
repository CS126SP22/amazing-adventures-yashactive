package student.adventure;

import com.google.gson.Gson;
import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AdvetureGameAPI {
    private static Layout gameLayout;
    private static Room currentRoom = gameLayout.getStaringRoomObj();
    private GameStatus statusOfGame;
    private int newGameId;
    ArrayList<String> pickedUpItems = new ArrayList<String>();
    Room[] rooms = gameLayout.getRooms();

    public AdvetureGameAPI() {
        currentRoom = gameLayout.getStaringRoomObj();
        pickedUpItems = new ArrayList<String>();
        rooms = gameLayout.getRooms();
        deserialize("src/main/resources/adventureMap.json");

    }

    public GameStatus getStatusOfGame() {
        return statusOfGame;
    }

    public void setNewGameId(int newGameId) {
        this.newGameId = newGameId;
    }


    /**
     * The function deserializes the JSON file into POJOS using gson
     * @param myJson The string that describes the path to the JSON file
     */
    public static void deserialize(String myJson)  {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(myJson));
            gameLayout = gson.fromJson(reader, Layout.class);
        } catch (Exception e) {
            System.out.println("invalid json format");
        }
    }

    public String executeCommands(Command inputCommand) {
            String input1 = "";
            input1 = inputCommand.getCommandName().trim().toLowerCase();
            String input2 = inputCommand.getCommandValue().trim().toLowerCase();
           // String formattedInput = input.trim().toLowerCase().replaceAll(" +", " ");
           // String[] splitFormattedInput = formattedInput.split("\\s");
            String command = input1;
            if (command.equals("quit") || command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            else if (command.equals("go")) {
                statusOfGame = new GameStatus(false,newGameId, changeRoom(currentRoom, input2)  ,"" , "", new AdventureState(),statusOfGame.getCommandOptions());
                return changeRoom(currentRoom, input2);
                //displayRoomInformation(currentRoom);
            }
            else if (command.equals("take")) {
                statusOfGame = new GameStatus(false,newGameId, takeItemFromRoom(currentRoom, input2, pickedUpItems) ,"" , "", new AdventureState(),statusOfGame.getCommandOptions());
                return takeItemFromRoom(currentRoom, input2, pickedUpItems);
            }
            else if (command.equals("drop")) {
                statusOfGame = new GameStatus(false,newGameId, dropItemInRoom(currentRoom, input2, pickedUpItems) ,"" , "", new AdventureState(),statusOfGame.getCommandOptions());
                return dropItemInRoom(currentRoom, input2, pickedUpItems);
            }
            else if (command.equals("examine")) {
                statusOfGame = new GameStatus(false,newGameId, displayRoomInformation(currentRoom) ,"" , "", new AdventureState(),statusOfGame.getCommandOptions());
                return displayRoomInformation(currentRoom);
            }
            else if (command.equals("print")) {
                printPickedUpItemsList(pickedUpItems);
            } else {
                statusOfGame = new GameStatus(false, newGameId, "I don't understand " + input1 + "!" ,"" , "", new AdventureState(),statusOfGame.getCommandOptions());
                return ("I don't understand " + input1 + "!");

            }
            return "";
        }

    /**
     * The function adds the item taken by the user to the itemslist while removing it from the array of items in the room
     * @param currentRooms The Room Object representing the current Room the user is in
     * @param userInput The String with the input from the user
     * @param itemList The ArrayList that keeps track of the items picked up by the user
     */
    public String takeItemFromRoom(Room currentRooms, String userInput, ArrayList<String> itemList) {
        String toReturn;
        String input = userInput;
        String[] availableItemsInRoom = currentRooms.getItems();
        for (int index = 0; index < availableItemsInRoom.length; index++) {
            if (input.equalsIgnoreCase(availableItemsInRoom[index])) {
                itemList.add(availableItemsInRoom[index]);
                List<String> list = new ArrayList<String>(Arrays.asList(availableItemsInRoom));
                list.remove(input);
                availableItemsInRoom = list.toArray(new String[0]);
                currentRooms.setItems(availableItemsInRoom);
                return "executed";
            }
        }
        toReturn = "There is no item " + input + " in the room";
        return toReturn;
    }

    /**
     * The function enables the user to drop an item from his list into the room
     * @param currentRooms The Room Object representing the current Room the user is in
     * @param userInput The String with the input from the user
     * @param itemList The ArrayList that keeps track of the items picked up by the user
     */
    public String dropItemInRoom(Room currentRooms, String userInput, ArrayList<String> itemList) {
        String input = userInput;
        String[] currItemList = currentRooms.getItems();
        String[] newItemRoomList = new String[currItemList.length + 1];
        System.arraycopy(currItemList,0, newItemRoomList,0, currItemList.length);
        if (itemList.indexOf(input) != -1) {
            int index = itemList.indexOf(input);
            newItemRoomList[currItemList.length] = itemList.get(index);
            itemList.remove(input);
        } else {
            return ("You don't have " + input + "!");
        }
        currentRooms.setItems(newItemRoomList);
        return "executed";
    }

    /**
     * The function prints out information like Room name, description, list of available items and available directions from the room.
     * @param currentRooms The room object representing the current room the user is in.
     */
    public String displayRoomInformation(Room currentRooms) {
        Room currentRoomObj = currentRooms;
        String displayMessage = "";
        if (currentRoomObj.getName().equals(gameLayout.getEndingRoom())) {
            displayMessage = "YOU WIN SUCKER";
            return displayMessage;
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
        displayMessage += "\n"+ currentRoomObj.getDescription();
        displayMessage += "\n" + "From here, you can go: "+ availableDirectionNames;
        String itemsInRoom = Arrays.toString(itemsList);
        itemsInRoom = itemsInRoom.substring(1, itemsInRoom.length() -1);
        displayMessage += "\n" + "Items Visible: " + itemsInRoom;
        return displayMessage;
    }

    /**
     * The function changes the current room of the user to a new room based on the user input.
     * @param currentRooms The room object representing the current room the user is in.
     * @param userInput The String array represents the input from the user
     */
    public String changeRoom(Room currentRooms, String userInput) {
        String toReturn;
        Direction[] canGoDirections = currentRooms.getDirections();
        int counter = canGoDirections.length;
        Room[] rooms = gameLayout.getRooms();
        while (counter != 0) {
            if (canGoDirections[counter - 1].getDirectionName().equalsIgnoreCase(userInput)) {
                Direction newDirection = canGoDirections[counter - 1];
                String nextRoomName = newDirection.getNextRoomName();
                System.out.println(nextRoomName);
                for (Room nextRoom : rooms) {
                    if (nextRoom.getName().equalsIgnoreCase(nextRoomName)) {
                        currentRoom = nextRoom;
                    }
                }
                return "executed";
            }
            counter--;
        }
        return ("I can't go " + userInput);
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

    /**
     * The function prints the Rooms visited
     * @param items List of rooms visited
     */
    public static void printRoomsVisited(ArrayList<Room> items) {
        for (int i = 0 ; i < items.size(); i++) {
            System.out.println(items.get(i).getName());
        }
    }


}
