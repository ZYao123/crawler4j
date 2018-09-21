package juzi;

import com.sun.xml.internal.bind.v2.TODO;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class databaseManager {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.144.129.146/test";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "Ridcax";

    Connection conn = null;

    public databaseManager() {
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
