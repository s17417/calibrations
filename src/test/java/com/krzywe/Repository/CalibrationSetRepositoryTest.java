package com.krzywe.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import com.krzywe.Model.MaterialType;
import com.krzywe.Utils.Specifications.CalibrationSetSpec;
import com.krzywe.Utils.Specifications.ICalibrationSetSpec;


@DataJpaTest
@TestPropertySource("classpath:test.properties")
@Sql(statements = {
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005e1', 'aaa', 'DBS','2022-05-04', '2022-06-04','2022-06-04 10:30')",
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005e2', 'bbb', 'CSF','2021-05-04', '2021-06-04','2021-06-04 10:30')",
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005e3', 'ccc', 'CSF','2020-05-04', '2020-06-04','2022-06-04 10:30')",
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005e4', 'ddd', 'CSF','2019-05-04', '2019-06-04','2022-06-04 10:30')",
		"INSERT INTO CalibrationSet (id, name, materialType, preparationDate, expirationDate, createdDate) "
				+ "VALUES ('786fa357-31ef-403b-ac97-088624b005e5', 'eee', 'CSF','2018-05-04', '2018-06-04','2018-06-04 10:30')"
				}
)
public class CalibrationSetRepositoryTest {
	
	@Autowired
	private CalibrationSetRepository repository;
	
	private ICalibrationSetSpec spec = new CalibrationSetSpec();
	
	@Test
	public void testInjection() {
		assertThat(repository).isNotNull();
	}
	
	@Test
	public void calibrationSet_findByIdAsView() {
		var value = repository.findByIdAsView("786fa357-31ef-403b-ac97-088624b005e1");
		assertThat(value)
		.isPresent()
		.get()
		.hasFieldOrPropertyWithValue("id", "786fa357-31ef-403b-ac97-088624b005e1")
		.hasNoNullFieldsOrPropertiesExcept("modifiedDate","createdDate");
	}
	
	@Test
	public void calibrationSet_findByNameAsView() {
		var specification = spec.nameLikeSpecification("bbb");
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(1)
		.element(0)
		.satisfies(obj -> obj.getName().contentEquals("bbb"));
	}
	
	@Test
	public void calibrationSetFindByNameAsView_NullValue() {
		var specification = spec.nameLikeSpecification(null);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(5);
	}
	
	@Test
	public void calibrationSet_findByMaterialTypeAsView() {
		var specification = spec.exactMaterialTypeSpecification(MaterialType.DBS);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(1)
		.element(0)
		.satisfies(obj -> obj.getMaterialType().equals(MaterialType.DBS));
	}
	
	@Test
	public void calibrationSetFindByMaterialTypeAsView_NullValue() {
		var specification = spec.exactMaterialTypeSpecification(null);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(5);
	}
	
	@Test
	public void calibrationSet_findByCreatedDateAsView() {
		var from = LocalDateTime.of(2021, 4, 10, 5, 30);
		var to = LocalDateTime.of(2021, 7, 23, 5, 30);
		var specification = spec.createdDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(1)
		.element(0)
		.satisfies(obj -> obj.getMaterialType().equals(MaterialType.DBS));
	}
	
	@Test
	public void calibrationSetFindByCreatedDateAsView_onlyFrom() {
		var from = LocalDateTime.of(2021, 4, 10, 5, 30);
		LocalDateTime to = null;
		var specification = spec.createdDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(4);
	}
	
	@Test
	public void calibrationSetFindByCreatedDateAsView_onlyTo() {
		var to = LocalDateTime.of(2021, 4, 10, 5, 30);
		LocalDateTime from = null;
		var specification = spec.createdDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(1);
	}
	
	@Test
	public void calibrationSet_findByPreparationDateAsView() {
		var from = LocalDateTime.of(2021, 4, 10, 5, 30);
		var to = LocalDateTime.of(2021, 7, 23, 5, 30);
		var specification = spec.preprationDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(1);
	}
	
	@Test
	public void calibrationSetFindByPreparationDateAsView_onlyFrom() {
		var from = LocalDateTime.of(2021, 4, 10, 5, 30);
		LocalDateTime to = null;
		var specification = spec.preprationDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(2);
	}
	
	@Test
	public void calibrationSetFindByPreparationDateAsView_onlyTo() {
		var to = LocalDateTime.of(2021, 4, 10, 5, 30);
		LocalDateTime from = null;
		var specification = spec.preprationDateRangeSpecification(from, to);
		var result = repository.findBy(
				specification,
				spec.getFluentQueryAsSimpleCalibrationSetView()
				.andThen(o -> o.all())
				);
		assertThat(result)
		.hasSize(3);
	}
	
	

}
