# Convene
========

Convene makes it easier to meet up with friends by finding the halfway point between 
both of you and showing bars and cafes in the area. It allows users to log in with Facebook 
and choose a friend to meet from their friends list, or to simply search for a specific location.

Features
--------
- Log in with Facebook and choose a friend to meet, uses Facebooks own api to retrieve friends location.
- Search for a friends address, which will be converted into latitude and longtitude points using the Geocoder api.
- Heads up notifications alerting the user when someone has requested to meet up with them.
- Custom map markers for cafes, bars and friends locations.

Installation
------------
Currently Convene can only be installed on a physical device by running the program in Android studio and 
connecting the device to the laptop via USB. In the future we plan to release the app on the Google play store
to make it more widely accessible.

Design Decisions
----------------
A. User Interface.

The app can essentially be split into two parts.The inital part which includes the Facebook login , contact search,
address search , splash screen and logo, and the Google maps part which includes finding the users location ,
the friends location, the halfway point between them and displaying this information on a map along with nearby bars and cafes.
It became apparent that we were going to have too many features in the inital part to display in one screen, but we knew having to
open a new activity for small features such as the search bar would seem clunky and inefficient. We decided instead to seperate the
search bar into a fragment within the initial activity which allows the user to quickly transition between searching for an
address or choosing someone from the Facebook friends list.
    The Google maps part is also a fragment within this main activity, meaning the user never actually has to wait for a different 
activity to open when using the app which makes it faster and more efficient. We added custm map markers which makes it easier to 
differentiate between pubs, cafes, the users location, the friends location and the halfway point.

B. Code design.

1. Layout and visual design.
For the inital part the main objective was designing the layout of the app. Most of the code for this was written in XML in the
layout files as even though elements of the layout can be written in Java code it is much simpler and quicker to do it in XML.
In the case of the Google maps part placing it as a fragment within the main activity is done in the maps activity XML file, but all 
the code associated with the custom markers and camera zoom levels is done in Java in the MainActivity Java class.




