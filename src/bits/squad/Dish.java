package bits.squad;

public class Dish {
    private boolean isAvailable;
    private String name;
    private String description;
    private int price;

    public Dish(String name, String description, boolean isAvailable, int price) {
        this.isAvailable = isAvailable;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ":\n" + description + "\n"
                + (isAvailable?"In Stock":"Out of stock") + "\t\t\t" + price + "zl";
    }
}
