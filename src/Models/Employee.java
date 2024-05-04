package Models;

import java.util.List;

import Services.CompanyService;
import UserInteractor.Interactable;

public class Employee extends Personnel {
    private String managerId;

    public Employee(Interactable interactor, CompanyService service) {
        super(interactor, service);
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(CompanyService service, String managerId) {
        this.managerId = ValidateManagerId(service, managerId) ? managerId : null;
    }

    public void setManagerId(CompanyService service, String managerId, boolean shouldSkipCheck) {
        if (shouldSkipCheck) {
            this.managerId = managerId;
            return;
        }
        setManagerId(service, managerId);
    }

    @Override
    public void enter() {
        interactor.displayMessage("Please enter the Employee information: \n");
        super.enter();
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Employee information:",
                super.toString(),
                "ID of manager: " + managerId);
    }

    @Override
    public void delete() {
        if (managerId != null) {
            // Update manager's managed employees list
            Personnel personnel = service.findPersonnel(p -> p.getId().equals(managerId.trim()));
            if (personnel != null) {
                DepartmentManager manager = (DepartmentManager) personnel;
                List<Employee> managedEmployees = manager.getManagedEmployees();
                managedEmployees.removeIf(p -> p.getId().equals(getId().trim()));
                manager.setManagedEmployees(managedEmployees);
            }
        }

        // Remove this Personnel in data repository
        super.delete();
    }

    public void toBeManagedBy(DepartmentManager dm) {
        if (managerId != null) {
            interactor.displayMessage(
                    "Assignment process stopped. Reason: This employee has been assigned to Department Manager with ID: "
                            + managerId + ".");
            return;
        }
        dm.addManagedPersonnel(this);
        managerId = dm.getId();
    }

    private boolean ValidateManagerId(CompanyService service, String managerId) {
        Class<?> manager = service.findPersonnelType(p -> p.getId().equals(managerId));
        return manager.equals(DepartmentManager.class);
    }
}
