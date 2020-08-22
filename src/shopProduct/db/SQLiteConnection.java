package shopProduct.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnection {

    private static final Logger logger = Logger.getLogger(SQLiteConnection.class.getName());

    public static Connection getConnection() throws SQLException {
        try {
//            Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
            String url = "jdbc:sqlite:db" + File.separator + "products.db";
            return DriverManager.getConnection(url);

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Ошибка в привязке или закрытии к бд", ex);
            throw new SQLException("Ошибка в подключении к базе данных");
        }

    }

}
