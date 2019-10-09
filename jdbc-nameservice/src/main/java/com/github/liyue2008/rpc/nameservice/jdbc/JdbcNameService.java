package com.github.liyue2008.rpc.nameservice.jdbc;

import com.github.liyue2008.rpc.NameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author LiYue
 * Date: 2019/10/9
 */
public class JdbcNameService implements NameService, Closeable {
    private static final Logger logger = LoggerFactory.getLogger(JdbcNameService.class);
    private static final Collection<String> schemes = Collections.singleton("jdbc");
    private static final String DDL_SQL_FILE_NAME = "ddl";
    private static final String LOOKUP_SERVICE_SQL_FILE_NAME = "lookup-service";
    private static final String ADD_SERVICE_SQL_FILE_NAME = "add-service";
    private Connection connection = null;
    private String subprotocol = null;

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        try {
            close();
            subprotocol = nameServiceUri.toString().split(":")[1];
            logger.info("Database: {}.", subprotocol);
            String username = System.getProperty("nameservice.jdbc.username");
            String password = System.getProperty("nameservice.jdbc.password");
            logger.info("Connecting to database: {}...", nameServiceUri);
            if(null == username) {
                connection = DriverManager.getConnection(nameServiceUri.toString());
            } else {
                connection = DriverManager.getConnection(nameServiceUri.toString(), username, password);
            }
            logger.info("Maybe execute ddl to init database...");
            maybeExecuteDDL(connection);
            logger.info("Database connected.");

        } catch (SQLException | IOException e) {
            logger.warn("Exception: ", e);
            throw new RuntimeException(e);
        }
    }

    private void maybeExecuteDDL(Connection connection) throws IOException, SQLException {
        try (Statement statement = connection.createStatement()) {
                String ddlSqlString = readSql(DDL_SQL_FILE_NAME);
                statement.execute(ddlSqlString);
        }

    }

    private String readSql(String filename) throws IOException {
        String ddlFile = toFileName(filename);
        try (InputStream in  = this.getClass().getClassLoader()
                .getResourceAsStream(ddlFile)) {
            if (null != in) {
                return inputStreamToString(in);
            } else {
                throw new IOException(ddlFile + " not found in classpath!");
            }
        }

    }

    private String toFileName(String filename) {
        return filename + "." + subprotocol + ".sql";
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException{
        try (PreparedStatement statement = connection.prepareStatement(readSql(ADD_SERVICE_SQL_FILE_NAME))) {

            statement.setString(1, serviceName);
            statement.setString(2, uri.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Exception: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public URI lookupService(String serviceName) throws IOException{
        try (PreparedStatement statement = connection.prepareStatement(readSql(LOOKUP_SERVICE_SQL_FILE_NAME))) {

            statement.setString(1, serviceName);
            ResultSet resultSet = statement.executeQuery();
            List<URI> uriList = new ArrayList<>();
            while (resultSet.next()) {
                uriList.add(URI.create(resultSet.getString(1)));
            }
            return uriList.get(ThreadLocalRandom.current().nextInt(uriList.size()));
        } catch (SQLException e) {
            logger.warn("Exception: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (null != connection) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.warn("Close exception: ", e);
        }
    }
}
