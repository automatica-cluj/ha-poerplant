package net.mhulea.powerplant;

import net.mhulea.auroraclient.apimodel.plant.FimerPlant;
import net.mhulea.auroraclient.client.FimerClient;
import net.mhulea.auroraclient.exception.FimerClinetException;
import net.mhulea.powerplant.repository.DeviceRepository;
import net.mhulea.powerplant.repository.TelemetryTimeseriesRepository;
import net.mhulea.powerplant.service.PowerPlantService;
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
    public void testAuth() throws FimerClinetException {
        FimerClient client = new FimerClient();
        System.out.println(client.toString());
        assertNotNull(client);
        client.authenticate();
        assertTrue(client.isAuthenticated());
    }

    @Test
    public void testGetPlantDetails() throws FimerClinetException {
        FimerClient client = new FimerClient();
        client.authenticate();
        FimerPlant plant = client.getPlantDetails("33228238");
        assertNotNull(plant.getFirstReportedDate());
        System.out.println(plant);
    }

}
