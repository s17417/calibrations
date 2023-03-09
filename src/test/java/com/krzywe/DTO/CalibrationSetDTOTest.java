package com.krzywe.DTO;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.krzywe.DTO.mappers.CalibrationSetDtoMapperImpl;
import com.krzywe.Model.MaterialType;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalibrationSetDTOTest {
	
	public CalibrationSetDtoMapperImpl mapperImpl = new CalibrationSetDtoMapperImpl();
	
	public CalibrationSetDTO calibrationSetDTO;
	
	@BeforeEach
	public void beforeEach() {
		calibrationSetDTO = new CalibrationSetDTO();
		calibrationSetDTO.setName("name");
		calibrationSetDTO.setExpirationDate(LocalDate.of(2020, 1, 1));
		calibrationSetDTO.setPreparationDate(LocalDate.of(2019, 1, 1));
		calibrationSetDTO.setMaterialType(MaterialType.CSF);

	}
	
	@Test
	public void testDTOMapping() {
		var result = mapperImpl.CalibrationSetDtoToCalibrationSet(calibrationSetDTO);
		assertThat(result)
		.satisfies(obj -> {
			assertThat(obj.getName()).contains("name");
			assertThat(obj.getMaterialType()).isEqualByComparingTo(MaterialType.CSF);
			assertThat(obj.getPreparationDate()).isEqualTo(LocalDate.of(2019, 1, 1));
			assertThat(obj.getExpirationDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		});
	}

}
