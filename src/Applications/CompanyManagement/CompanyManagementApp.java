package Applications.CompanyManagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import Applications.ConsoleApplication;
import Comparators.SortByMonthSalaryDesc;
import Comparators.SortByName;
import Models.Company.Company;
import Models.Personnel.DepartmentManager;
import Models.Personnel.Director;
import Models.Personnel.Employee;
import Models.Personnel.Personnel;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Console.ConsoleInteractable;

public class CompanyManagementApp implements ConsoleApplication {
    private ConsoleInteractable userInteractor;
    private CompanyService service;
    private static final List<String> actionOptions = new ArrayList<>(List.of(
            "Enter company information",
            "Assign Employee as Department Manager",
            "Add an Employee",
            "Delete an Employee (Caution! All references of this Personnel will be deleted if any!)",
            "Display all employees",
            "Display total salary of the company",
            "Find Employee who has the highest salary",
            "Find Department Manager who has the most managed employees",
            "Sort Employees' names alphabetically",
            "Sort Employees' salaries in descending order",
            "Find Director has the most share",
            "Display all Directors' monthly incomes"));

    private final int MIN_OPTION_ID = 1;
    private final int MAX_OPTION_ID = actionOptions.size();
    private Company company;

    public CompanyManagementApp(Company company,
            ConsoleInteractable userInteractor, CompanyService service) {
        this.company = company;
        this.userInteractor = userInteractor;
        this.service = service;
    }

    @Override
    public void run() {
        List<Personnel> personnels = service.getPersonnels();
        boolean shouldContinue = true;

        while (shouldContinue) {
            int selectedOption;
            if (personnels == null) {
                userInteractor.displayMessage(
                        "\n\nWelcome to Company Management App!\nFirst you have to hydrate the app by enter the company information:\n");
                selectedOption = 1;
            } else {
                selectedOption = selectAnOption(userInteractor, actionOptions);
            }

            executeStrategy(selectedOption);

            shouldContinue = promptUserToContinue();
        }

        stop();
    }

    private void executeStrategy(int selectedOption) {
        HashMap<Integer, Function<Void, Void>> strategyMap = strategyMapper();
        strategyMap.get(selectedOption).apply(null);
    }

    private HashMap<Integer, Function<Void, Void>> strategyMapper() {
        HashMap<Integer, Function<Void, Void>> mapper = new HashMap<>();
        mapper.put(1, enterCompanyInfo);
        mapper.put(2, assignEmployeeAsDepartmentManager);
        mapper.put(3, addNewPersonnel);
        mapper.put(4, deletePersonnel);
        mapper.put(5, displayAllPersonnel);
        mapper.put(6, calculateTotalSalaries);
        mapper.put(7, findHighestSalaryEmployee);
        mapper.put(8, findManagerWithMostManagedEmployees);
        mapper.put(9, sortPersonnelNamesAlphabetically);
        mapper.put(10, sortPersonnelsSalaryDesc);
        mapper.put(11, findLargestSharePercentageDirector);
        mapper.put(12, calculateEveryDirectorIncome);
        return mapper;
    }

    private Function<Void, Void> enterCompanyInfo = v -> {
        company.enter();
        return v;
    };
    private Function<Void, Void> assignEmployeeAsDepartmentManager = v -> {
        String employeeId = userInteractor
                .readLine("Enter ID of Employee you want to assign as a Department Manager: ");
        Employee employee = (Employee) service.findPersonnel(p -> p.getId().equals(employeeId));
        employee.assignAsDepartmentManager();
        return v;
    };
    private Function<Void, Void> addNewPersonnel = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels = company.addNewPersonel(personnels);
        service.savePersonnels(personnels);
        return v;
    };
    private Function<Void, Void> deletePersonnel = v -> {
        String employeeId = userInteractor
                .readLine("Enter ID of Employee you want to assign as a Department Manager: ");
        Employee employee = (Employee) service.findPersonnel(p -> p.getId().equals(employeeId));
        employee.delete();
        return v;
    };
    private Function<Void, Void> displayAllPersonnel = v -> {
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

    private Function<Void, Void> calculateTotalSalaries = v -> {
        List<Personnel> personnels = service.getPersonnels();
        BigDecimal total = BigDecimal.ZERO;
        for (Personnel personnel : personnels) {
            total = total.add(personnel.calculateMonthlySalary());
        }

        userInteractor.displayMessage("Total Salaries of the company: " + total.toPlainString());
        return v;
    };

    private Function<Void, Void> findHighestSalaryEmployee = v -> {
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

    private Function<Void, Void> findManagerWithMostManagedEmployees = v -> {
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

    private Function<Void, Void> sortPersonnelNamesAlphabetically = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels.sort(new SortByName());
        service.savePersonnels(personnels);

        userInteractor.displayMessage("Personnels sorted by name alphabetically:\n");
        for (Personnel personnel : personnels) {
            userInteractor.displayMessage(personnel.toString());
        }
        return v;
    };

    private Function<Void, Void> sortPersonnelsSalaryDesc = v -> {
        List<Personnel> personnels = service.getPersonnels();
        personnels.sort(new SortByMonthSalaryDesc());
        service.savePersonnels(personnels);

        userInteractor.displayMessage("Personnels sorted by salary descendingly:\n");
        for (Personnel personnel : personnels) {
            userInteractor.displayMessage(personnel.toString());
        }
        return v;
    };

    private Function<Void, Void> findLargestSharePercentageDirector = v -> {
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

    private Function<Void, Void> calculateEveryDirectorIncome = v -> {
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

    @Override
    public void displayOptions(Iterable<String> options) {
        userInteractor.displayMessage("\n\nChoose an action you want to perform:\n\n");
        int optionNumber = 1;
        for (String option : options) {
            userInteractor.displayMessage(optionNumber++ + ". " + option + ".\n");
        }
    }

    @Override
    public int selectAnOption(ConsoleInteractable userInteractor, Iterable<String> options) {
        displayOptions(options);
        String promptingMessage = "--> Select an action: (from " + MIN_OPTION_ID + " to " + MAX_OPTION_ID
                + " only)";
        int selectedOption = userInteractor.readInt(
                promptingMessage + ": ",
                "Valid option must be between " + MIN_OPTION_ID + " and " + MAX_OPTION_ID,
                option -> option < MIN_OPTION_ID || option > MAX_OPTION_ID);

        return selectedOption;
    }

    private void stop() {
        userInteractor.displayMessage("Program stopped...\nWish you a great day!\n");
    }

    private boolean promptUserToContinue() {
        boolean shouldContinue = true;
        String userAnswer = userInteractor.readLine(
                "\n\n--> Do you wish to continue using the app (Y/N) - Yes is the default.\nEnter 'N' to stop the program: ");
        String refinedAnswer = userAnswer.trim().toLowerCase();
        if (refinedAnswer.equals("n") || refinedAnswer.equals("no")) {
            shouldContinue = false;
        }
        return shouldContinue;
    }
}
