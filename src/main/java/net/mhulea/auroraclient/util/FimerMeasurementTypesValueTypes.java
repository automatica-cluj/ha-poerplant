package net.mhulea.auroraclient.util;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Map.entry;

public class FimerMeasurementTypesValueTypes {
    //Power - GenerationPower, DCGenerationPower, Irradiance, GridPowerExport, StoredPower, ActivePowerEV
    //Energy - GenerationEnergy, DCGenerationEnergy, Insolation, StorageInEnergy, StorageOutEnergy, GridEnergyExport, GridEnergyImport, SelfConsumedEnergy, ActiveEnergyEV, SessionEnergyEV
    //Voltage - Voltage, DCVoltage
    //Frequency - LineFrequency
    //Wind - WindDirection, WindSpeed
    //Temperature - CellTemp, AmbientTemp
    //Current - Current, DCCurrent
    public static final Map<String, String[]> MEASUREMENT_DATA_TYPES = Map.ofEntries(
            entry("power", new String[]{"GenerationPower", "DCGenerationPower", "Irradiance", "GridPowerExport", "StoredPower", "ActivePowerEV"}),
            entry("energy", new String[]{"GenerationEnergy", "DCGenerationEnergy", "Insolation", "StorageInEnergy", "StorageOutEnergy", "GridEnergyExport", "GridEnergyImport", "SelfConsumedEnergy", "ActiveEnergyEV", "SessionEnergyEV"}),
            entry("voltage", new String[]{"Voltage", "DCVoltage"}),
            entry("frequency", new String[]{"LineFrequency"}),
            entry("wind", new String[]{"WindDirection", "WindSpeed"}),
            entry("temperature", new String[]{"CellTemp", "AmbientTemp"}),
            entry("current", new String[]{"Current", "DCCurrent"})
    );

    public static final String[] VALUE_TYPES={"maximum", "minimum", "average", "cumulative", "delta"};

    public static boolean isMeasurementDataPairValid(String measurementType, String dataType){
        return Arrays.stream(MEASUREMENT_DATA_TYPES.get(measurementType)).filter(Predicate.isEqual(dataType)).count()==1;
    }

    public static boolean isValueTypeValid(String valueType){
        return Arrays.stream(VALUE_TYPES).filter(Predicate.isEqual(valueType)).count()==1;
    }


    public static void main(String[] args) {
        System.out.println(FimerMeasurementTypesValueTypes.isMeasurementDataPairValid("wind","abc"));
        System.out.println(FimerMeasurementTypesValueTypes.isMeasurementDataPairValid("wind","WindDirection"));
    }


//    static enum Power {
//        GenerationPower, DCGenerationPower, Irradiance, GridPowerExport, StoredPower, ActivePowerEV;
//    }
//
//    static enum Energy {
//        GenerationEnergy, DCGenerationEnergy, Insolation, StorageInEnergy, StorageOutEnergy, GridEnergyExport, GridEnergyImport, SelfConsumedEnergy, ActiveEnergyEV, SessionEnergyEV;
//    }
}
