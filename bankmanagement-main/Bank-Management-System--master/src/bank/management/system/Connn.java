
package bank.management.system;

import java.sql.*;

public class Connn {

    Connection connection;
    Statement statement;

    public Connn() {
        try {
            String url = "jdbc:mysql://localhost:3306/bankSystem";
            String username = "root";
            String password = System.getenv("BANK_DB_PASSWORD");

            if (password == null || password.isEmpty()) {
                throw new IllegalStateException(
                        "BANK_DB_PASSWORD environment variable is not set"
                );
            }

            connection = DriverManager.getConnection(
                    url, username, password
            );

            statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
