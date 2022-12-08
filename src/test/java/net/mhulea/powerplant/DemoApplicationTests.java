package net.mhulea.powerplant;

import net.mhulea.powerplant.dto.DeviceDto;
import net.mhulea.powerplant.dto.MapperDevice;
import net.mhulea.powerplant.dto.MapperPlant;
import net.mhulea.powerplant.dto.PlantDto;
import net.mhulea.powerplant.entity.Device;
import net.mhulea.powerplant.entity.Plant;
import net.mhulea.powerplant.repository.DeviceRepository;
import net.mhulea.powerplant.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private PlantRepository plantRepo;

    @Autowired
    private DeviceRepository devRepo;


    @Test
    void addPlantWithDevice(){
        Plant p = new Plant();
        p.setExternalId(1L);
       // p.setId(6L);
        //p.ser("Mihai");

        Device d = new Device();
        d.setExternalId(1L);
        //.setType("Inverter");

        p.getDevices().add(d);
        d.setPlant(p);
        plantRepo.save(p);

    }

    @Test
    void mapperTest1() {


        PlantDto pdto = new PlantDto();//2L,"ABC");
        pdto.setId(2L);
        pdto.setOwner("ABC");

        Plant plant = MapperPlant.INSTANCE.plantDtoToPlant(pdto);

        plant = plantRepo.save(plant);

        assertTrue(plant.getId()==pdto.getId());
    }

    @Test
    void mapperTest2(){
        Plant p = plantRepo.findById(1L).get();

        DeviceDto ddto = new DeviceDto();

        ddto.setPlant(MapperPlant.INSTANCE.plantToPlantDto(p));
        ddto.setId(123L);
        Device d = MapperDevice.INSTANCE.deviceDtoToDevice(ddto);

        assertNotNull(devRepo.save(d));

    }

    @Test
    void getEntity(){

        Device d = devRepo.findById(123L).get();
        System.out.println(d.toString());
        DeviceDto ddto = MapperDevice.INSTANCE.deviceToDeviceDto(d);
        System.out.println(ddto);
        assertNotNull(ddto.getId());
    }


}
