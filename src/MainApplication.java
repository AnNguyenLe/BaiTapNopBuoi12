import java.util.Scanner;

import Applications.ConsoleApplication;
import Applications.CompanyManagement.CompanyManagementApp;
import DataAccess.DataAccessable;
import DataAccess.DataRepository;
import Models.Company.Company;
import Models.Personnel.Personnel;
import Services.CompanyManagement.CompanyManagementService;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Console.ConsoleInteractable;
import UserInteractor.Console.ConsoleInteractor;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleInteractable userInteractor = new ConsoleInteractor(scanner);
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
