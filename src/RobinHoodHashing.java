/***
 * this is the Robin hash array.
 *
 * @author Mohammed Arab
 *
 */
public class RobinHoodHashing implements CCDatabase {
    private int size = 101;
    private CreateAccount robinHashTable[] = new CreateAccount[size];
    private int fillLevel = 0;

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {
        // TODO Auto-generated method stub

        boolean created = false;
        if (fillLevel > (0.6 * size)) {
            moveOldArray();
        }
        created = pslIndex(new CreateAccount(accountNumber, name, address, creditLimit, balance), robinHashTable);
        if (created) {
            fillLevel += 1;
            return true;
        }
        return false;

    }

    @Override
    public boolean deleteAccount(long accountNumber) {
        return isDeleted(accountNumber);
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {
        int index = index(accountNumber);
        if (index != -1) {
            if (robinHashTable[index] != null && robinHashTable[index].getAccountNumber() == accountNumber) {
                robinHashTable[index].setCreditLimit(newLimit);
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    @Override
    public String getAccount(long accountNumber) {
        int index = index(accountNumber);
        if (index == -1) {
            return null;
        } else {
            return robinHashTable[index].getString();
        }

    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {
        double newBalance = 0.0;
        int index = index(accountNumber);

        if (index == -1) {
            return false;
        } else {
            newBalance = robinHashTable[index].getBalance() + price;
            if (robinHashTable[index].getCreditLimit() < newBalance) {
                throw new Exception("price is over limit");
            } else {
                robinHashTable[index].setBalance(newBalance);
                return true;
            }
        }
    }

    /***
     * //returns index of existing account.
     */
    public int index(long accountNumber) {

        int index = hashFunction(accountNumber);

        while (robinHashTable[index] != null) {
            if (robinHashTable[index].getAccountNumber() == accountNumber) {
                return index;
            }
            index = (index + 1) % size;
        }

        return -1;
    }

    /***
     * inserts to the right spot using psl probing.
     */
    public boolean pslIndex(CreateAccount account, CreateAccount robinHashTable[]) {

        int index = hashFunction(account.getAccountNumber());
        int psl = 0;
        CreateAccount temp[] = new CreateAccount[1];
        if (robinHashTable[index] == null) {
            robinHashTable[index] = account;
            return true;
        }
        while (robinHashTable[index] != null
                && robinHashTable[index].getAccountNumber() != account.getAccountNumber()) {

            if (robinHashTable[index].getPSL() > account.getPSL()) {
                index = (index + 1) % size;
                account.setPSL(psl += 1);
            } else if (robinHashTable[index].getPSL() == account.getPSL()) {
                index = (index + 1) % size;
                account.setPSL(psl += 1);
            } else {

                temp[0] = robinHashTable[index];
                robinHashTable[index] = null;
                robinHashTable[index] = account;
                index = (index + 1) % size;
                account = temp[0];
                account.setPSL(account.getPSL() + 1);

                temp[0] = null;
            }

        }
        if (robinHashTable[index] == null) {
            robinHashTable[index] = account;
            return true;
        } else {
            return false;
        }

    }

    /***
     * deletes the given account and shifting accounts to lower psl.
     */
    public boolean isDeleted(long accountNumber) {
        int index = index(accountNumber);
        if (index == -1) {
            return false;
        } else {
            robinHashTable[index].setDeleted(true);
            robinHashTable[index] = null;
            index = (index + 1) % size;
            while (robinHashTable[index] != null && robinHashTable[index].getPSL() > 0) {
                if (index == 0) {
                    robinHashTable[size - 1] = robinHashTable[index];
                    robinHashTable[index] = null;
                    robinHashTable[size - 1].setPSL(robinHashTable[size - 1].getPSL() - 1);

                } else {
                    robinHashTable[index - 1] = robinHashTable[index];
                    robinHashTable[index] = null;
                    robinHashTable[index - 1].setPSL(robinHashTable[index - 1].getPSL() - 1);
                }
                index = (index + 1) % size;
            }
            fillLevel -= 1;
            return true;
        }
    }

    /***
     * returns the actual hash value.
     */
    public int hashFunction(long accountNumber) {
        int hashValue;
        int index;
        short c4 = (short) (accountNumber % 10000);// first 4 digits from the right
        short c3 = (short) ((accountNumber / 10000) % 10000);// second 4 digits from the right
        short c2 = (short) ((accountNumber / 100000000) % 10000); // 3rd 4 digits from the right
        short c1 = (short) ((accountNumber / 1000000000000L) % 10000);// 4th 4 digits from the right first 4 digits from

        hashValue = (17 * c1) + (17 ^ 2) * c2 + (17 ^ 3) * c3 + (17 ^ 4) * c4;
        index = hashValue % size;

        return index;
    }

    /***
     * moves old table to a new table if the size of the old one get's 60%.
     */
    public void moveOldArray() {
        int oldSize = size;
        size = getNextPrime(oldSize);
        CreateAccount[] newHashTable = new CreateAccount[size];

        for (int i = 0; i < oldSize; i++) {
            if (robinHashTable[i] != null) {
                robinHashTable[i].setPSL(0);
                pslIndex(robinHashTable[i], newHashTable);
            }

        }
        robinHashTable = newHashTable;
        newHashTable = null;

    }

    /***
     * returns if the next double size is boolean or not.
     */
    public boolean isPrime(int number) {

        // Eliminate the need to check versus even numbers

        if (number % 2 == 0) {
            return false;
        }
        // Check against all odd numbers

        for (int i = 3; i * i <= number; i += 2) {

            if (number % i == 0) {
                return false;
            }

        }

        // If we make it here we know it is odd

        return true;

    }

    /***
     * Receives a number and returns the next prime number that follows.
     */

    public int getNextPrime(int minNumberToCheck) {
        for (int i = minNumberToCheck * 2; true; i++) {

            if (isPrime(i)) {
                return i;
            }

        }

    }
}
