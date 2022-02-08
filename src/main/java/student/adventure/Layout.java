package student.adventure;

public class Layout {
    private String startingRoom;
    private String endingRoom;
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

    public Room getStaringRoomObj() {
        for (Room currentRoom : rooms) {
            if (currentRoom.getName().equals(startingRoom)) {
                return currentRoom;
            }
        }
        return rooms[0];
    }
}
