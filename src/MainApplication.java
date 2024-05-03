import java.util.Scanner;

import Applications.CompanyManagementApp;
import Applications.ConsoleApplication;
import DataAccess.DataAccessable;
import DataAccess.DataRepository;
import Models.Company;
import Models.Personnel;
import Services.CompanyManagementService;
import Services.CompanyService;
import UserInteractor.ConsoleInteractor;
import UserInteractor.Interactable;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Interactable userInteractor = new ConsoleInteractor(scanner);
        DataAccessable<Personnel> dataAccessor = new DataRepository<>();
        CompanyService companyManagementService = new CompanyManagementService(dataAccessor);
        Company company = new Company(userInteractor, companyManagementService);
        ConsoleApplication app = new CompanyManagementApp(company, userInteractor, companyManagementService);
        try {
            app.run();
        } catch (NullPointerException e) {
            userInteractor.displayMessage("Cannot perform this action! \n" +
                    "This error might be caused by empty lists.\n" +
                    "Make sure you did not operate on a non-predefined company!\n" +
                    "--> SOLUTION: If this is the case, this issue can be solve by select option: 'Enter company information'.");
            app.run();
        } catch (Exception e) {
            userInteractor.displayMessage(e.getMessage());
            userInteractor
                    .displayMessage("\nUnexpected error has occured!\n" +
                            "Please contact the support team.\n" +
                            "Please restart to use the application. Sorry for this inconvenience!\n");
        }

        scanner.close();
    }
}
