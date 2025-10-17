package Model;

public class Transaction {
    private String accountNumber;
    private String type;
    private double amount;
    private String timestamp;
    private String relatedAccount;

    public Transaction(String accountNumber, String type, double amount, String timestamp, String relatedAccount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.relatedAccount = relatedAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }
}


