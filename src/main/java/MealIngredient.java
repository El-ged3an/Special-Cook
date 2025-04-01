public class MealIngredient {
    
    private int mealIngredientId;
    private int mealId;
    private int ingredientId;
    private double quantity; // You can change to int if quantity is always a whole number

    // No-argument constructor
    public MealIngredient() {
    }

    // Parameterized constructor
    public MealIngredient(int mealIngredientId, int mealId, int ingredientId, double quantity) {
        this.mealIngredientId = mealIngredientId;
        this.mealId = mealId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getMealIngredientId() {
        return mealIngredientId;
    }

    public void setMealIngredientId(int mealIngredientId) {
        this.mealIngredientId = mealIngredientId;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MealIngredient{" +
                "mealIngredientId=" + mealIngredientId +
                ", mealId=" + mealId +
                ", ingredientId=" + ingredientId +
                ", quantity=" + quantity +
                '}';
    }
}
