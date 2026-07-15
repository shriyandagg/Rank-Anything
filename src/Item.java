public class Item {
    private String name;
    private double manualRating;

    public Item(String name, double manualRating) {
        this.name = name.trim();
        this.manualRating = manualRating;
    }

    public String getName() {
        return name;
    }

    public double getManualRating() {
        return manualRating;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setManualRating(double manualRating) {
        this.manualRating = manualRating;
    }
}