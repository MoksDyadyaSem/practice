import java.sql.*;

public class LibraryManager {
    final String password = "postgres";
    final String user = "postgres";
    final String dbName = "postgres";
    final String url = "jdbc:postgresql://localhost:5432/";

    // Подключение
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url + dbName, user, password);
    }

    public void getBooks() {
        System.out.println("список всех книг:");
        String SQL = "SELECT * FROM books";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("id: " + statement.getInt("id") + " | название: " +
                        statement.getString("title") + " | id автора: " +
                        statement.getInt("author_id") + " | цена: " +
                        statement.getInt("price") + " | кол-во доступных копий: " +
                        statement.getInt("amount"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getAvailableBooks() {
        System.out.println("список доступных к заказу книг:");
        String SQL = "SELECT * FROM books WHERE amount > 0";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("id: " + statement.getInt("id") + " | название: " +
                        statement.getString("title") + " | цена: " +
                        statement.getInt("price") + " | кол-во доступных копий: " +
                        statement.getInt("amount"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getAuthors() {
        System.out.println("список авторов и количество доступных копий их книг:");
        String SQL = "SELECT authors.firstname, authors.lastname, books.amount " +
                "FROM authors JOIN books ON authors.id = books.author_id";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("имя: " +
                        statement.getString("firstname") + " | фамилия: " +
                        statement.getString("lastname") + " | кол-во доступных копий: " +
                        statement.getInt("amount"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getPrice() {
        System.out.println("список книг с ценой:");
        String SQL = "SELECT title,price FROM books";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("название: " +
                        statement.getString("title") + " | цена: " +
                        statement.getString("price"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getDataInfo() {
        System.out.println("список совершенных заказов после 1 января 2023 года:");
        String SQL = "SELECT * FROM orders WHERE order_date > '2023-01-01'";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("id: " + statement.getInt("id") + " | customer_id: " +
                        statement.getInt("customer_id") + " | book_id: " +
                        statement.getInt("book_id") + " | дата заказа: " +
                        statement.getDate("order_date").toString() + " | кол-во книг в заказе: " +
                        statement.getInt("amount"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getClients() {
        System.out.println("список всех клиентов:");
        String SQL = "SELECT * FROM customers";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("id: " + statement.getInt("id") + " | имя: " +
                        statement.getString("firstname") + " | фамилия: " +
                        statement.getString("lastname") + " | почтовый адрес: " +
                        statement.getString("email"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getClientsOrders() {
        System.out.println("список заказов клиентов:");
        String SQL = "SELECT customers.firstname, customers.lastname, orders.order_date " +
                "FROM customers " +
                "JOIN orders ON customers.id = orders.customer_id";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("имя: " +
                        statement.getString("firstname") + " | фамилия: " +
                        statement.getString("lastname") + " | дата заказа: " +
                        statement.getDate("order_date").toString());
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getOrderedBooks() {
        System.out.println("список заказанных книг:");
        String SQL = "SELECT DISTINCT books.title " +
                "FROM books " +
                "JOIN orders ON books.id = orders.book_id";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("название: " +
                        statement.getString("title"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getEmailCom() {
        System.out.println("список клиентов с почтовым адресом, заканчивающимся на '.com':");
        String SQL = "SELECT * FROM customers WHERE email LIKE '%.com'";
        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("id: " + statement.getInt("id") + " | имя: " +
                        statement.getString("firstname") + " | фамилия: " +
                        statement.getString("lastname") + " | почтовый адрес: " +
                        statement.getString("email"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getEmptyClients() {
        System.out.println("список клиентов, которые не сделали ни одного заказа:");
        String SQL = "SELECT firstname, lastname FROM customers WHERE id NOT IN (SELECT customer_id FROM orders)";
        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL);
             ResultSet statement = info.executeQuery()) {
            while (statement.next()) {
                System.out.println("имя: " +
                        statement.getString("firstname") + " | фамилия: " +
                        statement.getString("lastname"));
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void addCustomer(String firstname, String lastname, String email) {
        String SQL = "INSERT INTO customers (firstname, lastname, email) VALUES (?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL)) {
            info.setString(1, firstname);
            info.setString(2, lastname);
            info.setString(3, email);

            int rowsAffected = info.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Читатель успешно добавлен.");
            } else {
                System.out.println("Не удалось добавить читателя.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void deleteCustomer(int customerId) {
        String SQL = "DELETE FROM customers WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement info = connection.prepareStatement(SQL)) {
            info.setInt(1, customerId);

            int rowsAffected = info.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Читатель успешно удален.");
            } else {
                System.out.println("Не удалось найти читателя с указанным ID.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}