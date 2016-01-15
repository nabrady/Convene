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

2. Maps functionality
Most of the code associated with the maps functionality is written in the mapsFragment file in the fragments folder.However the code for finding the users location is in the LaunchActivity class, as we decided finding the users location when the app is opened would save time rather than having to find it when they searched for a friends location. We decided to create a Java class called UserUtils which allows us to set the value of global variables from within any class. This is seen here using  UserUtils.setmLastLocation(mLastLocation); to set the users location, which can then be used in the AppMainActivity class. Finding the friends location on the map is done one of two ways, if the user wants to meet someone from their Facebook friends list then their friends location is retrieved from their Facebook profile in the GetLocation class, if they type in an address then it is found using the Geocder api in getLocation class in the AppMainActivity file. The equation for finding the midpoint of both locations was found online and it is done in the onSearch class in the mapsFragment file along with all the code for custom markers , zoom levels etc.

3. Facebook integration.
Integrating Facebook into our app was a big priority for us.The Facebook api allows us to access information from our own profiles and the profiles of our friends. Most of the code for this is written in the FBProfileInfo class. The app retrieves the users Facebook profile picture and sets it as their display picture in the app. It also allows us to see who on our Facebook friends list has downloaded the app and logged in with Facebook. These friends are then displayed in a list view making it easy to choose from friends to meet.

4. Requests and notifications.




