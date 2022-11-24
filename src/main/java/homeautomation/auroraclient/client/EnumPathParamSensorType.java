/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.auroraclient.client;

/**
 *
 * @author mihai
 */
enum EnumPathParamSensorType {
    POWER("power"),
    ENERGY("energy"),
    CURRENT("current"),
    VOLTAGE("voltage"),
    TEMPERATURE("temperature")
    ;


    private final String text;

    /**
     * @param text
     */
    EnumPathParamSensorType(final String text) {
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