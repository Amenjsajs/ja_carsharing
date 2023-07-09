package carsharing.menu;

import carsharing.model.Customer;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.dao.db.car.DbCarDao;
import carsharing.dao.db.company.DbCompanyDao;
import carsharing.dao.db.customer.DbCustomerDao;

import java.util.List;
import java.util.Scanner;

public class SelectMenuOption {
    private static List<Company> companyList;
    private static List<Car> carList;

    private static MenuOptionSelectorInterface loginOrBack(Menu menu) {
        return () -> {
            Menu.setActive(menu);
        };
    }

    public static MenuOptionSelectorInterface login(Menu menu) {
        return loginOrBack(menu);
    }


    public static MenuOptionSelectorInterface back() {
        return () -> {
        };
    }

    public static MenuOptionSelectorInterface back(Menu menu) {
        return loginOrBack(menu);
    }

    public static MenuOptionSelectorInterface exit(Menu menu) {
        return () -> {
            System.exit(0);
        };
    }

    public static MenuOptionSelectorInterface addCustomer(Scanner input, DbCustomerDao dbCustomerDao){
        return ()->{
            System.out.println("Enter the customer name:");
            String name = input.nextLine();
            Customer customer = new Customer();
            customer.setName(name);
            dbCustomerDao.add(customer);
            System.out.println("The customer was added!\n");
        };
    }

    public static MenuOptionSelectorInterface getCustomerList(Menu customerListMenu, Menu customerMenu, Menu companySubmenuInCustomerMenu, Menu carMenuInCompanyCustomerMenu, Scanner input, DbCustomerDao dbCustomerDao, DbCarDao dbCarDao, DbCompanyDao dbCompanyDao) {
        return () -> {
            List<Customer> customerList = dbCustomerDao.findAll();
            if (customerList.isEmpty()) {
                System.out.println("The customer list is empty!\n");
            } else {
                for (int i = 1, len = customerList.size(); i <= len; i++) {
                    final Customer customer = customerList.get(i - 1);
                    customerListMenu.addOption(String.valueOf(i), customer.getName(), () -> {
                        customerMenu.addOption("1", "Rent a car", customerRentCar(input, customer, companySubmenuInCustomerMenu, carMenuInCompanyCustomerMenu, dbCustomerDao, dbCompanyDao, dbCarDao));
                        customerMenu.addOption("2", "Return a rented car", customerReturnRentedCar(customer, dbCustomerDao));
                        customerMenu.addOption("3", "My rented car", getCustomerRentedCar(customer.getId(), dbCustomerDao, dbCarDao, dbCompanyDao));
                        customerMenu.addOption("0", "Back", back(customerListMenu.getParent()));
                        Menu.setActive(customerMenu);
                        Menu.run(customerMenu.getParent(), input);
                    });
                }
                customerListMenu.addOption("0", "Back", back(customerListMenu.getParent()));
                Menu.setActive(customerListMenu);
                Menu.run(customerListMenu.getParent(), input);
            }
        };
    }

    public static MenuOptionSelectorInterface customerRentCar(Scanner input, Customer customer, Menu companyListSubmenuInCustomerMenu, Menu carMenuInCompanyCustomerMenu, DbCustomerDao dbCustomerDao, DbCompanyDao dbCompanyDao, DbCarDao dbCarDao) {
        return () -> {
            if (customer.getRentedCarId() != 0) {
                System.out.println("You've already rented a car!\n");
            } else {
                List<Company> allCompanies = dbCompanyDao.findAll();
                if (allCompanies.isEmpty()) {
                    System.out.println("The company list is empty!\n");
                } else {
                    System.out.println("Choose a company:");
                    for (int i = 1, len = allCompanies.size(); i <= len; i++) {
                        final Company company = allCompanies.get(i - 1);
                        companyListSubmenuInCustomerMenu.addOption(String.valueOf(i), company.getName(),
                                getCarsToRend(input, carMenuInCompanyCustomerMenu, customer, company, dbCustomerDao, dbCarDao));
                    }
                    companyListSubmenuInCustomerMenu.addOption("0", "Back", back(companyListSubmenuInCustomerMenu.getParent()));
                    Menu.setActive(companyListSubmenuInCustomerMenu);
                    Menu.run(companyListSubmenuInCustomerMenu.getParent().getParent(), input);
                }
            }
        };
    }

    public static MenuOptionSelectorInterface getCarsToRend(Scanner input, Menu carMenuInCompanyCustomerMenu, Customer customer, Company company, DbCustomerDao dbCustomerDao, DbCarDao dbCarDao) {
        return () -> {
            List<Car> allCompanyCars = dbCarDao.findAllCars(company.getId());
            if (allCompanyCars.isEmpty()) {
                System.out.printf("No available cars in the '%s' company\n", company.getName());
            } else {
                System.out.println("Choose a car:");
                for (int i = 1, len = allCompanyCars.size(); i <= len; i++) {
                    final Car car = allCompanyCars.get(i - 1);
                    carMenuInCompanyCustomerMenu.addOption(String.valueOf(i), car.getName(), () -> {
                        customer.setRentedCarId(car.getId());
                        dbCustomerDao.update(customer);
                        System.out.printf("You rented '%s'\n\n", car.getName());

                        Menu.setActive(carMenuInCompanyCustomerMenu.getParent());
                    });
                }
                carMenuInCompanyCustomerMenu.addOption("0", "Back", back(carMenuInCompanyCustomerMenu.getParent()));
                Menu.setActive(carMenuInCompanyCustomerMenu);
                Menu.run(carMenuInCompanyCustomerMenu.getParent(), input);
            }
        };
    }

    public static MenuOptionSelectorInterface customerReturnRentedCar(Customer customer, DbCustomerDao dbCustomerDao) {
        return () -> {
            if (customer.getRentedCarId() == 0) {
                System.out.println("You didn't rent a car!\n");
            } else {
                customer.setRentedCarId(null);
                dbCustomerDao.update(customer);
                System.out.println("You've returned a rented car!\n");
            }
        };
    }

    public static MenuOptionSelectorInterface getCustomerRentedCar(int customerId, DbCustomerDao dbCustomerDao, DbCarDao dbCarDao, DbCompanyDao dbCompanyDao) {
        return () -> {
            Customer customer = dbCustomerDao.findById(customerId);

            if (customer.getRentedCarId() == 0) {
                System.out.println("You didn't rent a car!\n");
            } else {
                Car rentedCar = dbCarDao.findById(customer.getRentedCarId());
                Company company = dbCompanyDao.findById(rentedCar.getCompanyId());

                System.out.println("Your rented car:");
                System.out.println(rentedCar.getName());
                System.out.println("Company:");
                System.out.println(company.getName());
                System.out.println();
            }
        };
    }

    public static MenuOptionSelectorInterface getCompanyList(Menu companyListMenu, Menu carMenu, Scanner input, DbCompanyDao dbCompanyDao, DbCarDao dbCarDao) {
        return () -> {
            companyList = dbCompanyDao.findAll();

            if (companyList.isEmpty()) {
                System.out.println("The company list is empty!\n");
            } else {
                for (int i = 1, len = companyList.size(); i <= len; i++) {
                    final Company company = companyList.get(i - 1);
                    companyListMenu.addOption(String.valueOf(i), company.getName(), () -> {
                        carMenu.setOption("1", SelectMenuOption.getCompanyCarList(company.getId(), dbCarDao));
                        carMenu.setOption("2", SelectMenuOption.addCar(input, company.getId(), dbCarDao));
                        Menu.setActive(carMenu);
                        System.out.printf("'%s' company\n", company.getName());
                        Menu.run(carMenu.getParent(), input);
                    });
                }
                companyListMenu.addOption("0", "Back", back(companyListMenu.getParent()));
                Menu.setActive(companyListMenu);
                Menu.run(companyListMenu.getParent(), input);
            }
        };
    }

    public static MenuOptionSelectorInterface addCompany(Scanner input, DbCompanyDao dbCompanyDao) {
        return () -> {
            System.out.println("Enter the company name:");
            String name = input.nextLine();

            System.out.println();

            Company company = new Company();
            company.setName(name);
            dbCompanyDao.add(company);
            System.out.println("The company was created!\n");
        };
    }

    public static MenuOptionSelectorInterface addCar(Scanner input, int companyId, DbCarDao dbCarDao) {
        return () -> {
            System.out.println("Enter the car name:");
            String name = input.nextLine();
            System.out.println();

            Car car = new Car();
            car.setName(name);
            car.setCompanyId(companyId);

            dbCarDao.add(car);
            System.out.println("The car was added!\n");
        };
    }

    public static MenuOptionSelectorInterface getCompanyCarList(int companyId, DbCarDao dbCarDao) {
        return () -> {
            carList = dbCarDao.findAllCars(companyId);

            if (carList.isEmpty()) {
                System.out.println("The car list is empty!\n");
            } else {
                System.out.println("Car list:");
                Car car;
                for (int i = 1, len = carList.size(); i <= len; i++) {
                    car = carList.get(i - 1);
                    System.out.printf("%s. %s\n", i, car.getName());
                }
                System.out.println();
            }
        };
    }
}
