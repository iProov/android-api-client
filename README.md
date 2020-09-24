# iProov Android API Client

## 👋 Introduction

The iProov Android API Client is a simple wrapper for the [iProov REST API v2](https://secure.iproov.me/docs.html) written using [Retrofit](https://square.github.io/retrofit/) in both Java and Kotlin and [Fuel]() in Kotlin for the HTTP networking and JSON serialization/deserialization using [Gson](https://github.com/google/gson). This thus provides three libraries to choose from, to suit technologies you are used to. We also have an iOS Swift API client available [here](https://github.com/iProov/ios-api-client).

v5 of the [iProov SDK](https://github.com/iProov/android) removed the built-in functionality to obtain tokens using the SDK. This library therefore provides that missing functionality as a separate library, and also provides additional functionality such as the ability to enrol photos.

## ⚠️ Important security notice

The iProov REST API should only ever be called directly from your back-end, however this library is designed to help you with debugging/evaluating the [iProov Android SDK](https://github.com/iProov/android), to get up-and-running quickly with a pure on-device demo.

Use of the Android API Client requires providing it with your API secret. **You should never embed your API secret within a production app**. 

## ✍️ Registration

You can obtain API credentials by registering on the [iProov Partner Portal](https://www.iproov.net).

## 📲 Installation

Choose which of these libraries to use for easy access to the basic iProov API v2.

+ **kotlinfuel** is built in Kotlin and uses Fuel for network calls
    + Maven: `com.iproov.android-api-client:kotlin-fuel:1.0.0`
    + Limited to SDK 19+
    + Depends on coroutines

+ **kotlinretrofit** is built in Kotlin and uses Retrofit for network calls
    + Maven `com.iproov.android-api-client:kotlin-retrofit:1.0.0`
    + Limited to SDK 9+
    + Depends on coroutines

+ **javaretrofit** is built in Java and uses Retrofit for network calls
    + Maven `com.iproov.android-api-client:java-retrofit:1.0.0`
    + Limited to SDK 9+
    
Add to the repositories section to your build.gradle file (example shown is in groovy gradle and for kotlin fuel library):

```gradle
    repositories {
        maven { url 'https://raw.githubusercontent.com/iProov/android/nextgen/maven/' }
    }
```

Add the dependencies section to your app build.gradle file:

```gradle
    dependencies {
        implementation 'com.iproov.android-api-client:kotlin-fuel:1.0.0'
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
class KotlinActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "KotlinActivity"
    }

    private lateinit var connection: IProov.IProovConnection
    private lateinit var clientId: String
    private lateinit var secret: String
    private lateinit var userId: String

    private val uiSupervisorJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + uiSupervisorJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_kotlin)
        setSupportActionBar(toolbar)

        connection = IProov.getIProovConnection(this)

        buttonStart.setOnClickListener { getToken(userId) }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.stop()
        uiScope.coroutineContext.cancelChildren()
    }

    private fun report(message: String) {
        Log.i(TAG, "Report: $message")
    }

    private fun getToken(userId: String) {
        report("Start Kotlin Retrofit")

        val apiClient = ApiClientRetrofit(
            context = this,
            baseUrl = "https://eu.rp.secure.iproov.me/api/v2/",
            logLevel = HttpLoggingInterceptor.Level.BODY,
            apiKey = clientId,
            secret = secret
        )

        uiScope.launch {
            try {
                val token = withContext(Dispatchers.IO) { 
                    apiClient.getToken(ClaimType.VERIFY, userId)
                }
                report("getToken success = $token")
                startIProov(token.token)
            } catch (httpEx: HttpException) {
                httpEx.printStackTrace()
                report("getToken failure $httpEx")
            }
        }
    }

    private fun startIProov(tokenValue: String) {
        val options = IProov.Options()
        options.apply {
            capture.maxRoll = 0.05f
            capture.maxYaw = 0.05f
        }

        connection.launch(options, tokenValue, object : IProov.IProovCaptureListener {
            override fun onSuccess(token: String) {
                report("Successfully iProoved. Token: $token")
                finish()
            }

            override fun onFailure(reason: String?, feedback: String?) {
                report("Failed to iProov. Reason: $reason feedback: $feedback")
                finish()
            }

            override fun onProgressUpdate(message: String, progress: Double) {
                report("Progress ${(progress * 100).toInt()}: $message")
            }

            override fun onCancelled() {
                report("Cancelled")
                finish()
            }

            override fun onError(e: IProovException) {
                report("Error: ${e.localizedMessage}")
                finish()
            }
        })
    }
}
```
##### Java
```java
public class JavaActivity extends AppCompatActivity {

    private static final String TAG = "JavaActivity";

    private IProov.IProovConnection connection = IProov.getIProovConnection(this);
    private String clientId = "";
    private String secret = "";
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_java);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connection = IProov.getIProovConnection(this);

        findViewById(R.id.buttonStart).setOnClickListener(view -> getTokenAndStartIProov(userId));
    }

    @Override
    protected void  onDestroy() {
        super.onDestroy();
        connection.stop();
    }

    private void report(String message) {
        Log.i(TAG, "Report: " + message);
    }

    private void getTokenAndStartIProov(String userID) {

        ApiClientJavaRetrofit apiClient = new ApiClientJavaRetrofit(
                this,
                "https://eu.rp.secure.iproov.me/api/v2/",
                HttpLoggingInterceptor.Level.BODY,
                clientId,
                secret);

        apiClient.getToken(
                ClaimType.VERIFY,
                userID,
                (call, response) -> {
                    report("getToken success = $token");
                    startIproovForVerifyClaim(response.body().getToken());
                },
                throwable -> {
                    report("Failed to get token.");
                });
    }

    private void startIproovForVerifyClaim(final String token) {
        IProov.Options options = new IProov.Options()
            .capture.setMaxRoll(0.05f)
            .capture.setMaxYaw(0.05f);

        connection.launch(options, token, new IProov.IProovCaptureListener() {

            @Override
            public void onSuccess(String token) {
                report(String.format("Successfully iProoved. Token: %s", token));
                finish();
            }

            @Override
            public void onFailure(String reason, String feedback) {
                report(String.format("Failed to iProov. Reason: %s feedback: %s", reason, feedback));
                finish();
            }

            @Override
            public void onProgressUpdate(String message, double progress) {
                report(String.format("Progress %d: %s", (int)(progress * 100), message));
            }

            @Override
            public void onCancelled() {
                report("Cancelled");
                finish();
            }

            @Override
            public void onError(IProovException e) {
                report(String.format("Error: %s", e));
                finish();
            }
        });
    }
}
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
