public class Item {
    private String name;
    private double rating;

    public Item(String name, double rating) {
        this.name = name.trim();
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}