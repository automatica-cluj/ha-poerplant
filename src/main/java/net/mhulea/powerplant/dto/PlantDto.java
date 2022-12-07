package net.mhulea.powerplant.dto;

import lombok.*;
import net.mhulea.powerplant.entity.Plant;

import java.io.Serializable;

/**
 * A DTO for the {@link Plant} entity
 */
@Data
public class PlantDto implements Serializable {
    private Long id;
    private String owner;

}