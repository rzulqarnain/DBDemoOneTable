package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    public static Connection getConnection(boolean create) throws SQLException {
        String suffix = create ? "create" : "shutdown";
        try {
            if (create) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            }
            try {
                return DriverManager.getConnection("jdbc:derby:" + SetupDatabase.DATABASE + ";" + suffix + "=true");
            } catch (SQLNonTransientConnectionException e) {
                System.out.println("Database shutdown successfully");
            } catch (SQLException e) {
                System.out.println("Database already shutdown -- doing nothing");
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            System.out.println(ex);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
