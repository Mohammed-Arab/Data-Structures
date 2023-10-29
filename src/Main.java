import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

/***
 * main class which calls the three databases classes.
 *
 * @author Mohammed Arab
 *
 */
public class Main {
    /***
     * main function which should run the test's.
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CCDatabase array = new SortedArrayCCDatabase();
        CCDatabase hashTable = new HashTable();
        CCDatabase robinHashTable = new RobinHoodHashing();
        Duration arrays = runTests(array, "src/ops-20.txt");
        Duration hashTables = runTests(hashTable, "src/ops-20.txt");
        Duration robinHashTables = runTests(robinHashTable, "src/ops-20.txt");
        System.out.println(arrays.getNano() + " NS");
        System.out.println(hashTables.getNano() + " NS");
        System.out.println(robinHashTables.getNano() + " NS");

    }

    private static Duration runTests(CCDatabase database, String filename) {
        try (Scanner opsFile = new Scanner(new File(filename))) {
            Instant start = null;
            Instant end = null;

            while (opsFile.hasNextLine()) {
                String operation = opsFile.nextLine();
                // opsFile.next();
                if (operation.equals("start")) {
                    start = Instant.now();
                } else if (operation.equals("stop")) {
                    end = Instant.now();
                } else if (operation.equals("cre")) {
                    long acc = opsFile.nextLong();
                    opsFile.nextLine();
                    String name = opsFile.nextLine();
                    String adrs = opsFile.nextLine();
                    double lmt = opsFile.nextDouble();
                    double balance = opsFile.nextDouble();
                    database.createAccount(acc, name, adrs, lmt, balance);

                } else if (operation.equals("del")) {
                    long acc = opsFile.nextLong();
                    opsFile.nextLine();
                    database.deleteAccount(acc);

                } else if (operation.equals("lim")) {
                    long acc = opsFile.nextLong();
                    opsFile.nextLine();
                    double lmt = opsFile.nextDouble();
                    database.adjustCreditLimit(acc, lmt);
                } else if (operation.equals("pur")) {
                    long acc = opsFile.nextLong();
                    opsFile.nextLine();
                    double price = opsFile.nextDouble();
                    try {
                        database.makePurchase(acc, price);
                    } catch (Exception ex) {
                        // to see the error's we get for the make purchase undo the comment below
                        // ex.printStackTrace();

                    }
                }

            }
            return Duration.between(start, end);

        } catch (IOException ex) {
            // ...handle not opening FIle error
            System.out.println("I am ERROR and I didn't open the file");
        }
        return null;
    }

}