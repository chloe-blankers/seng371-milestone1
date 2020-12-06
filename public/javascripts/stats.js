

function loadWhaleNumberGraph( graphID, dataJSON ){
    // Parse JSON
    let data = JSON.parse( dataJSON );
    if ( data.whales[data.whales.length - 1].id != -1 ){
        console.log("ERROR: Expected an empty array element with id == -1 in last position in JSON string (in loadWhaleFrequencyGraph();)");
        return false;
    }
    data.whales.pop();

    // Calculate Frequencies
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
        console.log( whaleFreq[data.whales[i].species] );

    }

    // Get data in format that ChartJS likes
    let vals = [];
    for ( i in keys ){
        vals.push( whaleFreq[keys[i]] );
    }

    let graphHolder = document.getElementById( graphID );
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
    canvasElement.style = "width: 100px;"
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName('graph-loading')[0] );
}


function loadGraph( graphID, url ){

    let graphHolder = document.getElementById( graphID );
    let canvasElement = document.createElement('canvas');
    //var myChart = createChart();
    canvasElement.style = "width: 100px;"
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName('graph-loading')[0] );

}
