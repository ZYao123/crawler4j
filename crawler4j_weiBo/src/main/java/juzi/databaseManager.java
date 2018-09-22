package juzi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class databaseManager {
    private static final Logger logger = LoggerFactory.getLogger(databaseManager.class);
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.144.129.146/test?useUnicode=true&characterEncoding=UTF-8";

    // Database credentials
    static final String USER = "your acconut and password";
    static final String PASS = "your acconut and password";

    Connection conn = null;
    private static databaseManager manager = new databaseManager();

    public static databaseManager getManager() {
        if (manager == null)
            logger.error("manager is null");
        return manager;
    }

    private databaseManager() {
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPs(String sql) throws SQLException {
        //预编译并返回PreparedStatement
        return conn.prepareStatement(sql);
    }


    public int insert(PreparedStatement ps, String[] arr) throws SQLException {
        for (int i = 0; i < arr.length; i++) {
            ps.setString(i + 1, arr[i]);
        }
        return ps.executeUpdate();
    }
}
