package Applications.CompanyManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import Applications.ConsoleApplication;
import Models.Company.Company;
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
        Controller c = new Controller(userInteractor, service, company);
        HashMap<Integer, Function<Void, Void>> mapper = new HashMap<>();
        mapper.put(1, c.enterCompanyInfo);
        mapper.put(2, c.assignEmployeeAsDepartmentManager);
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
