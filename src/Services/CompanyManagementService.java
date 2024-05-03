package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import DataAccess.DataAccessable;
import Models.Personnel;

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
        boolean hasBeenRemoved = personnels.removeIf(p -> p.getId() == personnelId);
        if (hasBeenRemoved) {
            savePersonnels(personnels);
        }
        return hasBeenRemoved;
    }

    public Personnel findPersonnel(Predicate<Personnel> predicate) {
        Optional<Personnel> p = personnels.stream().filter(predicate).findFirst();
        return p.equals(null) ? null : p.get();
    }

    public <T extends Personnel> List<T> getListOf(Class<T> tClass) {
        List<T> list = new ArrayList<>();
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
        List<T> list = new ArrayList<>();
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
        System.out.printf("%-40s | %-30s | %-10s | %-10s\n", "ID", "Fullname", "Phone Number", "Gender");
        for (Personnel p : personnels) {
            System.out.printf("%-40s | %-30s | %-10s | %-10s\n", p.getId(), p.getFullName(), p.getPhoneNumber(),
                    p.getGender());
        }
    }
}