package carsharing.model;

public class Car extends AbstractCommonEntity{
    private int companyId;

    public Car() {}

    public Car(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "Car{" +
                "companyId=" + companyId +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
