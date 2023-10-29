/***
 * makes an object of these fileds.
 *
 * @author ma7am
 *
 */
public class CreateAccount {
    /***
     * main instructor.
     */
    public CreateAccount(long accountNumber, String name, String address, double creditLimit, double balance) {
        //
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.creditLimit = creditLimit;
        this.balance = balance;
        this.isDeleted = false;
        this.setPSL(0);

    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getString() {
        return accountNumber + "\n" + name + "\n" + address + "\n" + creditLimit + "\n" + balance;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getPSL() {
        return PSL;
    }

    public void setPSL(int pSL) {
        PSL = pSL;
    }

    private final long accountNumber;
    private final String name;
    private final String address;
    private double creditLimit;
    private double balance;
    private boolean isDeleted;
    private int PSL;

}
