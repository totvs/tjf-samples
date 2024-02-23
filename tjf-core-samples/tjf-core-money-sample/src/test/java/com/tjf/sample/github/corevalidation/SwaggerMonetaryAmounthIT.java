package com.tjf.sample.github.corevalidation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.tjf.sample.github.coremoney.CoreMoneyApplication;

@SpringBootTest(classes = CoreMoneyApplication.class)
@AutoConfigureMockMvc
public class SwaggerMonetaryAmounthIT {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void testSchemasMonetaryAmountApiRepresentation() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.components.schemas.AccountModel.properties.price.$ref", equalTo("#/components/schemas/MonetaryAmountApiRepresentation")))
				.andExpect(jsonPath("$.components.schemas.MonetaryAmountApiRepresentation.required[0]", equalTo("currency")))
				.andExpect(jsonPath("$.components.schemas.MonetaryAmountApiRepresentation.required[1]", equalTo("value")));
	}

}
