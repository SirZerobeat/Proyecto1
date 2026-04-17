package com.test.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.test.demo.dto.PokemonDetail;
import com.test.demo.dto.StatDetail;

@Service
public class PokemonService {
    private final RestClient restClient;
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    public PokemonService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(POKEAPI_BASE_URL).build();
    }

    public PokemonDetail getPokemonByName(String name) {
        try {
            // Fetch Pokemon data
            Map<String, Object> pokemonData = restClient.get()
                    .uri("/pokemon/{name}", name.toLowerCase())
                    .retrieve()
                    .body(Map.class);

            if (pokemonData == null) {
                throw new RuntimeException("Pokemon not found");
            }

            // Extract data
            int id = ((Number) pokemonData.get("id")).intValue();
            String pokemonName = (String) pokemonData.get("name");
            int height = ((Number) pokemonData.get("height")).intValue();
            int weight = ((Number) pokemonData.get("weight")).intValue();

            // Get image
            Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
            String imageUrl = (String) sprites.get("front_default");
            if (imageUrl == null) {
                imageUrl = (String) ((Map<String, Object>) sprites.get("other"))
                        .get("official-artwork");
            }

            // Get types
            List<String> types = new ArrayList<>();
            List<Map<String, Object>> typesList = (List<Map<String, Object>>) pokemonData.get("types");
            for (Map<String, Object> typeData : typesList) {
                Map<String, Object> typeInfo = (Map<String, Object>) typeData.get("type");
                types.add((String) typeInfo.get("name"));
            }

            // Get abilities
            List<String> abilities = new ArrayList<>();
            List<Map<String, Object>> abilitiesList = (List<Map<String, Object>>) pokemonData.get("abilities");
            for (Map<String, Object> abilityData : abilitiesList) {
                Map<String, Object> abilityInfo = (Map<String, Object>) abilityData.get("ability");
                abilities.add((String) abilityInfo.get("name"));
            }

            // Get stats
            List<StatDetail> stats = new ArrayList<>();
            List<Map<String, Object>> statsList = (List<Map<String, Object>>) pokemonData.get("stats");
            for (Map<String, Object> statData : statsList) {
                Map<String, Object> statInfo = (Map<String, Object>) statData.get("stat");
                String statName = (String) statInfo.get("name");
                int baseStat = ((Number) statData.get("base_stat")).intValue();
                stats.add(new StatDetail(statName, baseStat));
            }

            PokemonDetail detail = new PokemonDetail(id, pokemonName, imageUrl, height, weight, types, abilities, stats);

            // Try to fetch description from species
            try {
                Map<String, Object> speciesData = restClient.get()
                        .uri("/pokemon-species/{id}", id)
                        .retrieve()
                        .body(Map.class);

                if (speciesData != null) {
                    List<Map<String, Object>> flavorTexts = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
                    if (flavorTexts != null && !flavorTexts.isEmpty()) {
                        for (Map<String, Object> flavor : flavorTexts) {
                            Map<String, Object> lang = (Map<String, Object>) flavor.get("language");
                            if ("en".equals(lang.get("name"))) {
                                String description = (String) flavor.get("flavor_text");
                                description = description.replace("\n", " ").replace("\f", " ");
                                detail.setDescription(description);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // If we can't fetch species, it's okay, continue without description
            }

            return detail;
        } catch (RestClientException e) {
            throw new RuntimeException("Pokemon not found: " + name, e);
        }
    }
}
