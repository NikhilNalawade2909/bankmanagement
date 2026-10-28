# Bank Management System

A desktop-based Bank Management System developed using **Java Swing, JDBC, and MySQL**. The application provides an ATM-style graphical interface for account registration, authentication, deposits, withdrawals, balance enquiry, and transaction history.

## Features

- User registration and account creation
- Login using card number and PIN
- Cash deposits and withdrawals
- Fast Cash with predefined withdrawal amounts
- Balance Enquiry
- Mini Statement displaying transaction history
- PIN Change
- Input validation and insufficient-balance checks
- MySQL database integration using JDBC

## Technologies Used

- **Programming Language:** Java
- **GUI:** Java Swing and AWT
- **Database:** MySQL
- **Database Connectivity:** JDBC with MySQL Connector/J
- **IDE:** IntelliJ IDEA
- **Additional Library:** JCalendar

## Database

The application uses the `bankSystem` database with the following tables:

| Table | Description |
|---|---|
| signup | Personal registration details |
| Signuptwo | Additional registration details |
| signupthree | Account and card information |
| login | Account authentication |
| bank | Deposit and withdrawal transactions |

## How to Run

1. Install Java JDK 17 or a compatible version.
2. Install MySQL Server and create the `bankSystem` database.
3. Create the required database tables.
4. Open the project in IntelliJ IDEA.
5. Configure MySQL Connector/J and JCalendar.
6. Set the `BANK_DB_PASSWORD` environment variable locally.
7. Run `Login.java`.

**Note:** The database schema must be configured before running the application.

## Project Highlights

- Built an interactive ATM-style interface using Java Swing.
- Integrated Java with MySQL using JDBC.
- Implemented deposit and withdrawal transactions.
- Added balance validation and transaction history.
- Used parameterized SQL queries in updated banking operations.

## Future Improvements

- Replace PIN-based transaction identification with unique account IDs.
- Securely hash authentication credentials.
- Improve transaction handling and database constraints.
- Add automated testing and database migration scripts.

## Disclaimer

This is an educational project intended for learning and demonstration. It is not designed for real banking operations.

## Developer

**Nikhil Nalawade**

GitHub: https://github.com/NikhilNalawade2909

Project Repository: https://github.com/NikhilNalawade2909/bankmanagement
