package org.dsinczak.boot.common.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JPA converter for {@link LocalDate} allowing conversion to and from DB column.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalDate date) {
				final Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
				return Date.from(instant);
		}

		@Override
		public LocalDate convertToEntityAttribute(Date value) {
				final Instant instant = value.toInstant();
				return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		}
}

