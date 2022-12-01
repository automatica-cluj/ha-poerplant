/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.auroraclient.client;

import homeautomation.auroraclient.apimodel.FimerAutheticationResponse;
import homeautomation.auroraclient.apimodel.dailyproduction.FimerResponse;
import homeautomation.auroraclient.apimodel.telemetry.FimerResponseTelemetryAggregated;
import homeautomation.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;

/**
 *
 * @author mihai
 */

public class FimerPowerPlantClient {

    private String auroraApiToken;
    private String auroraApiKey;
    @Getter
    private String plantId;
    @Getter
    private String deviceId;
    private String basicAuth;

    public FimerPowerPlantClient(String auroraApiKey, String plantId, String deviceId, String basicAuth) {
        this.auroraApiKey = auroraApiKey;
        this.plantId = plantId;
        this.deviceId = deviceId;
        this.basicAuth = basicAuth;
    }

    public void authorize() {
        FimerAutheticationResponse response = Unirest.get("https://api.auroravision.net/api/rest/authenticate")
                .header("X-AuroraVision-ApiKey", auroraApiKey)
                .header("Accept", "application/json")
                .header("Authorization", this.basicAuth)
                .asObject(FimerAutheticationResponse.class)
                .getBody();
        auroraApiToken = response.getResult();
        System.out.println(response.toString());
    }

    public HttpResponse<FimerResponse> getDailyProduction(Long startDate, Long endDate) {
        //Unirest.setTimeouts(0, 0);
        return Unirest.get("https://api.auroravision.net/api/rest/v1/plant/" + plantId + "/dailyProduction?startDate=" + startDate + "&endDate=" + endDate)
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponse.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        System.out.println("ERRRO AUTHORIZATION");
                    }
                });
    }

    public HttpResponse<FimerResponseTelemetryAggregated> getTelemetryAvarageGenerationPowerData(Long startDate, Long endDate) {
        //Unirest.setTimeouts(0, 0);
        return Unirest.get("https://api.auroravision.net/api/rest/v1/stats/power/aggregated/"+this.plantId+"/GenerationPower/maximum?" + "startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", this.auroraApiToken)
                .asObject(FimerResponseTelemetryAggregated.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        System.out.println("ERRRO AUTHORIZATION");
                    } else {
                        System.out.println("UNEXPECTED ERROR");
                    }
                });
    }

    public HttpResponse<FimerResponseTelemetryTimeseries> getTelemetryTimeseriesData(EnumPathParamDataType dataType, EnumQueryParamSampleSize sampleSize, Long startDate, Long endDate) {
        //Unirest.setTimeouts(0, 0);
        return Unirest.get("https://api.auroravision.net/api/rest/v1/stats/"+dataType.toString()+"/timeseries/"+deviceId+"/GenerationPower/average?sampleSize=" + sampleSize + "&startDate=" + startDate + "&endDate=" + endDate + "&timeZone=Europe/Rome")
                .header("X-AuroraVision-Token", auroraApiToken)
                .asObject(FimerResponseTelemetryTimeseries.class)
                .ifFailure(Error.class, r -> {
                    if (r.getStatus() == 401) {
                        auroraApiToken = null;
                        System.out.println("ERRRO AUTHORIZATION");
                    } else {
                        System.out.println("UNEXPECTED ERROR " + r.getStatus() + " " + r.getStatusText());
                    }
                });
    }

    public boolean isAuthenticated() {
        return auroraApiToken != null;
    }

}
