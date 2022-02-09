package student.adventure;

public class Direction {
    /** the name of the direction */
    private String directionName;
    /** the name of room which will be obtained by taking that direction */
    private String nextRoomName;

    public String getDirectionName() {
        return directionName;
    }
    public String getNextRoomName() {
        return nextRoomName;
    }
    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }
    public void setNextRoomName(String nextRoomName) {
        this.nextRoomName = nextRoomName;
    }
}
