package Models.Company;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import CustomExceptions.NegativeNumberException;
import CustomExceptions.NullOrEmptyStringException;
import Extensions.StringExtensions;
import Models.Personnel.Personnel;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class Company {
    private String name;
    private String taxId;
    private BigDecimal monthlyIncome;

    private Interactable interactor;
    private CompanyService service;

    public Company(Interactable interactor, CompanyService service) {
        setTaxId();
        this.interactor = interactor;
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
        boolean isQualified = true;
        do {
            try {
                interactor.displayMessage("Please enter the Company information: \n");
                setName(interactor.readLine("Company's name: "));
                setMonthlyIncome(interactor.readBigDecimal("Monthly Income (> 0): "));
            } catch (Exception e) {
                isQualified = false;
                interactor.displayMessage(e.getMessage() + "\n");
            }
        } while (!isQualified);
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Company information:",
                "Name: " + getName(),
                "TaxID: " + taxId,
                "Monthly Income: " + monthlyIncome.toPlainString());
    }

    public BigDecimal calculateTotalSalaries(){
        BigDecimal total = BigDecimal.ZERO;
        List<Personnel> personnels = service.getPersonnels();
        for (Personnel personnel : personnels) {
            total.add(personnel.calculateMonthlySalary());
        }

        return total;
    }

    public BigDecimal calculateProfits(){
        return monthlyIncome.subtract(calculateTotalSalaries());
    }
}
