package com.formation.utils;
import java.time.LocalDate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class  DateUtils    {
    public static boolean isDateRangeValid(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }
    
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
