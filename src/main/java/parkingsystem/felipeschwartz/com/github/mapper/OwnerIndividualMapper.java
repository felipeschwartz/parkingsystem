package parkingsystem.felipeschwartz.com.github.mapper;

import org.mapstruct.Mapper;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerIndividualDTO;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;

@Mapper(componentModel = "spring")
public interface OwnerIndividualMapper {
    OwnerIndividualDTO toDTO(OwnerIndividual owner);
    OwnerIndividual toEntity(OwnerIndividualDTO dto);
}
