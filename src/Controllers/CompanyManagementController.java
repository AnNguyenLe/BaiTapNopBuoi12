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

    public CompanyManagementController(ConsoleApplication app, Interactable userInteractor, CompanyService service,
            Company company) {
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
        if (!hasAnyPersonnels(service.getPersonnels())) {
            return v;
        }

        List<DepartmentManager> departmentManagers = service.getListOf(DepartmentManager.class);
        if (departmentManagers == null || departmentManagers.size() == 0) {
            userInteractor.displayMessage(
                    "There is no department manager to perform this action.\n--> SOLUTION: Add a Department Manager first.\n");
            return v;
        }

        List<Employee> unmanagedEmployees = service.getListOf(Employee.class, e -> e.getManagerId() == null);
        if (unmanagedEmployees == null || unmanagedEmployees.size() == 0) {
            userInteractor.displayMessage(
                    "There is no unmanaged employee to perform this action.\n--> SOLUTION: Add an Employee first.\n");
            return v;
        }
        service.displayTableOfPersonnels("LIST OF UNMANAGED EMPLOYEES", unmanagedEmployees);
        String employeeId = userInteractor.readLine("Enter employee's ID you want to assign to be managed: ").trim();
        if (!unmanagedEmployees.stream().anyMatch(p -> p.getId().equals(employeeId))) {
            userInteractor.displayMessage(
                    "You did not enter one of the employee IDs in the list!\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }

        Personnel employee = unmanagedEmployees.stream().filter(p -> p.getId().equals(employeeId)).findFirst()
                .orElse(null);
        if (employee == null) {
            userInteractor.displayMessage(
                    "Something wrong with the employee ID you enter.\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }

        service.displayTableOfPersonnels("LIST OF DEPARTMENT MANAGERS", departmentManagers);
        String dmId = userInteractor
                .readLine("Enter department manager's ID who will manage the selected employee above: ").trim();

        if (!departmentManagers.stream().anyMatch(p -> p.getId().equals(dmId))) {
            userInteractor.displayMessage(
                    "You did not enter one of the department manager IDs in the list!\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }

        Personnel dm = departmentManagers.stream().filter(p -> p.getId().equals(dmId)).findFirst().orElse(null);
        ;
        if (dm == null) {
            userInteractor.displayMessage(
                    "Something wrong with the department manager ID you enter.\nMake sure you enter a valid ID in the next try.\n");
            return v;
        }

        ((Employee) employee).toBeManagedBy((DepartmentManager) dm);
        return v;
    };
    public Function<Void, Void> addNewPersonnel = v -> {
        company.addNewPersonel();
        return v;
    };
    public Function<Void, Void> deletePersonnel = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        service.displayTableOfPersonnels("ALL PERSONNELS OF THE COMPANY", personnels);

        String employeeId = userInteractor
                .readLine("Enter ID of Employee you want to delete: ");
        Personnel employee = service.findPersonnel(p -> p.getId().equals(employeeId.trim()));

        if (employee == null) {
            userInteractor.displayMessage(
                    "Something wrong with the personnel ID you enter.\nMake sure you enter a valid ID in the next try.\n");
            userInteractor.displayMessage("Cannot find a personnel with ID: " + employeeId);
            return v;
        }
        employee.delete();
        return v;
    };
    public Function<Void, Void> displayAllPersonnel = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        service.displayTableOfPersonnels("ALL PERSONNELS OF THE COMPANY", personnels);
        return v;
    };

    public Function<Void, Void> calculateTotalSalaries = v -> {
        List<Personnel> personnels = service.getPersonnels();
        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Personnel personnel : personnels) {
            total = total.add(personnel.calculateMonthlySalary());
        }

        userInteractor.displayMessage("Total Salaries of the company: " + total.toPlainString());
        return v;
    };

    public Function<Void, Void> findHighestSalaryEmployee = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        BigDecimal maxSalary = BigDecimal.ZERO;
        Personnel p = personnels.get(0);
        for (Personnel personnel : personnels) {
            if (personnel instanceof Employee &&
                    maxSalary.compareTo(personnel.calculateMonthlySalary()) < 0) {
                p = personnel;
            }
        }

        userInteractor.displayMessage("Highest Employee Salary: " + p.calculateMonthlySalary().toPlainString() + "\n");
        userInteractor.displayMessage(p.toString());
        return v;
    };

    public Function<Void, Void> findManagerWithMostManagedEmployees = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        Personnel dm = personnels.stream().filter(p -> p.getIsDeptManager()).findFirst().get();
        if (dm == null) {
            userInteractor.displayMessage("--> Currently, there is no Department Manager in the company personnels.\n");
            return v;
        }
        DepartmentManager maxDm = (DepartmentManager) dm;
        DepartmentManager temp;
        for (Personnel personnel : personnels) {
            if (personnel instanceof DepartmentManager) {
                temp = (DepartmentManager) personnel;
                if (maxDm.getTotalManagedEmployees() < temp.getTotalManagedEmployees()) {
                    maxDm = temp;
                }
            }
        }

        userInteractor.displayMessage("Department Manager has the most managed employee: "
                + maxDm.getTotalManagedEmployees() + " employees.\n");
        userInteractor.displayMessage(maxDm.toString());
        return v;
    };

    public Function<Void, Void> sortPersonnelNamesAlphabetically = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        personnels.sort(new SortByName());

        service.displayTableOfPersonnels("Personnels sorted by name alphabetically:", personnels);
        return v;
    };

    public Function<Void, Void> sortPersonnelsSalaryDesc = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }
        ;

        personnels.sort(new SortByMonthSalaryDesc());

        service.displayTableOfPersonnels("Personnels sorted by salary descendingly:", personnels);
        return v;
    };

    public Function<Void, Void> findLargestSharePercentageDirector = v -> {
        List<Personnel> personnels = service.getPersonnels();

        if (!hasAnyPersonnels(personnels)) {
            return v;
        }

        Personnel personnel = personnels.stream().filter(p -> p.getIsDirector()).findFirst().get();
        if (personnel == null) {
            userInteractor.displayMessage("--> Currently, there is no Director in the company personnels.\n");
            return v;
        }
        Director maxDirector = (Director) personnel;
        Director temp;

        for (Personnel p : personnels) {
            if (p instanceof Director) {
                temp = (Director) p;
                if (maxDirector.getSharePercentage().compareTo(temp.getSharePercentage()) < 0) {
                    maxDirector = temp;
                }
            }
        }

        userInteractor
                .displayMessage("Director has the largest share: "
                        + maxDirector.getSharePercentage().multiply(BigDecimal.valueOf(100)) + "%\n");
        userInteractor.displayMessage(maxDirector.toString());
        return v;
    };

    public Function<Void, Void> calculateEveryDirectorIncome = v -> {
        if (company.getMonthlyIncome() == null) {
            userInteractor.displayMessage(
                    "Cannot perform this action. Make sure you have already filled in the company's information before perform calculating Director's monthly income!\n");
            return v;
        }
        List<Personnel> personnels = service.getPersonnels();
        if (!hasAnyPersonnels(personnels)) {
            return v;
        }
        Director temp;
        int counter = 0;

        userInteractor.displayMessage("Calculating monthly income of every director...\n");
        for (Personnel personnel : personnels) {
            if (personnel.getIsDirector()) {
                temp = (Director) personnel;

                userInteractor.displayMessage(temp.getFullName() + ": "
                        + temp.calculateMonthlyIncome(company).toPlainString());
                ++counter;
            }
        }

        if (counter == 0) {
            userInteractor.displayMessage("There is no director in the company personnels.\n");
        }

        return v;
    };

    public Function<Void, Void> exitProgram = v -> {
        app.stop();
        return v;
    };

    private boolean hasAnyPersonnels(List<Personnel> personnels) {
        if (personnels == null || personnels.size() == 0) {
            userInteractor.displayMessage("Currently, there is no personnel in the company data.\n");
            return false;
        }
        return true;
    }
}
