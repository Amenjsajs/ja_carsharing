package carsharing.dao.db.customer;

import carsharing.model.Customer;
import carsharing.dao.DbClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClientCustomer extends DbClient<Customer> {

    public DbClientCustomer(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Customer> selectForList(String query) {
        List<Customer> list = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int id = resultSetItem.getInt("ID");
                String name = resultSetItem.getString("NAME");
                int rentedCarId = resultSetItem.getInt("RENTED_CAR_ID");
                Customer customer = new Customer(id, name, rentedCarId);
                list.add(customer);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
