package com.krzywe.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.Model.Analyte;

public interface AnalyteRepository extends JpaRepository<Analyte, String>, JpaSpecificationExecutor<Analyte> {
	
	@EntityGraph(attributePaths = {"aliases"})
	public Optional<AnalyteWithAliasesView> findAnalyteById(String Id);
	
	@Query("SELECT a.id AS id, a.name AS name FROM Analyte a")
	public List<AnalyteNamesView> findAnalytesNames();
	
	
	/**
	 * List must contain uppercase strings (comparision based on uppercase) 
	 * @param alias
	 * @return
	 */
	@Query(value = "SELECT c.aliases "
			+ "FROM analyte_aliases c "
			+ "WHERE upper(c.aliases) IN (:names) ", nativeQuery=true)
	public List<String> findAliases(List<String> names);
	
	/**
	 * List must contain uppercase strings (comparision based on uppercase) 
	 * @param alias
	 * @return
	 */
	@Query(value = "SELECT a.name "
			+ "FROM analyte a "
			+ "WHERE upper(a.name) IN (:aliasList)", nativeQuery=true)
	public List<String> findNames(List<String> aliasList);
}
