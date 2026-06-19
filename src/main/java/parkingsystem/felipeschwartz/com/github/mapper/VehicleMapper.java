package parkingsystem.felipeschwartz.com.github.mapper;

import org.mapstruct.Mapper;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerIndividualDTO;
import parkingsystem.felipeschwartz.com.github.data.dto.VehicleDTO;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleDTO toDTO(Vehicle vehicle);
    Vehicle toEntity(VehicleDTO dto);
}
