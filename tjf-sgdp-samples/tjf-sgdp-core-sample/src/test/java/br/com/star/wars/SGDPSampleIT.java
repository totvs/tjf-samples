package br.com.star.wars;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarWarsServicesApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SGDPSampleIT {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void testA_getAll() throws Exception {
		mockMvc.perform(get("/sgpd/v1/jedis").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
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

}
