package Controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import Applications.ConsoleApplication;
import Comparators.SortByMonthSalaryDesc;
import Comparators.SortByName;
import Models.Company;
import Models.DepartmentManager;
import Models.Director;
import Models.Employee;
import Models.Personnel;
import Services.CompanyService;
import UserInteractor.Interactable;

public class CompanyManagementController {
    private Interactable userInteractor;
    private CompanyService service;
    private Company company;
    private ConsoleApplication app;

    public CompanyManagementController(ConsoleApplication app, Interactable userInteractor, CompanyService service, Company company) {
        this.userInteractor = userInteractor;
        this.service = service;
        this.company = company;
        this.app = app;
    }

    public Function<Void, Void> enterCompanyInfo = v -> {
        company.enter();
        return v;
    };
    public Function<Void, Void> assignEmployeeToDepartmentManager = v -> {
        List<Employee> unmanagedEmployees = service.getListOf(Employee.class, e -> e.getManagerId() == null);
        service.displayTableOfPersonnels("LIST OF UNMANAGED EMPLOYEES", unmanagedEmployees);
        String employeeId = userInteractor.readLine("Enter employee's ID you want to assign to be managed: ");
        Personnel employee = service.findPersonnel(p -> p.getId().equals(employeeId));
        if (employee == null) {
            userInteractor.displayMessage("Something wrong with the employee ID you enter.\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }
        
        List<DepartmentManager> departmentManagers = service.getListOf(DepartmentManager.class);
        service.displayTableOfPersonnels("LIST OF DEPARTMENT MANAGERS", departmentManagers);
        String dmId = userInteractor.readLine("Enter department manager's ID who will manage the selected employee above: ");
        Personnel dm = service.findPersonnel(p -> p.getId().equals(dmId));
        if (dm == null) {
            userInteractor.displayMessage("Something wrong with the department manager ID you enter.\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }

        ((Employee)employee).toBeManagedBy((DepartmentManager)dm);
        return v;
    };
    public Function<Void, Void> addNewPersonnel = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels = company.addNewPersonel(personnels);
        service.savePersonnels(personnels);
        return v;
    };
    public Function<Void, Void> deletePersonnel = v -> {
        String employeeId = userInteractor
                .readLine("Enter ID of Employee you want to assign as a Department Manager: ");
        Employee employee = (Employee) service.findPersonnel(p -> p.getId().equals(employeeId));
        employee.delete();
        return v;
    };
    public Function<Void, Void> displayAllPersonnel = v -> {
        List<Personnel> personnels = service.getPersonnels();
        if (personnels.size() == 0) {
            userInteractor
                    .displayMessage("Personnels: Empty list.\nThere is no personnel in the company info currently.\n");
            return v;
        }

        service.displayTableOfPersonnels("ALL PERSONNELS OF THE COMPANY", personnels);
        return v;
    };

    public Function<Void, Void> calculateTotalSalaries = v -> {
        List<Personnel> personnels = service.getPersonnels();
        BigDecimal total = BigDecimal.ZERO;
        for (Personnel personnel : personnels) {
            total = total.add(personnel.calculateMonthlySalary());
        }

        userInteractor.displayMessage("Total Salaries of the company: " + total.toPlainString());
        return v;
    };

    public Function<Void, Void> findHighestSalaryEmployee = v -> {
        List<Personnel> personnels = service.getPersonnels();
        BigDecimal maxSalary = BigDecimal.ZERO;
        Personnel p = personnels.get(0);
        for (Personnel personnel : personnels) {
            if (maxSalary.compareTo(personnel.calculateMonthlySalary()) < 0) {
                p = personnel;
            }
        }

        userInteractor.displayMessage("Highest Employee Salary: " + maxSalary.toPlainString());
        userInteractor.displayMessage(p.toString());
        return v;
    };

    public Function<Void, Void> findManagerWithMostManagedEmployees = v -> {
        List<Personnel> personnels = service.getPersonnels();
        Personnel dm = personnels.stream().filter(p -> p.getIsDeptManager()).findFirst().get();
        DepartmentManager maxDm = (DepartmentManager) dm;
        DepartmentManager temp;
        for (Personnel personnel : personnels) {
            if (personnel.getIsDeptManager()) {
                temp = (DepartmentManager) personnel;
                if (maxDm.getTotalManagedEmployees() < temp.getTotalManagedEmployees()) {
                    maxDm = temp;
                }
            }
        }

        userInteractor.displayMessage("Department Manager has the most managed employee: "
                + maxDm.getTotalManagedEmployees() + " employees.");
        userInteractor.displayMessage(maxDm.toString());
        return v;
    };

    public Function<Void, Void> sortPersonnelNamesAlphabetically = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels.sort(new SortByName());

        service.displayTableOfPersonnels("Personnels sorted by name alphabetically:", personnels);
        return v;
    };

    public Function<Void, Void> sortPersonnelsSalaryDesc = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels.sort(new SortByMonthSalaryDesc());

        service.displayTableOfPersonnels("Personnels sorted by salary descendingly:", personnels);
        return v;
    };

    public Function<Void, Void> findLargestSharePercentageDirector = v -> {
        List<Personnel> personnels = service.getPersonnels();
        Director maxDirector = (Director) personnels.stream().filter(p -> p.getIsDirector()).findFirst().get();
        Director temp;

        for (Personnel personnel : personnels) {
            if (personnel.getIsDirector()) {
                temp = (Director) personnel;
                if (maxDirector.getSharePercentage() < temp.getSharePercentage()) {
                    maxDirector = temp;
                }
            }
        }

        userInteractor
                .displayMessage("Director has the largest share: " + maxDirector.getSharePercentage() * 100 + "%");
        userInteractor.displayMessage(maxDirector.toString());
        return v;
    };

    public Function<Void, Void> calculateEveryDirectorIncome = v -> {
        List<Personnel> personnels = service.getPersonnels();
        userInteractor.displayMessage("Monthly income of every Director:\n");
        Director temp;
        for (Personnel personnel : personnels) {
            if (personnel.getIsDirector()) {
                temp = (Director) personnel;

                userInteractor.displayMessage(temp.getFullName() + ": "
                        + temp.calculateMonthlyIncome(company).toPlainString());
            }
        }
        return v;
    };

    public Function<Void, Void> exitProgram = v -> {
        app.stop();
        return v;
    };
}
