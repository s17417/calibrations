package com.krzywe.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.Model.CalibrationPoint;

public interface CalibrationPointRepository extends JpaRepository<CalibrationPoint, String> {
	
	@EntityGraph(attributePaths = {"targetValues", "aliases"})
	public Optional<CalibrationPointView> findCalibrationPointById(String id);
	
	@EntityGraph(attributePaths = {"targetValues","aliases"})
	public List<CalibrationPointView> findAllByCalibrationSetId(String calibrationSet_id);
	
	public Optional<CalibrationPointView> findByPointId(String pointId);
	
	public Optional<CalibrationPoint> findOneByAliasesAndCalibrationSetId(String alias, String calibrationSetId);
	
	@Query(value = "SELECT aliases "
			+ "FROM CalibrationPoint_aliases c "
			+ "WHERE c.CalibrationPoint_id=?1", nativeQuery = true)
	public List<String> findAliases(String id);

}
