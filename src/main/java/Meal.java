public class Meal {
    private int meal_id;
    private String name;
    private String description;
    private double price;

    // No-argument constructor
    public Meal() {}

    // Parameterized constructor
    public Meal(int meal_id, String name, String description, double price) {
        this.meal_id = meal_id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return meal_id;
    }
    public void setId(int meal_id) {
        this.meal_id = meal_id;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    // toString method for debugging and logging purposes
    @Override
    public String toString() {
        return "Meal{" +
                "meal_id=" + meal_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
