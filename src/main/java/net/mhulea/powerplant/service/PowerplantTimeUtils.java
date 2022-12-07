/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.powerplant.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mihai
 */
public class PowerplantTimeUtils {
    
    /**
     * Convert a LongDate into standard date format accepted by Aurora API. 
     * @param date
     * @return 
     */
    public static Long getFormatedDate(LocalDate date){
        return Long.valueOf(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    public static Long getFormatedDate(LocalDateTime date){
        return Long.valueOf(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
    public static Long getToday(){
        return Long.valueOf(getFormatedDate(LocalDate.now()));
    }

    public static Long getDayRelativeToToday(int plusDays){
        if(plusDays>=0)
            return Long.valueOf(getFormatedDate(LocalDate.now().plusDays(plusDays)));
        else
            return Long.valueOf(getFormatedDate(LocalDate.now().minusDays(plusDays)));

    }

    public static Long getDayFromUnixEpoch(Long dateSeconds){
        LocalDate date =
                Instant.ofEpochSecond(dateSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
        return Long.valueOf(getFormatedDate(date));
    }
}
