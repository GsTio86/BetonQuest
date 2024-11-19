package org.betonquest.betonquest.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult implements AutoCloseable {
    private final Connection connection;

    private final PreparedStatement statement;

    private final ResultSet resultSet;

    public QueryResult(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void close() throws SQLException {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } finally {
            try {
                if (statement != null && !statement.isClosed()) {
                    statement.close();
                }
            } finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }
    }
}
