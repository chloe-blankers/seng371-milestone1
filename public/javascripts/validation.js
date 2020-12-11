


function loadValidations(){

    let toValidate = document.getElementsByClassName("fv");

    for ( i in toValidate ){

        if ( toValidate[i].id == "weight" ){
            toValidate[i].required = true;
            toValidate[i].type = "number";
            toValidate[i].text = "Enter the weight in kg.";
            toValidate[i].min = "0"
        }
        else if ( toValidate[i].id == "whale-id" ){
            toValidate[i].required = true;
            toValidate[i].type = "number";
            toValidate[i].text = "Enter the whale ID.";
            toValidate[i].min = "0"
            //loadWhaleIDRangeRequirements( toValidate[i] );
        }
    }
}

async function loadWhaleIDRangeRequirements( element ){

    const whaleIdResponse = await fetch( '/observations/getWhaleIdRange', { method: 'GET', headers: { 'Accept': 'application/txt+json'} } )
                .then( (data) => data.json() )
                .then( (data) => {
                        console.log( "Min ID: " + data['minWhaleID'])
                        console.log( "Max ID: " + data['maxWhaleID'])
                } );

}