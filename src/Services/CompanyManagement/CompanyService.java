package Services.CompanyManagement;

import java.util.List;
import java.util.function.Predicate;

import Models.Personnel.Personnel;

public interface CompanyService {
    List<Personnel> getPersonnels();
    void savePersonnels(List<Personnel> personnels);
    Class<?> findPersonnelType(Predicate<Personnel> predicate);
    boolean removePersonnel(String personnelId);
    Personnel findPersonnel(Predicate<Personnel> predicate);
}
