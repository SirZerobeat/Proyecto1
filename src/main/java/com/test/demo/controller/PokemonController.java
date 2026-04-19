package com.test.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.dto.EnhancedPokemonDetail;
import com.test.demo.dto.MoveLearnInfo;
import com.test.demo.service.PokemonService;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/pokemon/{name}/moves")
    public ResponseEntity<Map<String, List<MoveLearnInfo>>> getPokemonMoves(@PathVariable String name) {
        try {
            int pokemonId = Integer.parseInt(name);
            Map<String, List<MoveLearnInfo>> moves = pokemonService.getMovesByPokemonId(pokemonId);
            return ResponseEntity.ok(moves);
        } catch (NumberFormatException e) {
            // If not a number, try to fetch by name first
            try {
                EnhancedPokemonDetail pokemon = pokemonService.getPokemonByName(name);
                Map<String, List<MoveLearnInfo>> moves = pokemonService.getMovesByPokemonId(pokemon.getId());
                return ResponseEntity.ok(moves);
            } catch (Exception ex) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

