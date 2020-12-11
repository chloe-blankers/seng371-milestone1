


function loadValidations(){

    let toValidate = document.getElementsByClassName("fv");

    for ( i in toValidate ){

        console.log( toValidate[i] );
        if ( toValidate[i].id == "weight" ){
            console.log( "YEPPPERS");
            toValidate[i].required = true;
            toValidate[i].type = "number";
            toValidate[i].text = "Enter the weight in kg.";
            toValidate[i].min = "0"
        }
    }
}