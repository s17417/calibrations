package com.krzywe.Utils.Specifications;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;

public interface ICalibrationSetSpec {

	Specification<CalibrationSet> nameLikeSpecification(String name);

	Specification<CalibrationSet> exactMaterialTypeSpecification(MaterialType materialType);

	Specification<CalibrationSet> createdDateRangeSpecification(LocalDateTime from, LocalDateTime to);

	Specification<CalibrationSet> preprationDateRangeSpecification(LocalDateTime from, LocalDateTime to);

	Function<FetchableFluentQuery<CalibrationSet>, FetchableFluentQuery<SimpleCalibrationSetView>> getFluentQueryAsSimpleCalibrationSetView();

}