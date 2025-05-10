import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private final List<Expense> expenses;
    private final String FILE_NAME = "expenses.dat";
    private int nextId = 1;

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpensesFromFile();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(String category, double amount, String description) {
        Expense expense = new Expense(nextId++, category, amount, description, LocalDate.now());
        expenses.add(expense);
        saveExpensesToFile();
    }

    public void updateExpense(int id, String category, double amount, String description) {
        for (Expense e : expenses) {
            if (e.getId() == id) {
                e.setCategory(category);
                e.setAmount(amount);
                e.setDescription(description);
                saveExpensesToFile();
                return;
            }
        }
    }

    public void deleteExpense(int id) {
        expenses.removeIf(e -> e.getId() == id);
        saveExpensesToFile();
    }

    public void clearAll() {
        expenses.clear();
        saveExpensesToFile();
        nextId = 1;
    }

    private void saveExpensesToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(expenses);
            out.writeInt(nextId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExpensesFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<?> loaded = (List<?>) in.readObject();
            for (Object obj : loaded) {
                if (obj instanceof Expense) {
                    expenses.add((Expense) obj);
                }
            }
            nextId = in.readInt(); // Restore nextId
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("\n--- All Expenses ---");
        for (Expense e : expenses) {
            System.out.println("ID: " + e.getId());
            System.out.println("Category: " + e.getCategory());
            System.out.println("Amount: ₱" + String.format("%.2f", e.getAmount()));
            System.out.println("Description: " + e.getDescription());
            System.out.println("Date: " + e.getDate());
            System.out.println("-----------------------");
        }
    }

}
