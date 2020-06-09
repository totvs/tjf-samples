package br.com.star.wars.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.sgdp.config.SGDPMetadata;
import com.totvs.tjf.sgdp.services.data.SGDPDataCommand;
import com.totvs.tjf.sgdp.services.data.SGDPDataResponse;
import com.totvs.tjf.sgdp.services.data.SGDPDataService;

import br.com.star.wars.model.Jedi;
import br.com.star.wars.model.JediRepository;

@Component
@Transactional
public class SWDataService implements SGDPDataService {

	@Autowired
	private JediRepository jediRepository;
	
	@Override
	public SGDPDataResponse execute(SGDPDataCommand command, SGDPMetadata metadata) {
		int identification = Integer.parseInt(command.getIdentifiers().get("identification"));
		List <Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("***** DATA COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		SGDPDataResponse response = new SGDPDataResponse(command);
		response.getData().put("data", mapper.valueToTree(list));
		return response;
	}

}
