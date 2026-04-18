package com.test.demo.dto;

import java.util.List;
import java.util.Map;

public class EnhancedPokemonDetail {
    // Original fields from PokemonDetail
    private int id;
    private String name;
    private String imageUrl;
    private int height;
    private int weight;
    private List<String> types;
    private List<AbilityDetail> abilities;
    private List<StatDetail> stats;
    private String description;

    // Enhanced fields
    private List<EvolutionDetail> evolutions;
    private Map<String, List<MoveLearnInfo>> moves;
    private TypeEffectiveness typeEffectiveness;
    private PokemonSpeciesInfo speciesInfo;
    private String generation;
    private String officialArtwork;
    private Map<String, String> sprites;

    public EnhancedPokemonDetail() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public List<String> getTypes() { return types; }
    public void setTypes(List<String> types) { this.types = types; }

    public List<AbilityDetail> getAbilities() { return abilities; }
    public void setAbilities(List<AbilityDetail> abilities) { this.abilities = abilities; }

    public List<StatDetail> getStats() { return stats; }
    public void setStats(List<StatDetail> stats) { this.stats = stats; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<EvolutionDetail> getEvolutions() { return evolutions; }
    public void setEvolutions(List<EvolutionDetail> evolutions) { this.evolutions = evolutions; }

    public Map<String, List<MoveLearnInfo>> getMoves() { return moves; }
    public void setMoves(Map<String, List<MoveLearnInfo>> moves) { this.moves = moves; }

    public TypeEffectiveness getTypeEffectiveness() { return typeEffectiveness; }
    public void setTypeEffectiveness(TypeEffectiveness typeEffectiveness) { this.typeEffectiveness = typeEffectiveness; }

    public PokemonSpeciesInfo getSpeciesInfo() { return speciesInfo; }
    public void setSpeciesInfo(PokemonSpeciesInfo speciesInfo) { this.speciesInfo = speciesInfo; }

    public String getGeneration() { return generation; }
    public void setGeneration(String generation) { this.generation = generation; }

    public String getOfficialArtwork() { return officialArtwork; }
    public void setOfficialArtwork(String officialArtwork) { this.officialArtwork = officialArtwork; }

    public Map<String, String> getSprites() { return sprites; }
    public void setSprites(Map<String, String> sprites) { this.sprites = sprites; }
}
