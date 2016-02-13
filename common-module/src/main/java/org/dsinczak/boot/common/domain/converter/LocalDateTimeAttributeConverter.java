package org.dsinczak.boot.common.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * JPA converter for {@link LocalDateTime} allowing conversion to and from DB column.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

		@Override
		public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
				return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
		}

		@Override
		public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
				return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
		}
}

