package carsharing.model;

public class Customer extends AbstractCommonEntity {
    private Integer rentedCarId;

    public Customer() {
    }

    public Customer(int id, String name, int rentedCarId) {
        super(id, name);
        this.rentedCarId = rentedCarId;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", rentedCarId='" + rentedCarId + "'" +
                '}';
    }
}
