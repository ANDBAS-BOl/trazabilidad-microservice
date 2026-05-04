package com.pragma.powerup.trazabilidad.application.mapper;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ITrazabilidadDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaEvento", ignore = true)
    OrderTraceModel toModel(CreateOrderTraceRequestDto dto);

    OrderTraceResponseDto toResponse(OrderTraceModel model);

    List<OrderTraceResponseDto> toResponses(List<OrderTraceModel> models);
}
