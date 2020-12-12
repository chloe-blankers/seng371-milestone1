# ADR 002 - Front End

## Status

Accepted

## Context

The front end needs to have a few important characteristics. It needs to be easily
maintainable, be modern looking, have client-side data validation, and robust. It 
also needs to allow the backend to easily serve up all the files. To have these
features, it should conform to general front end standards and the file structure
should work well with the play-bootstrap backend.

## Decision

To implement these features, we chose to set up a Material design colour scheme, build the
main UI from custom HTML, CSS, and JS (rather than from pre-built frameworks), and import the main
external framework (ChartJS) via a Content Delivery Network (CDN), rather than rely on local 
installs.

We did not, however, implement the full Material design standard, instead choosing to stay 
with only the Material design colour scheme.

## Consequences

Using a Material Design colour scheme (eg. having defined 'primary', 'background', 'on-background'
etc. colours ) allows us to quickly and easily change the main colour scheme by editing a few short
lines in the main CSS file. This is good for maintainability and building. Not implementing more of 
the material design standard features (eg. having 'btn' 'btn-small' 'btn-large' etc classes for buttons) 
increased the speed at with a basic UI could be implemented, but will create more work in the future as 
the implemented designs are not as flexible.

Building the main UI from custom HTML, CSS, and JS rather than relying on a framework (eg. Bootstrap) 
speeds up loading times, and gives more flexibility in the design. However it means that the design is 
less responsive to different screen sizes / mobile devices. Heavy use of the flex attribute throughout 
the CSS, along with @media tags for different devices does allow for fairly flexible UI which is fully
useable on all screen sizes and on mobile devices.

Using ChartJS gives us access to modern looking graphs that load very quickly, even over a CDN. 
This choice of ChartJS over more complex and feature rich chart/graph JS libraries increased
development time and loading speeds, however it does means that future development is more
limited by the types of graphs that can be used. However, changing graph frameworks is not 
particularly difficult, so the decision is unlikely to seriously hamper future work. Loading the
library through a CDN makes it easy to implement, but installing it through something like npm on 
the backend would allow us speed up loading time and use chartJS extensions which would give us 
more features to use. However, the play-bootstrap backend doesn't seem to work well with npm, so 
in the future this may mean that if we stay with the play-bootstrap backend we are limited to JS libraries
through CDN, unless we want to spend a lot of time making npm work with play-bootstrap.



