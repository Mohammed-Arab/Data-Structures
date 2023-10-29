/***
 * this is the sorted array class.
 *
 * @author mohammed arab
 *
 */
public class SortedArrayCCDatabase implements CCDatabase {

    private CreateAccount account[] = new CreateAccount[999999];
    // keep tracks of the array fillLevel starting from 0
    private int fillLevel = 0;

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {

        if (account[0] == null) {
            account[0] = new CreateAccount(accountNumber, name, address, creditLimit, balance);
            fillLevel += 1;
            return true;
        } else {
            for (int x = 0; x < 999999; x++) {
                if (account[x] == null) {
                    int index = binarySearch(accountNumber, x);
                    insert(index, new CreateAccount(accountNumber, name, address, creditLimit, balance), x);
                    fillLevel += 1;
                    return true;
                } else if (account[x] != null && account[x].getAccountNumber() == accountNumber) {
                    return false;
                }
            }
        }
        return false;

    }

    @Override
    public boolean deleteAccount(long accountNumber) {
        for (int i = 0; i < 999999; i++) {
            if (account[i] != null && account[i].getAccountNumber() == accountNumber) {
                delete(i, fillLevel);
                fillLevel -= 1;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {

        for (int i = 0; i < 999999; i++) {
            if (account[i] != null && account[i].getAccountNumber() == accountNumber) {
                account[i].setCreditLimit(newLimit);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getAccount(long accountNumber) {

        for (int i = 0; i < 999999; i++) {
            if (account[i] != null && account[i].getAccountNumber() == accountNumber) {
                return account[i].getString();
            }
        }
        return null;
    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {
        double newBalance = 0.0;

        for (int i = 0; i < 999999; i++) {
            if (account[i] != null && account[i].getAccountNumber() == accountNumber) {
                newBalance = account[i].getBalance() + price;
                if (account[i].getCreditLimit() < price) {
                    throw new Exception("price is over limit");
                } else if (newBalance <= account[i].getCreditLimit()) {
                    account[i].setBalance(newBalance);
                    return true;
                } else {
                    throw new Exception("balance is over limit");
                }
            }
        }

        return false;
    }

    // this function should return the index of where the account should be inserted
    private int binarySearch(long accountNumber, int fillLevel) {
        int start = 0;
        int end = fillLevel;

        while (start < end) {
            int mid = (start + end) / 2;
            if (account[mid].getAccountNumber() == accountNumber) {
                start = mid;
                end = mid;
            } else if (account[mid].getAccountNumber() < accountNumber) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return end;
    }

    // inserts the account in the right index while maintaining the array sorted
    private void insert(int index, CreateAccount account, int fillLevel) {
        for (int i = fillLevel; i > index; i--) {
            this.account[i] = this.account[i - 1];
        }
        this.account[index] = account;
    }

    private void delete(int index, int fillLevel) {
        for (int i = index; i < fillLevel - 1; i++) {
            account[i] = account[i + 1];
        }
        account[fillLevel - 1] = null;
    }

}
