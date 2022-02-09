package student.adventure;

public class Room {
    /** The name of the Room */
    private String name;
    /** The description of the Room */
    private String description;
    /** Direction array representing all the directions the user can travel to from his current room */
    private Direction[] directions;
    /** All the items present in the room */
    private String[] items;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Direction[] getDirections() {
        return this.directions;
    }

    public String[] getItems() {
        return this.items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}


