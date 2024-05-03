package Models;

import java.math.BigDecimal;

import Services.CompanyService;
import UserInteractor.Interactable;

public class Director extends Personnel {
    private double sharePercentage;

    public Director(Interactable interactor, CompanyService service) {
        super(interactor, service);
        this.setDailySalary(Constants.DIRECTOR_DAILY_SALARY);
        this.setIsDirector(true);
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
        interactor.displayMessage("Please enter the Director information: \n");
        super.enter();
        setSharePercentage(
                interactor.readDouble(
                        "Shares/Stocks Percentage ([0 - 1]): ",
                        "Percentage must be a value between 0 and 1!",
                        percentage -> percentage < 0 || percentage > 1));
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Director information:",
                super.toString(),
                "Shares/Stocks Percentage: " + sharePercentage * 100 + "%");
    }

    public BigDecimal calculateMonthlyIncome(Company company) {
        BigDecimal profitFromShare = BigDecimal.valueOf(sharePercentage).multiply(company.calculateProfits());
        return profitFromShare.add(calculateMonthlySalary());
    }
}
