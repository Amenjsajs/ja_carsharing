package carsharing.dao.interfaces;

import carsharing.model.Car;

import java.util.List;

public interface CarDaoInterface extends DaoInterface<Car>{
    List<Car> findAllCars(int companyId);
}
