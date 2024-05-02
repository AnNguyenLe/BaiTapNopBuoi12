package Services.CompanyManagement;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import DataAccess.DataAccessable;
import Models.Personnel.DepartmentManager;
import Models.Personnel.Employee;
import Models.Personnel.Personnel;

public class CompanyManagementService implements CompanyService {
    private List<Personnel> personnels;

    private DataAccessable<Personnel> dataRepository;

    public CompanyManagementService(DataAccessable<Personnel> dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Personnel> getPersonnels() {
        return personnels;
    }

    public void savePersonnels(List<Personnel> personnels) {
        this.personnels = personnels;
        dataRepository.writeAll(personnels);
    }

    public Class<?> findPersonnelType(Predicate<Personnel> predicate) {
        Optional<Personnel> p = personnels.stream().filter(predicate).findFirst();
        return p.equals(null) ? null : p.getClass();
    }

    public boolean removePersonnel(String personnelId) {
        return personnels.removeIf(p -> p.getId() == personnelId);
    }

    public Personnel findPersonnel(Predicate<Personnel> predicate) {
        Optional<Personnel> p = personnels.stream().filter(predicate).findFirst();
        return p.equals(null) ? null : p.get();
    }

    public void EnterCompanyInfo() {

    }

    @Override
    public void assignEmployeeAsDepartmentManager(Employee employee, DepartmentManager dm) {
        boolean hasBeenRemoved = personnels.removeIf(p -> p.getId().equals(employee.getId()));
        if (!hasBeenRemoved) {
            return;
        }

        personnels.add(dm);
        savePersonnels(personnels);
    }
}