package Models.Personnel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import CustomExceptions.NegativeNumberException;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class DepartmentManager extends Personnel {
    private int totalManagedEmployees;

    private List<Employee> managedEmployees;

    private CompanyService service;

    public DepartmentManager(Interactable interactor, CompanyService service) {
        super(interactor);
        this.service = service;
        this.setDailySalary(BigDecimal.valueOf(200));
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
        boolean isQualified = true;
        do {
            try {
                interactor.displayMessage("Please enter the Department Manager information: \n");
                super.enter();
                setTotalManagedEmployees(interactor.readInt("Total managed employees (> 0): "));

                List<Employee> employees = new ArrayList<>(totalManagedEmployees);
                for (int i = 0; i < totalManagedEmployees; i++) {
                    employees.set(i, new Employee(interactor, service));
                    employees.get(i).enter();
                }
            } catch (Exception e) {
                isQualified = false;
                interactor.displayMessage(e.getMessage() + "\n");
            }
        } while (!isQualified);
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

    public void delete() {
        // Reset all employees manager id to null
        if (totalManagedEmployees > 0) {
            for (Employee employee : managedEmployees) {
                employee.setManagerId(service, null, true);
            }
        }

        // Remove this Personnel in data repository
        boolean hasBeenRemoved = service.removePersonnel(getId());
        if (hasBeenRemoved) {
            interactor.displayMessage(
                    "Department Manager " + getFullName() + " with ID: " + getId() + " has been removed successfully!");
        }
    }
}
