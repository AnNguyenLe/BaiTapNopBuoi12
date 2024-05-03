package Comparators;

import java.util.Comparator;

import Models.Personnel;

public class SortByName implements Comparator<Personnel> {
    public int compare(Personnel a, Personnel b) {
        return a.getFullName().compareTo(b.getFullName());
    }
}
