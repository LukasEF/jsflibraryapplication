package dbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author gdm1
 */
public class DB_Manager
{

    public static Connection getConnection()
    {
        try
        {
            DriverManager.registerDriver(
                    new org.apache.derby.jdbc.ClientDriver());
            return DriverManager.getConnection("jdbc:derby://localhost:1527/LibraryDB", "username", "password");
        }
        catch (SQLException sqle)
        {
            System.err.println("Connection not established");
            return null;
        }
    }

}
