package Model;

import java.time.LocalDate;

public class Loan {
    private int loanId;
    private String accountNumber;
    private String loanType;
    private double amount;
    private int durationYears;
    private double interestRate;
    private double emi;
    private LocalDate startDate;
    private String status;
    private LocalDate lastPaymentDate;
    private double totalPaid;
    private double fine;
    private int missedMonths;

    // Constructor for new loan application
    public Loan(String accountNumber, String loanType, double amount, int durationYears, double emi) {
        this.accountNumber = accountNumber;
        this.loanType = loanType;
        this.amount = amount;
        this.durationYears = durationYears;
        this.interestRate = 10.0; // fixed as per requirement
        this.emi = emi;
        this.startDate = LocalDate.now();
        this.status = "Active";
        this.lastPaymentDate = LocalDate.now();
        this.totalPaid = 0.0;
        this.fine = 0.0;
    }

    public Loan(int loanId, String accountNumber, String loanType, double amount, int durationYears,
                double interestRate, double emi, LocalDate startDate, String status, LocalDate lastPaymentDate,
                double totalPaid, double fine, int missedMonths) {  // Add missedMonths
        this.loanId = loanId;
        this.accountNumber = accountNumber;
        this.loanType = loanType;
        this.amount = amount;
        this.durationYears = durationYears;
        this.interestRate = interestRate;
        this.emi = emi;
        this.startDate = startDate;
        this.status = status;
        this.lastPaymentDate = lastPaymentDate;
        this.totalPaid = totalPaid;
        this.fine = fine;
        this.missedMonths = missedMonths;  // Assign it
    }


    // Constructor for new loan creation
    public Loan(String accountNumber, String loanType, double loanAmount, int durationYears,
                double interestRate, double emiPerMonth, LocalDate loanStartDate, String status) {
        this.accountNumber = accountNumber;
        this.loanType = loanType;
        this.amount = amount;
        this.durationYears = durationYears;
        this.interestRate = interestRate;
        this.emi = emi;
        this.startDate = LocalDate.now();
        this.status = status;
        this.lastPaymentDate = LocalDate.now();
        this.totalPaid = 0.0;
        this.fine = 0.0;
    }

    // Constructor for fetching existing loan from DB
    public Loan(int loanId, String accountNumber, String loanType, double amount, int durationYears,
                double interestRate, double emi, LocalDate startDate, String status, LocalDate lastPaymentDate,
                double totalPaid, double fine) {
        this.loanId = loanId;
        this.accountNumber = accountNumber;
        this.loanType = loanType;
        this.amount = amount;
        this.durationYears = durationYears;
        this.interestRate = interestRate;
        this.emi = emi;
        this.startDate = startDate;
        this.status = status;
        this.lastPaymentDate = lastPaymentDate;
        this.totalPaid = totalPaid;
        this.fine = fine;
    }



    // Getters and Setters
    public int getLoanId() { return loanId; }
    public String getAccountNumber() { return accountNumber; }
    public String getLoanType() { return loanType; }
    public double getAmount() { return amount; }
    public int getDurationYears() { return durationYears; }
    public double getInterestRate() { return interestRate; }
    public double getEmi() { return emi; }
    public LocalDate getStartDate() { return startDate; }
    public String getStatus() { return status; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public double getTotalPaid() { return totalPaid; }
    public double getFine() { return fine; }

    public void setStatus(String status) { this.status = status; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }
    public void setFine(double fine) { this.fine = fine; }

    public int getMissedMonths() { return missedMonths; }
    public void setMissedMonths(int missedMonths) { this.missedMonths = missedMonths; }
}
