package com.tjf.sample.github.apijpa.specification;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ApiJpaSpecificationApplication.class)
@AutoConfigureMockMvc
public class ApiJpaSpecificationIT {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void findAllDroidsTest() throws Exception {
		String exectedResult = "[{\"id\":1,\"name\":\"Super Battle Droid\",\"function\":\"Super Soldado de Batalha\",\"height\":1.91},{\"id\":2,\"name\":\"Protocol Droid\",\"function\":\"Auxiliam diplomantas e politicos; Programados em etiqueta e equipados com formidaveis habilidades linguisticas\",\"height\":1.7},{\"id\":3,\"name\":\"Sith Probe Droid\",\"function\":\"Rastrear fugitivos\",\"height\":0.3},{\"id\":4,\"name\":\"Battle Droid\",\"function\":\"Substituir humanos no campo de batalha, usando a quantidade em seu favor\",\"height\":1.93},{\"id\":5,\"name\":\"Mouse Droid\",\"function\":\"Entregar Mensagens; Guiar Visitantes\",\"height\":0.25}]";
		mockMvc.perform(get("/api/v1/droid/findAll")).andExpect(status().isOk())
				.andExpect(content().json(exectedResult));
	}

	@Test
	public void findByNameDroidsTest() throws Exception {
		String exectedResult = "{\"hasNext\":false,\"items\":[{\"id\":4,\"name\":\"Battle Droid\",\"function\":\"Substituir humanos no campo de batalha, usando a quantidade em seu favor\",\"height\":1.93}]}";
		mockMvc.perform(get("/api/v1/droid/findByName/Battle Droid")).andExpect(status().isOk())
				.andExpect(content().json(exectedResult));
	}

	@Test
	public void findLikeDroidsTest() throws Exception {
		String exectedResult = "{\"hasNext\":false,\"items\":[{\"id\":2,\"name\":\"Protocol Droid\",\"function\":\"Auxiliam diplomantas e politicos; Programados em etiqueta e equipados com formidaveis habilidades linguisticas\",\"height\":1.7}]}";
		mockMvc.perform(get("/api/v1/droid/findLike/etiqueta")).andExpect(status().isOk())
				.andExpect(content().json(exectedResult));
	}

	@Test
	public void findBetweenDroidsTest() throws Exception {
		String exectedResult = "{\"hasNext\":false,\"items\":[{\"id\":1,\"name\":\"Super Battle Droid\",\"function\":\"Super Soldado de Batalha\",\"height\":1.91},{\"id\":2,\"name\":\"Protocol Droid\",\"function\":\"Auxiliam diplomantas e politicos; Programados em etiqueta e equipados com formidaveis habilidades linguisticas\",\"height\":1.7},{\"id\":4,\"name\":\"Battle Droid\",\"function\":\"Substituir humanos no campo de batalha, usando a quantidade em seu favor\",\"height\":1.93}]}";
		mockMvc.perform(get("/api/v1/droid/findBetween").header("from", 1).header("util", 2)).andExpect(status().isOk())
				.andExpect(content().json(exectedResult));
	}

	@Test
	public void findExistsDroidsTest() throws Exception {
		String exectedResult = "{\"hasNext\":false,\"items\":[{\"id\":5,\"name\":\"Mouse Droid\",\"function\":\"Entregar Mensagens; Guiar Visitantes\",\"height\":0.25}]}";
		mockMvc.perform(get("/api/v1/droid/findExists/0.29")).andExpect(status().isOk())
				.andExpect(content().json(exectedResult));
	}

}
