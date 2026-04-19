package com.test.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.test.demo.dto.AbilityDetail;
import com.test.demo.dto.EnhancedPokemonDetail;
import com.test.demo.dto.EvolutionDetail;
import com.test.demo.dto.MoveDetail;
import com.test.demo.dto.MoveLearnInfo;
import com.test.demo.dto.PokemonSpeciesInfo;
import com.test.demo.dto.StatDetail;
import com.test.demo.dto.TypeEffectiveness;

@Service
public class PokemonService {
    private final RestClient restClient;
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    // In-memory cache with TTL (1 hour)
    private final Map<String, CachedPokemon> cache = new ConcurrentHashMap<>();
    private final long CACHE_TTL_MINUTES = 60;

    // Thread pool for parallel API calls
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public PokemonService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(POKEAPI_BASE_URL).build();
    }

    public EnhancedPokemonDetail getPokemonByName(String name) {
        String cacheKey = name.toLowerCase();

        // Check cache first
        CachedPokemon cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            return cached.data;
        }

        try {
            // Fetch Pokemon data
            Map<String, Object> pokemonData = restClient.get()
                    .uri("/pokemon/{name}", cacheKey)
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

            // Create base detail object
            EnhancedPokemonDetail detail = new EnhancedPokemonDetail();
            detail.setId(id);
            detail.setName(pokemonName);
            detail.setImageUrl(imageUrl);
            detail.setOfficialArtwork(officialArtwork);
            detail.setHeight(height);
            detail.setWeight(weight);
            detail.setTypes(types);
            detail.setStats(stats);

            // Fetch species data in parallel with other API calls
            Map<String, Object> speciesDataTemp = null;
            String description = null;
            String generation = null;

            try {
                speciesDataTemp = restClient.get()
                        .uri("/pokemon-species/{id}", id)
                        .retrieve()
                        .body(Map.class);

                if (speciesDataTemp != null) {
                    List<Map<String, Object>> flavorTexts = (List<Map<String, Object>>) speciesDataTemp.get("flavor_text_entries");
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

                    Map<String, Object> genData = (Map<String, Object>) speciesDataTemp.get("generation");
                    if (genData != null) {
                        generation = (String) genData.get("name");
                    }
                }
            } catch (Exception e) {
                // Continue without species data
            }

            detail.setDescription(description != null ? description : "No description available");
            detail.setGeneration(generation);

            // Create final copies for use in lambdas
            final Map<String, Object> speciesData = speciesDataTemp;
            final List<String> typesFinal = types;
            final Map<String, Object> pokemonDataFinal = pokemonData;

            // Fetch enhanced data in parallel using CompletableFuture
            CompletableFuture<List<AbilityDetail>> abilityFuture = CompletableFuture.supplyAsync(
                    () -> fetchAbilityDetails(pokemonDataFinal, speciesData), executorService);

            CompletableFuture<TypeEffectiveness> typeFuture = CompletableFuture.supplyAsync(
                    () -> fetchTypeEffectiveness(typesFinal), executorService);

            CompletableFuture<List<EvolutionDetail>> evolutionFuture = CompletableFuture.supplyAsync(
                    () -> fetchEvolutionChain(pokemonDataFinal, speciesData), executorService);

            CompletableFuture<PokemonSpeciesInfo> speciesFuture = CompletableFuture.supplyAsync(
                    () -> fetchSpeciesInfo(speciesData), executorService);

            // Wait for all parallel tasks to complete (with timeout)
            CompletableFuture.allOf(abilityFuture, typeFuture, evolutionFuture, speciesFuture)
                    .get(10, TimeUnit.SECONDS);

            detail.setAbilities(abilityFuture.join());
            detail.setTypeEffectiveness(typeFuture.join());
            detail.setEvolutions(evolutionFuture.join());
            detail.setSpeciesInfo(speciesFuture.join());

            // Lazy load moves - empty list initially (fetch when user requests)
            detail.setMoves(new HashMap<>());

            detail.setSprites(extractSprites((Map<String, Object>) pokemonData.get("sprites")));

            // Cache the result
            cache.put(cacheKey, new CachedPokemon(detail, System.currentTimeMillis()));

            return detail;

        } catch (RestClientException e) {
            throw new RuntimeException("Pokemon not found: " + name, e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Request timeout - try again", e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pokemon data: " + e.getMessage(), e);
        }
    }

    // New method to fetch moves on demand (lazy loading)
    public Map<String, List<MoveLearnInfo>> getMovesByPokemonId(int pokemonId) {
        try {
            // Fetch Pokemon data to get moves
            Map<String, Object> pokemonData = restClient.get()
                    .uri("/pokemon/{id}", pokemonId)
                    .retrieve()
                    .body(Map.class);

            if (pokemonData != null) {
                return fetchMovesParallel(pokemonData);
            }
        } catch (Exception e) {
            // Continue without moves
        }
        return new HashMap<>();
    }

    private Map<String, List<MoveLearnInfo>> fetchMovesParallel(Map<String, Object> pokemonData) {
        Map<String, List<MoveLearnInfo>> movesByMethod = new ConcurrentHashMap<>();
        movesByMethod.put("level-up", Collections.synchronizedList(new ArrayList<>()));
        movesByMethod.put("machine", Collections.synchronizedList(new ArrayList<>()));
        movesByMethod.put("tutor", Collections.synchronizedList(new ArrayList<>()));
        movesByMethod.put("egg", Collections.synchronizedList(new ArrayList<>()));

        try {
            List<Map<String, Object>> movesList = (List<Map<String, Object>>) pokemonData.get("moves");

            // Process moves in parallel batches
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (Map<String, Object> moveData : movesList) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Map<String, Object> moveInfo = (Map<String, Object>) moveData.get("move");
                        String moveName = (String) moveInfo.get("name");
                        List<Map<String, Object>> versionDetails = (List<Map<String, Object>>) moveData.get("version_group_details");

                        if (versionDetails == null || versionDetails.isEmpty()) return;

                        Map<String, Object> latestVersion = versionDetails.get(versionDetails.size() - 1);
                        Map<String, Object> learnMethod = (Map<String, Object>) latestVersion.get("move_learn_method");
                        String methodName = (String) learnMethod.get("name");
                        Integer level = (Integer) latestVersion.get("level_learned_at");

                        Map<String, Object> moveDetail = restClient.get()
                                .uri("/move/{name}", moveName)
                                .retrieve()
                                .body(Map.class);

                        MoveDetail detail = extractMoveDetail(moveDetail, moveName);
                        MoveLearnInfo learnInfo = new MoveLearnInfo();
                        learnInfo.setMove(detail);
                        learnInfo.setLearnMethod(methodName);
                        learnInfo.setLevel(level);

                        if ("machine".equals(methodName)) {
                            List<Map<String, Object>> machines = (List<Map<String, Object>>) moveDetail.get("machines");
                            String machineType = "TM";
                            if (machines != null && !machines.isEmpty()) {
                                Map<String, Object> latestMachine = machines.get(machines.size() - 1);
                                Map<String, Object> versionGroupData = (Map<String, Object>) latestMachine.get("version_group");
                                String versionGroupName = (String) versionGroupData.get("name");
                                machineType = determineMachineTypeByVersion(versionGroupName);
                                Map<String, Object> machineItemData = (Map<String, Object>) latestMachine.get("item");
                                if (machineItemData != null) {
                                    String itemName = (String) machineItemData.get("name");
                                    if (itemName != null && itemName.matches(".*\\d+.*")) {
                                        machineType = itemName.toUpperCase();
                                    }
                                }
                            }
                            learnInfo.setMachineType(machineType);
                        }

                        List<MoveLearnInfo> moves = movesByMethod.get(methodName);
                        if (moves != null) {
                            moves.add(learnInfo);
                        }
                    } catch (Exception e) {
                        // Continue without this move
                    }
                }, executorService);

                futures.add(future);
            }

            // Wait for all move fetches to complete (with timeout)
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .get(30, TimeUnit.SECONDS);

        } catch (Exception e) {
            // Continue without moves
        }

        return movesByMethod;
    }

    private List<AbilityDetail> fetchAbilityDetails(Map<String, Object> pokemonData, Map<String, Object> speciesData) {
        List<AbilityDetail> abilities = new ArrayList<>();

        try {
            List<Map<String, Object>> abilitiesList = (List<Map<String, Object>>) pokemonData.get("abilities");
            for (Map<String, Object> abilityData : abilitiesList) {
                Map<String, Object> abilityInfo = (Map<String, Object>) abilityData.get("ability");
                String abilityName = (String) abilityInfo.get("name");
                boolean isHidden = (boolean) abilityData.get("is_hidden");

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
                    abilities.add(new AbilityDetail(abilityName, isHidden, "No description available", ""));
                }
            }
        } catch (Exception e) {
            // Continue without ability details
        }

        return abilities;
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

    private String determineMachineTypeByVersion(String versionGroupName) {
        if (versionGroupName == null) return "TM";
        if (versionGroupName.contains("scarlet") || versionGroupName.contains("violet")) {
            return "TM (Latest)";
        }
        if (versionGroupName.contains("sword") || versionGroupName.contains("shield")) {
            return "TM";
        }
        if (versionGroupName.contains("lets-go")) {
            return "TM/HM";
        }
        if (versionGroupName.contains("sun") || versionGroupName.contains("moon")) {
            return "TM";
        }
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
                // Get the base pokemon ID from the chain
                int currentPokemonId = ((Number) pokemonData.get("id")).intValue();
                parseEvolutionChain(chain, null, evolutions, currentPokemonId);
            }
        } catch (Exception e) {
            // Continue without evolutions
        }

        return evolutions;
    }

    private void parseEvolutionChain(Map<String, Object> chain, String evolvesFrom,
                                     List<EvolutionDetail> result, int currentPokemonId) {
        try {
            if (chain == null) return;

            Map<String, Object> species = (Map<String, Object>) chain.get("species");
            if (species == null) return;

            String speciesName = (String) species.get("name");
            String speciesUrl = (String) species.get("url");
            int speciesId = extractIdFromUrl(speciesUrl);

            if (speciesName == null) return;

            // Only add evolution if it's different from current pokemon (using ID)
            if (evolvesFrom != null && speciesId != currentPokemonId) {
                EvolutionDetail detail = new EvolutionDetail();
                detail.setEvolvesFrom(evolvesFrom);
                detail.setEvolvesTo(speciesName);

                List<Map<String, Object>> evolutionDetails = (List<Map<String, Object>>) chain.get("evolution_details");
                String method = "Unknown";
                Integer minLevel = null;
                String item = null;

                if (evolutionDetails != null && !evolutionDetails.isEmpty()) {
                    Map<String, Object> evoDetail = evolutionDetails.get(0);

                    Map<String, Object> triggerMap = (Map<String, Object>) evoDetail.get("trigger");
                    if (triggerMap != null) {
                        String trigger = (String) triggerMap.get("name");
                        method = trigger != null ? trigger : "Unknown";
                    }

                    Object minLevelObj = evoDetail.get("min_level");
                    if (minLevelObj != null) {
                        minLevel = ((Number) minLevelObj).intValue();
                    }

                    Map<String, Object> itemMap = (Map<String, Object>) evoDetail.get("item");
                    if (itemMap != null) {
                        item = (String) itemMap.get("name");
                    }
                }

                detail.setMethod(method);
                detail.setMinLevel(minLevel);
                detail.setItem(item);

                StringBuilder triggerDesc = new StringBuilder();
                triggerDesc.append(capitalizeFirst(method));

                if (minLevel != null) {
                    triggerDesc.append(" at Lv. ").append(minLevel);
                }
                if (item != null) {
                    triggerDesc.append(" with ").append(capitalizeFirst(item.replace("-", " ")));
                }

                detail.setTrigger(triggerDesc.toString());
                result.add(detail);
            }

            List<Map<String, Object>> evolvesTo = (List<Map<String, Object>>) chain.get("evolves_to");
            if (evolvesTo != null) {
                for (Map<String, Object> nextEvolution : evolvesTo) {
                    parseEvolutionChain(nextEvolution, speciesName, result, currentPokemonId);
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
        if (genderRate == -1) return 0;
        if (genderRate == 0) return 100;
        if (genderRate == 8) return 0;
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

    private int extractIdFromUrl(String url) {
        if (url == null || url.isEmpty()) return 0;
        try {
            String[] parts = url.split("/");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (Exception e) {
            return 0;
        }
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Cache data structure
    private static class CachedPokemon {
        EnhancedPokemonDetail data;
        long timestamp;

        CachedPokemon(EnhancedPokemonDetail data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > 60 * 60 * 1000; // 1 hour TTL
        }
    }
}
