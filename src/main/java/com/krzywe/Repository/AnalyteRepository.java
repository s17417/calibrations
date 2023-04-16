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
	
	@Query(value = "SELECT DISTINCT :alias "
			+ "FROM analyte a "
			+ "LEFT JOIN analyte_aliases c ON a.id=c.analyte_id "
			+ "WHERE upper(:alias) in (upper(c.aliases)) "
			+ "OR upper(:alias)=upper(a.name)", nativeQuery=true)
	public List<String> aliasExists(List<String> alias);
}
