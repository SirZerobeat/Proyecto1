# 🔴 PokéDex - Pokémon Search Application

A comprehensive full-stack web application that allows users to search and explore detailed information about Pokémon using the public PokeAPI. Built with Spring Boot and modern web technologies, featuring a beautiful dark-themed interface with complete Pokémon data visualization.

## ✨ Features (v1.1)

### 🎮 Core Features
- 🔍 **Search Pokémon** by name or ID with real-time API integration
- 📊 **Comprehensive Data Display** with organized tabbed interface
- 🖼️ **Official Pokémon Images** with high-resolution artwork
- 📋 **Five Information Tabs**:
  - **Overview**: Physical stats, description, gender ratio, type effectiveness
  - **Stats**: Base stats with animated percentage bars
  - **Moves**: Level-up, Machine (TM/HM/MT/DT), Tutor, and Egg moves
  - **Evolution**: Evolution chains with images and conditions
  - **Abilities**: Detailed ability descriptions and effects

### 🎨 Visual Features
- 🌙 **Dark Elegant Theme** - Reduced brightness with cyan accents
- 🏷️ **Type-Specific Colors** - All 18 Pokémon types with official colors:
  - Applied to type badges, weaknesses, resistances, and move types
  - Fire (Orange), Water (Blue), Grass (Green), Electric (Yellow), etc.
- 📱 **Fully Responsive** - Desktop, tablet, and mobile optimized
- ✨ **Smooth Animations** - Fade-in and slide transitions

### 📊 Data Display
- **Type Effectiveness**
  - Weaknesses (takes 2x damage)
  - Resistances (takes 0.5x damage)
  - Immunities (takes 0x damage)
- **Move Information** with complete details:
  - Power, Accuracy, PP, Priority
  - Damage Class (Physical/Special/Status)
  - Target Type and Effect Chance
  - Learn methods with conditions
- **Evolution Chains** with:
  - Pokémon images for each evolution
  - Evolution conditions and levels
  - Item requirements and trade info
- **Ability Details**
  - Hidden ability indicators
  - Full effect descriptions
- **Species Information**
  - Gender ratios (male/female percentages)
  - Egg groups and hatch steps
  - Base happiness and growth rate

## 🛠️ Tech Stack

### Backend
- **Java 21** - Latest LTS version
- **Spring Boot 4.0.5** - Modern web framework
- **Spring Web MVC** - REST API development
- **RestClient** - HTTP client for API calls
- **Maven** - Build and dependency management

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Dark theme with gradients and animations
- **JavaScript (ES6+)** - Vanilla, no frameworks
- **Responsive Design** - Mobile-first approach

### External APIs
- **PokeAPI v2** - https://pokeapi.co/api/v2/
  - Pokémon data, moves, abilities, evolutions
  - Type effectiveness and species information

## 📋 Prerequisites

- **Java 21+** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/download)
- **Web Browser** - Chrome, Firefox, Safari, or Edge

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/SirZerobeat/Proyecto1.git
cd Proyecto1
git checkout features/v1.1  # Optional: for v1.1 features
```

### 2. Build the Project
```bash
./mvnw clean install
```

On Windows:
```cmd
mvnw.cmd clean install
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

On Windows:
```cmd
mvnw.cmd spring-boot:run
```

The application will start on **http://localhost:8081**

## 🎮 Usage Guide

### Searching for Pokémon

1. **Enter a Pokémon name** (case-insensitive)
   - Examples: `pikachu`, `Charizard`, `DRAGONITE`

2. **Or enter a Pokémon ID**
   - Examples: `25` (Pikachu), `6` (Charizard), `149` (Dragonite)

3. **Click "Search"** or press **Enter** key

### Exploring Pokémon Data

#### Overview Tab
- View physical information (height, weight, gender ratio)
- Read Pokédex entry
- See type effectiveness at a glance
  - Weaknesses shown in red/orange
  - Resistances in green
  - Immunities in purple/dark

#### Stats Tab
- Base stats with visual percentage bars
- Quick comparison of strengths and weaknesses
- Max stats scale to 255 (max possible base stat)

#### Moves Tab
**Four categories of moves:**
- **Level-Up Moves** - Learned at specific levels
- **Machine Moves** - TM/HM/MT/DT moves from latest games
- **Tutor Moves** - Special move tutor moves
- **Egg Moves** - Inherited from parents

Each move shows:
- Move type (color-coded)
- Power and Accuracy
- PP (Power Points)
- Damage Class
- Priority and Target
- Full effect description

#### Evolution Tab
- Evolution chain with Pokémon images
- Evolution conditions clearly displayed
- Examples:
  - "Level Up at Lv. 16"
  - "Use Thunder Stone"
  - "Trade with Item"

#### Abilities Tab
- All abilities for the Pokémon
- Hidden ability indicator (if applicable)
- Full ability effect descriptions
- In-game flavor text

## 📁 Project Structure

```
Proyecto1/
├── src/
│   ├── main/
│   │   ├── java/com/test/demo/
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java              # Spring Web configuration
│   │   │   ├── controller/
│   │   │   │   └── PokemonController.java      # REST API endpoints
│   │   │   ├── service/
│   │   │   │   └── PokemonService.java         # Business logic & API calls
│   │   │   ├── dto/
│   │   │   │   ├── PokemonDetail.java          # Base Pokémon DTO
│   │   │   │   ├── EnhancedPokemonDetail.java  # Full DTO with all data
│   │   │   │   ├── MoveDetail.java             # Move information
│   │   │   │   ├── MoveLearnInfo.java          # Move learning metadata
│   │   │   │   ├── EvolutionDetail.java        # Evolution information
│   │   │   │   ├── TypeEffectiveness.java      # Type interactions
│   │   │   │   ├── AbilityDetail.java          # Ability descriptions
│   │   │   │   ├── PokemonSpeciesInfo.java     # Species data
│   │   │   │   └── StatDetail.java             # Stat information
│   │   │   ├── DemoApplication.java            # Spring Boot entry point
│   │   │   └── ServletInitializer.java         # WAR deployment config
│   │   ├── resources/
│   │   │   ├── static/
│   │   │   │   ├── index.html                  # Main HTML (v1.1: tabbed interface)
│   │   │   │   ├── css/
│   │   │   │   │   └── style.css               # Dark theme & 19 type colors
│   │   │   │   └── js/
│   │   │   │       └── script.js               # Tab system & display logic
│   │   │   └── application.properties          # App config (port: 8081)
│   │   └── test/
│   │       └── java/com/test/demo/
│   │           └── DemoApplicationTests.java
│   └── pom.xml                                  # Maven dependencies
├── .gitignore
├── README.md                                    # This file
├── CHANGELOG.md                                 # Version history
└── mvnw / mvnw.cmd                             # Maven wrapper scripts
```

## 🔌 API Endpoints

### GET /api/pokemon/{name}
Retrieves comprehensive Pokémon data by name or ID.

**Parameters:**
- `{name}` - Pokémon name (case-insensitive) or ID number

**Example Requests:**
```
GET http://localhost:8081/api/pokemon/pikachu
GET http://localhost:8081/api/pokemon/25
GET http://localhost:8081/api/pokemon/charizard
```

**Response Example (v1.1):**
```json
{
  "id": 25,
  "name": "pikachu",
  "imageUrl": "https://...",
  "officialArtwork": "https://...",
  "height": 4,
  "weight": 60,
  "types": ["electric"],
  "stats": [
    {"name": "hp", "baseStat": 35},
    {"name": "attack", "baseStat": 55},
    ...
  ],
  "abilities": [
    {"name": "static", "hidden": false, "effect": "...", "description": "..."}
  ],
  "moves": {
    "level-up": [
      {
        "move": {"moveName": "Tail Whip", "type": "normal", "power": null, ...},
        "learnMethod": "level-up",
        "level": 1
      }
    ],
    "machine": [...],
    "tutor": [...],
    "egg": [...]
  },
  "evolutions": [
    {
      "evolvesFrom": "pikachu",
      "evolvesTo": "raichu",
      "method": "level-up",
      "minLevel": 16,
      "trigger": "Level Up at Lv. 16"
    }
  ],
  "typeEffectiveness": {
    "weaknessesTo": ["ground"],
    "resistances": ["flying", "steel"],
    "immunities": [],
    "damageTo": ["water", "flying"]
  },
  "speciesInfo": {
    "malePercentage": 50.0,
    "femalePercentage": 50.0,
    "eggGroups": ["undiscovered"],
    "hatchSteps": 10,
    "baseHappiness": 35,
    "growthRate": "fast",
    "captureRate": 190
  },
  "generation": "generation-i",
  "description": "When several of these POKéMON gather, their electricity could build and cause lightning storms."
}
```

**Error Response:**
```json
HTTP 404 Not Found
```

## 🎨 UI/UX Design

### Color Scheme (v1.1)
- **Background**: Dark blue gradient (`#1a1a2e` → `#16213e`)
- **Primary Accent**: Cyan (`#00d4ff`)
- **Text**: Light gray (`#e0e0e0`)
- **Type Colors**: Official Pokémon type colors

### Type Color Reference
| Type | Color | Hex Code |
|------|-------|----------|
| Normal | Gray | `#A8A878` |
| Fire | Orange | `#F08030` |
| Water | Blue | `#6890F0` |
| Grass | Green | `#78C850` |
| Electric | Yellow | `#F8D030` |
| Ice | Cyan | `#98D8D8` |
| Fighting | Red | `#C03028` |
| Poison | Purple | `#A040A0` |
| Ground | Brown | `#E0C068` |
| Flying | Light Purple | `#A890F0` |
| Psychic | Pink | `#F85888` |
| Bug | Yellow-Green | `#A8B820` |
| Rock | Brown | `#B8A038` |
| Ghost | Purple | `#705898` |
| Dragon | Blue | `#7038F8` |
| Dark | Gray-Brown | `#705848` |
| Steel | Gray-Blue | `#B8B8D0` |
| Fairy | Rose | `#EE99AC` |

### Component Design
- **Tab Interface** - Clean button-based navigation
- **Info Boxes** - Color-bordered sections with consistent styling
- **Move Cards** - Expandable cards with all move details
- **Evolution Chain** - Horizontal flow with images and arrows
- **Type Tags** - Color-coded badges throughout

## 📊 Architecture

### Data Flow
```
User Input (Search)
    ↓
JavaScript Event Handler
    ↓
fetch('/api/pokemon/{name}')
    ↓
REST Controller
    ↓
PokemonService
    ├→ GET /pokemon/{name} (PokeAPI)
    ├→ GET /pokemon-species/{id}
    ├→ GET /type/{name} (for each type)
    ├→ GET /ability/{name} (for each ability)
    ├→ GET /evolution-chain/{id}
    └→ GET /move/{name} (for each move)
    ↓
EnhancedPokemonDetail (aggregated)
    ↓
JSON Response to Frontend
    ↓
JavaScript Processing
    ↓
DOM Rendering with Type Colors
    ↓
Display to User
```

## 🔧 Configuration

### Change Port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build Properties
- **Java Version**: 21 (in `pom.xml`)
- **Spring Boot**: 4.0.5
- **Packaging**: WAR (deployable web application)

## 📦 Building for Production

### Create Deployable Package
```bash
./mvnw clean package
```

Output: `target/demo-0.0.1-SNAPSHOT.war`

### Deploy
- Copy WAR file to application server (Tomcat, JBoss, etc.)
- Or run directly: `java -jar demo-0.0.1-SNAPSHOT.war`

## 🐛 Troubleshooting

### Port 8081 Already in Use
Change in `application.properties` or kill the process:
```bash
# Windows
taskkill /F /IM java.exe

# Linux/Mac
kill -9 $(lsof -t -i :8081)
```

### Build Fails
```bash
# Clean and rebuild
./mvnw clean install -DskipTests

# Verify Java version
java -version  # Should show Java 21+
```

### API Calls Failing
- Check internet connection (requires access to pokeapi.co)
- Verify Pokémon name/ID is correct
- Check browser console (F12) for error details
- Ensure application is running on http://localhost:8081

### Evolution Chain Not Showing
- Some Pokémon don't have evolutions (e.g., legendaries)
- Check browser console for API errors
- Evolution data might be loading - wait a moment

## 📚 Example Searches

Try these Pokémon to test different features:

| Name | ID | Notable Features |
|------|----|----|
| Pikachu | 25 | Standard evolutions, many moves, Electric type |
| Charizard | 6 | Dual-type (Fire/Flying), evolution chain from Charmander |
| Eevee | 133 | Multiple unique evolutions |
| Slowpoke | 79 | Complex evolution (Slowbro and Slowking) |
| Alakazam | 65 | High Special Attack, multiple evolutions |
| Shedinja | 292 | Unique Ghost/Bug type, interesting abilities |
| Dragonite | 149 | Dragon type, good stat spread |
| Gyarados | 130 | Water/Flying, evolution from Magikarp |

## 🚀 Future Enhancements (v1.2+)

- [ ] Favorites/Bookmark feature
- [ ] Search by type filter
- [ ] Pokémon comparison tool (2+ Pokémon side-by-side)
- [ ] Dark mode toggle (add light theme option)
- [ ] Advanced filtering (by type, stat range, ability)
- [ ] Move coverage calculator
- [ ] Caching layer for performance (Redis/in-memory)
- [ ] Evolution calculator for items/conditions
- [ ] Shiny variant display
- [ ] Form variations (Alola, Galar, regional forms)

## 📝 Version History

- **v1.1** (2026-04-17) - Enhanced UI with tabs, type colors, evolution images, dark theme
- **v1.0** (2026-04-17) - Initial release with basic search and stats

See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

## 📞 Support & Issues

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing issues for solutions
- Review the troubleshooting section above

## 🙏 Acknowledgments

- **PokeAPI** - Free, open-source Pokémon API
- **Spring Boot** - Modern Java framework
- **Pokémon** - © Nintendo/Game Freak/Pokémon Company

## 📄 License

This project is open-source and available for educational purposes.

---

**Version**: 1.1.0  
**Last Updated**: April 17, 2026  
**Status**: Active Development  

Happy Pokémon Hunting! 🎮✨


## 🛠️ Tech Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 4.0.5** - Web framework
- **Spring Web MVC** - REST API development
- **RestClient** - HTTP client for PokeAPI integration
- **Maven** - Build and dependency management

### Frontend
- **HTML5** - Markup
- **CSS3** - Styling with gradients and animations
- **JavaScript (Vanilla)** - Interactive functionality
- **Responsive Design** - Mobile-first approach

### External APIs
- **PokéAPI (v2)** - https://pokeapi.co/api/v2/
  - Pokémon data, stats, abilities, and species information

## 📋 Prerequisites

Before running this project, ensure you have installed:

- **Java 21+** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/download)
- **Web Browser** - Chrome, Firefox, Safari, or Edge

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/SirZerobeat/Proyecto1.git
cd Proyecto1
```

### 2. Build the Project
```bash
./mvnw clean install
```

On Windows:
```cmd
mvnw.cmd clean install
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

On Windows:
```cmd
mvnw.cmd spring-boot:run
```

The application will start on **http://localhost:8081**

## 🎮 Usage

### Accessing the Application
Open your web browser and navigate to:
```
http://localhost:8081
```

### Searching for Pokémon

1. **Enter a Pokémon name** (lowercase or uppercase)
   - Example: `pikachu`, `Charizard`, `DRAGONITE`

2. **Or enter a Pokémon ID**
   - Example: `25` (Pikachu), `6` (Charizard), `149` (Dragonite)

3. Click the **"Search"** button or press **Enter**

### View Results
The application displays:
- **Official Image** - High-quality Pokémon artwork
- **Name & ID** - Pokémon identification
- **Type(s)** - Tagged with color-coded types
- **Physical Attributes**
  - Height (in meters)
  - Weight (in kilograms)
- **Abilities** - All available abilities
- **Stats** - Visual representation of base stats
- **Description** - Official Pokédex entry

## 📁 Project Structure

```
Proyecto1/
├── src/
│   ├── main/
│   │   ├── java/com/test/demo/
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java              # Spring Web configuration
│   │   │   ├── controller/
│   │   │   │   └── PokemonController.java      # REST API endpoints
│   │   │   ├── service/
│   │   │   │   └── PokemonService.java         # Business logic & PokeAPI calls
│   │   │   ├── dto/
│   │   │   │   ├── PokemonDetail.java          # Pokemon data structure
│   │   │   │   └── StatDetail.java             # Stats data structure
│   │   │   ├── DemoApplication.java            # Spring Boot entry point
│   │   │   └── ServletInitializer.java         # WAR deployment config
│   │   ├── resources/
│   │   │   ├── static/
│   │   │   │   ├── index.html                  # Main HTML page
│   │   │   │   ├── css/
│   │   │   │   │   └── style.css               # Styling & animations
│   │   │   │   └── js/
│   │   │   │       └── script.js               # Client-side logic
│   │   │   ├── templates/
│   │   │   │   └── index.html                  # Template (legacy)
│   │   │   └── application.properties          # App configuration
│   │   └── test/
│   │       └── java/com/test/demo/
│   │           └── DemoApplicationTests.java
│   └── pom.xml                                  # Maven dependencies
├── .gitignore
├── README.md                                    # This file
└── mvnw / mvnw.cmd                             # Maven wrapper scripts
```

## 🔌 API Endpoints

### GET /api/pokemon/{name}
Retrieves Pokémon data by name or ID.

**Parameters:**
- `{name}` - Pokémon name (lowercase) or ID number

**Example Requests:**
```
GET http://localhost:8081/api/pokemon/pikachu
GET http://localhost:8081/api/pokemon/25
GET http://localhost:8081/api/pokemon/charizard
```

**Response Example:**
```json
{
  "id": 25,
  "name": "pikachu",
  "imageUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
  "height": 4,
  "weight": 60,
  "types": ["electric"],
  "abilities": ["static", "lightning-rod"],
  "stats": [
    {"name": "hp", "baseStat": 35},
    {"name": "attack", "baseStat": 55},
    {"name": "defense", "baseStat": 40},
    {"name": "special-attack", "baseStat": 50},
    {"name": "special-defense", "baseStat": 50},
    {"name": "speed", "baseStat": 90}
  ],
  "description": "When several of these POKéMON gather, their electricity could build and cause lightning storms."
}
```

**Error Response:**
```json
HTTP 404 Not Found
```

## 🧩 Component Architecture

### Backend Flow
```
User Request
    ↓
PokemonController (/api/pokemon/{name})
    ↓
PokemonService
    ├── Calls PokeAPI (/pokemon/{name})
    ├── Calls PokeAPI (/pokemon-species/{id})
    └── Processes & aggregates data
    ↓
PokemonDetail DTO (JSON Response)
```

### Frontend Flow
```
User Input (Search)
    ↓
JavaScript Event Listener
    ↓
Fetch API Call to /api/pokemon/{name}
    ↓
Parse JSON Response
    ↓
DOM Manipulation
    ↓
Display Pokemon Card
```

## 🎨 UI/UX Features

- **Gradient Background** - Purple-to-pink gradient for Pokemon theme
- **Smooth Animations** - Fade-in and slide-up effects
- **Responsive Grid Layout** - Adapts to all screen sizes
- **Visual Stats Bars** - Color-coded stat representation
- **Loading State** - User feedback during API calls
- **Error Handling** - Graceful error messages for invalid searches
- **Mobile Optimized** - Single-column layout on small screens

## 🔧 Configuration

### Change Port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build Properties
- **Java Version:** 21 (configured in `pom.xml`)
- **Spring Boot Version:** 4.0.5
- **Packaging:** WAR (deployable web application)

## 📦 Building for Production

### Create JAR/WAR Package
```bash
./mvnw clean package
```

Output will be in `target/demo-0.0.1-SNAPSHOT.war`

### Deploy
- Copy the WAR file to your application server (Tomcat, JBoss, etc.)
- Or run it directly: `java -jar demo-0.0.1-SNAPSHOT.war`

## 🐛 Troubleshooting

### Port 8081 Already in Use
Change the port in `application.properties` or kill the process:
```bash
# Windows
taskkill /F /IM java.exe

# Linux/Mac
kill -9 $(lsof -t -i :8081)
```

### Build Fails
```bash
# Clean and rebuild
./mvnw clean install -DskipTests

# Check Java version
java -version
```

### API Calls Failing
- Check internet connection (requires access to pokeapi.co)
- Verify Pokémon name/ID is correct
- Check browser console for errors (F12)

## 📚 Example Searches

Try these Pokémon:

| Name | ID | Type |
|------|----|----|
| Pikachu | 25 | Electric |
| Charizard | 6 | Fire/Flying |
| Dragonite | 149 | Dragon/Flying |
| Gyarados | 130 | Water/Flying |
| Alakazam | 65 | Psychic |
| Machamp | 68 | Fighting |
| Arcanine | 59 | Fire |
| Lapras | 131 | Water/Ice |

## 🚀 Future Enhancements

- [ ] Add favorites/bookmark feature
- [ ] Search by type filter
- [ ] Evolution chain display
- [ ] Move list and descriptions
- [ ] Comparison tool (compare 2+ Pokémon)
- [ ] Dark mode toggle
- [ ] Pagination for Pokémon list
- [ ] Advanced filtering and sorting
- [ ] Database caching layer
- [ ] User authentication

## 📝 License

This project is open-source and available for educational purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing issues for solutions
- Review the troubleshooting section above

## 🙏 Acknowledgments

- **PokeAPI** - Free, open-source Pokémon API
- **Spring Boot** - Modern Java framework
- **Pokemon** - © Nintendo/Game Freak

---

**Last Updated:** April 2026

Happy Pokémon Hunting! 🎮✨
