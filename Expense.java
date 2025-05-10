import java.io.Serializable;
import java.time.LocalDate;

public class Expense implements Serializable {
    private int id;
    private String category;
    private double amount;
    private String description;
    private LocalDate date;

    public Expense(int id, String category, double amount, String description, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
