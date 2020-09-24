# iProov Android API Client v1.0.2

## 📖 Table of contents

- [Introduction](#-introduction)
- [Registration](#-registration)
- [Installation](#-installation)
- [Supported functionality](#-supported-functionality)
- [Example](#-example)
- [Example App](#-example-app)

## 👋 Introduction

The iProov Android API Client is a simple wrapper for the [iProov REST API v2](https://secure.iproov.me/docs.html) written using [Retrofit](https://square.github.io/retrofit/) in both Java and Kotlin and [Fuel]() in Kotlin for the HTTP networking and JSON serialization/deserialization using [Gson](https://github.com/google/gson). This thus provides three libraries to choose from, to suit technologies you are used to. We also have an iOS Swift API client available [here](https://github.com/iProov/ios-api-client).

v5 of the [iProov SDK](https://github.com/iProov/android) removed the built-in functionality to obtain tokens using the SDK. This library therefore provides that missing functionality as a separate library, and also provides additional functionality such as the ability to enrol photos.

### ⚠️ Important security notice

The iProov REST API should only ever be called directly from your back-end, however this library is designed to help you with debugging/evaluating the [iProov Android SDK](https://github.com/iProov/android), to get up-and-running quickly with a pure on-device demo.

Use of the Android API Client requires providing it with your API secret. **You should never embed your API secret within a production app**.

## ✍️ Registration

You can obtain API credentials by registering on the [iProov Partner Portal](https://www.iproov.net).

## 📲 Installation

Choose which of these libraries to use for easy access to the basic iProov API v2.

+ **kotlinfuel** is built in Kotlin and uses Fuel for network calls
    + Maven: `com.iproov.android-api-client:kotlin-fuel:1.0.0`
    + Limited to SDK 19+

+ **kotlinretrofit** is built in Kotlin and uses Retrofit for network calls
    + Maven `com.iproov.android-api-client:kotlin-retrofit:1.0.0`
    + Limited to SDK 9+

+ **javaretrofit** is built in Java and uses Retrofit for network calls
    + Maven `com.iproov.android-api-client:java-retrofit:1.0.0`
    + Limited to SDK 9+

Add to the repositories section to your build.gradle file (example shown is in groovy gradle and for kotlin retrofit library):

```gradle
    repositories {
        maven { url 'https://raw.githubusercontent.com/iProov/android/master/maven/' }
    }
```

Add the dependencies section to your app build.gradle file:

```gradle
    dependencies {
        implementation 'com.iproov.android-api-client:kotlin-retrofit:1.0.0'
    }
```

When using any of the Kotlin versions, you will also need to add the coroutines dependencies to your app build.gradle file:

```gradle
    dependencies {
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0'
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0'
    }
```

## 🛠 Supported functionality

All three libraries offer the same functionality:

- **`getToken()`** - Get an enrol/verify token.
- **`enrolPhoto()`** - Once you have an enrolment token, you can enrol a photo against it.
- **`validate()`** - Validates an existing token.
- **`enrolPhotoAndGetVerifyToken()`** - A helper function which chains together `getToken()` for the enrolment token, `enrolPhoto()` to enrol the photo, and then `getToken()` for the verify token, which you can then use to launch the SDK.

## 🤳 Example

Example of using iProov API Client (in this case Kotlin and Java Retrofit respectively) together with iProov to get a verify token for an existing user and then launch the iProov SDK to perform the verification:

##### Kotlin
```kotlin
private val uiSupervisorJob = SupervisorJob()
private val uiScope = CoroutineScope(Dispatchers.Main + uiSupervisorJob)

val apiClient = ApiClientRetrofit(
    context = this,
    baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
    logLevel = HttpLoggingInterceptor.Level.BODY,
    apiKey = "{{ your API key }}",
    secret = "{{ your API secret }}"
)

uiScope.launch {
    try {
        val token = withContext(Dispatchers.IO) {
            apiClient.getToken(ClaimType.VERIFY, userId).token
        }
        // Pass the token to the iProov SDK

    } catch (httpEx: HttpException) {
        // Handle exception

    }
}
```
##### Java
```java
ApiClientJavaRetrofit apiClient = new ApiClientJavaRetrofit(
        this,
        "https://eu.rp.secure.iproov.me/api/v2/",
        HttpLoggingInterceptor.Level.BODY,
        "{{ your API key }}",
        "{{ your API secret }}");

apiClient.getToken(
        ClaimType.VERIFY,
        userID,
        (call, response) -> {
            String token = response.body().getToken();
            // Pass the token to the iProov SDK

        },
        throwable -> {
          // Handle exception

        });
```
## 🚀 Example App

The Example App included, written in Kotlin with Coroutines, demonstrates the use of all three libraries, with simple text output to both screen and logs.

To run the example you need to include a *secrets.json* file in the
src/main/assets/ directory that looks like this:

~~~
{
  "api_key": <YOUR_CLIENT_ID>,
  "secret": <YOUR_SECRET>
}
~~~

This file SHOULD NOT be added to the repo. It is excluded by gitignore
for your own safety.
