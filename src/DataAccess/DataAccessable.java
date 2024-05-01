package DataAccess;

import java.util.List;

public interface DataAccessable<T> {
    List<T> getAll();

    void writeAll(List<T> elements);
}
