package Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import Models.Personnel;

public interface CompanyService {
    List<Personnel> getPersonnels();

    void savePersonnels(List<Personnel> personnels);

    void addPersonnel(Personnel personnel);

    Class<?> findPersonnelType(Predicate<Personnel> predicate);

    boolean removePersonnel(String personnelId);

    Personnel findPersonnel(Predicate<Personnel> predicate);

    <T extends Personnel> List<T> getListOf(Class<T> tClass);

    <T extends Personnel> List<T> getListOf(Class<T> tClass, Predicate<T> predicate);

    <T extends Personnel> void displayTableOfPersonnels(String title, List<T> personnels);

    BigDecimal getRemainingSharePercentage();

    void setRemainingSharePercentage(BigDecimal remainingSharePercentage);
}
