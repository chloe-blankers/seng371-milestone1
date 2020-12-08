

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
    const table = submitButton.parentElement.children[0];
    const rows = table.children[1].children; // Get the rows from the tbody, ignore the thead.

    // Iterate through list, if a whale was selected, add it to whalesToSubmit
    let whalesToSubmit = [];
    for( i in rows ){
        const row = rows[i];
        if ( typeof row == 'number' ) break; // The last entry in rows is the length of rows.
        if( row.children[4].children[0].selected == "true" ){
            // Do what you want with the data here.
            console.log( row.children[0].innerHTML );
            console.log( row.children[1].innerHTML );
            console.log( row.children[2].innerHTML );
            console.log( row.children[3].innerHTML );
            console.log( "END OF WHALE");
        }

    }



}