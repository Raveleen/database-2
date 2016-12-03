import java.sql.*;
import java.util.ArrayList;

public class DataBaseControl {
    private static DataBaseControl dataBaseControl;
    private String DB_CONNECTION;
    private String DB_USER;
    private String DB_PASSWORD;
    private Connection conn;

    static {
        dataBaseControl = new DataBaseControl();
    }

    private DataBaseControl() {
        DB_CONNECTION = "jdbc:mysql://localhost:3306/database2";
        DB_USER = "root";
        DB_PASSWORD = "1604";
        dbInit();
    }

    public static DataBaseControl getInstance() {
        return dataBaseControl;
    }

    private void dbInit() {
        try{
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Customers (customer_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(20) NOT NULL, phone VARCHAR(16) NOT NULL , " +
                    "address VARCHAR(16) NOT NULL)");
            statement.execute("CREATE TABLE IF NOT EXISTS Products (product_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(16) NOT NULL, weight INT, price INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS Orders (order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "customer_id INT, date DATETIME NOT NULL , total_weight INT, total_price INT, " +
                    "FOREIGN KEY (customer_id) REFERENCES Customers (customer_id))");
            statement.execute("CREATE TABLE IF NOT EXISTS Items_Orders ( order_id INT, product_id INT, " +
                    "FOREIGN KEY (order_id) REFERENCES Orders (order_id)," +
                    "FOREIGN KEY (product_id) REFERENCES Products (product_id))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DATABASE IS WORKING WRONG.");
        }
    }

    public void addProduct(String title, int weight, int price) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Products " +
                    "(title, weight, price) VALUES (?, ?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, weight);
            preparedStatement.setInt(3, price);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printProducts() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT *FROM Products");
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String[] temp = new String[] {
                    resultSetMetaData.getColumnName(1), resultSetMetaData.getColumnName(2),
                    resultSetMetaData.getColumnName(3), resultSetMetaData.getColumnName(4),
            };
            System.out.println("+------------+----------------+----------+----------+");
            System.out.printf("|%12s|%16s|%10s|%10s|\n", temp[0], temp[1], temp[2], temp[3]);
            System.out.println("+------------+----------------+----------+----------+");

            while (resultSet.next()) {
                System.out.printf("|%12d|%16s|%10d|%10d|\n", resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getInt(3), resultSet.getInt(4));
            }
            System.out.println("+------------+----------------+----------+----------+");
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void addClient(String name, String phone, String address) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Customers " +
                    "(name, phone, address) VALUES (?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, address);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printClients() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT *FROM Customers");
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String[] temp = new String[] {
                    resultSetMetaData.getColumnName(1), resultSetMetaData.getColumnName(2),
                    resultSetMetaData.getColumnName(3), resultSetMetaData.getColumnName(4),
            };
            System.out.println("+-------------+--------------------+----------------+----------------+");
            System.out.printf("|%13s|%20s|%16s|%16s|\n", temp[0], temp[1], temp[2], temp[3]);
            System.out.println("+-------------+--------------------+----------------+----------------+");

            while (resultSet.next()) {
                System.out.printf("|%13d|%20s|%16s|%16s|\n", resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4));
            }
            System.out.println("+-------------+--------------------+----------------+----------------+");
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void addOrder(int customerId, ArrayList<Integer> array) {
        try {
            int totalPrice = totalPrice(array);
            int totalWeight = totalWeight(array);

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Orders " +
                    "(customer_id, date, total_weight, total_price) VALUES (?, NOW(), ?, ?)");
            preparedStatement.setInt(1, customerId);
            preparedStatement.setInt(2, totalWeight);
            preparedStatement.setInt(3, totalPrice);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT order_id FROM Orders;");
            ResultSet resultSet = preparedStatement1.executeQuery();

            int orderId = 0;
            while (resultSet.next()) {
                int temp = resultSet.getInt(1);
                if (temp > orderId) {
                    orderId = temp;
                }
            }
            PreparedStatement ps1 = conn.prepareStatement("INSERT INTO Items_Orders (order_id, product_id) VALUES (?, ?)");
            for (int a : array) {
                ps1.setInt(1, orderId);
                ps1.setInt(2, a);
                ps1.executeUpdate();
            }
            ps1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printOrders() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT *FROM Orders");
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            String[] temp = new String[] {
                    resultSetMetaData.getColumnName(1), resultSetMetaData.getColumnName(2),
                    resultSetMetaData.getColumnName(3), resultSetMetaData.getColumnName(4),
                    resultSetMetaData.getColumnName(5)
            };
            System.out.println("+----------+-------------+------------------------+--------------+--------------+");
            System.out.printf("|%10s|%13s|%24s|%14s|%14s|\n", temp[0], temp[1], temp[2], temp[3], temp[4]);
            System.out.println("+----------+-------------+------------------------+--------------+--------------+");

            while (resultSet.next()) {
                System.out.printf("|%10d|%13d|%24s|%14d|%14d|\n", resultSet.getInt(1), resultSet.getInt(2),
                        resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5));
            }
            System.out.println("+----------+-------------+------------------------+--------------+--------------+");
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private int totalWeight(ArrayList<Integer> arrayList) throws SQLException {
        int totalWeight = 0;


        for (int a : arrayList) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT weight FROM Products WHERE product_id = ?");
            preparedStatement.setInt(1, a);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalWeight += resultSet.getInt(1);

            preparedStatement.close();
            resultSet.close();
        }
        return totalWeight;
    }

    private int totalPrice(ArrayList<Integer> arrayList) throws SQLException {
        int totalPrice = 0;

        for (int a : arrayList) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT price FROM Products WHERE product_id = ?");
            preparedStatement.setInt(1, a);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            totalPrice += resultSet.getInt(1);

            preparedStatement.close();
            resultSet.close();
        }
        return totalPrice;
    }
}
