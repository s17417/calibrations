package com.krzywe.Utils.Specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;

public interface ICalibrationSetSpec {

	Specification<CalibrationSet> getAllSpecificationsChain(
			String name,
			MaterialType materialType,
			LocalDateTime createdDateFrom,
			LocalDateTime createdDateTo,
			LocalDate preparationDateFrom,
			LocalDate preparationDateTo
			);

	Specification<CalibrationSet> getAll();

}
