/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.services;

import homeautomation.auroraclient.apimodel.telemetry.FimerTelemetryTimeseriesData;
import homeautomation.auroraclient.client.EnumPathParamDataType;
import homeautomation.auroraclient.client.EnumQueryParamSampleSize;
import homeautomation.entity.Device;
import homeautomation.entity.TelemetryTimeseriesData;
import homeautomation.repository.DeviceRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import homeautomation.repository.TelemetryTimeseriesRepository;

import java.time.Instant;

/**
 * @author mihai
 */
@Service
public class TelemetryService {

    @Autowired
    private TelemetryTimeseriesRepository repo;

    @Autowired
    private DeviceRepository devRepo;

    public void writeTelemetryData(Long deviceId, EnumPathParamDataType dataType, EnumQueryParamSampleSize sampleSize, Optional<List<FimerTelemetryTimeseriesData>> data) {

        if (data.isPresent()) {

            //TODO now create device if not exists
            Device result = devRepo.findById(deviceId).orElseGet(() -> {
                Device x = new Device();
                x.setId(deviceId);
                return devRepo.save(x);
            });

            Iterator<TelemetryTimeseriesData> i = data.get().stream().map(
                            d -> {
                                TelemetryTimeseriesData r = new TelemetryTimeseriesData();
                                r.setStart(d.getStart());
                                r.setUnits(d.getUnits());
                                r.setValue(d.getValue());
                                r.setDataType(dataType.toString());
                                r.setSampleSize(sampleSize.toString());
                                r.setDevice(result);

                                return r;
                            })
                    .iterator();
            while (i.hasNext()) {
                TelemetryTimeseriesData newEntry = i.next();
                repo.findByStartAndSampleSize(newEntry.getStart(), sampleSize.toString()).ifPresentOrElse(record -> {
                            Optional.ofNullable(record.getValue()).ifPresentOrElse( //existing record already contains some saved data
                                    (x) -> {
                                        if(!record.getSampleSize().equals(newEntry.getSampleSize().toString())||!record.getDataType().equals(newEntry.getDataType().toString())){
                                            repo.save(newEntry);
                                            System.out.println("SAVE RECORD, DIFFERENT SAMPLE SIZE or DATA TYPE");
                                        }else {
                                            System.out.println("DO NOTHING, VALUE EXISTS");
                                        }
                                    } //. value already exists
                                    , () -> {
                                        if(newEntry.getValue()!=null) {
                                            record.setValue(newEntry.getValue());
                                            repo.save(record);
                                            System.out.println("REPLACE EMPTY or NULL VALUE");
                                        }
                                    }//.value does not exists
                            );

                        }//.present record
                        , () -> {
                            System.out.println("SAVE NEW VALUE");
                            repo.save(newEntry);
                        }//.not present record
                        );
            }//.while
        }//.if
    }

    /**
     * Return all telemetry data received no later than deltaSeconds.
     *
     * @param deltaSeconds
     * @return
     */
    public List<TelemetryTimeseriesData> getLastUpdate(int deltaSeconds) {
        return repo.findByValueGreaterThan(Instant.now().getEpochSecond() - deltaSeconds).orElseGet(()->Collections.emptyList());

    }

}
