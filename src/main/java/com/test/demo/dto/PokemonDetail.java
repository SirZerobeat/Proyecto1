package com.test.demo.dto;

import java.util.List;

public class PokemonDetail {
    private int id;
    private String name;
    private String imageUrl;
    private int height;
    private int weight;
    private List<String> types;
    private List<String> abilities;
    private List<StatDetail> stats;
    private String description;

    public PokemonDetail() {}

    public PokemonDetail(int id, String name, String imageUrl, int height, int weight,
                         List<String> types, List<String> abilities, List<StatDetail> stats) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.height = height;
        this.weight = weight;
        this.types = types;
        this.abilities = abilities;
        this.stats = stats;
    }

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

    public List<String> getAbilities() { return abilities; }
    public void setAbilities(List<String> abilities) { this.abilities = abilities; }

    public List<StatDetail> getStats() { return stats; }
    public void setStats(List<StatDetail> stats) { this.stats = stats; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
