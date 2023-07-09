package carsharing.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DbClient<T> {
    protected final Connection connection;

    public DbClient(Connection connection) {
        this.connection = connection;
    }

    public void run(String str) {
        try (Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(str); // Statement execution
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public T select(String query) {
        List<T> list = selectForList(query);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    protected abstract List<T> selectForList(String query);
}
