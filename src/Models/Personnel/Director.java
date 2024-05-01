package Models.Personnel;

import java.math.BigDecimal;

import Models.Company.Company;
import Services.CompanyManagement.CompanyService;
import UserInteractor.Interactable;

public class Director extends Personnel {
    private double sharePercentage;

    public Director(Interactable interactor, CompanyService service) {
        super(interactor, service);
        this.setDailySalary(BigDecimal.valueOf(300));
    }

    public double getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(double sharePercentage) {
        if (sharePercentage < 0 || sharePercentage > 1) {
            throw new IllegalArgumentException("Share Percentage must be in range 0 - 1.0!");
        }
        this.sharePercentage = sharePercentage;
    }

    @Override
    public void enter() {
        boolean isQualified = true;
        do {
            try {
                interactor.displayMessage("Please enter the Director information: \n");
                super.enter();
                setSharePercentage(interactor.readDouble("Shares/Stocks Percentage ([0 - 1]): "));
            } catch (Exception e) {
                isQualified = false;
                interactor.displayMessage(e.getMessage() + "\n");
            }
        } while (!isQualified);
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Director information:",
                super.toString(),
                "Shares/Stocks Percentage: " + sharePercentage);
    }

    public BigDecimal calculateMonthlyIncome(Company company) {
        BigDecimal profitFromShare = BigDecimal.valueOf(sharePercentage).multiply(company.calculateProfits());
        return profitFromShare.add(calculateMonthlySalary());
    }
}
