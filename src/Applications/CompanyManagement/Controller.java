package Applications.CompanyManagement;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import Comparators.SortByMonthSalaryDesc;
import Comparators.SortByName;
import Models.Company.Company;
import Models.Personnel.DepartmentManager;
import Models.Personnel.Director;
import Models.Personnel.Employee;
import Models.Personnel.Personnel;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Console.ConsoleInteractable;

public class Controller {
    private ConsoleInteractable userInteractor;
    private CompanyService service;
    private Company company;

    public Controller(ConsoleInteractable userInteractor, CompanyService service, Company company) {
        this.userInteractor = userInteractor;
        this.service = service;
        this.company = company;
    }

    public Function<Void, Void> enterCompanyInfo = v -> {
        company.enter();
        return v;
    };
    public Function<Void, Void> assignEmployeeAsDepartmentManager = v -> {
        String employeeId = userInteractor
                .readLine("Enter ID of Employee you want to assign as a Department Manager: ");
        Employee employee = (Employee) service.findPersonnel(p -> p.getId().equals(employeeId));
        employee.assignAsDepartmentManager();
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

        for (Personnel personnel : personnels) {
            userInteractor.displayMessage(personnel.toString());
        }
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
        service.savePersonnels(personnels);

        userInteractor.displayMessage("Personnels sorted by name alphabetically:\n");
        for (Personnel personnel : personnels) {
            userInteractor.displayMessage(personnel.toString());
        }
        return v;
    };

    public Function<Void, Void> sortPersonnelsSalaryDesc = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels.sort(new SortByMonthSalaryDesc());
        service.savePersonnels(personnels);

        userInteractor.displayMessage("Personnels sorted by salary descendingly:\n");
        for (Personnel personnel : personnels) {
            userInteractor.displayMessage(personnel.toString());
        }
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
        Director temp;
        for (Personnel personnel : personnels) {
            if (personnel.getIsDirector()) {
                temp = (Director) personnel;

                userInteractor.displayMessage("Director " + temp.getFullName() + "has income: "
                        + temp.calculateMonthlyIncome(company).toPlainString());
            }
        }
        return v;
    };
}
