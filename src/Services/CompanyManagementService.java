package Services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import CustomExceptions.NegativeNumberException;
import DataAccess.DataAccessable;
import Models.Personnel;

public class CompanyManagementService implements CompanyService {
    private DataAccessable<Personnel> dataRepository;
    private BigDecimal remainingSharePercentage = BigDecimal.ONE;

    public CompanyManagementService(DataAccessable<Personnel> dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Personnel> getPersonnels() {
        return dataRepository.getAll();
    }

    public void savePersonnels(List<Personnel> personnels) {
        if (personnels == null) {
            return;
        }
        dataRepository.writeAll(personnels);
    }

    public void addPersonnel(Personnel personnel) {
        List<Personnel> personnels = getPersonnels();
        personnels.add(personnel);
    }

    public Class<?> findPersonnelType(Predicate<Personnel> predicate) {
        Optional<Personnel> p = getPersonnels().stream().filter(predicate).findFirst();
        return p.equals(null) ? null : p.getClass();
    }

    public boolean removePersonnel(String personnelId) {
        List<Personnel> personnels = getPersonnels();
        boolean hasBeenRemoved = personnels.removeIf(p -> p.getId() == personnelId);
        if (hasBeenRemoved) {
            savePersonnels(personnels);
        }
        return hasBeenRemoved;
    }

    public Personnel findPersonnel(Predicate<Personnel> predicate) {
        Personnel p = getPersonnels().stream().filter(predicate).findFirst().orElse(null);
        if (p == null) {
            return null;
        }
        return p;
    }

    public <T extends Personnel> List<T> getListOf(Class<T> tClass) {
        List<Personnel> personnels = getPersonnels();
        List<T> list = new ArrayList<>();
        if (personnels == null || personnels.size() == 0) {
            return list;
        }
        T t;
        for (Personnel personnel : personnels) {
            if (tClass.isInstance(personnel)) {
                t = tClass.cast(personnel);
                list.add(t);
            }
        }

        return list;
    }

    public <T extends Personnel> List<T> getListOf(Class<T> tClass, Predicate<T> predicate) {
        List<Personnel> personnels = getPersonnels();
        List<T> list = new ArrayList<>();
        if (personnels == null || personnels.size() == 0) {
            return list;
        }
        T t;
        for (Personnel personnel : personnels) {
            if (tClass.isInstance(personnel)) {
                t = tClass.cast(personnel);
                if (predicate.test(t)) {
                    list.add(t);
                }
            }
        }

        return list;
    }

    public <T extends Personnel> void displayTableOfPersonnels(String title, List<T> personnels) {
        System.out.println("\n" + title + "\n");
        System.out.printf("%-40s | %-30s | %-20s | %-30s | %-40s\n", "ID", "Fullname", "Phone Number", "Gender",
                "Monthly Salary");
        for (Personnel p : personnels) {
            System.out.printf("%-40s | %-30s | %-20s | %-30s | %-40s\n",
                    p.getId(),
                    p.getFullName(),
                    p.getPhoneNumber(),
                    p.getGender(),
                    p.calculateMonthlySalary());
        }
    }

    public BigDecimal getRemainingSharePercentage() {
        return remainingSharePercentage;
    }

    public void setRemainingSharePercentage(BigDecimal remainingSharePercentage) {
        if (remainingSharePercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeNumberException("Company's remaining share percentage");
        }
        if (remainingSharePercentage.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Remaining share percentage cannot be larger than 1!");
        }
        this.remainingSharePercentage = remainingSharePercentage;
    }
}