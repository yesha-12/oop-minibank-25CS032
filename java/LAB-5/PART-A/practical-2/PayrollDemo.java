abstract class Employee {

    protected String name;
    protected int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    abstract double monthlySalary();

    void display() {
        System.out.println(
            "ID: " + id +
            ", Name: " + name +
            ", Salary: " + monthlySalary()
        );
    }
}

class FullTime extends Employee {

    private double fixedSalary;

    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    @Override
    double monthlySalary() {
        return fixedSalary;
    }
}

class PartTime extends Employee {

    private double hours;
    private double rate;

    PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {

    private double stipend;

    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    @Override
    double monthlySalary() {
        return stipend;
    }

    void addInternNote() {
        System.out.println("Note: This employee is an Intern.");
    }
}

public class PayrollDemo {

    public static void main(String[] args) {

        Employee[] employees = {
            new FullTime("Rahul", 101, 50000),
            new PartTime("Priya", 102, 80, 300),
            new Intern("Aman", 103, 15000)
        };

        double totalSalary = 0;

        for (Employee employee : employees) {

            employee.display();

            totalSalary += employee.monthlySalary();

            if (employee instanceof Intern) {
                Intern intern = (Intern) employee;
                intern.addInternNote();
            }
        }

        System.out.println("Total Salary = " + totalSalary);
    }
}