

function selectMe( button ){
    if (button.selected == "true"){
        button.style["background-color"]="var(--surface)";
        button.selected="false";
    }
    else {
        button.style["background-color"]="var(--primary)";
        button.selected="true";
    }
}

function submitWhales( submitButton ){

    // Get the rows (whales that can be selected)
    const selector = document.getElementById("wSelect");
    const rows = selector.getElementsByClassName("whale-selector-row");

    // Iterate through list, if a whale was selected, add it to whalesToSubmit
    let whalesToSubmit = [];
    for( i in rows ){
        const row = rows[i];
        if ( typeof row == 'number' ) break; // The last entry in rows is the length of rows.
        if ( row.getElementsByClassName("row-select-radio-box")[0].selected == "true" ){
            // Add the values from the row in here. How do you want it? JSON?
            console.log( row );
        }
    }
}