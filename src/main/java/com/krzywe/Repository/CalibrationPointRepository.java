package com.krzywe.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.Model.CalibrationPoint;

public interface CalibrationPointRepository extends JpaRepository<CalibrationPoint, String> {
	
	@Query(value = 
			"SELECT * "
			+ "FROM CalibrationPoint c "
			+ "LEFT JOIN TargetValue t ON t.calibrationPoint_id=c.id "
			+ "LEFT JOIN Analyte a ON a.id=t.analyte_id "
			+ "WHERE c.id=?1",
			nativeQuery = true
			)
	public Optional<CalibrationPointView> findCalibrationPointByIdAsView(String id);
	
	@Query(value = "SELECT * "
			+ "FROM CalibrationPoint c "
			+ "LEFT JOIN TargetValue t ON t.calibrationPoint_id=c.id "
			+ "LEFT JOIN Analyte a ON a.id=t.analyte_id "
			+ "WHERE c.calibrationSet_id=?1", nativeQuery = true)
	public List<CalibrationPointView> findAllAsView(String calibrationSet_id);


}
