package carsharing.dao.db.company;

import carsharing.model.Company;
import carsharing.dao.interfaces.CompanyDaoInterface;

import java.sql.Connection;
import java.util.List;

public class DbCompanyDao implements CompanyDaoInterface {
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS COMPANY(" +
            "ID INTEGER NOT NULL AUTO_INCREMENT, " +
            "NAME VARCHAR(255) UNIQUE NOT NULL);" +
            "ALTER TABLE COMPANY " +
            "ADD CONSTRAINT PK_Company PRIMARY KEY (ID);";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT = "SELECT * FROM COMPANY WHERE ID = %d";
    private static final String INSERT_DATA = "INSERT INTO COMPANY(NAME) VALUES ('%s')";
    private static final String UPDATE_DATA = "UPDATE COMPANY SET NAME " +
            "= '%s' WHERE ID = %d";
    private static final String DELETE_DATA = "DELETE FROM COMPANY WHERE ID = %d";

    private final DbClientCompany dbClientCompany;

    public DbCompanyDao(Connection connection) {
        dbClientCompany = new DbClientCompany(connection);
        dbClientCompany.run(CREATE_DB);
    }

    @Override
    public void add(Company company) {
        dbClientCompany.run(String.format(
                INSERT_DATA, company.getName()));
    }

    @Override
    public List<Company> findAll() {
        return dbClientCompany.selectForList(SELECT_ALL);
    }

    @Override
    public Company findById(int id) {
        Company company = dbClientCompany.select(String.format(SELECT, id));

        if (company != null) {
            return company;
        } else {
            return null;
        }
    }

    @Override
    public void update(Company company) {
        dbClientCompany.run(String.format(
                UPDATE_DATA, company.getName(), company.getId()));
    }

    @Override
    public void deleteById(int id) {
        dbClientCompany.run(String.format(DELETE_DATA, id));
    }
}
