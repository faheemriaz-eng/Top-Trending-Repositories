# Trending App

### Assumptions and Notes

- To limit the API calls, I’ve opted to use caching, with an option to allow user to fetch new data as well. For caching, I chose shared preferences to keep it simple and lightweight. I could have used cache in retrofit client but I didn’t want to rely on a third party client for caching. Having my own lightweight and simple caching gives me more control.
- Used Repository pattern because Repository pattern implements separation of concerns by abstracting the data persistence logic in your applications.
- Add Refresh functionality under options menu if user wants to see the fresh data.
- Did some refactoring in unit-tests,data-layer,viewModel after the functionality is done.

### Build With 🏗️

- Kotlin - Programming language for Android
- Hilt-Dagger - Standard library to incorporate Dagger dependency injection into an Android application.
- Retrofit -  A HTTP client.
- Coroutines - For asynchronous
- LiveData - Data objects that notify views.
- ViewModel - Stores UI-related data that isn't destroyed on UI changes.
- ViewBinding - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- Glide - An image loading and caching library for Android focused on smooth scrolling

### Project Architecture 🗼

This app uses [MVVM (Model View View-Model)] architecture.