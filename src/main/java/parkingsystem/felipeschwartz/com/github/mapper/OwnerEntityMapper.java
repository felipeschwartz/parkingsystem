package parkingsystem.felipeschwartz.com.github.mapper;

import org.mapstruct.Mapper;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerEntityDTO;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;

@Mapper(componentModel = "spring")
public interface OwnerEntityMapper {
    OwnerEntityDTO toDTO(OwnerEntity owner);
    OwnerEntity toEntity(OwnerEntityDTO dto);
}
