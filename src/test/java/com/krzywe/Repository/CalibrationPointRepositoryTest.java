package com.krzywe.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.Model.Analyte;
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
		}
)
public class CalibrationPointRepositoryTest {
	
	@Autowired
	private CalibrationPointRepository repository;
	
	@Test
	public void testInjection() {
		assertThat(repository).isNotNull();
	}
	
	public void testFindCalibrationPointByIdAsView() {
		var result = repository.findCalibrationPointByIdAsView("786fa357-31ef-403b-ac97-088624b005a1");
		assertThat(result)
		.isPresent()
		.get()
		.satisfies(obj -> 
			assertThat(obj.getTargetValues())
			.isNotEmpty()
			.hasSize(2)
			.elements(0,1)
			.isOfAnyClassIn(CalibrationPointView.class)
			.isOfAnyClassIn(TargetValue.class)
			.allSatisfy(objt -> assertThat(objt.getAnalyte())
					.isNotNull()
					.isOfAnyClassIn(Analyte.class)
					)
			);
	}
	
	public void testFindAllAsView() {
		var result = repository.findAllAsView("786fa357-31ef-403b-ac97-088624b005s1");
		assertThat(result)
		.isNotEmpty()
		.hasSize(2)
		.elements(0,1)
		.isOfAnyClassIn(CalibrationPointView.class)
		.allSatisfy(obj -> 
			assertThat(obj.getTargetValues())
			.isNotEmpty()
			.hasSize(2)
			.elements(0,1)
			.isOfAnyClassIn(TargetValue.class)
			.allSatisfy(objt -> assertThat(objt.getAnalyte())
					.isNotNull()
					.isOfAnyClassIn(Analyte.class)
					)
			);
	}

}
