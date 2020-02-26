package br.com.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;

import br.com.star.wars.StarWarsServicesApplication;
import br.com.star.wars.messaging.Subscriber;
import br.com.star.wars.model.JediRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarWarsServicesApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SGDPSampleIT {

	@Autowired
	private JediRepository jediRepository;

	@Autowired
	MockMvc mockMvc;

	@Test
	@Rollback(false)
	public void testA_endpointGetJedisAuditing() throws Exception {
		mockMvc.perform(get("/sgpd/v1/jedis").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@Rollback(false)
	public void testB_getMetadata() throws Exception {
		String expectedResult = "{\n" + "    \"models\": {\n" + "        \"br.com.star.wars.model.Jedi\": {\n"
				+ "            \"sgdpSupport\": true,\n" + "            \"description\": \"Jedi\",\n"
				+ "            \"attributes\": {\n" + "                \"identification\": {\n"
				+ "                    \"type\": \"int\",\n"
				+ "                    \"description\": \"Identification\",\n" + "                    \"sgdpData\": {\n"
				+ "                        \"sensitive\": true,\n" + "                        \"type\": \"CPF\",\n"
				+ "                        \"allowsAnonymization\": true\n" + "                    },\n"
				+ "                    \"sgdpPurposes\": [\n" + "                        {\n"
				+ "                            \"classification\": \"REGULAR_EXERCISE_OF_LAW\",\n"
				+ "                            \"justification\": \"Numero de identificação do Jedi\"\n"
				+ "                        }\n" + "                    ]\n" + "                },\n"
				+ "                \"gender\": {\n" + "                    \"type\": \"String\",\n"
				+ "                    \"description\": \"Gender\",\n" + "                    \"sgdpData\": {\n"
				+ "                        \"sensitive\": true,\n" + "                        \"type\": \"EMPTY\",\n"
				+ "                        \"allowsAnonymization\": false\n" + "                    },\n"
				+ "                    \"sgdpPurposes\": []\n" + "                },\n"
				+ "                \"name\": {\n" + "                    \"type\": \"String\",\n"
				+ "                    \"description\": \"Nome do Jedi\",\n"
				+ "                    \"sgdpData\": null,\n" + "                    \"sgdpPurposes\": []\n"
				+ "                },\n" + "                \"id\": {\n" + "                    \"type\": \"int\",\n"
				+ "                    \"description\": null,\n" + "                    \"sgdpData\": null,\n"
				+ "                    \"sgdpPurposes\": []\n" + "                },\n"
				+ "                \"email\": {\n" + "                    \"type\": \"String\",\n"
				+ "                    \"description\": \"Email\",\n" + "                    \"sgdpData\": {\n"
				+ "                        \"sensitive\": false,\n" + "                        \"type\": \"EMAIL\",\n"
				+ "                        \"allowsAnonymization\": true\n" + "                    },\n"
				+ "                    \"sgdpPurposes\": [\n" + "                        {\n"
				+ "                            \"classification\": \"CONSENTMENT\",\n"
				+ "                            \"justification\": \"Email para contato.\"\n"
				+ "                        },\n" + "                        {\n"
				+ "                            \"classification\": \"CONTRACT_EXECUTION\",\n"
				+ "                            \"justification\": \"Necessário para contato.\"\n"
				+ "                        }\n" + "                    ]\n" + "                }\n" + "            },\n"
				+ "            \"usedAt\": []\n" + "        }\n" + "    },\n" + "    \"codes\": {\n"
				+ "        \"br.com.star.wars.model.Jedi\": {\n"
				+ "            \"description\": \"Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi\"\n"
				+ "        }\n" + "    },\n" + "    \"package\": \"br.com.star.wars\"\n" + "}";

		mockMvc.perform(get("/sgdp/metadata").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	@Rollback(false)
	public void testC_getJedisAuditing() {	
		ApiFieldRequest field = new ApiFieldRequest();
		ApiPageRequest page = new ApiPageRequest();
		ApiSortRequest sort = new ApiSortRequest();

		jediRepository.findAllProjected(field, page, sort);
	}
	
	@Test
	@Rollback(false)
	public void testD_countAuditing() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//testA gera 8 mensagens e o testC gera mais 8
		assertEquals(16, Subscriber.testCount);
	}
}
