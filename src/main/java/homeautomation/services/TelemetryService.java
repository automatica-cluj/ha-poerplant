/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.services;

import homeautomation.auroraclient.apimodel.telemetry.FimerTelemetryTimeseriesData;
import homeautomation.entity.Device;
import homeautomation.entity.TelemetryTimeseriesData;
import homeautomation.repository.DeviceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import homeautomation.repository.TelemetryTimeseriesRepository;
import java.util.Iterator;

/**
 *
 * @author mihai
 */
@Service
public class TelemetryService {
    
    @Autowired
    private TelemetryTimeseriesRepository repo;
    
    @Autowired 
    private DeviceRepository devRepo;
    
    public void writeTelemtryData(Long deviceId, String dataType, List<FimerTelemetryTimeseriesData> data){
        Device device = devRepo.findById(deviceId).get();
        System.out.println(device);
        Iterator<TelemetryTimeseriesData> i = data.stream().map(
               d -> {
                   TelemetryTimeseriesData r = new TelemetryTimeseriesData();
                   r.setStart(d.getStart());
                   r.setUnits(d.getUnits());
                   r.setValue(d.getValue());
                   r.setDataType(dataType);
                   r.setDevice(device);
                  
                           return r;})
               .iterator();
       while(i.hasNext())
           repo.save(i.next());
    }
    
}
