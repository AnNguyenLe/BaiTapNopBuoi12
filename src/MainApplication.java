import java.util.Scanner;

import Applications.CompanyManagementApp;
import Applications.ConsoleApplication;
import Controllers.CompanyManagementController;
import DataAccess.DataAccessable;
import DataAccess.DataRepository;
import Models.Company;
import Models.Personnel;
import Services.CompanyManagementService;
import Services.CompanyService;
import UserInteractor.ConsoleInteractable;
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
