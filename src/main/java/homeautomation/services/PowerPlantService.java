/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.services;

import homeautomation.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import homeautomation.auroraclient.client.EnumQueryParamSampleSize;
import homeautomation.auroraclient.client.FimerPowerPlantClient;
import homeautomation.schedulers.ScheduledTasks;
import javax.annotation.PostConstruct;
import kong.unirest.HttpResponse;
import lombok.Getter;

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
    
//    @Value("${fimer.api.key}")
//    private String auroraApiKey;
    
    @Getter
    @Value("${fimer.plant.id}")
    private String plantId;
    
    @Getter
    @Value("${fimer.device.id}")
    private String deviceId;
    
//    @Value("${fimer.api.basicauth}")
//    private String basicAuth;
    
    private FimerPowerPlantClient fimerClient; 
    
    @Autowired 
    private TelemetryService telemetryService;
    
    @Autowired
    private Environment environment;
    
    @PostConstruct
    public void init() {        
        
        String x = environment.getProperty("fimer.api.key");
        LOG.info("Fimer client loaded.");      
        fimerClient = 
                new FimerPowerPlantClient(environment.getProperty("fimer.api.key"), plantId, deviceId, environment.getProperty("fimer.api.basicauth"));    
        LOG.info("Fimer client loaded.");      
    }
    
    public void collectDailyGeneratedPower(){
        if(!fimerClient.isAuthenticated()){
                    fimerClient.authorize();
        }
        else{
                    HttpResponse<FimerResponseTelemetryTimeseries> response = fimerClient.getTelemetryTimeseriesGenerationPowerData(EnumQueryParamSampleSize.HOUR,20221122L, 20221123L);
                    LOG.info("RESPONSE="+response.getBody());
                    telemetryService.writeTelemtryData(Long.valueOf(fimerClient.getDeviceId()), "power", response.getBody().getResult());
        }
    }

    
}
