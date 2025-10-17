package Model;

public class Customer {
    private String name;
    private String email;
    private String accountNumber;
    private String cardNumber;
    private String pin;
    private String branch;
    private double balance;
    private String employmentStatus;
    private String accountType;
    private double monthlySalary;

    // Constructor used during signup
    public Customer(String name, String email, String accountNumber, String cardNumber, String pin,
                    String branch, String employmentStatus, String accountType, double monthlySalary) {
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.branch = branch;
        this.balance = 500.0; // Initial balance
        this.employmentStatus = employmentStatus;
        this.accountType = accountType;
        this.monthlySalary = monthlySalary;
    }

    // Constructor used when retrieving from DB
    public Customer(String accountNumber, String cardNumber, String pin, String branch,
                    String name, String email, double balance,
                    String employmentStatus, String accountType, double monthlySalary) {
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.branch = branch;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.employmentStatus = employmentStatus;
        this.accountType = accountType;
        this.monthlySalary = monthlySalary;
    }

    // Constructor used during signup (no balance yet)
    public Customer(String name, String email, String accountNumber, String cardNumber, String pin, String branch) {
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.branch = branch;
        this.balance = 500.0; // Default initial balance
    }

    // Constructor used when retrieving info from DB
    public Customer(String accountNumber, String cardNumber, String pin, String branch,
                    String name, String email, double balance) {
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.branch = branch;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public Customer(String accountNumber, String name, String email, String branch, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.balance = balance;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAccountNumber() { return accountNumber; }
    public String getCardNumber() { return cardNumber; }
    public String getPin() { return pin; }
    public String getBranch() { return branch; }
    public double getBalance() { return balance; }
    public String getEmploymentStatus() { return employmentStatus; }
    public String getAccountType() { return accountType; }
    public double getMonthlySalary() { return monthlySalary; }

    // Optional Setters (if needed later)
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
