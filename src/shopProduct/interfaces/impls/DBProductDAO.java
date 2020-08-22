package shopProduct.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shopProduct.db.SQLiteConnection;
import shopProduct.interfaces.ProductDAO;
import shopProduct.objects.Product;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBProductDAO implements ProductDAO {

    private final Logger logger = Logger.getLogger(DBProductDAO.class.getName());
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @Override
    public boolean add(Product product) {
        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "INSERT INTO products (prodid, title, price) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, product.getProdid());
            statement.setString(2, product.getTitle());
            statement.setInt(3, product.getPrice());


            int result = statement.executeUpdate();
            if (result > 0) {
                int id = statement.getGeneratedKeys().getInt(1);// получить сгенерированный id вставленной записи
                product.setId(id);
                productList.add(product);
                return true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при добавлении записи в БД", ex);
        }

        return false;
    }

    @Override
    public boolean delete(Product product) {

        try (Connection con = SQLiteConnection.getConnection();
             Statement statement = con.createStatement()) {

            int result = statement.executeUpdate("DELETE FROM products WHERE id = " + product.getId());

            if (result > 0) {
                productList.remove(product);
                return true;
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при удалении записи из БД", ex);

        }

        return false;
    }

    @Override
    public boolean update(Product product) {

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "UPDATE products SET prodid = ?, title = ?, price = ? WHERE id = ?")) {

            statement.setInt(1, product.getProdid());
            statement.setString(2, product.getTitle());
            statement.setInt(3, product.getPrice());
            statement.setInt(4, product.getId());

            int result = statement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при обновлении записи в БД", ex);
        }

        return false;
    }

    @Override
    public ObservableList<Product> findAll() {
        productList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery("select * from products")) {

            createProducts(rs);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка в поиске всех записей", ex);
        }

        return productList;
    }

    @Override
    public ObservableList<Product> find(String text) {
        productList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement("select * from products where title like ?")) {

            String searchStr = "%" + text + "%";
            statement.setString(1, searchStr);

            ResultSet rs = statement.executeQuery();
            createProducts(rs);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при поиске записей по названию", ex);
        }

        return productList;
    }

    public ObservableList<Product> findRange(int from, int to) {
        productList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "select * from [products] where [price] >= ? and [price] < ?")) {

            statement.setInt(1, from);
            statement.setInt(2, to);

            ResultSet rs = statement.executeQuery();
            createProducts(rs);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при поиске записи в диапазоне", ex);
        }

        return productList;
    }

    public ObservableList<Product> findAllCondition(String text, int from, int to) {
        productList.clear();
        String sql = "SELECT * FROM [products] WHERE [price] >= ? AND [price] < ? AND [title] LIKE ?";

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {

            String searchStr = "%" + text + "%";

            statement.setInt(1, from);
            statement.setInt(2, to);
            statement.setString(3, searchStr);

            ResultSet rs = statement.executeQuery();
            createProducts(rs);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка при поиске записи по всем условиям", ex);
        }

        return productList;
    }

    private void createProducts(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Product person = new Product();
            person.setId(rs.getInt("id"));
            person.setProdid(rs.getInt("prodid"));
            person.setTitle(rs.getString("title"));
            person.setPrice(rs.getInt("price"));
            productList.add(person);
        }
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }
}
