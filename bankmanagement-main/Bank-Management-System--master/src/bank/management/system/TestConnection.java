
package bank.management.system;

public class TestConnection {
    public static void main(String[] args) {

        Connn c = new Connn();

        if (c.connection != null) {
            System.out.println("MySQL Connected Successfully!");
        } else {
            System.out.println("MySQL Connection Failed!");
        }
    }
}
