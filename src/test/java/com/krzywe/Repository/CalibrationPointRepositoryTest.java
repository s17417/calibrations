package com.krzywe.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.TargetValueView;
import com.krzywe.Model.Analyte;
import com.krzywe.Model.CalibrationPoint;
import com.krzywe.Model.TargetValue;


@DataJpaTest
@TestPropertySource("classpath:test.properties")
@Sql(statements = {
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005s1', 'calibrationSet_1', 'DBS','2022-05-04', '2022-06-04','2022-06-04 10:30')",
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005s2', 'calibrationSet_2', 'CSF','2021-05-04', '2021-06-04','2021-06-04 10:30')",
		
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a1', 'analyte_1')",
		"INSERT INTO Analyte (id, name) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005a2', 'analyte_2')",					
		
		"INSERT INTO CalibrationPoint (id, pointId, calibrationSet_id) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005p1', 'calibrationPoint_1','786fa357-31ef-403b-ac97-088624b005s1')",
		"INSERT INTO CalibrationPoint (id, pointId, calibrationSet_id) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005p2', 'calibrationPoint_2','786fa357-31ef-403b-ac97-088624b005s1')",
		
		"INSERT INTO TargetValue (id, targetValue, Units, calibrationPoint_id, analyte_id) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005t1', 50.0, 'umol/L', '786fa357-31ef-403b-ac97-088624b005p1', '786fa357-31ef-403b-ac97-088624b005a1')",
		"INSERT INTO TargetValue (id, targetValue, Units, calibrationPoint_id, analyte_id) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005t2', 30.0, 'umol/L', '786fa357-31ef-403b-ac97-088624b005p1', '786fa357-31ef-403b-ac97-088624b005a2')",
				
		"INSERT INTO CalibrationPoint_aliases (calibrationSet_id, CalibrationPoint_id, aliases, ALIAS_ORDER) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005s1', '786fa357-31ef-403b-ac97-088624b005p1', 'ADA', 0)",
		"INSERT INTO CalibrationPoint_aliases (calibrationSet_id, CalibrationPoint_id, aliases, ALIAS_ORDER) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005s1', '786fa357-31ef-403b-ac97-088624b005p1', 'AZA', 1)"
		}
)
public class CalibrationPointRepositoryTest {
	
	@Autowired
	private CalibrationPointRepository repository;
	
	@Test
	public void testInjection() {
		assertThat(repository).isNotNull();
	}
	
	@Test
	public void testFindCalibrationPointByIdAsView() {
		var result = repository.findCalibrationPointById("786fa357-31ef-403b-ac97-088624b005p1");
		
		assertThat(result)
		.isPresent()
		.get()
		.satisfies(obj -> assertThat(obj.getAliases()))
		.isInstanceOf(CalibrationPointView.class)
		.satisfies(obj -> 
			assertThat(obj.getTargetValues())
			.isNotEmpty()
			.hasSize(2)
			.elements(0,1)
			.allSatisfy(objt -> {
				assertThat(objt)
				.isInstanceOf(TargetValueView.class);
				assertThat(objt.getAnalyte())
				.isNotNull()
				.isInstanceOf(AnalyteView.class);
			})
		);
	}
	
	@Test
	public void testFindAllAsView() {
		var result = repository.findAllByCalibrationSetId("786fa357-31ef-403b-ac97-088624b005s1");
		assertThat(result)
		.isNotEmpty()
		.hasSize(2)
		.elements(0,1)
		.allSatisfy(obj -> {
			assertThat(obj)
			.isInstanceOf(CalibrationPointView.class);
			if (!obj.getTargetValues().isEmpty())
			assertThat(obj.getTargetValues())
			.elements(0,1)
			.allSatisfy(objt -> {
				assertThat(objt)
				.isInstanceOf(TargetValueView.class);
				assertThat(objt.getAnalyte())
				.isNotNull()
				.isInstanceOf(AnalyteView.class);
			});
		});
	}
	
	@Test
	public void testfindAliases() {
		var result = repository.findAliases("786fa357-31ef-403b-ac97-088624b005p1");
		assertThat(result)
		.isNotEmpty()
		.hasSize(2);
	}
	
	@Test
	public void testFindByPointId() {
		var result = repository.findByPointId("calibrationPoint_1");
		assertThat(result)
		.isNotEmpty()
		.satisfies(obj -> assertThat(obj.get().getPointId()).isEqualTo("calibrationPoint_1"));
	}

	@Test
	public void testFindByAlias() {
		var result = repository.findOneByAliasesAndCalibrationSetId("ADA","786fa357-31ef-403b-ac97-088624b005s1");
		assertThat(result)
		.isNotEmpty()
		.get()
		.satisfies(obj -> {
			assertThat(obj).isOfAnyClassIn(CalibrationPoint.class);
			assertThat(obj.getAliases()).contains("ADA", "AZA");
		});
	}
}
