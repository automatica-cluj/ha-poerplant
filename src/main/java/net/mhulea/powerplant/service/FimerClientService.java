package net.mhulea.powerplant.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.mhulea.auroraclient.apimodel.plant.FimerPlant;
import net.mhulea.auroraclient.client.FimerClient;
import net.mhulea.auroraclient.exception.FimerClinetException;
import net.mhulea.powerplant.entity.Device;
import net.mhulea.powerplant.entity.Plant;
import net.mhulea.powerplant.repository.DeviceRepository;
import net.mhulea.powerplant.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Getter
@Service
public class FimerClientService {
    private FimerClient fimerClient;

    @Value("${fimer.plant.id}")
    private String plantId;

    @Value("${fimer.device.id}")
    private String deviceId;

    @Autowired
    private PlantRepository plantRepo;

    @Autowired
    private DeviceRepository devRepo;

    /**
     * Populate database with plant and device entities if not exists.
     */
    @PostConstruct
    public void init() {
        fimerClient = new FimerClient();

        try {

            FimerPlant r = fimerClient.getPlantDetails(plantId);

            Plant plant = plantRepo.getByExternalId(Long.valueOf(plantId)).
                    orElseGet(() -> {
                        Plant l = new Plant();
                        l.setExternalId(Long.valueOf(plantId));
                        l.setPlantName(r.getPlantName());
                        l.setPlantState(r.getPlantState());
                        l.setFirstReportedDate(r.getFirstReportedDate());
                        return plantRepo.save(l);
                    });

            devRepo.getByExternalId(Long.valueOf(deviceId))
                    .orElseGet(() -> {
                        Device l = new Device();
                        l.setExternalId(Long.valueOf(deviceId));
                        plant.addDevice(l);
                        plantRepo.save(plant);
                        return l;
                    });
        } catch (FimerClinetException e) {
            throw new RuntimeException(e);
        }
    }

}
