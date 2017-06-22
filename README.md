WiFi Manager Prototype
========

This is a prototype Android application that is designed to represent wifi strength in an aesthetically pleasing way.

Setup
-----

The following setup guide assumes that you are using the Android SDK bundle.

In order to setup the project, ensure that you have Google Play services installed. To install:

1. Open up the Android SDK manager
2. Under the "Extras" folder, select "Google Play services" and click install

Before the library can be used, it must be imported into Eclipse alongside the project and a dependency must be defined. To import:

1. File -> Import -> Android -> Existing Android Code into Workspace
2. Find the library that was downloaded and import it into Eclipse:
  1. Downloaded library "google-play-services_lib" should be located in "(path-to-adt-bundle)\sdk\extras\google\google_play_services\libproject"
  2. In Eclipse, click on File -> Import -> Android -> Existing Android Code into Workspace
3. Define a dependency on the appropriate project:
  1. Right click on the project and select Properties
  2. In the pop-up window, select "Android" from the left navigation bar
  3. Under the "Library" section, click on the add button, and select the google play library that was imported

The project should now be set up properly.

Colour Scheme and Text
-----

If you want to change the colour scheme, just go to res/values/colors.xml. The rest should be pretty self explanatory.

If anyone wants to change any of the text (like the descriptions or something), use the res/values/strings.xml file. Again, rest should be pretty self explanatory.
