"# project-here-itage" 

Technologies:
- Firebase (for data storage, and authentication)
- Google Maps plugin/map fragment
- We pull the data from this API: 
    https://opendata.vancouver.ca/explore/dataset/heritage-sites/information/

Features (And bugs/intricacies):
- Heritage sites superimposed on map using markers:
    This by itself should just "work".
    The currently selected marker should change color to blue.
    All the points are within the Vancouver general area (from this API) :(
  
- Searchbar:
    You have to type *exactly* the building name (eg. "Alexander Residence").
        (We have tried implementing text comparisons to allow for finding locations
        with similar names, but that hasn't worked yet.)
    It won't work without exact matches for building names, but it should not crash either.

- Upper Right Button:
    If you are not signed in, it should display "Sign In" and take you to
        the Login fragment (and by extension, Sign Up).
    If you ARE signed in, the upper right button should display the part
        of your email before the @ sign. Clicking on this version of the button
        should open some options to log out and to access the Favorites list.

- Login/Signup
    These should work. Completing one should take you back to the main activity.

- Favorites list
    The favorites list should not be loadable if there are no favorites selected from the session.
    Favorites are not saved across sessions atm.
    Right now this should display the full list of favorites stored.
    There are two options for the user. One links to the "further information" page, 
    second should enable the user to delete the record from the favorite list.

- Card
    This should be automatically popping up when a location is selected on the map.
        It should have a functioning favorites button (that changes color based on status)
        An information blurb, and a link to "read more".
  
- Recent Search History
    This should show the last 6 markers selected, and clicking on a title should jump the map to that position.
    There are some bugs about manipulating "focus" - the search history is only supposed to pop up when
    the edit text is being actively typed in.