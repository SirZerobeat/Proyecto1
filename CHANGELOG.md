# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2026-04-17

### Added
- ✅ **Tabbed Interface** - Five comprehensive tabs: Overview, Stats, Moves, Evolution, Abilities
- ✅ **Type Effectiveness** - Display weaknesses, resistances, and immunities for all Pokémon types
- ✅ **Type-Specific Colors** - All 18 Pokémon types have official colors applied throughout the UI:
  - Fire (Orange), Water (Blue), Grass (Green), Electric (Yellow)
  - Rock (Brown), Dark (Gray-Brown), Fairy (Rose Pink), Psychic (Purple)
  - And 10 more types with authentic colors
- ✅ **Move Details** - Complete move information including:
  - Power, Accuracy, PP, Damage Class (Physical/Special/Status)
  - Priority, Target Type, Effect Chance
  - Learn methods: Level-up, Machine (TM/HM/MT/DT), Tutor, Egg
- ✅ **Evolution Chains** - Full evolution paths with:
  - Evolution conditions (Level, Item, Trade, etc.)
  - Pokémon images for each evolution (120x120px)
  - Properly formatted trigger descriptions
- ✅ **Ability Descriptions** - Detailed ability effects and in-game descriptions
- ✅ **Gender Ratios** - Display male/female percentages and genderless Pokémon
- ✅ **Species Information** - Egg groups, hatch steps, base happiness, growth rate
- ✅ **Dark Theme** - Elegant black/dark blue color scheme:
  - Background: `#1a1a2e` to `#16213e` gradient
  - Accents: Cyan (`#00d4ff`)
  - Better readability, reduced eye strain

### Improved
- ✅ **Machine Moves** - Now properly identifies latest Pokémon game moves:
  - Scarlet/Violet (Latest)
  - Sword/Shield
  - Lets Go (TM/HM)
  - Sun/Moon
- ✅ **Evolution Chain Display** - No duplicate Pokémon, proper hierarchy:
  - Current Pokémon with image at top
  - Arrow indicators for evolution flow
  - Evolution images below names
- ✅ **Move Type Display** - Type badges show color-coded types with full details
- ✅ **Type Effectiveness Section** - Color-coded weakness/resistance/immunity tags
- ✅ **Responsive Design** - Better mobile experience with dark theme
- ✅ **Error Handling** - Graceful fallbacks for missing data

### Fixed
- ✅ **Evolution Chain Parsing** - Fixed recursive parsing to avoid duplicates
- ✅ **Evolution Details Formatting** - Clear trigger descriptions (e.g., "Level Up at Lv. 16")
- ✅ **Machine Moves Fetching** - Properly retrieves TM/HM data from latest games
- ✅ **Type Effectiveness** - Correctly aggregates multi-type damage relations
- ✅ **API Error Handling** - Continues gracefully when optional endpoints fail

### Technical Details

#### New Backend DTOs
- `MoveDetail.java` - Move information with power, accuracy, priority, etc.
- `MoveLearnInfo.java` - Move learning method and metadata
- `EvolutionDetail.java` - Evolution conditions and triggers
- `TypeEffectiveness.java` - Type weaknesses, resistances, immunities
- `AbilityDetail.java` - Ability descriptions and effects
- `PokemonSpeciesInfo.java` - Gender ratios, breeding, happiness data
- `EnhancedPokemonDetail.java` - Master DTO combining all enhanced data

#### Enhanced Service Methods
- `fetchMoveDetails()` - Organizes moves by learn method with full details
- `fetchTypeEffectiveness()` - Calculates all type interactions
- `fetchAbilityDetails()` - Fetches and translates ability descriptions
- `fetchEvolutionChain()` - Recursively parses full evolution trees
- `fetchSpeciesInfo()` - Extracts breeding and species data
- `determineMachineTypeByVersion()` - Identifies machine type by game version

#### Frontend Improvements
- 19 new CSS classes for type-specific colors
- 120+ new CSS lines for dark theme styling
- Enhanced JavaScript with:
  - Tab management system
  - Dynamic type color application
  - Evolution image fetching
  - Improved data formatting

### Performance
- Single API response with all data aggregated
- Graceful degradation if optional endpoints fail
- Parallel processing of independent API calls
- Efficient DOM manipulation with batch updates

### Browser Support
- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Requires ES6+ support

### Dependencies
- Spring Boot 4.0.5
- Java 21
- PokeAPI v2 (https://pokeapi.co/api/v2/)

---

## [1.0.0] - 2026-04-17

### Initial Release
- ✅ Basic Pokémon search by name or ID
- ✅ Display official Pokémon images
- ✅ Show base stats with visual bars
- ✅ Display types and abilities
- ✅ Show height and weight
- ✅ Display Pokédex descriptions
- ✅ Responsive single-card design
- ✅ Real-time PokeAPI integration

---

**Development Status**: v1.1 Released  
**Next Version**: v1.2 (Future enhancements planned)

