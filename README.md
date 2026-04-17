# 🔴 PokéDex - Pokémon Search Application

A full-stack web application that allows users to search and explore detailed information about Pokémon using the public PokeAPI. Built with Spring Boot and modern web technologies.

## ✨ Features

- 🔍 **Search Pokémon** by name or ID
- 🖼️ **Official Pokemon Images** from PokeAPI
- 📊 **Detailed Stats** with visual bars (HP, Attack, Defense, Sp.Atk, Sp.Def, Speed)
- 🏷️ **Type Information** - Display all Pokémon types
- 🎯 **Abilities** - View all available abilities
- 📏 **Physical Data** - Height and Weight information
- 📖 **Pokedex Entries** - Detailed descriptions of each Pokémon
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile devices
- ⚡ **Real-time API Integration** - Live data from PokéAPI

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
