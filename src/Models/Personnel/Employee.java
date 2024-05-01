package Models.Personnel;

import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class Employee extends Personnel {
    private String managerId;
    private CompanyService service;

    public Employee(Interactable interactor, CompanyService service) {
        super(interactor);
        this.service = service;
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
        boolean isQualified = true;
        do {
            try {
                interactor.displayMessage("Please enter the Employee information: \n");
                super.enter();
                setManagerId(service, interactor.readLine("Employee's manager ID: "));
            } catch (Exception e) {
                isQualified = false;
                interactor.displayMessage(e.getMessage() + "\n");
            }
        } while (!isQualified);
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Employee information:",
                super.toString(),
                "ID of manager: " + managerId);
    }

    private boolean ValidateManagerId(CompanyService service, String managerId) {
        Class<?> manager = service.findPersonnelType(p -> p.getId() == managerId);
        return manager.equals(DepartmentManager.class);
    }
}
