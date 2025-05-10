import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Personal Expense Tracker ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // clear buffer
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    manager.addExpense(category, amount, description);
                    break;

                case 2:
                    manager.viewExpenses();
                    break;

                case 3:
                    System.out.print("Enter expense ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // clear buffer
                    System.out.print("Enter new category: ");
                    String newCategory = scanner.nextLine();
                    System.out.print("Enter new amount: ");
                    double newAmount = scanner.nextDouble();
                    scanner.nextLine(); // clear buffer
                    System.out.print("Enter new description: ");
                    String newDescription = scanner.nextLine();
                    manager.updateExpense(updateId, newCategory, newAmount, newDescription);
                    break;

                case 4:
                    System.out.print("Enter expense ID to delete: ");
                    int deleteId = scanner.nextInt();
                    manager.deleteExpense(deleteId);
                    break;

                case 0:
                    System.out.println("Exiting Expense Tracker.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
