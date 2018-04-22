var level_1 = {
	cards: 8,
  time: 5000
}

var level_2 = {
  cards: 8,
  time: 4000
}

var level_3 = {
  cards: 8,
  time: 2000
}

var level = {
	cards: 0,
	time: 0
}

var turns = 0;

/**
Pairs game
*/

//check what level time it should use
function leveltime() {
	if (turns < 5) {
		level.time = level_1.time;
	}
	else if (turns < 8) {
		level.time = level_2.time;
	}
	else {
		level.time = level_3.time;
	}
	console.log (level.time, turns);
	return level.time;
}

/**
	generate output, generate options, draw to screen and display for 5 seconds, call drawOptions function
**/
function draw() {
  var types = ['king', 'queen', 'prince', 'princess', 'jester', 'castle', 'knight', 'dragon', 'jester2', 'prince2', 'princess2', 'castle2', 'dragon2'];
  var output = createPairs(types),
    options = createOptions(output, types);

  var card_container = document.querySelector('div.cards');

  output.forEach(function(d) {
    var div = document.createElement('div');
    div.className = 'card ' + d;
    //div.innerHTML = '<p>' + d + '</p>';
    card_container.append(div)
  });

  //show for 5 seconds
  setTimeout(function() {
    card_container.style.display = 'none';
    drawOptions(options);
  }, leveltime());
}

/**
 Extract correct answer then shuffle array, draw options and attach click handler to each one
**/
function drawOptions(opts) {
  //first one is the correct one
  var correct_answer = opts[0];
  //then shuffle array
  opts.sort(function() {
    return 0.5 - Math.random()
  });
  var options_container = document.querySelector('div.options');
  options_container.style.display = 'block';
  opts.forEach(function(d) {
    var div = document.createElement('div');
    div.className = 'card option';
    div.innerHTML = '<p>' + d + '</p>';
    div.addEventListener('click', clicked.bind(this, d, correct_answer));
    options_container.append(div);
  })

  //show success/failure message
  function clicked(d, e) {
    var message = d === e ? 'You are correct!' : 'You are incorrect!'
    alert(message);
	console.log(turns);
	//location.reload();
	replay();
  }
}

function toggleDisplay() {
var card_container = document.querySelector('div.cards');
var options_container = document.querySelector('div.options');
    if (options_container.style.display = 'none') {
		card_container.style.display = 'block';
    } else {
        options_container.style.display = 'block';
		card_container.style.display = 'none';
    }
}


//take types array and create array of 8 with some pairs
function createPairs(types) {
  let output = [];
  while (output.length < 8) {
    //get random number between 0 and number of types
    let i = Math.floor(Math.random() * types.length);
    output.push(types[i]);
  }
  return output;
}

function createOptions(arr, types) {
  var pairs = countDuplicates(arr),
    altA = randomNumber(4, [pairs]),
    altB = randomNumber(4, [pairs, altA]);

  return [pairs, altA, altB];
}

//return random number between 0 and max that does not exist in exclusion array
function randomNumber(max, exclusion) {
  var m = max,
    e = exclusion,
    res = Math.floor(Math.random() * max);
  return exclusion.indexOf(res) === -1 ? res : randomNumber(m, e);
}


//count Duplicates
function countDuplicates(arr) {
  //first clone array

  var clone = arr.slice(0).sort(),
    pairs = 0;

    console.log('LIST', clone, clone.length);

  //loop over array
  arr.forEach(function(el) {
    //create array of current el
    var c = clone.filter(function(d) {
    console.log('EL', el, 'D', d);
      return d === el;
    });

    //increment pairs value, determine pair by dividing length by 2 and rounding down.
    console.log('round:', c, Math.floor(c.length / 2));
    pairs += Math.floor(c.length / 2);

    //remove used values from array
    clone.splice(clone.indexOf(c[0]), c.length);

    console.log('AFTER SPLICE', clone);
  });

  console.log('PAIRS', pairs);

  return pairs;
}

/**
	clean up for the next level. Increment turns, toggle back to display and
  clear elements
**/
function replay(){
	var card_container = document.querySelector('div.cards');
	var options_container = document.querySelector('div.options');
	turns ++;
	var cards_el = document.getElementsByClassName("cards")[0];
	cleanUpChildNodes(cards_el);
	var options_el = document.getElementsByClassName("options")[0];
	cleanUpChildNodes(options_el);
	toggleDisplay();
	draw();
}

/**
	Remove child nodes of element
**/
function cleanUpChildNodes(node){
	while(node.hasChildNodes()){
  	node.removeChild(node.lastChild);
  }
}