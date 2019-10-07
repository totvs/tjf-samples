package br.com.starwars.familytree;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FamilyTreeIT {
	
	private static String idFirst;
	private static String idSecond;
	private static String idThird;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeClass
	public static void setUp() {
		idFirst = UUID.randomUUID().toString();
		idSecond = UUID.randomUUID().toString();
		idThird = UUID.randomUUID().toString();
	}
			
	@Test
	public void testA_postFirstPerson() throws Exception {		
		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{  \"id\": \"" + idFirst + "\",\r\n" + 
						"    \"name\": \"Leia Organa\",\r\n" + 
						"    \"gender\": \"female\"}}")).andExpect(status().isOk());
	}
		
	@Test
	public void testA_postSecondPerson() throws Exception {		
		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{  \"id\": \"" + idSecond + "\",\r\n" + 
						"    \"name\": \"Anakin Skywalker\",\r\n" + 
						"    \"gender\": \"male\"}}")).andExpect(status().isOk());	
	}
	
	@Test
	public void testA_postThirdPerson() throws Exception {		
		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{  \"id\": \"" + idThird + "\",\r\n" + 
						"    \"name\": \"Han Solo\",\r\n" + 
						"    \"gender\": \"male\"}}")).andExpect(status().isOk());	
	}
	
	@Test
	public void testB_getPerson() throws Exception {
		mockMvc.perform(get("/api/v1/familytree/person/" + idFirst).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testC_addFamilyTree() throws Exception {
		mockMvc.perform(post("/api/v1/familytree/person/" + idFirst + "/relative/" + idSecond + "/child").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testD_updateFamilyTree() throws Exception {
		mockMvc.perform(post("/api/v1/familytree/person/" + idFirst + "/relative/" + idThird + "/son").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
