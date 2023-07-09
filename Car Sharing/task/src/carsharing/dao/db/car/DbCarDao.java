package carsharing.dao.db.car;

import carsharing.model.Car;
import carsharing.dao.interfaces.CarDaoInterface;

import java.sql.Connection;
import java.util.List;

public class DbCarDao implements CarDaoInterface {
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CAR(" +
            "ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            "NAME VARCHAR(255) UNIQUE NOT NULL, " +
            "COMPANY_ID INT NOT NULL, " +
            "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
            "REFERENCES COMPANY(ID));";
    private static final String SELECT_ALL = "SELECT * FROM CAR";
    private static final String SELECT_CARS = "SELECT * FROM CAR WHERE COMPANY_ID = %d";

    private static final String SELECT = "SELECT * FROM CAR WHERE ID = %d";
    private static final String INSERT_DATA = "INSERT INTO CAR(NAME, COMPANY_ID) VALUES ('%s', %d)";
    private static final String UPDATE_DATA = "UPDATE CAR SET NAME " +
            "= '%s' WHERE ID = %d";
    private static final String DELETE_DATA = "DELETE FROM CAR WHERE ID = %d";

    private final DbClientCar dbClientCar;

    public DbCarDao(Connection connection) {
        dbClientCar = new DbClientCar(connection);
        dbClientCar.run(CREATE_DB);
    }

    @Override
    public void add(Car car) {
        dbClientCar.run(String.format(
                INSERT_DATA,car.getName(), car.getCompanyId()));
    }

    @Override
    public List<Car> findAll() {
        return dbClientCar.selectForList(SELECT_ALL);
    }

    @Override
    public List<Car> findAllCars(int companyId) {
        return dbClientCar.selectForList(String.format(SELECT_CARS, companyId));
    }

    @Override
    public Car findById(int id) {
        Car car = dbClientCar.select(String.format(SELECT, id));

        if (car != null) {
            return car;
        } else {
            return null;
        }
    }

    @Override
    public void update(Car car) {
        dbClientCar.run(String.format(
                UPDATE_DATA, car.getName(), car.getId()));
    }

    @Override
    public void deleteById(int id) {
        dbClientCar.run(String.format(DELETE_DATA, id));
    }
}
