/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.auroraclient.client;

import com.google.gson.Gson;
import net.mhulea.auroraclient.apimodel.FimerAutheticationResponse;
import net.mhulea.auroraclient.apimodel.dailyproduction.FimerResponse;
import net.mhulea.auroraclient.apimodel.plant.FimerPlant;
import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryAggregated;
import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;
import net.mhulea.powerplant.exceptions.MeasurementsAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulate logic for calling Aurora REST API.
 *
 * @author mihai
 */

public class FimerClient {

    private static final Logger LOG = LoggerFactory.getLogger(FimerClient.class);

    /**
     * Token (static) common to all client instances considering that all instances are handling the same user.
     * Each client instance will be used to communicate with a separate device.
     */
    private static String auroraApiToken;
    private String auroraApiKey;

    private String basicAuth;

    @Autowired
    private Environment environment;

    public FimerClient(String auroraApiKey, String basicAuth) {
        this.auroraApiKey = auroraApiKey;
        this.basicAuth = basicAuth;
    }

    /**
     * Create Fimer client loading API key and auth from env variable.
     *
     */
    public FimerClient() {
        this(System.getenv().get("fimer.api.key"),
                System.getenv().get("fimer.api.basicauth")
                );
        LOG.info("Creating new Aurora client with "+System.getenv().get("fimer.api.key")+" and "+System.getenv().get("fimer.api.basicauth"));
    }

    public void authenticate() {
        FimerAutheticationResponse response = Unirest.get("https://api.auroravision.net/api/rest/authenticate")
                .header("X-AuroraVision-ApiKey", auroraApiKey)
                .header("Accept", "application/json")
                .header("Authorization", this.basicAuth)
                .asObject(FimerAutheticationResponse.class)
                .getBody();
        auroraApiToken = response.getResult();

        LOG.info("Authentication response:"+response.toString());
    }

    public FimerResponse getDailyProduction(String plantId, Long startDate, Long endDate) {
        if(!isAuthenticated())
            authenticate();
        return Unirest.get("https://api.auroravision.net/api/rest/v1/plant/" + plantId + "/dailyProduction?startDate=" + startDate + "&endDate=" + endDate)
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponse.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        System.out.println("ERR AUTHORIZATION");
                    }
                }).getBody();
    }

    public FimerResponseTelemetryAggregated getTelemetryAvarageGenerationPowerData(String plantId, Long startDate, Long endDate) {
        if(!isAuthenticated())
            authenticate();
        return Unirest.get("https://api.auroravision.net/api/rest/v1/stats/power/aggregated/"+plantId+"/GenerationPower/maximum?" + "startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", this.auroraApiToken)
                .asObject(FimerResponseTelemetryAggregated.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        LOG.info("ERR AUTHORIZATION");
                    } else {
                        LOG.info("UNEXPECTED ERROR");
                    }
                }).getBody();
    }

    public FimerResponseTelemetryTimeseries getTelemetryTimeseriesData(String deviceId, EnumPathParamDataType dataType, EnumQueryParamSampleSize sampleSize, Long startDate, Long endDate) {
        if(!isAuthenticated())
            authenticate();
        return Unirest.get("https://api.auroravision.net/api/rest/v1/stats/"+dataType+"/timeseries/"+deviceId.toString()+"/GenerationPower/average?sampleSize=" + sampleSize + "&startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponseTelemetryTimeseries.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        LOG.info("ERR AUTHORIZATION");
                    } else {
                        LOG.info("UNEXPECTED ERROR " + r.getStatus() + " " + r.getStatusText());
                    }
                }).getBody();
    }

    public FimerPlant getPlantDetails(String plantId){
        if(!isAuthenticated())
            authenticate();
        Map r = Unirest.get("https://api.auroravision.net/api/rest/v1/plant/"+plantId+"/info")
                .header("X-AuroraVision-Token", auroraApiToken)
                .header("Accept", "application/json")
                .asObject(i -> new Gson().fromJson(i.getContentReader(), HashMap.class))
                .getBody();
        if(r!=null) {
            r = (Map) r.get("result");
            FimerPlant plant = new FimerPlant();
            plant.setPlantName(r.get("plantName").toString());
            plant.setPlantState(r.get("plantState").toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a Z");
            String str =( (Map) r.get("plantConfiguration")).get("firstReportedDate").toString();
            LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
            plant.setFirstReportedDate(dateTime);
            return plant;
        }else{
            throw new MeasurementsAppException("Error getting plant info details.");
        }

    }

    public boolean isAuthenticated() {
        return auroraApiToken != null;
    }

}
