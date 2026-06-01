package parkingsystem.felipeschwartz.com.github.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

@Converter(autoApply = false)
public class VehicleTypeConverter implements AttributeConverter<VehicleType, Short> {
    @Override
    public Short convertToDatabaseColumn(VehicleType attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }
    @Override
    public VehicleType convertToEntityAttribute(Short dbData) {
        return VehicleType.fromCode(dbData);
    }
}
