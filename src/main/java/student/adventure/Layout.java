package student.adventure;

public class Layout {
    /** The Room at which the user starts the game */
    private String startingRoom;
    /** The Room at which the user ends the game */
    private String endingRoom;
    /** An array of Rooms representing all the rooms in the game */
    private Room[] rooms;

    public String getEndingRoom() {
        return endingRoom;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setEndingRoom(String endingRoom) {
        this.endingRoom = endingRoom;
    }

    public void setStartingRoom(String startingRoom) {
        this.startingRoom = startingRoom;
    }

    /**
     * function that gives the Room object of the Room wherein the user starts the game
     * @return Room object of the Room wherein the user starts the game
     */
    public Room getStaringRoomObj() {
        for (Room currentRoom : rooms) {
            if (currentRoom.getName().equals(startingRoom)) {
                return currentRoom;
            }
        }
        return rooms[0];
    }
}
