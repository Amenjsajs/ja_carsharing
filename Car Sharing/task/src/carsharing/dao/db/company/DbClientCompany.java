package carsharing.dao.db.company;

import carsharing.model.Company;
import carsharing.dao.DbClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClientCompany extends DbClient<Company> {
    public DbClientCompany(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Company> selectForList(String query) {
        List<Company> list = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int id = resultSetItem.getInt("ID");
                String name = resultSetItem.getString("NAME");
                Company company = new Company(id, name);
                list.add(company);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
