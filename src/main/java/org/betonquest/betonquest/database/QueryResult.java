package org.betonquest.betonquest.database;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult implements AutoCloseable {

    private final BetonQuestLogger log = BetonQuest.getInstance().getLoggerFactory().create(getClass());

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
        closeQuietly(resultSet);
        closeQuietly(statement);
        closeQuietly(connection);
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
                log.debug("Failed to close resource: " + closeable);
            }
        }
    }
}
