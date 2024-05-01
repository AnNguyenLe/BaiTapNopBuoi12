package Applications.CompanyManagement;

import java.util.ArrayList;
import java.util.List;

import Applications.ConsoleApplication;
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

    public CompanyManagementApp(
            ConsoleInteractable userInteractor, CompanyService service) {
        this.userInteractor = userInteractor;
        this.service = service;
    }

    @Override
    public void run() {
        selectAnOption(userInteractor, actionOptions);
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
        String promptingMessage = "\n--> Select an action: (from " + MIN_OPTION_ID + " to " + MAX_OPTION_ID
                + " only): ";
        int selectedOption;
        do {
            selectedOption = userInteractor.readInt(promptingMessage);
        } while (selectedOption < MIN_OPTION_ID || selectedOption > MAX_OPTION_ID);

        return selectedOption;
    }
}
