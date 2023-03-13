package com.krzywe.Controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.krzywe.Config.ConfigAttributeMap;
import com.krzywe.Config.ConfigManualProjection;
import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;
import com.krzywe.Service.ICalibrationSetService;

@AutoConfigureJsonTesters
@WebMvcTest(CalibrationSetController.class)
@ExtendWith(MockitoExtension.class)
@Import({ConfigManualProjection.class, ConfigAttributeMap.class})
@WebAppConfiguration
public class CalibrationSetControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ICalibrationSetService service;
	
	@Autowired
	private JacksonTester<CalibrationSetDTO> tester;
	
	@Autowired
	private ProjectionFactory factory;
	
	private CalibrationSetView calibrationSetView;
	private SimpleCalibrationSetView simpleCalibrationSetView;
	
	private CalibrationSetDTO calibrationSetDTO;
	
	
	
	@BeforeEach
	public void beforeEach() {
		calibrationSetDTO = new CalibrationSetDTO();
		calibrationSetDTO.setName("name");
		calibrationSetDTO.setMaterialType(MaterialType.CSF);
		calibrationSetDTO.setPreparationDate(LocalDate.of(2020, 10, 1));
		calibrationSetDTO.setExpirationDate(LocalDate.of(2023, 10, 1));
		
		var object = new CalibrationSet();
		object.setName(calibrationSetDTO.getName());
		object.setMaterialType(calibrationSetDTO.getMaterialType());
		object.setPreparationDate(calibrationSetDTO.getPreparationDate());
		object.setExpirationDate(calibrationSetDTO.getExpirationDate());
		
		calibrationSetView = factory.createProjection(CalibrationSetView.class, object);
		simpleCalibrationSetView = factory.createProjection(SimpleCalibrationSetView.class, object);
	}
	
	@Test
	public void create() throws Exception {
		when(service.create(any(CalibrationSetDTO.class)))
		.thenReturn(calibrationSetView);
				
		mvc.perform(
				post("/v1/calibration_set")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(calibrationSetDTO).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isCreated(),
				content().contentType(MediaType.APPLICATION_JSON),
				redirectedUrlPattern("**/v1/calibration_set/"+calibrationSetView.getId()),
				jsonPath("$.name", is("name")),
				jsonPath("$.materialType", is("CSF"))
				);	
	}
	
	
	@Test
	public void update() throws Exception {
		when(service.update(any(CalibrationSetDTO.class),anyString()))
		.thenReturn(calibrationSetView);
				
		mvc.perform(
				put("/v1/calibration_set/"+calibrationSetView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(calibrationSetDTO).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				jsonPath("$.name", is("name")),
				jsonPath("$.materialType", is("CSF"))
				);		
	}
	
	@Test
	public void findById() throws Exception {
		when(service.findById(anyString()))
		.thenReturn(calibrationSetView);
				
		mvc.perform(
				get("/v1/calibration_set/"+calibrationSetView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				jsonPath("$.name", is("name")),
				jsonPath("$.materialType", is("CSF"))
				);	
	}
	

	@Test
	public void findAll() throws Exception {
		var page = new PageImpl<SimpleCalibrationSetView>(
				List.of(simpleCalibrationSetView),
				PageRequest.of(0, 25),
				1L);
		when(service.findAll(
				anyString(),
				any(MaterialType.class),
				any(LocalDate.class),
				any(LocalDate.class),
				any(LocalDateTime.class),
				any(LocalDateTime.class),
				anyInt(),
				anyInt(),
				any(Direction.class),
				anyString()
				)
			)
		.thenReturn(page);
		System.out.println(page);
		mvc.perform(
				get("/v1/calibration_set")
				.param("name", "name")
				.param("materialType", "CSF")
				.param("preparationDateFrom", "2020-01-01")
				.param("preparationDateTo", "2021-01-01")
				.param("createdDateFrom", "2020-01-01T08:30")
				.param("createdDateTo", "2021-01-01T08:30")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				jsonPath("$.content[0].name", is("name")),
				jsonPath("$.content[0].materialType", is("CSF"))
				);	
	}
}
