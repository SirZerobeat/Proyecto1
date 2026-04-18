package com.test.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.dto.EnhancedPokemonDetail;
import com.test.demo.service.PokemonService;

@RestController
@RequestMapping("/api")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemon/{name}")
    public ResponseEntity<EnhancedPokemonDetail> getPokemon(@PathVariable String name) {
        try {
            EnhancedPokemonDetail pokemon = pokemonService.getPokemonByName(name);
            return ResponseEntity.ok(pokemon);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
