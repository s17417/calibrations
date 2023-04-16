package com.krzywe.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@TestPropertySource("classpath:test.properties")
@Sql(statements = {
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a1', 'analyte_1')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a2', 'analyte_2')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a3', 'analyte_3')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a4', 'analyte_4')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a5', 'analyte_5')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a6', 'analyte_6')",
		"INSERT INTO analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a7', 'analyte_7')",
		"INSERT INTO analyte_aliases (analyte_id, alias_order, aliases) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a1', 0 ,'APA')",
		"INSERT INTO analyte_aliases (analyte_id, alias_order, aliases) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a1', 1 ,'ALA')"
})
public class AnalyteRepositoryTest {
	
	@Autowired
	private AnalyteRepository repository;
	
	@Test
	public void testInjection() {
		assertThat(repository).isNotNull();
	}

	@Test
	public void findAnalyteByIdTest() {
		var result  = repository
				.findAnalyteById("786fa357-31ef-403b-ac97-088624b005a4");
		assertThat(result)
		.isNotEmpty()
		.get()
		.satisfies(obj -> obj.getId().equals("786fa357-31ef-403b-ac97-088624b005a4"));		
	}
	
	@Test
	public void findAnalytesNamesTest() {
		var result = repository.findAnalytesNames();
		assertThat(result)
		.isNotEmpty()
		.hasSize(7)
		.allSatisfy(obj -> obj.getName().contains("analyte"));
	}
	
	@Test
	public void aliasExistTest() {
		var result = repository.aliasExists(List.of("APA"));
		assertThat(result)
		.hasSize(1)
		.element(0)
		.usingComparator(String::compareTo)
		.isEqualTo("APA");
	}
}
