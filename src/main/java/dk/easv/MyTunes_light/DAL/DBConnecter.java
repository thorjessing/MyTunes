package dk.easv.MyTunes_light.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DBConnecter {
        //designate file path for db settings
        private static final String PROPERTIES_FILE = "Config/config.settings";
        private SQLServerDataSource dataSource;

        public DBConnecter() throws IOException {
            //load db settings to properties
            Properties props = new Properties();
            props.load(new FileInputStream(PROPERTIES_FILE));

            //dataSource configured with database connection details
            dataSource = new SQLServerDataSource();
            dataSource.setServerName(props.getProperty("Server"));
            dataSource.setDatabaseName(props.getProperty("Database"));
            dataSource.setUser(props.getProperty("User"));
            dataSource.setPassword(props.getProperty("Password"));
            dataSource.setPortNumber(1433);
            dataSource.setTrustServerCertificate(true);

        }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    //method to check whether connection to database is true or false
    public static void main(String[] args) throws Exception {
        DBConnecter dbConnecter = new DBConnecter();

        try (Connection connection = dbConnecter.getConnection()) {
            System.out.println("open = " + !connection.isClosed());
        } //Connection gets closed here
    }
}
