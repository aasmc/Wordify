# Wordify 
Qualification project for Android Developer Professional course from Otus.

## Functionality

The app allows the user to find definitions, examples of usage and characteristics of more than 
300 000 words. The app uses Words API to retrieve data. https://www.wordsapi.com
To run the app you must provide API key (you can get a free key from https://rapidapi.com/dpventures/api/wordsapi/pricing).
Add gradle.properties to your project and your key to gradle properties:
```text
# Project-wide Gradle settings.
# IDE (e.g. Android Studio) users:
# Gradle settings configured through the IDE *will override*
# any settings specified in this file.
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
# Automatically convert third-party libraries to use AndroidX
android.enableJetifier=true
# Kotlin code style for this project: "official" or "obsolete":
kotlin.code.style=official
org.gradle.parallel=true
org.gradle.caching=true
words_key="YOUR_API_KEY"
```

## Screens:
### 1. Home Screen.
Shows the user all the words that were previously searched. The user can save a word to 
favourites, or open the screen with detailed information about a word.

<img src="https://github.com/aasmc/Wordify/blob/master/Art/home_screen_dark.png" width="380" height="800" />
   
### 2. Word Details Screen.
Displays various characteristics of a word:
- pronunciation
- syllables
- different definitions
- examples of usage
- derivations,
- etc.

<img src="https://github.com/aasmc/Wordify/blob/master/Art/details_screen_dark.png" width="380" height="800" />

### 3. Favourites Screen. 
Displays a list of words the user saved as favourite. 

<img src="https://github.com/aasmc/Wordify/blob/master/Art/fav_screen_dark.png" width="380" height="800" />

### 4. Settings Screen.
Here the user can:
- save sorting preference by
    - word name in ascending and descending order
    - time added in ascending and descending order
- save app theme preferences:
    - system theme
    - light theme
    - dark theme

<img src="https://github.com/aasmc/Wordify/blob/master/Art/settings_screen_dark.png" width="380" height="800" />
 
### Search
When the user searches for a word, a list of recently searched words is displayed. 

<img src="https://github.com/aasmc/Wordify/blob/master/Art/search_screen_light.png" width="380" height="800" />

## Architecture
The app is build according to the guidelines of Clean Architecture and some of the MVI patterns (to ensure
unidirectional data flow). 
## 1. Domain layer
Contains:
- model classes describing a word and its properties.
- repository contracts for working with words and preferences
- business logic in the form of use cases

## 2. Data layer
Contains logic to retrieve data from the Internet, save it to the database and provide to the UI. 

### Networking
- HTTP requests are handled with Retrofit and OkHttpClient. 
- Serialization is performed with Moshi (I had to write custom JSON adapter, because sometimes API returns 
  different results).
- MockWebServer is used for testing. 
- Authentication interceptor is used to add API key as a header for all requests. 
- Network status interceptor is used to monitor network connection

### Database
Data persistence is performed with SharedPreferences and ORM Room. DAOs are tested using inMemory database. 
Potentially the number of words saved in the database may exceed a thousand, therefore Paging 3 library 
is used for pagination of the data. 

### Repository
WordifyWordRepository implements domain contract for repository. It uses **Room database** as the single source of truth.
When the user searches for a word, the request first goes to the database, if there's no such word, the request
proceeds to the Network, if it is successful, the word is then saved to the database, and provided to the client 
from the database. 

**Kotlin flows** are used to retrieve paginated data from the cache. **Suspend** functions are used for one-shot events like
retrieving a single word from database or setting an isFavourite property of a word.

## 3. Presentation Layer
All the UI in the app is written with **Jetpack Compose**. To ensure unidirectional data flow I use events,
which trigger various changes of the data, which is then observed by the UI. State of the views is immutable
to ensure thread safety and consistency. 

**Navigation** between screens is performed using Jetpack Compose Navigation library. Each screen except the
Settings screen contains a nested graph.  

## Dependency Injection
DI is performed using Dagger Hilt. 
Api, Cache and Preferences are singletons.
Repository is installed in ActivityRetainedComponent. 

### Known Issues
- When isFavourite button of a word in Details screen is clicked, then navigating to some other screen
  (not back pressed), then returning back to the Details screen, the icon is wrong. One possible solution
  is refreshing the word from cached on button click, but it forces the screen to flicker for a moment,
  because the word is getting loaded. 