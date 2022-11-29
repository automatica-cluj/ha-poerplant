/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.services;

import homeautomation.auroraclient.apimodel.telemetry.FimerTelemetryTimeseriesData;
import homeautomation.auroraclient.client.EnumQueryParamSampleSize;
import homeautomation.entity.Device;
import homeautomation.entity.TelemetryTimeseriesData;
import homeautomation.repository.DeviceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import homeautomation.repository.TelemetryTimeseriesRepository;

import java.time.Instant;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author mihai
 */
@Service
public class TelemetryService {

    @Autowired
    private TelemetryTimeseriesRepository repo;

    @Autowired
    private DeviceRepository devRepo;

    public void writeTelemtryData(Long deviceId, String dataType, EnumQueryParamSampleSize sampleSize, Optional<List<FimerTelemetryTimeseriesData>> data) {

        if (data.isPresent()) {
            Optional<Device> result = devRepo.findById(deviceId).or(() -> {
                Device x = new Device();
                x.setId(deviceId);
                devRepo.save(x);
                return Optional.of(x);
            });

            Iterator<TelemetryTimeseriesData> i = data.get().stream().map(
                            d -> {
                                TelemetryTimeseriesData r = new TelemetryTimeseriesData();
                                r.setStart(d.getStart());
                                r.setUnits(d.getUnits());
                                r.setValue(d.getValue());
                                r.setDataType(dataType);
                                r.setSampleSize(sampleSize.toString());
                                r.setDevice(result.get());

                                return r;
                            })
                    .iterator();
            while (i.hasNext()) {
                TelemetryTimeseriesData ttd = i.next();
                repo.findByStartAndSampleSize(ttd.getStart(), sampleSize.toString()).ifPresentOrElse(record -> {
                            Optional.ofNullable(record.getValue()).ifPresentOrElse( //existing record already contains some saved data
                                    (x) -> {
                                        if(!record.getSampleSize().equals(sampleSize.toString())){
                                            repo.save(ttd);
                                            System.out.println("SAVE RECORD, DIFFERENT SAMPLE SIZE");
                                        }else {
                                            System.out.println("DO NOTHING, VALUE EXISTS");
                                        }
                                    } //. value already exists
                                    , () -> {
                                        if(ttd.getValue()!=null) {
                                            record.setValue(ttd.getValue());
                                            repo.save(record);
                                            System.out.println("REPLACE EMPTY or NULL VALUE");
                                        }
                                    }//.value does not exists
                            );

                        }//.present record
                        , () -> {
                            System.out.println("SAVE NEW VALUE");
                            repo.save(ttd);
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
        List<TelemetryTimeseriesData> result = repo.findByValueGreaterThan(Instant.now().getEpochSecond() - deltaSeconds);
        return result;
    }

}
