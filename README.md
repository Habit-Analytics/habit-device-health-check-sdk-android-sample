# Device Health SDK

Device Health is an SDK that allows you to easily perform tests to check the state of a device.

> The minimum Android version supported is Android 6. 

> Built with Android Studio 4.1.3

 <br/>

## Requesting an App ID and API Key

To integrate the Device Health SDK you will need an **App ID** and an **API key**. For more information on how to obtain these please email us at developer@habit.io
 <br/><br/> 


# Installation
You can integrate the SDK in your app by importing the **DeviceHealth.aar** directly into your project. 

To integrate the Device Health SDK you will need request the **DeviceHealth.aar** library file as well as the **AppID** and the **API key**. For more information on how to obtain these please email us at developer@habit.io

1. Once you have the _**DeviceHealth.aar**_ file, copy it to the _**libs**_ folder in the _**main module (app)**_.

2. Afterwards, add the following lines to the dependencies in _**build.gradle**_ of your _**main module. (app)**_

    ```
        compile files('libs/DeviceHealth.aar')
        implementation 'com.android.volley:volley:1.1.1'
        implementation 'com.google.zxing:core:3.4.1'
        implementation 'com.budiyev.android:code-scanner:2.1.0'
        implementation 'com.jakewharton:butterknife:10.2.3'
        implementation 'com.intuit.sdp:sdp-android:1.0.4'
    ```

3. Click Sync Now button to rebuild the gradle.


 <br/><br/> 
# Usage


 <br/>

## SDK Configuration
### **Language**

The language of DeviceHealthSDK can be configured and currently supports **Portuguese** and **English** (default). Make sure you change this setting before starting to perform the tests. Usage is as follows:

```java
// Configuration of SDK language: {en, pt} 
// this -> Context

DeviceHealth.setLanguage(this, "en");
```

 <br/> 

### **Theme Color**
In order to provide for a smoother transition between the app and the test screens it is possible to change the overall theme color for the SDK. By default the theme color is blue but it can be changed to whatever **Color** you specify. Make sure you change this setting before starting to perform the tests.

```java
// Configuration of the SDK theme Color

// this -> Context
    DeviceHealth.setThemeColor(this, Color.argb(255, 156, 34, 93));
```
 <br/><br/> 

## Tests Selection
The tests to perform should be passed as a parameter in an array of strings. The available tests can be obtained using **TestType** as seen in the following snippet. _The order in which the tests are presented is the same order as they are defined in the array._
If no tests are passed as a parameter, then it is assumed that all supported tests will be performed.

```java
     String[] testsToPerform = new String[]{
                TestType.buttons_v1,
                TestType.multi_touch_v1,
                TestType.charging_v1,
                TestType.speaker_recording_v1,
                TestType.earspeaker_v1,
                TestType.loudspeaker_v1,
                TestType.read_text_v1,
                TestType.device_front_video_v1
        };
```


For more details on the what the different tests entail please check the [Available Tests](#available-tests) section of this guide. 

 <br/><br/> 

# Perform tests
After all is setup you just need to call the following function with the appropriate parameters and the tests interface will appear and once they're finished a response will be received with all the results. For more details regarding the format of the response check the [Test Results](#tests-results) section. 

You can optionally pass the IMEI of the device via parameter but otherwise the SDK will request the IMEI.

```java
// this -> Context, this -> Activity
DeviceHealth.performTests(this, this, "<-your app id->", "<-your api key->", "<-device IMEI->", testsToPerform, new TestCallback() {
            @Override
            public void onResponse(JSONObject obj) {
		
		// obj == null : Canceled
		// obj != null : Success

                if (obj != null) {
                    tvDeviceInfo.setText(formatString(obj.toString()));
                }
            }
        });
```
<br/> 



<br/> 

## Device Info
If you wish to only obtain information about the device without running any tests you can do so by calling the following function:

```java
// this -> Context, this -> Activity

  DeviceHealth.getDeviceInfo(this, this, new DeviceInfoCallback() {
      @Override
      public void onResponse(JSONObject obj) {

		// obj == null : Canceled
		// obj != null : Success
    
      }
  });
```

For more details regarding the format of the response check the [Device Info](#device-info) section.

 <br/><br/> 

# Available Tests

The SDK supports several tests from which you can choose from. The currently supported tests are the following:

| Test | TestType | Description |
| ------ | ------ | ----------- |
| Multi touch | multi_touch_v1 | This tests if the screen is capable of detecting gestures with multiple touch. |
| Buttons | buttons_v1 |This test checks if the physical buttons of the phone are working properly |
| Screen | device_front_video_v1 |This test is performed using a mirror to record a video to detect if there are any anomalies with the screen, if it's broken or if there are dead pixels in the screen.|
| Sound system | speaker_recording_v1 | Plays and records a sound. |
| Sound system - Earspeaker | earspeaker_v1 | The user is prompted to hear a word through the earspeaker of the device and write it. |
| Sound system - Loudspeaker | loudspeaker_v1 | The user is prompted to hear a word through the loudspeaker of the device and write it. |
| Read text | read_text_v1 | The user is asked to say and record a specific text. |
| Charging | charging_v1 | The user is asked to plug in the charging cable. |

 <br/><br/> 


# Results formats

## Tests Results


The response obtained after the tests are finished include the tests_results as well as the device_info_v1 in a JSON object with the following format: 

```swift
{
  "tests_results" : {
    "speaker_recording_v1" : {
      "url" : "<url-to-uploaded-file>",
      "success" : Bool
    },
    "buttons_v1" : {
      "volume_down" : {
        "success" : Bool,
        "timestamp" : "<timestamp>"
      },
      "volume_up" : {
        "success" : Bool,
        "timestamp" : "<timestamp>"
      },
      "screenshot" : {
        "timestamp" : "<timestamp>",
        "success" : Bool
      },
      "timestamp" : "<timestamp>"
    },
    "read_text_v1" : {
      "timestamp" : "<timestamp>",
      "url" : "<url-to-secure-location",
      "success" : Bool,
      "text" : "<word-presented>"
    },
    "earspeaker_v1" : {
      "success" : Bool,
      "audio_word" : "<audio-string-representation>",
      "timestamp" : "<timestamp>",
      "input_word" : "<string-input-by-user>"
    },
    "multi_touch_v1" : {
      "timestamp" : "<timestamp>",
      "success" : Bool
    },
    "charging_v1" : {
      "battery_state" : "charging",
      "battery_level" : 0.99000000953674316,
      "success" : Bool,
      "timestamp" : "<timestamp>"
    },
    "device_front_video_v1" : {
      "url" : "<url-to-uploaded-file>",
      "timestamp" : "<timestamp>",
      "success" : Bool,
      "start_qr_code" : "<start-qr-code-string>",
      "end_qr_code" : "<end-qr-code-string>",
      "md5" : "<md5-string-value>"
    },
    "loudspeaker_v1" : {
      "timestamp" : "<timestamp>",
      "input_word" : "<string-input-by-user>",
      "success" : Bool,
      "audio_word" : "<audio-string-representation>"
    }
  }
```

</br>

## Device Info
The response for the DeviceInfo is a JSON object in the following format: 


```swift
{
    "android_id" : String,
    "imei_values" : [String],
    "imei_image_url" : String,
    "brand" : String,
    "thermal_state" : String,
    "device_name" : String,
    "device_model_commercial" : String,
    "device_model_identifier" : String,
    "manufacturer" : String,
    "os_version" : String,
    "os_name" : String,
    "uptime" : Float,
    "root_access" : Bool,
    "board" : String,
    "fingerprint" : String,
    "cpu_temperature" : String,
    "app_version" : String,
    "app_build_number" : String,
    "sdk_version_build" : String,
    "battery_capacity" : String,
    "battery_health" : String,
    "battery_temperature" : String,
    "battery_voltage" : String,
    "battery_state" : String,
    "battery_level" : Float,
    "ram_total" : Float,
    "ram_available" : Float,
    "process_name" : String,
    "timestamp" : String,
    "device_model" : String,
    "processor_count" : Int,
    "free_disk_space" : String,
    "used_disk_space" : String,
    "total_disk_space" : String,
    "carriers" : [
      {
        "carrier_name" : String,
        "allows_voip" : Bool,
        "iso_country_code" : String,
        "mobile_country_code" : String,
        "mobile_network_code" : String,
        "imei" : String
      }
    ],
}
```



## Status Codes

The SDK supports several tests from which you can choose from. The currently supported tests are the following:

| Code |  Description |
| ------ | ------ | 
| 200 | Test finished | 
| 301 | Test was canceled by user 
| 401 | Invalid App ID |
| 402  | Invalid API Key |
| 403  | Invalid IMEI |
| 406  | A server error occurred. | 


 <br/><br/> 

