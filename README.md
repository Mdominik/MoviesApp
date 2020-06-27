# MoviesApp
## The app parses JSON data from theMovieDB API.
![Alt Text](presentation.gif)

TODO in V.2:

App Software design:
1) Use Parcelable to move data between activities
2) constant values always as final
3) Tasks in Service class
4) API Key delete from github
5) Use Retrofit to establish HTTP communication
6) GSON library instead of plain JSON

Features:
1) Preferences: right top corner settings -> change day/night mode.
2) Add favourite movies (by clicking heart)
3) Writing comments
4) All comments and favs will appear on the MainActivity
5) Movie detail additionally contains info about:
  - trailer (embedded yoututbe video)
  - length of the movie in min
  - genre
  - director
  - cast (actors and actress, scrolling horizontally)
  - right bottom corner - click to show filtering options (filter by top rated, popularity + genre)
