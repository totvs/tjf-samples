package br.com.star.wars.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.sgdp.services.mask.SGDPMaskCommand;
import com.totvs.tjf.sgdp.services.mask.SGDPMaskException;
import com.totvs.tjf.sgdp.services.mask.SGDPMaskResponse;
import com.totvs.tjf.sgdp.services.mask.SGDPMaskService;

import br.com.star.wars.model.Jedi;
import br.com.star.wars.model.JediRepository;

@Component
@Transactional
public class SWMaskService implements SGDPMaskService {

	@Autowired
	private JediRepository jediRepository;
	
	@Override
	public SGDPMaskResponse execute(SGDPMaskCommand command) {
		int identification = Integer.parseInt(command.getIdentifiers().get("identification"));
		List <Jedi> list = jediRepository.findByIdentificationEquals(identification);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("***** MASK COMMAND *****");
		try {
			System.out.println(mapper.writeValueAsString(list));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		list.forEach((jedi) -> {
			try {
				mask(jedi, command.getMetadata());
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
