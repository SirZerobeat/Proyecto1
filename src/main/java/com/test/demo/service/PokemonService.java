package com.test.demo.service;

import com.test.demo.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PokemonService {
    private final RestClient restClient;
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    public PokemonService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(POKEAPI_BASE_URL).build();
    }

    public EnhancedPokemonDetail getPokemonByName(String name) {
        try {
            // Fetch Pokemon data
            Map<String, Object> pokemonData = restClient.get()
                    .uri("/pokemon/{name}", name.toLowerCase())
                    .retrieve()
                    .body(Map.class);

            if (pokemonData == null) {
                throw new RuntimeException("Pokemon not found");
            }

            int id = ((Number) pokemonData.get("id")).intValue();
            String pokemonName = (String) pokemonData.get("name");
            int height = ((Number) pokemonData.get("height")).intValue();
            int weight = ((Number) pokemonData.get("weight")).intValue();

            // Get images
            Map<String, Object> sprites = (Map<String, Object>) pokemonData.get("sprites");
            String imageUrl = (String) sprites.get("front_default");
            String officialArtwork = null;

            try {
                Map<String, Object> other = (Map<String, Object>) sprites.get("other");
                if (other != null) {
                    Map<String, Object> officialArtworkData = (Map<String, Object>) other.get("official-artwork");
                    if (officialArtworkData != null) {
                        officialArtwork = (String) officialArtworkData.get("front_default");
                    }
                }
            } catch (Exception e) {
                // Continue without official artwork
            }

            if (imageUrl == null) {
                imageUrl = officialArtwork;
            }

            // Get types
            List<String> types = new ArrayList<>();
            List<Map<String, Object>> typesList = (List<Map<String, Object>>) pokemonData.get("types");
            for (Map<String, Object> typeData : typesList) {
                Map<String, Object> typeInfo = (Map<String, Object>) typeData.get("type");
                types.add((String) typeInfo.get("name"));
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

            // Fetch species data for descriptions and more
            Map<String, Object> speciesData = null;
            String description = null;
            String generation = null;

            try {
                speciesData = restClient.get()
                        .uri("/pokemon-species/{id}", id)
                        .retrieve()
                        .body(Map.class);

                if (speciesData != null) {
                    List<Map<String, Object>> flavorTexts = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
                    if (flavorTexts != null && !flavorTexts.isEmpty()) {
                        for (Map<String, Object> flavor : flavorTexts) {
                            Map<String, Object> lang = (Map<String, Object>) flavor.get("language");
                            if ("en".equals(lang.get("name"))) {
                                String tempDescription = (String) flavor.get("flavor_text");
                                if (tempDescription != null) {
                                    tempDescription = tempDescription.replace("\n", " ").replace("\f", " ");
                                    description = tempDescription;
                                    break;
                                }
                            }
                        }
                    }

                    // Get generation
                    Map<String, Object> genData = (Map<String, Object>) speciesData.get("generation");
                    if (genData != null) {
                        generation = (String) genData.get("name");
                    }
                }
            } catch (Exception e) {
                // Continue without species data
            }

            // Create the enhanced detail object
            EnhancedPokemonDetail detail = new EnhancedPokemonDetail();
            detail.setId(id);
            detail.setName(pokemonName);
            detail.setImageUrl(imageUrl);
            detail.setOfficialArtwork(officialArtwork);
            detail.setHeight(height);
            detail.setWeight(weight);
            detail.setTypes(types);
            detail.setStats(stats);
            detail.setDescription(description != null ? description : "No description available");
            detail.setGeneration(generation);

            // Fetch enhanced data (abilities, moves, evolutions, type effectiveness, species info)
            detail.setAbilities(fetchAbilityDetails(pokemonData, speciesData));
            detail.setMoves(fetchMoveDetails(pokemonData));
            detail.setTypeEffectiveness(fetchTypeEffectiveness(types));
            detail.setEvolutions(fetchEvolutionChain(pokemonData, speciesData));
            detail.setSpeciesInfo(fetchSpeciesInfo(speciesData));
            detail.setSprites(extractSprites(sprites));

            return detail;

        } catch (RestClientException e) {
            throw new RuntimeException("Pokemon not found: " + name, e);
        }
    }

    private List<AbilityDetail> fetchAbilityDetails(Map<String, Object> pokemonData, Map<String, Object> speciesData) {
        List<AbilityDetail> abilities = new ArrayList<>();

        try {
            List<Map<String, Object>> abilitiesList = (List<Map<String, Object>>) pokemonData.get("abilities");
            for (Map<String, Object> abilityData : abilitiesList) {
                Map<String, Object> abilityInfo = (Map<String, Object>) abilityData.get("ability");
                String abilityName = (String) abilityInfo.get("name");
                boolean isHidden = (boolean) abilityData.get("is_hidden");

                // Fetch ability details from API
                try {
                    Map<String, Object> abilityDetail = restClient.get()
                            .uri("/ability/{name}", abilityName)
                            .retrieve()
                            .body(Map.class);

                    String description = extractEnglishDescription(
                            (List<Map<String, Object>>) abilityDetail.get("effect_entries"));
                    String effect = extractEnglishEffect(
                            (List<Map<String, Object>>) abilityDetail.get("flavor_text_entries"));

                    abilities.add(new AbilityDetail(abilityName, isHidden, description, effect));
                } catch (Exception e) {
                    // Fallback without description
                    abilities.add(new AbilityDetail(abilityName, isHidden, "No description available", ""));
                }
            }
        } catch (Exception e) {
            // Continue without ability details
        }

        return abilities;
    }

    private Map<String, List<MoveLearnInfo>> fetchMoveDetails(Map<String, Object> pokemonData) {
        Map<String, List<MoveLearnInfo>> movesByMethod = new HashMap<>();
        movesByMethod.put("level-up", new ArrayList<>());
        movesByMethod.put("machine", new ArrayList<>());
        movesByMethod.put("tutor", new ArrayList<>());
        movesByMethod.put("egg", new ArrayList<>());

        try {
            List<Map<String, Object>> movesList = (List<Map<String, Object>>) pokemonData.get("moves");
            for (Map<String, Object> moveData : movesList) {
                Map<String, Object> moveInfo = (Map<String, Object>) moveData.get("move");
                String moveName = (String) moveInfo.get("name");
                List<Map<String, Object>> versionDetails = (List<Map<String, Object>>) moveData.get("version_group_details");

                if (versionDetails == null || versionDetails.isEmpty()) continue;

                // Get the latest version detail
                Map<String, Object> latestVersion = versionDetails.get(versionDetails.size() - 1);
                Map<String, Object> learnMethod = (Map<String, Object>) latestVersion.get("move_learn_method");
                String methodName = (String) learnMethod.get("name");
                Integer level = (Integer) latestVersion.get("level_learned_at");

                try {
                    // Fetch move details
                    Map<String, Object> moveDetail = restClient.get()
                            .uri("/move/{name}", moveName)
                            .retrieve()
                            .body(Map.class);

                    MoveDetail detail = extractMoveDetail(moveDetail, moveName);
                    MoveLearnInfo learnInfo = new MoveLearnInfo();
                    learnInfo.setMove(detail);
                    learnInfo.setLearnMethod(methodName);
                    learnInfo.setLevel(level);

                    // Determine machine type if applicable
                    if ("machine".equals(methodName)) {
                        String machineType = determineMachineType(moveName, (Map<String, Object>) moveDetail.get("machines"));
                        learnInfo.setMachineType(machineType);
                    }

                    List<MoveLearnInfo> moves = movesByMethod.getOrDefault(methodName, new ArrayList<>());
                    moves.add(learnInfo);
                    movesByMethod.put(methodName, moves);

                } catch (Exception e) {
                    // Continue without this move
                }
            }
        } catch (Exception e) {
            // Continue without moves
        }

        return movesByMethod;
    }

    private MoveDetail extractMoveDetail(Map<String, Object> moveData, String moveName) {
        int moveId = ((Number) moveData.get("id")).intValue();
        String type = ((Map<String, Object>) moveData.get("type")).get("name").toString();
        Integer power = (Integer) moveData.get("power");
        Integer accuracy = (Integer) moveData.get("accuracy");
        Integer pp = (Integer) moveData.get("pp");
        String damageClass = ((Map<String, Object>) moveData.get("damage_class")).get("name").toString();
        int priority = ((Number) moveData.get("priority")).intValue();
        Integer effectChance = (Integer) moveData.get("effect_chance");
        String target = ((Map<String, Object>) moveData.get("target")).get("name").toString();

        String description = extractEnglishDescription(
                (List<Map<String, Object>>) moveData.get("effect_entries"));
        String effect = extractEnglishEffect(
                (List<Map<String, Object>>) moveData.get("flavor_text_entries"));

        return new MoveDetail(moveId, moveName, type, power, accuracy, pp != null ? pp : 0,
                damageClass, description, priority, effect, effectChance, target);
    }

    private String determineMachineType(String moveName, Map<String, Object> machines) {
        if (machines == null) return "TM";
        // Simple heuristic - in real implementation, would need to check generation
        return "TM";
    }

    private TypeEffectiveness fetchTypeEffectiveness(List<String> types) {
        List<String> weaknesses = new ArrayList<>();
        List<String> resistances = new ArrayList<>();
        List<String> immunities = new ArrayList<>();
        List<String> damageTo = new ArrayList<>();

        try {
            for (String type : types) {
                Map<String, Object> typeData = restClient.get()
                        .uri("/type/{name}", type)
                        .retrieve()
                        .body(Map.class);

                if (typeData != null) {
                    Map<String, Object> damageRelations = (Map<String, Object>) typeData.get("damage_relations");

                    addTypeNamesFromUrls(
                            (List<Map<String, Object>>) damageRelations.get("double_damage_from"),
                            weaknesses);
                    addTypeNamesFromUrls(
                            (List<Map<String, Object>>) damageRelations.get("half_damage_from"),
                            resistances);
                    addTypeNamesFromUrls(
                            (List<Map<String, Object>>) damageRelations.get("no_damage_from"),
                            immunities);
                    addTypeNamesFromUrls(
                            (List<Map<String, Object>>) damageRelations.get("double_damage_to"),
                            damageTo);
                }
            }
        } catch (Exception e) {
            // Continue without type effectiveness
        }

        // Remove duplicates
        weaknesses = new ArrayList<>(new HashSet<>(weaknesses));
        resistances = new ArrayList<>(new HashSet<>(resistances));
        immunities = new ArrayList<>(new HashSet<>(immunities));
        damageTo = new ArrayList<>(new HashSet<>(damageTo));

        return new TypeEffectiveness(weaknesses, resistances, immunities, damageTo);
    }

    private void addTypeNamesFromUrls(List<Map<String, Object>> typesList, List<String> target) {
        if (typesList == null) return;
        for (Map<String, Object> typeData : typesList) {
            String name = (String) typeData.get("name");
            if (name != null && !target.contains(name)) {
                target.add(name);
            }
        }
    }

    private List<EvolutionDetail> fetchEvolutionChain(Map<String, Object> pokemonData, Map<String, Object> speciesData) {
        List<EvolutionDetail> evolutions = new ArrayList<>();

        try {
            if (speciesData == null) return evolutions;

            Map<String, Object> evolutionChainUrl = (Map<String, Object>) speciesData.get("evolution_chain");
            if (evolutionChainUrl == null) return evolutions;

            String chainUrl = (String) evolutionChainUrl.get("url");
            if (chainUrl == null) return evolutions;

            Map<String, Object> chainData = restClient.get()
                    .uri(chainUrl.replace(POKEAPI_BASE_URL, ""))
                    .retrieve()
                    .body(Map.class);

            if (chainData != null) {
                Map<String, Object> chain = (Map<String, Object>) chainData.get("chain");
                parseEvolutionChain(chain, null, evolutions);
            }
        } catch (Exception e) {
            // Continue without evolutions
        }

        return evolutions;
    }

    private void parseEvolutionChain(Map<String, Object> chain, String evolvesFrom,
                                     List<EvolutionDetail> result) {
        try {
            Map<String, Object> species = (Map<String, Object>) chain.get("species");
            String speciesName = (String) species.get("name");

            if (evolvesFrom != null) {
                EvolutionDetail detail = new EvolutionDetail();
                detail.setEvolvesFrom(evolvesFrom);
                detail.setEvolvesTo(speciesName);

                Map<String, Object> evolutionDetails = (Map<String, Object>) chain.get("evolution_details");
                if (evolutionDetails != null && !evolutionDetails.isEmpty()) {
                    Map<String, Object> detail_map = (Map<String, Object>) ((List<?>) evolutionDetails).get(0);
                    detail.setMethod((String) detail_map.get("trigger"));
                    detail.setMinLevel((Integer) detail_map.get("min_level"));
                    if (detail_map.get("item") != null) {
                        detail.setItem(((Map<String, Object>) detail_map.get("item")).get("name").toString());
                    }
                }

                result.add(detail);
            }

            List<Map<String, Object>> evolvesTo = (List<Map<String, Object>>) chain.get("evolves_to");
            if (evolvesTo != null) {
                for (Map<String, Object> nextEvolution : evolvesTo) {
                    parseEvolutionChain(nextEvolution, speciesName, result);
                }
            }
        } catch (Exception e) {
            // Continue parsing
        }
    }

    private PokemonSpeciesInfo fetchSpeciesInfo(Map<String, Object> speciesData) {
        if (speciesData == null) return new PokemonSpeciesInfo();

        try {
            int genderRate = ((Number) speciesData.get("gender_rate")).intValue();
            double malePercentage = calculateMalePercentage(genderRate);
            double femalePercentage = 100 - malePercentage;

            List<String> eggGroups = new ArrayList<>();
            List<Map<String, Object>> eggGroupsList = (List<Map<String, Object>>) speciesData.get("egg_groups");
            if (eggGroupsList != null) {
                for (Map<String, Object> group : eggGroupsList) {
                    eggGroups.add((String) group.get("name"));
                }
            }

            int hatchSteps = ((Number) speciesData.get("hatch_counter")).intValue();
            int baseHappiness = ((Number) speciesData.get("base_happiness")).intValue();

            String growthRate = "";
            Map<String, Object> growthRateData = (Map<String, Object>) speciesData.get("growth_rate");
            if (growthRateData != null) {
                growthRate = (String) growthRateData.get("name");
            }

            int captureRate = ((Number) speciesData.get("capture_rate")).intValue();

            return new PokemonSpeciesInfo(malePercentage, femalePercentage, eggGroups,
                    hatchSteps, baseHappiness, growthRate, captureRate);
        } catch (Exception e) {
            return new PokemonSpeciesInfo();
        }
    }

    private double calculateMalePercentage(int genderRate) {
        if (genderRate == -1) return 0; // Genderless
        if (genderRate == 0) return 100; // All male
        if (genderRate == 8) return 0; // All female
        return (8 - genderRate) * 12.5;
    }

    private String extractEnglishDescription(List<Map<String, Object>> entries) {
        if (entries == null) return "No description available";
        for (Map<String, Object> entry : entries) {
            Map<String, Object> lang = (Map<String, Object>) entry.get("language");
            if (lang != null && "en".equals(lang.get("name"))) {
                String text = (String) entry.get("short_effect");
                if (text == null) {
                    text = (String) entry.get("effect");
                }
                if (text != null) {
                    return text.replace("\n", " ");
                }
            }
        }
        return "No description available";
    }

    private String extractEnglishEffect(List<Map<String, Object>> entries) {
        if (entries == null) return "";
        for (Map<String, Object> entry : entries) {
            Map<String, Object> lang = (Map<String, Object>) entry.get("language");
            if (lang != null && "en".equals(lang.get("name"))) {
                String text = (String) entry.get("flavor_text");
                if (text != null) {
                    return text.replace("\n", " ").replace("\f", " ");
                }
            }
        }
        return "";
    }

    private Map<String, String> extractSprites(Map<String, Object> sprites) {
        Map<String, String> result = new HashMap<>();
        try {
            if (sprites.containsKey("front_default")) {
                result.put("front_default", (String) sprites.get("front_default"));
            }
            if (sprites.containsKey("back_default")) {
                result.put("back_default", (String) sprites.get("back_default"));
            }
            if (sprites.containsKey("front_shiny")) {
                result.put("front_shiny", (String) sprites.get("front_shiny"));
            }
            if (sprites.containsKey("back_shiny")) {
                result.put("back_shiny", (String) sprites.get("back_shiny"));
            }
        } catch (Exception e) {
            // Continue
        }
        return result;
    }
}
