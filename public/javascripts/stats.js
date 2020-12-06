



function loadWhaleNumberGraph( graphID, dataJSON ){

    // Get relevant graph Holder
    let graphHolder = document.getElementById( graphID );

    // Parse JSON
    let data = JSON.parse( dataJSON );
    if ( data.whales[data.whales.length - 1].id != -1 ){
        console.log("ERROR: Expected an empty array element with id == -1 in last position in JSON string (in loadWhaleNumberGraph();)");
        let header = graphHolder.getElementsByClassName('graph-loading')[0].getElementsByClassName('graph-loading-message')[0];
        header.style = "color: var(--error);"
        header.innerHTML = "Error: Unable to Process Data."
        return false;
    }
    data.whales.pop();

    // Calculate Number of Whales of Each Species
    let whaleFreq = {};
    let keys = [];
    for ( i in data.whales ){
        if ( whaleFreq[data.whales[i].species] ) {
            whaleFreq[data.whales[i].species] += 1;
        }
        else {
            whaleFreq[data.whales[i].species] = 1;
            keys.push( data.whales[i].species );
        }
    }

    // Get data in format that ChartJS likes
    let vals = [];
    for ( i in keys ){
        vals.push( whaleFreq[keys[i]] );
    }

    // Create Chart
    let canvasElement = document.createElement('canvas');
    var myChart = new Chart(canvasElement, {
        type: 'bar',
        data: {
            labels: keys,
            datasets: [{
                label:              '# of Whales Per Species',
                data:               vals,
                backgroundColor:    '#2196f3',
                borderColor:        '#2196f3',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            title: {
                display: false,
                text: 'Chart of the Number of Whales seen of each Species.'
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        stepSize: 1
                    }
                }]
            }
        }
    });
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName('graph-loading')[0] );
}


function loadWhaleWeightGraph( graphID, dataJSON ){

    // Get relevant graph Holder
    let graphHolder = document.getElementById( graphID );

    // Parse JSON
    console.log( dataJSON );
    let data = JSON.parse( dataJSON );
    if ( data.whales[data.whales.length - 1].id != -1 ){
        let header = graphHolder.getElementsByClassName('graph-loading')[0].getElementsByClassName('graph-loading-message')[0];
        header.style = "color: var(--error);"
        header.innerHTML = "Error: Unable to Process Data."
        return false;
    }
    data.whales.pop();

    // Calculate Average Weights
    let whaleDict = {};
    for ( var whale in data.whales ){
        whale = data.whales[whale];
        if ( whaleDict[whale.species] ) {
            whaleDict[whale.species] = [ (whaleDict[whale.species][0] * whaleDict[whale.species][1] +
                               whale.weight) / (whaleDict[whale.species][1] + 1), whaleDict[whale.species][1] + 1 ];
        }
        else {
            whaleDict[whale.species] = [ whale.weight, 1 ];
        }
    }

    // Get data in format that ChartJS likes
    let keys = [];
    let vals = [];
    let cusStepSize = 0;
    for ( var key in whaleDict ){
        keys.push(key);
        vals.push(whaleDict[key][0]);
        if ( whaleDict[key][0] > cusStepSize ) cusStepSize = whaleDict[key][0];
    }
    if (cusStepSize > 100000 ) cusStepSize = 25000;
    else if (cusStepSize > 50000 ) cusStepSize = 10000;
    else if (cusStepSize > 10000 ) cusStepSize = 2500;
    else if (cusStepSize > 1000 ) cusStepSize = 250;
    else cusStepSize = 10;

    // Create Chart
    let canvasElement = document.createElement('canvas');
    var myChart = new Chart(canvasElement, {
        type: 'bar',
        data: {
            labels: keys,
            datasets: [{
                label:              'Ave Weight of Whales Per Species',
                data:               vals,
                backgroundColor:    '#2196f3',
                borderColor:        '#2196f3',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            title: {
                display: false,
                text: 'Chart of the Average Weight of Whales of each Species.'
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        stepSize: cusStepSize
                    }
                }]
            }
        }
    });
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName('graph-loading')[0] );
}

