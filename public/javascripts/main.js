

function changeTheme( themeName ) {
    let background;
    let surface;
    let primary;
    let primaryVar;
    let secondary;
    let secondaryVar;
    let onBackground;
    let onSurface;
    let onPrimary;
    let onSecondary;
    let error;
    let divider;

    switch(themeName){
        case "dark":
            background =    "#222222";
            surface =       "#393939";
            primary =       "#5db0d3";
            primaryVar =    "#5db0d3";
            secondary =     "#f29766";
            secondaryVar =  "#f29766";
            onBackground =  "#c0c0c0";
            onSurface =     "#aaaaaa";
            onPrimary =     "#aaaaaa";
            onSecondary =   "#aaaaaa";
            error =         "#FF5252";
            divider =       "#BDBDBD";
            break;
        case "light":
        default:
            background =    "#dddddd";
            surface =       "#ffffff";
            primary =       "#2196f3";
            primaryVar =    "#1976D2";
            secondary =     "#00BCD4";
            secondaryVar =  "#00BCD4";
            onBackground =  "#676767";
            onSurface =     "#000000";
            onPrimary =     "#FFFFFF";
            onSecondary =   "#FFFFFF";
            error =         "#FF5252";
            divider =       "#BDBDBD";
    }
    document.documentElement.style.cssText = `--background: ${background}; --surface: ${surface}; --primary: ${primary}; --primary-var: ${primaryVar}; --secondary: ${secondary}; --secondary-var: ${secondaryVar}; --on-background: ${onBackground}; --on-surface: ${onSurface}; --on-primary: ${onPrimary}; --on-secondary: ${onSecondary}; --error: ${error}; --divider: ${divider};`
}
