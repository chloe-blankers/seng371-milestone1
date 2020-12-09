

async function loadGraphs( gidWhaleNum, gidWhaleWeight, gidObservationDays ){

    // Use REST API to get JSON representing all the whales.
    const whaleResponse = await fetch( '/Whales/getWhales', { method: 'GET', headers: { 'Accept': 'application/txt+json'} } )
        .then( (data) => data.json() )
        .then( (data) => {
                loadWhaleNumberGraph( gidWhaleNum, data['body'] )
                loadWhaleWeightGraph( gidWhaleWeight, data['body'] )
        } );

    // Use REST API to get JSON Representing all the observations.




}

function loadWhaleNumberGraph( graphID, whalesJSON ){

    // Get relevant graph Holder
    let graphHolder = document.getElementById( graphID );

    // Parse JSON
    let whales = [];
    for ( i in whalesJSON ){
        whales.push( whalesJSON[i] );
    }

    // Calculate Number of Whales of Each Species
    let whaleFreq = {};
    let keys = [];
    for ( i in whales ){
        if ( whaleFreq[whales[i]['species']] ) {
            whaleFreq[whales[i]['species']] += 1;
        }
        else {
            whaleFreq[ whales[i]['species']] = 1;
            keys.push( whales[i]['species'] );
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

function loadWhaleWeightGraph( graphID, whalesJSON ){

    // Get relevant graph Holder
    let graphHolder = document.getElementById( graphID );

    // Parse JSON
    let whales = [];
    for ( i in whalesJSON ){
        whales.push( whalesJSON[i] );
    }

    // Calculate Average Weights
    let whaleDict = {};
    for ( var i in whales ){
        whale = whales[i];
        if ( whaleDict[whale['species']] ) {
            whaleDict[whale['species']] = [ (whaleDict[whale['species']][0] * whaleDict[whale['species']][1] +
                               whale['weight']) / (whaleDict[whale['species']][1] + 1), whaleDict[whale['species']][1] + 1 ];
        }
        else {
            whaleDict[whale['species']] = [ whale['weight'], 1 ];
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

