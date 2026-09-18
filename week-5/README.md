# DummyJSON auth demo

A Jetpack Compose Android app using https://dummyjson.com/docs/auth.

Networking uses Retrofit with the Gson converter and an OkHttp client. `AuthService` declares the endpoints with `@POST`, `@Body`, `@GET`, and `@Header`. `AuthNetwork` configures timeouts and an OkHttp interceptor for the Accept header. `DummyJsonAuthApi` maps Retrofit HTTP errors to the repository's API errors. The repository supplies the access token explicitly for the profile request.

- Sign in with `emilys` / `emilyspass`, or another DummyJSON account.
- Login calls `POST /auth/login` with a 30-minute access-token lifetime.
- Profile loads from `GET /auth/me` with `Authorization: Bearer <accessToken>`.
- The access token stays in ViewModel-owned memory across configuration changes, but is lost when the process ends. Passwords and tokens are not persisted or logged.
- Sign out clears the token. A profile response of 401 or 403 clears the session and asks the user to sign in again.
- Network/server errors allow a manual retry. There is no Authenticator, refresh endpoint call, refresh-token storage, or automatic retry.

Open the project in Android Studio and run the app, or run `gradlew.bat :app:assembleDebug :app:testDebugUnitTest` with a configured JDK and Android SDK.
