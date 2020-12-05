

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
            labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
            datasets: [{
                label: '# of Votes',
                data: [12, 19, 3, 5, 2, 3],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
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
    graphHolder.replaceChild( canvasElement, graphHolder.getElementsByClassName ('graph-loading')[0] );

}

const getWhaleData = async () => {

    const response = await fetch('http://example.com/movies.json');
    const myJson = await response.json();

}
