public class Item {
    private String name;

    public Item(String name) {
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}