package net.mhulea.powerplant;

import net.mhulea.auroraclient.apimodel.plant.FimerPlant;
import net.mhulea.auroraclient.client.EnumPathParamMeasurementType;
import net.mhulea.auroraclient.client.EnumQueryParamSampleSize;
import net.mhulea.auroraclient.client.FimerClient;
import net.mhulea.powerplant.repository.DeviceRepository;
import net.mhulea.powerplant.repository.TelemetryTimeseriesRepository;
import net.mhulea.powerplant.service.PowerPlantService;
import net.mhulea.powerplant.service.PowerplantTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuroraTest {

    @Autowired
    private TelemetryTimeseriesRepository teleRepo;

    @Autowired
    private DeviceRepository devRepo;

    @Autowired
    private PowerPlantService powerPlantService;

    @Test
    public void testAuth(){
        FimerClient client = new FimerClient();
        System.out.println(client.toString());
        assertNotNull(client);
        client.authenticate();
        assertTrue(client.isAuthenticated());
    }

    @Test
    public void testGetPlantDetails(){
        FimerClient client = new FimerClient();
        client.authenticate();
        FimerPlant plant = client.getPlantDetails("33228238");
        assertNotNull(plant.getFirstReportedDate());
        System.out.println(plant);
    }

//    @Test
//    public void testGetDeviceMeasurements(){
//
//        powerPlantService.collectTelemetryTimeseriesData(
//                PowerplantTimeUtils.getToday(),
//                PowerplantTimeUtils.getDayRelativeToToday(1),
//                EnumPathParamMeasurementType.POWER,
//                EnumQueryParamSampleSize.HOUR
//        );
//    }

//    @Test
//    public void testCollectInstallaitonToDate(){
//        powerPlantService.collectTelemetryTimeseriesInstallationToDate(
//                PowerplantTimeUtils.getDayRelativeToToday(1),
//                EnumPathParamMeasurementType.POWER,
//                EnumQueryParamSampleSize.DAY);
//    }
}
