package br.com.star.wars.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest.ApiSortDirection;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import br.com.star.wars.model.Jedi;

@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
public class JediController {

	@GetMapping
	public ApiCollectionResponse<Jedi> getAll(ApiSortRequest sort) throws IOException {
		// Recupera o arquivos JSON com a lista de Jedis.
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		File file = new File(classLoader.getResource("Jedis.json").getFile());

		// Converte o JSON recuperado para List<Jedi>.
		ObjectMapper mapper = new ObjectMapper();
		List<Jedi> jedis = mapper.readValue(file,
				mapper.getTypeFactory().constructCollectionType(List.class, Jedi.class));

		// Ordena a lista de Jedis.
		this.sortList(jedis, sort.getSort());
		
		return ApiCollectionResponse.of(jedis);
	}

	private void sortList(List<Jedi> jedis, Map<String, ApiSortDirection> sort) {
		// Converte o Map de ordenacao para List, assim poderemos navegar entre
		// a lista de valores de forma reversa para aplicar a ordenacao na
		// lista de Jedis.
		List<Map.Entry<String, ApiSortDirection>> keys;
		keys = new ArrayList<Map.Entry<String, ApiSortDirection>>(sort.entrySet());

		for (int i = keys.size() - 1; i >= 0; i--) {
			ApiSortDirection direction = keys.get(i).getValue();

			switch (keys.get(i).getKey()) {
			case "name":
				this.sortListByName(jedis, direction);
				break;
			case "gender":
				this.sortListByGender(jedis, direction);
				break;
			}
		}
	}

	private void sortListByName(List<Jedi> jedis, ApiSortDirection direction) {
		Comparator<Jedi> compareByName = (Jedi j1, Jedi j2) -> j1.getName().compareTo(j2.getName());

		if (direction == ApiSortDirection.ASC) {
			jedis.sort(compareByName);
		} else {
			jedis.sort(compareByName.reversed());
		}
	}

	private void sortListByGender(List<Jedi> jedis, ApiSortDirection direction) {
		Comparator<Jedi> compareByGender = (Jedi j1, Jedi j2) -> j1.getGender().compareTo(j2.getGender());

		if (direction == ApiSortDirection.ASC) {
			jedis.sort(compareByGender);
		} else {
			jedis.sort(compareByGender.reversed());
		}
	}

}
