package com.krzywe.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.CalibrationSet_;
import com.krzywe.Model.MaterialType;

import jakarta.persistence.criteria.Predicate;

public interface CalibrationSetRepository extends JpaRepository<CalibrationSet, String>,JpaSpecificationExecutor<CalibrationSet> {

	public Optional<SimpleCalibrationSetView> findCalibrationSetById(String Id);

	
	
	@Query(value = "Select * from CalibrationSet c", nativeQuery = true)
	public Page<SimpleCalibrationSetView> findAllAsView(Specification<CalibrationSet> specification, Pageable pageable);

	@Query(value = "Select * from CalibrationSet c where c.id=?1", nativeQuery = true)
	public Optional<CalibrationSetView> findByIdAsView(String id);
	
	
}
