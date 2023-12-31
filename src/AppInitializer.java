import java.math.BigDecimal;
import java.sql.*;

public class AppInitializer {
    public static void main(String[] args) {
        try {
            Customer customer = new Customer(1002, "Maleesha Silva", "Panadura", 2500.00, "2000/08/28");
            if (saveCustomer(customer)) {
                System.out.println("Success!");
            } else {
                System.out.println("Try Again!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean saveCustomer(Customer c) throws ClassNotFoundException, SQLException {
        // create a query
        String sql = "INSERT INTO customer (id, name, address, salary, dob) VALUES (?, ?, ?, ?, ?)";


        // statement
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {

            preparedStatement.setLong(1, c.getId());
            preparedStatement.setString(2, c.getName());
            preparedStatement.setString(3, c.getAddress());
            preparedStatement.setBigDecimal(4, BigDecimal.valueOf(c.getSalary()));

            // preparedStatement.setDouble(4, c.getSalary());
            preparedStatement.setString(5, c.getDob());

            System.out.println("ID: " + c.getId());
            System.out.println("Name: " + c.getName());
            System.out.println("Address: " + c.getAddress());
            System.out.println("Salary: " + BigDecimal.valueOf(c.getSalary()));
            System.out.println("DOB: " + c.getDob());
            System.out.println("SQL Query: " + preparedStatement.toString());

            // execute CRUD
            return preparedStatement.executeUpdate() > 0;
        }
    }

    private static Customer findById(long id) throws ClassNotFoundException, SQLException {
        // create a query
        String sql = "SELECT * FROM customer WHERE id = ?";

        // statement
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet set = preparedStatement.executeQuery()) {
                if (set.next()) {
                    return new Customer(
                            set.getLong(1),
                            set.getString(2),
                            set.getString(3),
                            set.getDouble(4),
                            set.getString(5)
                    );
                }
            }
        }
        return null;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        // driver load
        Class.forName("com.mysql.cj.jdbc.Driver");

        // create a connection
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hibernate_db", "root", "Ma@77331");
    }
}
