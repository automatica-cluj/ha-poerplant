/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.services;

import homeautomation.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import homeautomation.auroraclient.client.EnumQueryParamSampleSize;
import homeautomation.auroraclient.client.FimerPowerPlantClient;
import homeautomation.schedulers.ScheduledTasks;
import java.time.Instant;
import javax.annotation.PostConstruct;
import kong.unirest.HttpResponse;
import lombok.Getter;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 * @author mihai
 */

@Service
public class PowerPlantService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
    
    @Getter
    @Value("${fimer.plant.id}")
    private String plantId;
    
    @Getter
    @Value("${fimer.device.id}")
    private String deviceId;
    
    private FimerPowerPlantClient fimerClient; 
    
    @Autowired 
    private TelemetryService telemetryService;
    
    @Autowired
    private Environment environment;
    
    @PostConstruct
    public void init() {            
        LOG.info("Fimer client loaded."+System.getenv("fimer.api.key")+" "+System.getenv("fimer.api.basicauth")); 
        fimerClient = 
                new FimerPowerPlantClient(environment.getProperty("fimer.api.key"), plantId, deviceId, environment.getProperty("fimer.api.basicauth"));    
        LOG.info("Fimer client loaded.");      
    }
    
    public void collectDailyGeneratedPower(Long startDate, Long endDate, EnumQueryParamSampleSize sampleSize){
        
        if(!fimerClient.isAuthenticated()){
                    fimerClient.authorize();
        }
        else{
                    HttpResponse<FimerResponseTelemetryTimeseries> response =
                            fimerClient.getTelemetryTimeseriesGenerationPowerData(
                                    sampleSize,
                                    startDate,
                                    endDate);
                    LOG.info("RESPONSE="+response.getBody());
                    telemetryService.writeTelemtryData(
                            Long.valueOf(fimerClient.getDeviceId()),
                            "power",
                            sampleSize,
                            Optional.ofNullable(response.getBody().getResult()));
        }
    }
//       public boolean requiresUpdated(){
//        
//    }

    
}
