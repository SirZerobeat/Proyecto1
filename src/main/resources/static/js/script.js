const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const pokemonContent = document.getElementById('pokemonContent');
const loading = document.getElementById('loading');
const error = document.getElementById('error');

// Tab management
const tabButtons = document.querySelectorAll('.tab-button');
const tabContents = document.querySelectorAll('.tab-content');

// Store current pokemon data for lazy loading
let currentPokemon = null;
let movesLoaded = false;

// Search events
searchBtn.addEventListener('click', searchPokemon);
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchPokemon();
    }
});

// Tab buttons
tabButtons.forEach(button => {
    button.addEventListener('click', () => {
        const tabName = button.dataset.tab;
        switchTab(tabName);
    });
});

async function searchPokemon() {
    const name = searchInput.value.trim();
    if (!name) {
        showError('Please enter a Pokémon name or ID');
        return;
    }

    loading.style.display = 'block';
    error.style.display = 'none';
    pokemonContent.style.display = 'none';

    try {
        const response = await fetch(`/api/pokemon/${name}`);

        if (!response.ok) {
            throw new Error('Pokémon not found');
        }

        const pokemon = await response.json();
        displayPokemon(pokemon);
        loading.style.display = 'none';
        switchTab('overview'); // Switch to first tab
    } catch (err) {
        showError(err.message);
        loading.style.display = 'none';
    }
}

function displayPokemon(pokemon) {
    // Store current pokemon for lazy loading moves
    currentPokemon = pokemon;
    movesLoaded = false;

    // Hero section
    document.getElementById('pokemonId').textContent = String(pokemon.id).padStart(4, '0');
    document.getElementById('pokemonName').textContent = pokemon.name;
    document.getElementById('pokemonImage').src = pokemon.imageUrl || pokemon.officialArtwork;
    document.getElementById('generation').textContent = pokemon.generation || 'Unknown';

    // Types
    const typesList = document.getElementById('typesList');
    typesList.innerHTML = '';
    if (pokemon.types && pokemon.types.length > 0) {
        pokemon.types.forEach(type => {
            const tag = document.createElement('span');
            tag.className = `type-tag type-${type.toLowerCase()}`;
            tag.textContent = type;
            typesList.appendChild(tag);
        });
    }

    // Overview tab
    displayOverview(pokemon);

    // Stats tab
    displayStats(pokemon);

    // Moves tab - don't load immediately
    document.getElementById('levelUpMoves').innerHTML = '<p style="text-align: center; color: #808080;">Loading moves...</p>';
    document.getElementById('machineMoves').innerHTML = '';
    document.getElementById('tutorMoves').innerHTML = '';
    document.getElementById('eggMoves').innerHTML = '';

    // Evolution tab
    displayEvolutions(pokemon);

    // Abilities tab
    displayAbilities(pokemon);

    // Show content
    pokemonContent.style.display = 'block';
    error.style.display = 'none';
}

function displayOverview(pokemon) {
    // Physical information
    document.getElementById('pokemonHeight').textContent =
        pokemon.height ? (pokemon.height / 10).toFixed(1) + ' m' : 'Unknown';
    document.getElementById('pokemonWeight').textContent =
        pokemon.weight ? (pokemon.weight / 10).toFixed(1) + ' kg' : 'Unknown';

    // Species info
    if (pokemon.speciesInfo) {
        const genderRatio = pokemon.speciesInfo.malePercentage === 0 && pokemon.speciesInfo.femalePercentage === 0
            ? 'Genderless'
            : `${pokemon.speciesInfo.malePercentage.toFixed(1)}% Male / ${pokemon.speciesInfo.femalePercentage.toFixed(1)}% Female`;

        document.getElementById('genderRatio').textContent = genderRatio;
        document.getElementById('captureRate').textContent = pokemon.speciesInfo.captureRate + '%';
        document.getElementById('baseHappiness').textContent = pokemon.speciesInfo.baseHappiness;

        const eggGroups = pokemon.speciesInfo.eggGroups && pokemon.speciesInfo.eggGroups.length > 0
            ? pokemon.speciesInfo.eggGroups.map(g => capitalizeFirst(g)).join(', ')
            : 'Unknown';
        document.getElementById('eggGroups').textContent = eggGroups;

        document.getElementById('hatchSteps').textContent =
            (pokemon.speciesInfo.hatchSteps * 255) + ' steps (~' + pokemon.speciesInfo.hatchSteps + ' cycles)';
        document.getElementById('growthRate').textContent = capitalizeFirst(pokemon.speciesInfo.growthRate);
    }

    // Description
    const description = document.getElementById('pokemonDescription');
    description.textContent = pokemon.description || 'No description available';

    // Type effectiveness
    displayTypeEffectiveness(pokemon);
}

function displayTypeEffectiveness(pokemon) {
    if (!pokemon.typeEffectiveness) return;

    const weaknessList = document.getElementById('weaknessList');
    const resistancesList = document.getElementById('resistancesList');
    const immunitiesList = document.getElementById('immunitiesList');

    weaknessList.innerHTML = '';
    resistancesList.innerHTML = '';
    immunitiesList.innerHTML = '';

    // Weaknesses
    if (pokemon.typeEffectiveness.weaknessesTo && pokemon.typeEffectiveness.weaknessesTo.length > 0) {
        pokemon.typeEffectiveness.weaknessesTo.forEach(type => {
            const tag = document.createElement('span');
            tag.className = `tag type-${type.toLowerCase()}`;
            tag.textContent = type;
            weaknessList.appendChild(tag);
        });
    } else {
        weaknessList.innerHTML = '<span style="color: #808080;">None</span>';
    }

    // Resistances
    if (pokemon.typeEffectiveness.resistances && pokemon.typeEffectiveness.resistances.length > 0) {
        pokemon.typeEffectiveness.resistances.forEach(type => {
            const tag = document.createElement('span');
            tag.className = `tag type-${type.toLowerCase()}`;
            tag.textContent = type;
            resistancesList.appendChild(tag);
        });
    } else {
        resistancesList.innerHTML = '<span style="color: #808080;">None</span>';
    }

    // Immunities
    if (pokemon.typeEffectiveness.immunities && pokemon.typeEffectiveness.immunities.length > 0) {
        pokemon.typeEffectiveness.immunities.forEach(type => {
            const tag = document.createElement('span');
            tag.className = `tag type-${type.toLowerCase()}`;
            tag.textContent = type;
            immunitiesList.appendChild(tag);
        });
    } else {
        immunitiesList.innerHTML = '<span style="color: #808080;">None</span>';
    }
}

function displayStats(pokemon) {
    const statsList = document.getElementById('statsList');
    statsList.innerHTML = '';

    if (!pokemon.stats || pokemon.stats.length === 0) {
        statsList.innerHTML = '<p>No stats available</p>';
        return;
    }

    pokemon.stats.forEach(stat => {
        const statItem = document.createElement('div');
        statItem.className = 'stat-item';

        const statName = document.createElement('div');
        statName.className = 'stat-name';
        statName.textContent = capitalizeFirst(stat.name);

        const statBar = document.createElement('div');
        statBar.className = 'stat-bar';

        const statFill = document.createElement('div');
        statFill.className = 'stat-fill';
        const percentage = Math.min((stat.baseStat / 255) * 100, 100);
        statFill.style.width = percentage + '%';
        statFill.textContent = stat.baseStat;

        statBar.appendChild(statFill);

        statItem.appendChild(statName);
        statItem.appendChild(statBar);
        statsList.appendChild(statItem);
    });
}

function displayMoves(pokemon) {
    if (!pokemon.moves || Object.keys(pokemon.moves).length === 0) {
        document.getElementById('levelUpMoves').innerHTML = '<p>No moves available</p>';
        return;
    }

    displayMoveList('levelUpMoves', pokemon.moves['level-up'] || []);
    displayMoveList('machineMoves', pokemon.moves['machine'] || []);
    displayMoveList('tutorMoves', pokemon.moves['tutor'] || []);
    displayMoveList('eggMoves', pokemon.moves['egg'] || []);
}

// New function to fetch moves asynchronously
async function fetchAndDisplayMoves(pokemonId) {
    try {
        const response = await fetch(`/api/pokemon/${pokemonId}/moves`);
        if (!response.ok) {
            throw new Error('Failed to fetch moves');
        }
        const moves = await response.json();

        // Update the pokemon object with moves
        if (currentPokemon) {
            currentPokemon.moves = moves;
            displayMoves(currentPokemon);
            movesLoaded = true;
        }
    } catch (err) {
        console.error('Error fetching moves:', err);
        document.getElementById('levelUpMoves').innerHTML = '<p style="color: #808080;">Error loading moves</p>';
    }
}

function displayMoveList(elementId, moves) {
    const container = document.getElementById(elementId);
    container.innerHTML = '';

    if (!moves || moves.length === 0) {
        container.innerHTML = '<p style="color: #808080;">No moves available</p>';
        return;
    }

    moves.forEach(moveInfo => {
        const moveCard = document.createElement('div');
        moveCard.className = 'move-card';

        const moveName = document.createElement('div');
        moveName.className = 'move-name';
        moveName.textContent = moveInfo.move.moveName;

        const moveMeta = document.createElement('div');
        moveMeta.className = 'move-meta';

        // Type with color
        const typeTag = document.createElement('span');
        typeTag.className = `move-type type-${moveInfo.move.type.toLowerCase()}`;
        typeTag.textContent = moveInfo.move.type;
        moveMeta.appendChild(typeTag);

        // Power
        if (moveInfo.move.power) {
            const powerTag = document.createElement('span');
            powerTag.className = 'move-stat';
            powerTag.textContent = `Power: ${moveInfo.move.power}`;
            moveMeta.appendChild(powerTag);
        }

        // Accuracy
        if (moveInfo.move.accuracy) {
            const accTag = document.createElement('span');
            accTag.className = 'move-stat';
            accTag.textContent = `Acc: ${moveInfo.move.accuracy}%`;
            moveMeta.appendChild(accTag);
        }

        // PP
        if (moveInfo.move.pp) {
            const ppTag = document.createElement('span');
            ppTag.className = 'move-stat';
            ppTag.textContent = `PP: ${moveInfo.move.pp}`;
            moveMeta.appendChild(ppTag);
        }

        // Damage Class
        const damageTag = document.createElement('span');
        damageTag.className = 'move-stat';
        damageTag.textContent = capitalizeFirst(moveInfo.move.damageClass);
        moveMeta.appendChild(damageTag);

        // Priority
        if (moveInfo.move.priority !== 0) {
            const priorityTag = document.createElement('span');
            priorityTag.className = 'move-stat';
            priorityTag.textContent = `Priority: ${moveInfo.move.priority}`;
            moveMeta.appendChild(priorityTag);
        }

        // Target
        if (moveInfo.move.target) {
            const targetTag = document.createElement('span');
            targetTag.className = 'move-stat';
            targetTag.textContent = `Target: ${capitalizeFirst(moveInfo.move.target)}`;
            moveMeta.appendChild(targetTag);
        }

        // Level (if applicable)
        if (moveInfo.level) {
            const levelTag = document.createElement('span');
            levelTag.className = 'move-stat';
            levelTag.textContent = `Lv. ${moveInfo.level}`;
            moveMeta.appendChild(levelTag);
        }

        // Machine type
        if (moveInfo.machineType) {
            const machineTag = document.createElement('span');
            machineTag.className = 'move-stat';
            machineTag.textContent = moveInfo.machineType;
            moveMeta.appendChild(machineTag);
        }

        moveCard.appendChild(moveName);
        moveCard.appendChild(moveMeta);

        // Description
        if (moveInfo.move.description) {
            const description = document.createElement('div');
            description.className = 'move-description';
            description.textContent = moveInfo.move.description;
            moveCard.appendChild(description);
        }

        container.appendChild(moveCard);
    });
}

function displayEvolutions(pokemon) {
    const evolutionChain = document.getElementById('evolutionChain');
    evolutionChain.innerHTML = '';

    if (!pokemon.evolutions || pokemon.evolutions.length === 0) {
        evolutionChain.innerHTML = '<p style="text-align: center; color: #808080;">This Pokémon has no evolutions</p>';
        return;
    }

    // Display current pokemon first
    const currentNode = document.createElement('div');
    currentNode.className = 'evolution-node';

    const currentImage = document.createElement('img');
    currentImage.src = pokemon.imageUrl || pokemon.officialArtwork;
    currentImage.alt = pokemon.name;
    currentImage.className = 'evolution-image';
    currentNode.appendChild(currentImage);

    const currentName = document.createElement('div');
    currentName.className = 'evolution-pokemon';
    currentName.textContent = pokemon.name + ' (Current)';

    currentNode.appendChild(currentName);
    evolutionChain.appendChild(currentNode);

    // Display evolutions - filter to only show actual evolutions (not the current pokemon repeated)
    const uniqueEvolutions = [];
    const seenSpecies = new Set([pokemon.name.toLowerCase()]);

    pokemon.evolutions.forEach(evo => {
        if (evo.evolvesTo && !seenSpecies.has(evo.evolvesTo.toLowerCase())) {
            uniqueEvolutions.push(evo);
            seenSpecies.add(evo.evolvesTo.toLowerCase());
        }
    });

    if (uniqueEvolutions.length === 0) {
        evolutionChain.innerHTML = '<p style="text-align: center; color: #808080;">No valid evolutions found</p>';
        return;
    }

    uniqueEvolutions.forEach(evo => {
        const arrow = document.createElement('div');
        arrow.className = 'evolution-arrow';
        arrow.textContent = '→';
        evolutionChain.appendChild(arrow);

        const evoNode = document.createElement('div');
        evoNode.className = 'evolution-node';

        // Try to get evolution image
        const evoImage = document.createElement('img');
        evoImage.alt = evo.evolvesTo;
        evoImage.className = 'evolution-image';

        // Fetch evolution pokemon data for image
        fetch(`/api/pokemon/${evo.evolvesTo.toLowerCase()}`)
            .then(response => response.json())
            .then(data => {
                evoImage.src = data.imageUrl || data.officialArtwork;
            })
            .catch(() => {
                evoImage.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="96" height="96"%3E%3C/svg%3E';
            });

        evoNode.appendChild(evoImage);

        const evoName = document.createElement('div');
        evoName.className = 'evolution-pokemon';
        evoName.textContent = capitalizeFirst(evo.evolvesTo);

        evoNode.appendChild(evoName);

        const evoDetails = document.createElement('div');
        evoDetails.className = 'evolution-details';

        let details = [];
        if (evo.minLevel) {
            details.push(`Level ${evo.minLevel}`);
        }
        if (evo.item) {
            details.push(`Use ${capitalizeFirst(evo.item)}`);
        }
        if (evo.trigger) {
            details.push(capitalizeFirst(evo.trigger));
        }

        evoDetails.textContent = details.length > 0 ? details.join(' • ') : 'Special evolution';
        evoNode.appendChild(evoDetails);

        evolutionChain.appendChild(evoNode);
    });
}

function displayAbilities(pokemon) {
    const abilitiesList = document.getElementById('abilitiesList');
    abilitiesList.innerHTML = '';

    if (!pokemon.abilities || pokemon.abilities.length === 0) {
        abilitiesList.innerHTML = '<p>No abilities available</p>';
        return;
    }

    pokemon.abilities.forEach(ability => {
        const card = document.createElement('div');
        card.className = 'ability-card';

        const name = document.createElement('div');
        name.className = 'ability-name';
        name.textContent = ability.name;

        if (ability.hidden) {
            const hiddenBadge = document.createElement('span');
            hiddenBadge.className = 'ability-hidden';
            hiddenBadge.textContent = 'Hidden Ability';
            name.appendChild(hiddenBadge);
        }

        card.appendChild(name);

        if (ability.effect) {
            const effect = document.createElement('div');
            effect.className = 'ability-effect';
            effect.textContent = ability.effect;
            card.appendChild(effect);
        }

        if (ability.description) {
            const description = document.createElement('div');
            description.className = 'ability-description';
            description.textContent = ability.description;
            card.appendChild(description);
        }

        abilitiesList.appendChild(card);
    });
}

function switchTab(tabName) {
    // Remove active class from all buttons and contents
    tabButtons.forEach(btn => btn.classList.remove('active'));
    tabContents.forEach(content => content.classList.remove('active'));

    // Add active class to selected button and content
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
    document.getElementById(tabName).classList.add('active');

    // Load moves on demand when Moves tab is clicked
    if (tabName === 'moves' && currentPokemon && !movesLoaded) {
        fetchAndDisplayMoves(currentPokemon.id);
    }
}

function createTypeTag(type) {
    const tag = document.createElement('span');
    tag.className = `tag type-${type.toLowerCase()}`;
    tag.textContent = type;
    return tag;
}

function capitalizeFirst(str) {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

function showError(message) {
    error.textContent = '❌ ' + message;
    error.style.display = 'block';
    pokemonContent.style.display = 'none';
}
