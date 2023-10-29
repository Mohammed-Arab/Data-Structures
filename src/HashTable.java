/***
 * creates simple hash table.
 *
 * @author ma7am
 *
 */
public class HashTable implements CCDatabase {
    private int size = 101;
    private CreateAccount hashTable[] = new CreateAccount[size];
    private int fillLevel = 0;

    @Override
    public boolean createAccount(long accountNumber, String name, String address, double creditLimit, double balance) {
        int index = hashFunction(accountNumber);
        if (fillLevel > (0.6 * size)) {
            moveOldArray();
            index = hashFunction(accountNumber);
        }
        if (hashTable[index] == null) {
            hashTable[index] = new CreateAccount(accountNumber, name, address, creditLimit, balance);
            fillLevel += 1;
            return true;
        } else {
            for (int i = 0; i < size; i++) {
                if (hashTable[index] != null && hashTable[index].isDeleted() == false
                        && hashTable[index].getAccountNumber() != accountNumber) {

                    index += 1;
                    if (index == size) {
                        index = 0;
                    }
                    if (hashTable[index] == null) {
                        hashTable[index] = new CreateAccount(accountNumber, name, address, creditLimit, balance);
                        fillLevel += 1;
                        return true;
                    }
                } else if (hashTable[index] != null && hashTable[index].getAccountNumber() == accountNumber) {
                    if (hashTable[index].isDeleted() == false) {
                        return false;
                    } else {
                        hashTable[index] = new CreateAccount(accountNumber, name, address, creditLimit, balance);
                        fillLevel += 1;
                        return true;
                    }
                } else {
                    hashTable[index] = new CreateAccount(accountNumber, name, address, creditLimit, balance);
                    fillLevel += 1;
                    return true;
                }

            }
        }
        return false;

    }

    @Override
    public boolean deleteAccount(long accountNumber) {

        int index = index(accountNumber);

        if (index == -1) {
            return false;
        } else {
            hashTable[index].setDeleted(true);
            return true;
        }
    }

    @Override
    public boolean adjustCreditLimit(long accountNumber, double newLimit) {
        int index = index(accountNumber);
        if (index != -1) {
            if (hashTable[index] != null && hashTable[index].getAccountNumber() == accountNumber) {
                hashTable[index].setCreditLimit(newLimit);
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
            return hashTable[index].getString();
        }

    }

    @Override
    public boolean makePurchase(long accountNumber, double price) throws Exception {
        double newBalance = 0.0;
        int index = index(accountNumber);

        if (index == -1) {
            return false;
        } else {
            newBalance = hashTable[index].getBalance() + price;
            if (hashTable[index].getCreditLimit() < newBalance) {
                throw new Exception("price is over limit");
            } else {
                hashTable[index].setBalance(newBalance);
                return true;
            }
        }
    }

    /***
     * returns actual hashValue b4 probing.
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
     * this function should return the index of the account if it exists and
     * !deleted else if it returns -1 then the account doesn't exist or has been
     * deleted.
     *
     */

    public int index(long accountNumber) {

        int index = hashFunction(accountNumber);

        while (hashTable[index] != null) {
            if (hashTable[index].getAccountNumber() == accountNumber && !hashTable[index].isDeleted()) {
                return index;
            }
            index = (index + 1) % size;
        }

        return -1;
    }

    /***
     * moves old table to a new table if the size of the old one get's 60%.
     */
    public void moveOldArray() {
        int oldSize = size;
        int index;
        size = getNextPrime(oldSize);
        CreateAccount[] newHashTable = new CreateAccount[size];

        for (int i = 0; i < oldSize; i++) {
            if (hashTable[i] != null && hashTable[i].isDeleted() == false) {
                index = hashFunction(hashTable[i].getAccountNumber());
                while (newHashTable[index] != null) {
                    index += 1;
                    if (index == size) {
                        index = 0;
                    }
                }
                newHashTable[index] = hashTable[i];
            }
        }
        hashTable = newHashTable;

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
