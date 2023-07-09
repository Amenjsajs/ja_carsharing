package carsharing.dao.db.car;

import carsharing.dao.DbClient;
import carsharing.model.Car;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbClientCar extends DbClient<Car> {
    public DbClientCar(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Car> selectForList(String query) {
        List<Car> list = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int id = resultSetItem.getInt("ID");
                String name = resultSetItem.getString("NAME");
                int companyId = resultSetItem.getInt("COMPANY_ID");
                Car car = new Car(id, name, companyId);
                list.add(car);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
