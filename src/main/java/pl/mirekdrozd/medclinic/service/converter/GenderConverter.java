package pl.mirekdrozd.medclinic.service.converter;

import pl.mirekdrozd.medclinic.repository.model.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender must not be null");
        }
        return gender.getId();
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        return Gender.fromId(dbData);
    }
}
