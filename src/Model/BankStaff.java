package Model;

public class BankStaff {
    private String bankUserNumber;
    private String name;
    private String email;
    private String password;
    private String branch;

    public BankStaff(String bankUserNumber, String name, String email, String password, String branch) {
        this.bankUserNumber = bankUserNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.branch = branch;
    }

    public String getBankUserNumber() {
        return bankUserNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBranch() {
        return branch;
    }

    public void setBankUserNumber(String bankUserNumber) {
        this.bankUserNumber = bankUserNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
