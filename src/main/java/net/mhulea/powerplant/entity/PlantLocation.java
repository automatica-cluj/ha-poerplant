package net.mhulea.powerplant.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class PlantLocation {
    private String country;
    private String region;
    private String city;
    private String street1;
    private String postalCode;
    private String timeZone;
    private Double latitude;
    private Double longitude;

}
