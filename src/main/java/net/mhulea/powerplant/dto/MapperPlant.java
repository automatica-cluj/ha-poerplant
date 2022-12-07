package net.mhulea.powerplant.dto;

import net.mhulea.powerplant.entity.Plant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperPlant {

    MapperPlant INSTANCE = Mappers.getMapper( MapperPlant.class );

    //Plant

    Plant plantDtoToPlant(PlantDto plantDto);

    PlantDto plantToPlantDto(Plant plant);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Plant updatePlantFromPlantDto(PlantDto plantDto, @MappingTarget Plant plant);


}
