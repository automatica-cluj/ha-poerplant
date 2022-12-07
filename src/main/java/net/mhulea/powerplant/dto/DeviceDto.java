package net.mhulea.powerplant.dto;

import net.mhulea.powerplant.entity.Device;
import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link Device} entity
 */
@Data
public class DeviceDto implements Serializable {
    private Long id;
    private PlantDto plant;
}