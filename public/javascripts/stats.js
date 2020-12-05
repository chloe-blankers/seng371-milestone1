

function loadGraph( graphID, url ){

    // Get Data from Server Through REST API
    /*const response = await fetch( url );
    const responseJSON = await response.json();
    const whaleData = JSON.parse( responseJSON );*/

    let graphHolder = document.getElementById( graphID );
    let canvasElement = document.createElement('canvas');
    var myChart = new Chart(canvasElement, {
        type: 'bar',
        data: {
            labels: ['ORCA', 'PORPOISE', 'GREY', 'HUMPBACK', 'BLUE', 'UNKNOWN'],
            datasets: [{
                label:              '# of Whales Per Species',
                data:               [12, 19, 3, 5, 2, 3],
                backgroundColor:    [ '#2196f3', '#2196f3', '#2196f3', '#2196f3', '#2196f3', '#2196f3'  ],
                borderColor:        [ '#2196f3', '#2196f3', '#2196f3', '#2196f3', '#2196f3', '#2196f3'  ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
    canvasElement.style = "width: 100px;"
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName('graph-loading')[0] );

}

const getWhaleData = async () => {

    const response = await fetch('http://example.com/movies.json');
    const myJson = await response.json();

}
