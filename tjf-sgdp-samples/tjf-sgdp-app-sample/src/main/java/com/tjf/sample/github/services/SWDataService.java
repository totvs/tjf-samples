package com.tjf.sample.github.services;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.model.Jedi;
import com.tjf.sample.github.model.JediRepository;
import com.totvs.sgdp.sdk.config.SGDPMetadata;
import com.totvs.sgdp.sdk.services.data.SGDPDataCommand;
import com.totvs.sgdp.sdk.services.data.SGDPDataService;

@Component
@Transactional
public class SWDataService extends SGDPDataService {

	@Autowired
	private JediRepository jediRepository;

	@Override
	public void execute(SGDPDataCommand command, SGDPMetadata metadata) {
		String identification = command.getIdentifiers().get("identification");
		List<Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("***** DATA COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		addData(list, metadata);
	}

}
