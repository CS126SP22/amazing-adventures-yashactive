package student.adventure;

public class Room {
    private String name;
    private String description;
    private Direction[] directions;
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


