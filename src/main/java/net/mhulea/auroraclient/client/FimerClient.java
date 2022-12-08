/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.auroraclient.client;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import net.mhulea.auroraclient.apimodel.FimerAutheticationResponse;
import net.mhulea.auroraclient.apimodel.dailyproduction.FimerResponse;
import net.mhulea.auroraclient.apimodel.plant.FimerPlant;
import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryAggregated;
import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import kong.unirest.Unirest;
import net.mhulea.auroraclient.exception.FimerClinetException;
import net.mhulea.auroraclient.util.EnumPathParamMeasurementType;
import net.mhulea.auroraclient.util.EnumQueryParamSampleSize;
import net.mhulea.auroraclient.util.FimerMeasurementTypesValueTypes;
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
     */
    public FimerClient() {
        this(System.getenv().get("fimer.api.key"),
                System.getenv().get("fimer.api.basicauth")
        );
        LOG.info("Creating new Aurora client with " + System.getenv().get("fimer.api.key") + " and " + System.getenv().get("fimer.api.basicauth"));
    }

    public void authenticate() throws FimerClinetException {
        HttpResponse<FimerAutheticationResponse> response = Unirest.get("https://api.auroravision.net/api/rest/authenticate")
                .header("X-AuroraVision-ApiKey", auroraApiKey)
                .header("Accept", "application/json")
                .header("Authorization", this.basicAuth)
                .asObject(FimerAutheticationResponse.class);

        Error er = response.mapError(Error.class);
        if (er == null)
            auroraApiToken = response.getBody().getResult();
        else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().getResult(),er);

        LOG.info("Authentication response:" + response.toString());
    }

    public FimerResponse getDailyProduction(String plantId, Long startDate, Long endDate) throws FimerClinetException {
        if (!isAuthenticated())
            authenticate();
        HttpResponse<FimerResponse> response = Unirest.get("https://api.auroravision.net/api/rest/v1/plant/" + plantId + "/dailyProduction?startDate=" + startDate + "&endDate=" + endDate)
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponse.class);

        Error er = response.mapError(Error.class);
        if (er == null)
            return response.getBody();
        else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().getResult(),er);

    }

    public FimerResponseTelemetryAggregated getTelemetryAvarageGenerationPowerData(String plantId, Long startDate, Long endDate) throws FimerClinetException {
        if (!isAuthenticated())
            authenticate();
        HttpResponse<FimerResponseTelemetryAggregated> response = Unirest.get("https://api.auroravision.net/api/rest/v1/stats/power/aggregated/" + plantId + "/GenerationPower/maximum?" + "startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", this.auroraApiToken)
                .asObject(FimerResponseTelemetryAggregated.class);

        Error er = response.mapError(Error.class);
        if (er == null)
            return response.getBody();
        else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().getResult(),er);

    }

    public FimerResponseTelemetryTimeseries getTelemetryTimeseriesDataGeneric(String deviceId, String measurementType, String valueType, String dataType, String sampleSize, Long startDate, Long endDate) throws FimerClinetException {
        if (!isAuthenticated())
            authenticate();
        if (!FimerMeasurementTypesValueTypes.isValueTypeValid(valueType) || !FimerMeasurementTypesValueTypes.isMeasurementDataPairValid(measurementType, dataType))
            throw new MeasurementsAppException("Request cannot be complete. Wrong parameters provided.");

        HttpResponse<FimerResponseTelemetryTimeseries> response = Unirest.get("https://api.auroravision.net/api/rest/v1/stats/{measurementType}/timeseries/{entityId}/{dataType}/{valueType}?sampleSize=" + sampleSize + "&startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", auroraApiToken)
                .routeParam("measurementType", measurementType)
                .routeParam("entityId", deviceId)
                .routeParam("valueType", valueType)
                .routeParam("dataType", dataType)
                .asObject(FimerResponseTelemetryTimeseries.class);

        Error er = response.mapError(Error.class);
        if (er == null)
            return response.getBody();
        else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().getResult(),er);
    }

    public FimerResponseTelemetryTimeseries getTelemetryTimeseriesData(String deviceId, EnumPathParamMeasurementType dataType, EnumQueryParamSampleSize sampleSize, Long startDate, Long endDate) throws FimerClinetException {
        if (!isAuthenticated())
            authenticate();

        HttpResponse<FimerResponseTelemetryTimeseries> response = Unirest.get("https://api.auroravision.net/api/rest/v1/stats/" + dataType + "/timeseries/" + deviceId.toString() + "/GenerationPower/average?sampleSize=" + sampleSize + "&startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponseTelemetryTimeseries.class);

        Error er = response.mapError(Error.class);
        if (er == null)
            return response.getBody();
        else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().getResult(),er);
    }

    public FimerPlant getPlantDetails(String plantId) throws FimerClinetException {
        if (!isAuthenticated())
            authenticate();

        HttpResponse<Map> response = Unirest.get("https://api.auroravision.net/api/rest/v1/plant/" + plantId + "/info")
                .header("X-AuroraVision-Token", auroraApiToken)
                .header("Accept", "application/json")
                .asObject(i -> new Gson().fromJson(i.getContentReader(), HashMap.class));

        Error er = response.mapError(Error.class);
        if (er == null) {
            Map r = (Map) response.getBody().get("result");
            FimerPlant plant = new FimerPlant();
            plant.setPlantName(r.get("plantName").toString());
            plant.setPlantState(r.get("plantState").toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a Z");
            String str = ((Map) r.get("plantConfiguration")).get("firstReportedDate").toString();
            LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
            plant.setFirstReportedDate(dateTime);
            return plant;
        } else
            throw new FimerClinetException(response.getStatus()+":"+response.getBody().toString(),er);
    }

    public boolean isAuthenticated() {
        return auroraApiToken != null;
    }

}
