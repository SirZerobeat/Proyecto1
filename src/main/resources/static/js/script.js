const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const pokemonCard = document.getElementById('pokemonCard');
const loading = document.getElementById('loading');
const error = document.getElementById('error');

// Search when button is clicked
searchBtn.addEventListener('click', searchPokemon);

// Search when Enter key is pressed
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchPokemon();
    }
});

async function searchPokemon() {
    const name = searchInput.value.trim();
    if (!name) {
        showError('Please enter a Pokémon name or ID');
        return;
    }

    loading.style.display = 'block';
    error.style.display = 'none';
    pokemonCard.style.display = 'none';

    try {
        const response = await fetch(`/api/pokemon/${name}`);

        if (!response.ok) {
            throw new Error('Pokémon not found');
        }

        const pokemon = await response.json();
        displayPokemon(pokemon);
        loading.style.display = 'none';
    } catch (err) {
        showError(err.message);
        loading.style.display = 'none';
    }
}

function displayPokemon(pokemon) {
    // Set basic info
    document.getElementById('pokemonId').textContent = String(pokemon.id).padStart(3, '0');
    document.getElementById('pokemonName').textContent = pokemon.name;
    document.getElementById('pokemonImage').src = pokemon.imageUrl;
    document.getElementById('pokemonHeight').textContent = (pokemon.height / 10).toFixed(1) + ' m';
    document.getElementById('pokemonWeight').textContent = (pokemon.weight / 10).toFixed(1) + ' kg';

    // Set types
    const typesList = document.getElementById('typesList');
    typesList.innerHTML = '';
    pokemon.types.forEach(type => {
        const tag = document.createElement('span');
        tag.className = 'tag';
        tag.textContent = type;
        typesList.appendChild(tag);
    });

    // Set abilities
    const abilitiesList = document.getElementById('abilitiesList');
    abilitiesList.innerHTML = '';
    pokemon.abilities.forEach(ability => {
        const tag = document.createElement('span');
        tag.className = 'tag';
        tag.textContent = ability.replace('-', ' ');
        abilitiesList.appendChild(tag);
    });

    // Set description
    const description = document.getElementById('pokemonDescription');
    description.textContent = pokemon.description || 'No description available';

    // Set stats
    const statsList = document.getElementById('statsList');
    statsList.innerHTML = '';
    pokemon.stats.forEach(stat => {
        const statItem = document.createElement('div');
        statItem.className = 'stat-item';

        const statName = document.createElement('div');
        statName.className = 'stat-name';
        statName.textContent = stat.name;

        const statBar = document.createElement('div');
        statBar.className = 'stat-bar';

        const statFill = document.createElement('div');
        statFill.className = 'stat-fill';
        // Max stat value is typically 255
        const percentage = (stat.baseStat / 255) * 100;
        statFill.style.width = percentage + '%';
        statFill.textContent = stat.baseStat;

        statBar.appendChild(statFill);

        statItem.appendChild(statName);
        statItem.appendChild(statBar);
        statsList.appendChild(statItem);
    });

    // Show card
    pokemonCard.style.display = 'block';
    error.style.display = 'none';
}

function showError(message) {
    error.textContent = '❌ ' + message;
    error.style.display = 'block';
    pokemonCard.style.display = 'none';
}
