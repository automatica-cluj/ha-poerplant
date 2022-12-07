package net.mhulea.auroraclient.apimodel.plant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FimerPlant {

    private String plantEntityId;
    private String plantName;
    private String plantState;
    private LocalDateTime firstReportedDate;

}
