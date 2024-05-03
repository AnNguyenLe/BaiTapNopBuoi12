package Comparators;

import java.util.Comparator;

import Models.Personnel;

public class SortByMonthSalaryDesc implements Comparator<Personnel> {
    public int compare(Personnel a, Personnel b) {
        return b.calculateMonthlySalary().compareTo(a.calculateMonthlySalary());
    }
}
