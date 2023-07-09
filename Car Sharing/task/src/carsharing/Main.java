package carsharing;

import carsharing.menu.Menu;
import carsharing.menu.SelectMenuOption;
import carsharing.dao.db.car.DbCarDao;
import carsharing.dao.db.company.DbCompanyDao;
import carsharing.dao.db.customer.DbCustomerDao;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Scanner input;
    private static Connection connection;

    public static void main(String[] args) {
        try {
            String database = "null";
            for (int i = 0, len = args.length; i < len; i++) {
                if (args[i].equals("-databaseFileName") && i + 1 < len) {
                    database = args[i + 1];
                }
            }

            if (database == null) {
                database = String.valueOf(System.currentTimeMillis());
            }

            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/" + database);
            connection.setAutoCommit(true);

            DbCompanyDao dbCompanyDao = new DbCompanyDao(connection);
            DbCarDao dbCarDao = new DbCarDao(connection);
            DbCustomerDao dbCustomerDao = new DbCustomerDao(connection);

            input = new Scanner(System.in);

            Menu mainMenu = new Menu();
            Menu.setActive(mainMenu);

            Menu customerListMenu = new Menu("Customer list");
            Menu customerMenu = new Menu();

            Menu companySubmenuInCustomerMenu = new Menu();
            Menu carMenuInCompanyCustomerMenu = new Menu();

            Menu companyMenu = new Menu();
            Menu companyListMenu = new Menu("Choose the company");
            Menu carMenu = new Menu();

            mainMenu.addSubMenu(companyMenu);
            mainMenu.addSubMenu(customerListMenu);
            mainMenu.addSubMenu(customerMenu);
            companyMenu.addSubMenu(companyListMenu);
            companyMenu.addSubMenu(carMenu);
            customerMenu.addSubMenu(companySubmenuInCustomerMenu);
            customerMenu.addSubMenu(carMenuInCompanyCustomerMenu);

            mainMenu.addOption("1", "Log in as a manager", SelectMenuOption.login(companyMenu));
            mainMenu.addOption("2", "Log in as customer", SelectMenuOption.getCustomerList(customerListMenu, customerMenu, companySubmenuInCustomerMenu, carMenuInCompanyCustomerMenu, input, dbCustomerDao, dbCarDao, dbCompanyDao));
            mainMenu.addOption("3", "Create a customer", SelectMenuOption.addCustomer(input, dbCustomerDao));
            mainMenu.addOption("0", "Exit", SelectMenuOption.exit(mainMenu));

            companyMenu.addOption("1", "Company list", SelectMenuOption.getCompanyList(companyListMenu, carMenu, input, dbCompanyDao, dbCarDao));
            companyMenu.addOption("2", "Create a company", SelectMenuOption.addCompany(input, dbCompanyDao));
            companyMenu.addOption("0", "Back", SelectMenuOption.back(mainMenu));

            carMenu.addOption("1", "Car list", ()->{});
            carMenu.addOption("2", "Create a car", ()->{});
            carMenu.addOption("0", "Back", SelectMenuOption.back(companyListMenu.getParent()));

            Menu.run(mainMenu, input);
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}