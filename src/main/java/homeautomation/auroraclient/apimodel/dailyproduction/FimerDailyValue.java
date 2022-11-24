/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.auroraclient.apimodel.dailyproduction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author mihai
 */

@Getter @Setter @ToString
public class FimerDailyValue {
    public long date;
    public double value;
}
