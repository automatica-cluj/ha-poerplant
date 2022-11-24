/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.auroraclient.client;

/**
 * Min5, Min15, Hour, Day, Month, Year
 * @author mihai
 */
public enum EnumQueryParamSampleSize {
    MIN5("Min5"),
    MIN15("Min15"),
    HOUR("Hour"),
    DAY("Day"),
    MONTH("Month"),
    YEAR("Year")    
    ;

    private final String text;

    /**
     * @param text
     */
    EnumQueryParamSampleSize(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}