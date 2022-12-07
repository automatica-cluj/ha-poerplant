package net.mhulea.powerplant.dto;

import net.mhulea.powerplant.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperDevice {

    MapperDevice INSTANCE = Mappers.getMapper( MapperDevice.class );

    Device deviceDtoToDevice(DeviceDto deviceDto);

    DeviceDto deviceToDeviceDto(Device device);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Device updateDeviceFromDeviceDto(DeviceDto deviceDto, @MappingTarget Device device);
}
