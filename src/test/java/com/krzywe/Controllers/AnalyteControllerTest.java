package com.krzywe.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
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
import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.Analyte;
import com.krzywe.Service.IAnalyteService;

@AutoConfigureJsonTesters
@WebMvcTest(AnalyteController.class)
@ExtendWith(MockitoExtension.class)
@Import({ConfigManualProjection.class, ConfigAttributeMap.class})
@WebAppConfiguration
public class AnalyteControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private IAnalyteService service;
	
	@Autowired
	private JacksonTester<Object> tester;
	
	@Autowired
	private ProjectionFactory factory;
	
	private AnalyteDTO dto;
	
	private AnalyteWithAliasesView aliasesView;
	
	private AnalyteNamesView analyteNamesView;
	
	private AnalyteView analyteView;
	
	@BeforeEach
	public void beforeEach() {
		dto = new AnalyteDTO();
		dto.setName("analyte_1");
		dto.setAliases(List.of("alias_1","alias_2"));
		
		var object = new Analyte();
		object.setName(dto.getName());
		object.setAliases(dto.getAliases());
		
		analyteNamesView = factory.createProjection(AnalyteNamesView.class, object);
		analyteView = factory.createProjection(AnalyteView.class, object);
		aliasesView = factory.createNullableProjection(AnalyteWithAliasesView.class, object);
	}
	
	@Test
	public void createTest() throws IOException, Exception {
		when(service.create(any(AnalyteDTO.class)))
		.thenReturn(aliasesView);
		
		mvc.perform(
				post("/v1/analyte")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(dto).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isCreated(),
				content().contentType(MediaType.APPLICATION_JSON),
				redirectedUrlPattern("**/v1/analyte/"+aliasesView.getId()),
				content().json(tester.write(aliasesView).getJson())
				);
	}
	
	@Test
	public void updateTest() throws IOException, Exception {
		when(service.update(anyString(),any(AnalyteDTO.class)))
		.thenReturn(aliasesView);
		
		mvc.perform(
				put("/v1/analyte/"+aliasesView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(tester.write(dto).getJson())
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(aliasesView).getJson())
				);
	}
	
	@Test
	public void getByIdTest() throws IOException, Exception {
		when(service.getById(anyString()))
		.thenReturn(aliasesView);
		
		mvc.perform(
				get("/v1/analyte/"+aliasesView.getId())
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(aliasesView).getJson())
				);
	}
	
	@Test
	public void getAllTest() throws IOException, Exception {
		var page = new PageImpl<AnalyteView>(
				List.of(analyteView),
				PageRequest.of(0, 25),
				1L);
		when(service.getAll(anyString(), anyString(), anyInt(), anyInt(), any(Direction.class), anyString()))
		.thenReturn(page);
		
		mvc.perform(
				get("/v1/analyte")
				.param("name", "name")
				.param("alias", "alias")
				.param("pageNumber", "0")
				.param("pageSize", "0")
				.param("sortDirection", "DESC")
				.param("sortAttribute", "createdDate")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(page).getJson())
				);
	}
	
	@Test
	public void getNames() throws IOException, Exception {
		when(service.getAllAnalyteNames())
		.thenReturn(List.of(analyteNamesView));
		
		mvc.perform(
				get("/v1/analyte/names")
				.with(csrf())
				.with(user("admin").roles("ADMIN"))
				)
		.andDo(print())
		.andExpectAll(
				status().isOk(),
				content().contentType(MediaType.APPLICATION_JSON),
				content().json(tester.write(List.of(analyteNamesView)).getJson())
				);
	}
	
	
}
