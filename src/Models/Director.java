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
        double remainingSharePercentage = service.getRemainingSharePercentage();
        if (sharePercentage < 0 || sharePercentage > remainingSharePercentage) {
            throw new IllegalArgumentException("Share Percentage must be in range 0 - " + remainingSharePercentage + "!");
        }
        this.sharePercentage = sharePercentage;
    }

    @Override
    public void enter() {
        interactor.displayMessage("Please enter the Director information: \n");
        double remainingSharePercentage = service.getRemainingSharePercentage();
        setSharePercentage(
                interactor.readDouble(
                        "Shares/Stocks Percentage ([0 - " + remainingSharePercentage + "]): ",
                        "Percentage must be a value between 0 and " + remainingSharePercentage,
                        percentage -> percentage < 0 || percentage > remainingSharePercentage));

        super.enter();
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
