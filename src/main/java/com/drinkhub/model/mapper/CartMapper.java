package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.entity.Cart;
import com.drinkhub.model.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);

    CartItem toEntity(CartItemDto cartItemDto);

    List<CartItemDto> toDtoList(List<CartItem> items);

    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CartItemDto dto, @MappingTarget CartItem entity);
}
