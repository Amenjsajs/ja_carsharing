package carsharing.dao.db.customer;


import carsharing.model.Customer;
import carsharing.dao.interfaces.CustomerDaoInterface;

import java.sql.Connection;
import java.util.List;

public class DbCustomerDao implements CustomerDaoInterface{
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CUSTOMER(" +
            "ID INTEGER NOT NULL AUTO_INCREMENT, " +
            "NAME VARCHAR(255) UNIQUE NOT NULL, " +
            "RENTED_CAR_ID INT DEFAULT null, " +
            "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) " +
            "REFERENCES CAR(ID));" +
            "ALTER TABLE CUSTOMER "+
            "ADD CONSTRAINT PK_Customer PRIMARY KEY (ID);";
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String SELECT = "SELECT * FROM CUSTOMER WHERE ID = %d";
    private static final String INSERT_DATA = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES ('%s', %d)";
    private static final String UPDATE_DATA = "UPDATE CUSTOMER SET RENTED_CAR_ID " +
            "= %d WHERE ID = %d";
    private static final String DELETE_DATA = "DELETE FROM CUSTOMER WHERE ID = %d";

    private final DbClientCustomer dbClientCustomer;

    public DbCustomerDao(Connection connection) {
        dbClientCustomer = new DbClientCustomer(connection);
        dbClientCustomer.run(CREATE_DB);
    }

    @Override
    public void add(Customer customer) {
        dbClientCustomer.run(String.format(
                INSERT_DATA, customer.getName(), customer.getRentedCarId()));
    }

    @Override
    public List<Customer> findAll() {
        return dbClientCustomer.selectForList(SELECT_ALL);
    }

    @Override
    public Customer findById(int id) {
        Customer customer = dbClientCustomer.select(String.format(SELECT, id));

        if (customer != null) {
            return customer;
        } else {
            return null;
        }
    }

    @Override
    public void update(Customer customer) {
        dbClientCustomer.run(String.format(
                UPDATE_DATA, customer.getRentedCarId(), customer.getId()));
    }

    @Override
    public void deleteById(int id) {
        dbClientCustomer.run(String.format(DELETE_DATA, id));
    }
}
