package carsharing.model;

public class Company extends AbstractCommonEntity{
    public Company(){}
    public Company(int id, String name){
        super(id, name);
    }

    @Override
    public String toString() {
        return "Company{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}