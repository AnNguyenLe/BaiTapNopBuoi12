package Models.Personnel;

import java.util.List;

import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class Employee extends Personnel {
    private String managerId;
    private CompanyService service;

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
        // Update manager's managed employees list
        Personnel personnel = service.findPersonnel(p -> p.getId().equals(managerId));
        if (personnel != null) {
            DepartmentManager manager = (DepartmentManager) personnel;
            List<Employee> managedEmployees = manager.getManagedEmployees();
            managedEmployees.removeIf(p -> p.getId().equals(getId()));
            manager.setManagedEmployees(managedEmployees);
        }

        // Remove this Personnel in data repository
        super.delete();
    }

    public DepartmentManager promoteToDepartmentManager(){
        return null;
    }

    private boolean ValidateManagerId(CompanyService service, String managerId) {
        Class<?> manager = service.findPersonnelType(p -> p.getId().equals(managerId));
        return manager.equals(DepartmentManager.class);
    }
}
