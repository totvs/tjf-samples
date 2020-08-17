package com.tjf.sample.github.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.model.Jedi;
import com.tjf.sample.github.model.JediRepository;
import com.totvs.sgdp.sdk.config.SGDPMetadata;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskCommand;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskException;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskResponse;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskService;

@Component
@Transactional
public class SWMaskService implements SGDPMaskService {

	@Autowired
	private JediRepository jediRepository;

	@Override
	public SGDPMaskResponse execute(SGDPMaskCommand command, SGDPMetadata metadata) {
		int identification = Integer.parseInt(command.getIdentifiers().get("identification"));
		List<Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("***** MASK COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		list.forEach((jedi) -> {
			try {
				mask(jedi, metadata);
				try {
					System.out.println(mapper.writeValueAsString(jedi));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				jediRepository.save(jedi);
			} catch (SGDPMaskException e) {
				e.printStackTrace();
			}
		});
		SGDPMaskResponse response = new SGDPMaskResponse(command);
		response.setRows(list.size());
		return response;
	}

}
