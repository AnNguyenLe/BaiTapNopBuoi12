package Models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import CustomExceptions.NegativeNumberException;
import Services.CompanyService;
import UserInteractor.Interactable;

public class DepartmentManager extends Personnel {
    private int totalManagedEmployees;

    private List<Employee> managedEmployees = new ArrayList<>();

    public DepartmentManager(Interactable interactor, CompanyService service) {
        super(interactor, service);
        this.setDailySalary(Constants.DEPARTMENT_MANAGER_DAILY_SALARY);
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
        boolean alreadyManagedByDm = managedEmployees.contains(employee);
        if (alreadyManagedByDm) {
            interactor.displayMessage(
                    "Assignment process stopped. Reason: This employee has been assigned to Department Manager with ID: "
                            + getId() + ".");
            return;
        }
        managedEmployees.add(employee);
        ++totalManagedEmployees;
        interactor.displayMessage("Successfully assigned Employee ID: " + employee.getId()
                + " to Department Manager ID: " + getId() + "!");
    }

    public void removeManagedPersonnel(String personnelId) {
        Employee employee = managedEmployees.stream().filter(p -> p.getId().equals(personnelId)).findFirst().get();
        if (employee == null) {
            interactor.displayMessage("Employee with ID is currently not managed by this Department Manager.");
            return;
        }
        employee.setManagerId(service, null);
        managedEmployees.remove(employee);
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
