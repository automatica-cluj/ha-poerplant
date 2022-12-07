package net.mhulea.powerplant.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class DeviceDetails {
    private String name;
    private String category;
    private String state;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private String fWVersion;
    private String communicationInterface;
    private String rs485Address;
    private String firstReportedDate;

}