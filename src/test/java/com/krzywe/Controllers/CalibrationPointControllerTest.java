package com.krzywe.Controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.krzywe.Config.ConfigAttributeMap;
import com.krzywe.Config.ConfigManualProjection;
import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.SimpleCalibrationPointView;
import com.krzywe.Model.Analyte;
import com.krzywe.Model.CalibrationPoint;
import com.krzywe.Model.TargetValue;
import com.krzywe.Service.ICalibrationPointService;

@AutoConfigureJsonTesters
@WebMvcTest(CalibrationPointController.class)
@ExtendWith(MockitoExtension.class)
@Import({ConfigManualProjection.class, ConfigAttributeMap.class})
@WebAppConfiguration
public class CalibrationPointControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ICalibrationPointService service;
	
	@Autowired
	private JacksonTester<Object> tester;
	
	@Autowired
	private ProjectionFactory factory;
	
	private CalibrationPointDTO dto;
	
	private CalibrationPointView pointView;
	
	private SimpleCalibrationPointView simplePointView;

	@BeforeEach
	public void beforeEach() {
		dto = new CalibrationPointDTO();
		dto.setPointId("point_1");
		dto.setAliases(List.of("alias_1", "alias_2", "alias_3"));
		
		var objectCalibrationPoint = new CalibrationPoint();
		var objectAnalyte = new Analyte();
		var objectTarget = new TargetValue();
		
		objectCalibrationPoint.setPointId(dto.getPointId());
		objectCalibrationPoint.setAliases(dto.getAliases());
		
		objectAnalyte.setName("Analyte");
		objectTarget.setAnalyte(objectAnalyte);
		objectTarget.setUnits("Unit");
		objectTarget.setCalibrationPoint(objectCalibrationPoint);
		objectTarget.setTargetValue(BigDecimal.TEN);
		pointView = factory.createProjection(CalibrationPointView.class,objectCalibrationPoint);
		simplePointView = factory.createProjection(SimpleCalibrationPointView.class, objectCalibrationPoint);
	}
	
	@Test
	public void createTest() throws IOException, Exception {
		when(service.create(anyString(), anyList()))
		.thenReturn(List.of(pointView));
		
		mvc.perform(
				post("/v1/calibration_set/786fa357-31ef-403b-ac97-088624b005s2/calibration_point")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(List.of(dto)).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isCreated(),
				content().contentType(MediaType.APPLICATION_JSON),
				redirectedUrlPattern("**/v1/calibration_set/786fa357-31ef-403b-ac97-088624b005s2/calibration_point"),
				content().json(tester.write(List.of(pointView)).getJson())
				);
	}
	
	@Test
	public void createTest_invalidFieldException() throws IOException, Exception {
		dto.setPointId(null);
		when(service.create(anyString(), anyList()))
		.thenReturn(List.of(pointView));
		
		mvc.perform(
				post("/v1/calibration_set/786fa357-31ef-403b-ac97-088624b005s2/calibration_point")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(List.of(dto)).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isUnprocessableEntity(),
				content().contentType(MediaType.APPLICATION_JSON)
				);
	}
	
	@Test
	public void updateTest() throws IOException, Exception {
		when(service.update(anyString(), any(CalibrationPointDTO.class)))
		.thenReturn(simplePointView);
		
		mvc.perform(
				put("/v1/calibration_point/"+pointView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(dto).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				jsonPath("$.pointId", is("point_1")),
				content().json(tester.write(simplePointView).getJson())
				);	
	}
	
	@Test
	public void updateTest_invalidFieldException() throws IOException, Exception {
		dto.setPointId(null);
		when(service.update(anyString(), any(CalibrationPointDTO.class)))
		.thenReturn(simplePointView);
		
		mvc.perform(
				put("/v1/calibration_point/"+pointView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(dto).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isUnprocessableEntity(),
				content().contentType(MediaType.APPLICATION_JSON)
				);	
	}
	
	@Test
	public void getByIdTest() throws Exception {
		when(service.getById(anyString()))
		.thenReturn(pointView);
		
		mvc.perform(
				get("/v1/calibration_point/"+pointView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(pointView).getJson())
				);		
	}
	
	@Test
	public void getAllByCalibrationSetIdTest() throws IOException, Exception {
		when(service.getAllByCalibrationSetId(anyString()))
		.thenReturn(List.of(pointView));
		mvc.perform(
				get("/v1/calibration_set/786fa357-31ef-403b-ac97-088624b005s2")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(List.of(pointView)).getJson())
				);
	}
}
