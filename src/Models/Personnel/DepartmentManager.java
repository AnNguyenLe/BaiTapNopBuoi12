package Models.Personnel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import CustomExceptions.NegativeNumberException;
import Models.Company.Constants;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class DepartmentManager extends Personnel {
    private int totalManagedEmployees;

    private List<Employee> managedEmployees;
    private List<Personnel> personnels;

    private CompanyService service;

    public DepartmentManager(Interactable interactor, CompanyService service) {
        super(interactor, service);
        this.setDailySalary(Constants.DEPARTMENT_MANAGER_DAILY_SALARY);
        this.personnels = service.getPersonnels();
        this.setIsDeptManager(true);
    }

    public int getTotalManagedEmployees() {
        return totalManagedEmployees;
    }

    private void setTotalManagedEmployees(int totalManagedEmployees) {
        if (totalManagedEmployees >= 0) {
            this.totalManagedEmployees = totalManagedEmployees;
            return;
        }

        throw new NegativeNumberException("Total Managed Employees");
    }

    public List<Employee> getManagedEmployees() {
        return managedEmployees;
    }

    public void setManagedEmployees(List<Employee> managedEmployees) {
        this.managedEmployees = managedEmployees;
        setTotalManagedEmployees(managedEmployees.size());
    }

    @Override
    public void enter() {
        interactor.displayMessage("Please enter the Department Manager information: \n");
        super.enter();
        setTotalManagedEmployees(interactor.readInt("Total managed employees (> 0): ",
                "Total managed employees cannot be a negative number!",
                total -> total < 0));

        List<Employee> employees = new ArrayList<>(totalManagedEmployees);
        for (int i = 0; i < totalManagedEmployees; i++) {
            Employee newEmployee = new Employee(interactor, service);
            newEmployee.enter();
            employees.add(newEmployee);
            personnels.add(newEmployee);
        }

        service.savePersonnels(personnels);
    }

    @Override
    public BigDecimal calculateMonthlySalary() {
        BigDecimal multiplier = BigDecimal.valueOf(100);
        return super.calculateMonthlySalary()
                .add(BigDecimal.valueOf(totalManagedEmployees).multiply(multiplier));
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Department Manager information:",
                super.toString(),
                "Total managed employees: " + totalManagedEmployees);
    }

    public void addManagedPersonnel(Employee employee) {
        List<Employee> updatedList = getManagedEmployees();
        updatedList.add(employee);
        setManagedEmployees(updatedList);
    }

    public void removeManagedPersonnel(String personnelId) {
        boolean hasBeenRemoved = managedEmployees.removeIf(p -> p.getId() == personnelId);
        if (hasBeenRemoved) {
            setManagedEmployees(managedEmployees);
        }
    }

    @Override
    public void delete() {
        // Reset all employees manager id to null
        if (totalManagedEmployees > 0) {
            List<Personnel> personnels = service.getPersonnels();
            for (Employee employee : managedEmployees) {
                personnels.removeIf(p -> p.getId().equals(employee.getId()));
                employee.setManagerId(service, null, true);
                personnels.add(employee);
            }

            service.savePersonnels(personnels);
        }

        // Remove this Personnel in data repository
        super.delete();
    }
}
