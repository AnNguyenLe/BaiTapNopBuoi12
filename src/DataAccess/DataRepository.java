package DataAccess;

import java.util.ArrayList;
import java.util.List;

public class DataRepository<T> implements DataAccessable<T> {
    private List<T> elements = new ArrayList<>();

    public List<T> getAll() {
        return elements;
    }

    public void writeAll(List<T> elements) {
        if (elements == null) {
            return;
        }
        this.elements = elements;
    }
}
