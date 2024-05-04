package Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import CustomExceptions.NegativeNumberException;
import CustomExceptions.NullOrEmptyStringException;
import Extensions.StringExtensions;
import Services.CompanyService;
import UserInteractor.Interactable;

enum Gender {
    Male,
    Female,
    Others
}

public abstract class Personnel {
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final int DAY_IN_CURRENT_MONTH = java.time.LocalDate.now().lengthOfMonth();

    private String id;
    private String fullName;
    private int yearOfBirth;
    private long phoneNumber;
    private Gender gender;
    private BigDecimal dailySalary = Constants.EMPLOYEE_DAILY_SALARY;
    private int noOfWorkingDays;
    private boolean isDeptManager;
    private boolean isDirector;

    private final int minYoB = CURRENT_YEAR - Constants.CURRENT_HUMAN_AGE_LIMIT;
    private final int maxYoB = CURRENT_YEAR;

    protected Interactable interactor;
    protected CompanyService service;

    public Personnel(Interactable interactor, CompanyService service) {
        setId();
        this.interactor = interactor;
        this.service = service;
    }

    public String getId() {
        return id;
    }

    private void setId() {
        id = UUID.randomUUID().toString();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (StringExtensions.isNullOrEmpty(fullName)) {
            throw new NullOrEmptyStringException("Full Name");
        }
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        if (yearOfBirth < minYoB || yearOfBirth > maxYoB) {
            throw new IllegalArgumentException("Year of birth must be in the range from " + minYoB + " and " + maxYoB);
        }
        this.yearOfBirth = yearOfBirth;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        if (phoneNumber < 0) {
            throw new NegativeNumberException("Phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return genderMapperEnumToString(gender);
    }

    public void setGender(String gender) {
        this.gender = genderMapperStringToEnum(gender);
    }

    public BigDecimal getDailySalary() {
        return dailySalary;
    }

    public void setDailySalary(BigDecimal dailySalary) {
        if (dailySalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeNumberException("Daily Salary");
        }
        this.dailySalary = dailySalary;
    }

    public int getNoOfWorkingDays() {
        return noOfWorkingDays;
    }

    public void setNoOfWorkingDays(int noOfWorkingDays) {
        if (noOfWorkingDays < 0) {
            throw new NegativeNumberException("Number of working days");
        }

        if (noOfWorkingDays > DAY_IN_CURRENT_MONTH) {
            throw new IllegalArgumentException("Total days of current month: " + DAY_IN_CURRENT_MONTH +
                    "Number of working days for this month cannot exceed " + DAY_IN_CURRENT_MONTH + "days!");
        }

        this.noOfWorkingDays = noOfWorkingDays;
    }

    @Override
    public String toString() {
        return String.join("\n",
                "Full Name: " + fullName,
                "Id: " + id,
                "Year of Birth: " + yearOfBirth,
                "Gender: " + genderMapperEnumToString(gender),
                "Daily Salary: " + dailySalary.toPlainString(),
                "Number of Working Days: " + noOfWorkingDays);
    }

    public void enter() {
        setFullName(
                interactor.readLine(
                        "Full Name: ",
                        "Full Name cannot be null or empty!",
                        str -> StringExtensions.isNullOrEmpty(str)));
        setYearOfBirth(
                interactor.readInt(
                        "Year of birth: ",
                        "Year of birth must be in the range from " + minYoB + " and " + maxYoB,
                        yearOfBirth -> yearOfBirth < minYoB || yearOfBirth > maxYoB));
        setPhoneNumber(
                interactor.readLong(
                        "Phone number: ",
                        "Phone number must be number AND must be a non-negative number!",
                        phoneNumber -> phoneNumber < 0));
        setGender(interactor.readLine("Gender (m/f/others): "));
        setDailySalary(
                interactor.readBigDecimal(
                        "Daily salary: ",
                        "Daily salary must be a non-negative number!",
                        dailySalary -> dailySalary.compareTo(BigDecimal.ZERO) < 0));
        setNoOfWorkingDays(
                interactor.readInt(
                        "Number of working days: ",
                        "Number of working must be between 1 - " + DAY_IN_CURRENT_MONTH,
                        noOfWorkingDays -> noOfWorkingDays < 1 || noOfWorkingDays > DAY_IN_CURRENT_MONTH));

        service.addPersonnel(this);
    }

    public BigDecimal calculateMonthlySalary() {
        return dailySalary.multiply(BigDecimal.valueOf(noOfWorkingDays));
    }

    public void delete() {
        // Remove this Personnel in data repository
        boolean hasBeenRemoved = service.removePersonnel(getId());
        if (hasBeenRemoved) {
            interactor.displayMessage(
                    "Personnel " + getFullName() + " with ID: " + getId() + " has been removed successfully!");
        }
    }

    private String genderMapperEnumToString(Gender gender) {
        return switch (gender) {
            case Male -> "Male";
            case Female -> "Female";
            default -> "Others/Prefer not to say";
        };
    }

    private Gender genderMapperStringToEnum(String genderStr) {
        if (StringExtensions.isNullOrEmpty(genderStr)) {
            return Gender.Others;
        }

        String valueStr = String.join("", genderStr.split("\\s+")).toLowerCase();

        String[] maleStrings = new String[] { "m", "male" };
        if (Arrays.stream(maleStrings).anyMatch(str -> str.equals(valueStr))) {
            return Gender.Male;
        }

        String[] femaleStrings = new String[] { "f", "female" };
        if (Arrays.stream(femaleStrings).anyMatch(str -> str.equals(valueStr))) {
            return Gender.Female;
        }

        return Gender.Others;
    }

    public boolean getIsDeptManager() {
        return isDeptManager;
    }

    protected void setIsDeptManager(boolean isDeptManager) {
        this.isDeptManager = isDeptManager;
    }

    public boolean getIsDirector() {
        return isDirector;
    }

    protected void setIsDirector(boolean isDirector) {
        this.isDirector = isDirector;
    }

}
