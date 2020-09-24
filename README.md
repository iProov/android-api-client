# iProov Demonstration APIClient

## WARNING
This code should not be used in production systems.
It is only provided to support demonstration apps.
Client api key and secret should not be held in client app.

## Example App
The Example App demonstrates the use of one of the libraries in one of 
the Activities. Select which activities to launch in the manifest to
see it in action.

To run the harness you need to include a *secrets.json* file in the
src/main/assets/ directory that looks like this:

~~~
{
  "api_key": <YOUR_CLIENT_ID>,
  "secret": <YOUR_SECRET>
}
~~~

This file SHOULD NOT be added to the repo. It is excluded by gitignore
for your own safety.

## Libraries
These libraries provide easy access to basic iProov API v2.

+ *kotlinfuel* is built in Kotlin and uses Fuel for network calls
    + Maven
         + com.iproov.android-api-client:kotlin-fuel:<latest-version>
    + Limited to SDK 19+

+ *kotlinretrofit* is built in Kotlin and uses Retrofit for network calls
    + Maven
         + com.iproov.android-api-client:kotlin-retrofit:<latest-version>
    + Limited to SDK 9+

+ *javaretrofit* is built in Java and uses Retrofit for network calls
    + Maven
         + com.iproov.android-api-client:java-retrofit:<latest-version>
    + Limited to SDK 9+
