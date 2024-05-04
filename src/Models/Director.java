package Models;

import java.math.BigDecimal;

import Services.CompanyService;
import UserInteractor.Interactable;

public class Director extends Personnel {
    private BigDecimal sharePercentage;

    public Director(Interactable interactor, CompanyService service) {
        super(interactor, service);
        this.setDailySalary(Constants.DIRECTOR_DAILY_SALARY);
        this.setIsDirector(true);
    }

    public BigDecimal getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(BigDecimal sharePercentage) {
        BigDecimal remainingSharePercentage = service.getRemainingSharePercentage();
        if (sharePercentage.compareTo(BigDecimal.ZERO) < 0 || sharePercentage.compareTo(remainingSharePercentage) > 0) {
            throw new IllegalArgumentException(
                    "Share Percentage must be in range 0 - " + remainingSharePercentage.toPlainString() + "!");
        }
        this.sharePercentage = sharePercentage;
    }

    @Override
    public void enter() {
        interactor.displayMessage("Please enter the Director information: \n");
        BigDecimal remainingSharePercentage = service.getRemainingSharePercentage();
        setSharePercentage(
                interactor.readBigDecimal(
                        "Shares/Stocks Percentage ([0 - " + remainingSharePercentage.toPlainString() + "]): ",
                        "Remaining share/stocks percentage: "
                                + remainingSharePercentage.multiply(BigDecimal.valueOf(100))
                                + "%.\n" +
                                "Percentage must be a value between 0 and " + remainingSharePercentage.toPlainString(),
                        percentage -> percentage.compareTo(BigDecimal.ZERO) < 0
                                || percentage.compareTo(remainingSharePercentage) > 0));

        super.enter();
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Director information:",
                super.toString(),
                "Shares/Stocks Percentage: " + sharePercentage.multiply(BigDecimal.valueOf(100)).toPlainString() + "%");
    }

    public BigDecimal calculateMonthlyIncome(Company company) {
        if (company.getMonthlyIncome() == null) {
            interactor.displayMessage(
                    "Cannot perform this action. Make sure you filled in the company's information before perform calculating Director's monthly income!\n");
            return null;
        }
        BigDecimal profitFromShare = sharePercentage.multiply(company.calculateProfits());
        return profitFromShare.add(calculateMonthlySalary());
    }
}
