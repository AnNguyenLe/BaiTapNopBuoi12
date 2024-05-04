package Models;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import CustomExceptions.NegativeNumberException;
import CustomExceptions.NullOrEmptyStringException;
import Extensions.StringExtensions;
import Services.CompanyService;
import UserInteractor.Interactable;

public class Company {
    private String name;
    private String taxId;
    private BigDecimal monthlyIncome;

    private Interactable interactor;
    private CompanyService service;

    private List<String> personnelTypes = List.of(
            Employee.class.getSimpleName(),
            DepartmentManager.class.getSimpleName(),
            Director.class.getSimpleName());

    public Company(Interactable userInteractor, CompanyService service) {
        setTaxId();
        this.interactor = userInteractor;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (StringExtensions.isNullOrEmpty(name)) {
            throw new NullOrEmptyStringException("Company's Name");
        }
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    private void setTaxId() {
        this.taxId = UUID.randomUUID().toString();
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeNumberException("Company's monthly income");
        }
        this.monthlyIncome = monthlyIncome;
    }

    public void enter() {

        interactor.displayMessage("Please enter the Company information: \n");
        setName(
                interactor.readLine(
                        "Company's name: ",
                        "Company name cannot be null or empty!",
                        str -> StringExtensions.isNullOrEmpty(str)));
        setMonthlyIncome(
                interactor.readBigDecimal(
                        "Monthly Income (> 0): ",
                        "Monthly income must be a non-negative number!",
                        bigDecimalValue -> bigDecimalValue.compareTo(BigDecimal.ZERO) < 0));

        int totalPersonnels = interactor.readInt(
                "How many personnels you want to add? : ",
                "Number of personnels must be a positive number!",
                value -> value <= 0);
        for (int i = 0; i < totalPersonnels; i++) {
            addNewPersonel();
        }
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Company information:",
                "Name: " + getName(),
                "TaxID: " + taxId,
                "Monthly Income: " + monthlyIncome.toPlainString());
    }

    public BigDecimal calculateTotalSalaries() {
        BigDecimal total = BigDecimal.ZERO;
        List<Personnel> personnels = service.getPersonnels();
        for (Personnel personnel : personnels) {
            total.add(personnel.calculateMonthlySalary());
        }

        return total;
    }

    public BigDecimal calculateProfits() {
        return monthlyIncome.subtract(calculateTotalSalaries());
    }

    private String promptChoosePersonnelType() {
        List<String> formattedList = IntStream.range(0, personnelTypes.size())
                .mapToObj(index -> (index + 1) + ". for " + personnelTypes.get(index))
                .collect(Collectors.toList());
        return String.join(" | ", formattedList);
    }

    public void addNewPersonel() {
        int maxValidOptionId = personnelTypes.size();
        int selectedOption = interactor
                .readInt(
                        "Enter a corresponding number to generate a personnel --> " + promptChoosePersonnelType()
                                + ": ",
                        "Number must be in range from 1 to " + maxValidOptionId + " only!",
                        optionNo -> optionNo < 1 || optionNo > maxValidOptionId);
        switch (selectedOption) {
            case 1:
                Employee employee = new Employee(interactor, service);
                employee.enter();
                break;
            case 2:
                DepartmentManager dm = new DepartmentManager(interactor, service);
                dm.enter();
                break;
            case 3:
                Director director = new Director(interactor, service);
                director.enter();
                BigDecimal remainingSharePercentage = service.getRemainingSharePercentage();
                remainingSharePercentage = remainingSharePercentage.subtract(director.getSharePercentage());
                service.setRemainingSharePercentage(remainingSharePercentage);
                break;
            default:
                Employee personnel = new Employee(interactor, service);
                personnel.enter();
                break;
        }
    }
}
