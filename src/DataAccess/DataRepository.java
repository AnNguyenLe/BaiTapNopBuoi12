package DataAccess;

import java.util.List;

public class DataRepository<T> implements DataAccessable<T> {
    private List<T> elements;

    public List<T> getAll() {
        return getElements();
    }

    public void writeAll(List<T> elements) {
        setElements(elements);
    }

    private List<T> getElements() {
        return elements;
    }

    private void setElements(List<T> elements) {
        this.elements = elements;
    }
}
