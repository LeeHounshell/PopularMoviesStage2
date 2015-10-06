Popular Movies, Stage2
======================

I'm participating in the Udacity+Google Android Nanodegree program.
And this is the Popular Movies, Stage2 assignment.


## Description

This app provides ability to discover Popular and Highly-Rated movies.
All movies and associated images, trailers, and reviews are cached
locally in SQLite.  The database uses a Content Provider generator.
To alter the database strucutre, edit the JSON configs and re-run the
generation script located under generate_data_contentprovider.
This will build a new 'data' directory with code to manage the database.
When the app is launched, it will fetch, store and display relevant movie data.
The local database caches forever, and there is no option to clear it
(other than reinstalling the app).


## Features

 * Movie Details are easily shared from the Action Bar.
 * A 'favorites' button will add or remove movies from the local 'favorites' table.
 * Movie Reviews are automatically loaded from the movie's detail view.
 * A 'reviews' button will update and scroll to the latest reviews.
 * A drop-down spinner allows easy selection and viewing of video trailers.
 * A Master/Detail view shows for Tablets.
 * Unit Tests for validating the generated database.
 * Ability to view All-Cached, Favorites, Popular and Highly-Rated from the Action Bar.
 * Ability browse offline all cached movies and favorites.

This app uses API and data from [The Movie Database](https://www.themoviedb.org/documentation/api) to retrieve movies.
You need to provide your own API key in order to run the app.  Place your API key into util/Tmdb_API_KEY.java


## Screenshots

![screen](../master/screens/phone-screenshot1.png)

![screen](../master/screens/phone-screenshot2.png)

![screen](../master/screens/tablet-screenshot1.png)


## License

This work is Copyright 2015 Lee Hounshell, and 
is licensed under a Creative Commons Attribution-NonCommercial 3.0 
Unported License. See http://creativecommons.org/licenses/by-nc/3.0 for
the license details.

