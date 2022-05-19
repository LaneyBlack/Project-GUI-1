package bits.squad;

public class Item<T extends Number> {
    private boolean isAvailable;
    private String name;
    private String description;
    private T price;

    public Item(String name, String description, boolean isAvailable, T price) {
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

    public T getPrice() {
        return price;
    }

    public int getIntPrice(){
        return price.intValue();
    }

    public void setPrice(T price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ":\n" + description + "\n"
                + (isAvailable?"In Stock":"Out of stock") + "\t\t\t" + price + "zl";
    }
}
