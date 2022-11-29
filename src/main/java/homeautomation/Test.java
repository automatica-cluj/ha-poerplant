/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mihai
 */
public class Test {
    public static void main(String[] args) {
       LocalDate localDate = LocalDate.now();
       String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
       System.out.println(formattedDate);
       localDate = localDate.plusDays(1);
       formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
       System.out.println(formattedDate); 
    }
}
