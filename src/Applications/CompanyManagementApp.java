package Applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import Controllers.CompanyManagementController;
import Models.Company;
import Services.CompanyService;
import UserInteractor.Interactable;

public class CompanyManagementApp implements ConsoleApplication {
    private Interactable userInteractor;
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
            "Display all Directors' monthly incomes",
            "Exit program"));

    private final int MIN_OPTION_ID = 1;
    private final int MAX_OPTION_ID = actionOptions.size();
    private Company company;

    public CompanyManagementApp(Company company,
            Interactable userInteractor, CompanyService service) {
        this.company = company;
        this.userInteractor = userInteractor;
        this.service = service;
    }

    @Override
    public void run() {
        int exitOption = MAX_OPTION_ID;
        boolean shouldContinue = true;
        while (shouldContinue) {
            int selectedOption;

            selectedOption = selectAnOption(userInteractor, actionOptions);

            executeStrategy(selectedOption);

            if (selectedOption == exitOption) {
                shouldContinue = false;
            }
        }
    }

    private void executeStrategy(int selectedOption) {
        HashMap<Integer, Function<Void, Void>> strategyMap = strategyMapper();
        strategyMap.get(selectedOption).apply(null);
    }

    private HashMap<Integer, Function<Void, Void>> strategyMapper() {
        CompanyManagementController c = new CompanyManagementController(this, userInteractor, service, company);
        HashMap<Integer, Function<Void, Void>> mapper = new HashMap<>();
        mapper.put(1, c.enterCompanyInfo);
        mapper.put(2, c.assignEmployeeToDepartmentManager);
        mapper.put(3, c.addNewPersonnel);
        mapper.put(4, c.deletePersonnel);
        mapper.put(5, c.displayAllPersonnel);
        mapper.put(6, c.calculateTotalSalaries);
        mapper.put(7, c.findHighestSalaryEmployee);
        mapper.put(8, c.findManagerWithMostManagedEmployees);
        mapper.put(9, c.sortPersonnelNamesAlphabetically);
        mapper.put(10, c.sortPersonnelsSalaryDesc);
        mapper.put(11, c.findLargestSharePercentageDirector);
        mapper.put(12, c.calculateEveryDirectorIncome);
        mapper.put(13, c.exitProgram);
        return mapper;
    }

    @Override
    public void displayOptions(Iterable<String> options) {
        userInteractor.displayMessage("\n\nChoose an action you want to perform:\n\n");
        int optionNumber = 1;
        for (String option : options) {
            userInteractor.displayMessage(optionNumber++ + ". " + option + ".\n");
        }
    }

    @Override
    public int selectAnOption(Interactable userInteractor, Iterable<String> options) {
        displayOptions(options);
        String promptingMessage = "--> Select an action: (from " + MIN_OPTION_ID + " to " + MAX_OPTION_ID
                + " only)";
        int selectedOption = userInteractor.readInt(
                promptingMessage + ": ",
                "Valid option must be between " + MIN_OPTION_ID + " and " + MAX_OPTION_ID + ".\n",
                option -> option < MIN_OPTION_ID || option > MAX_OPTION_ID);

        return selectedOption;
    }

    @Override
    public void stop() {
        userInteractor.displayMessage("Program stopped...\nWish you a great day!\n");
        return;
    }
}
