# Device Health SDK

Device Health is an SDK that allows you to easily perform tests to check the state of a device.

> The minimum Android version supported is Android 8.

> Built with Android Studio Dolphin | 2021.3.1

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
       // Default plugins for build.graddle
       implementation 'androidx.appcompat:appcompat:1.2.0'
       implementation 'com.google.android.material:material:1.3.0'
       implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
       testImplementation 'junit:junit:4.+'
       androidTestImplementation 'androidx.test.ext:junit:1.1.2'
       androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

       // Import the libraries for AAR
       compile files('libs/DeviceHealth.aar')
       implementation 'com.android.volley:volley:1.1.1'
       implementation 'com.google.zxing:core:3.4.1'
       implementation 'com.budiyev.android:code-scanner:2.1.0'
       implementation 'com.jakewharton:butterknife:10.2.3'
       implementation 'com.intuit.sdp:sdp-android:1.0.4'
       implementation 'com.scottyab:rootbeer-lib:0.1.0'

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

 <br/>

### **Hide Start Screen**

Also, to provide a smoother transition between the app and the test screens it is possible to hide the SDK's start screen in case you want to show a custom in-app screen. Make sure you change this setting before starting to perform the tests.

```java
// Hiding the Start screen

    DeviceHealth.hideStartScreen(true);
```

<br/><br/>

## Customization

In order to achieve a seamingless integration of the SDK, we've created customization features that allow you to customize the SDK in a way that fits in better with your app.

### **General customization**

The following general UI customizations can be done:

```java

      Customization customization = new Customization();

      customization.setSkipTestButtonText("<- your custom skip button text ->");
      customization.setNextButtonText("<- your custom next button text ->");

      ButtonStyle buttonStyle = new ButtonStyle();
      buttonStyle.setBackgroundColor(Color.rgb(255, 255, 0));
      buttonStyle.setForegroundColor(Color.rgb(255, 0, 0));
      buttonStyle.setBorderType(BorderType.Square);
      customization.setButtonsStyle(buttonStyle);

      customization.setSkipTestButtonColor(Color.rgb(0, 0, 0));
      customization.setProgressBarBackgroundColor(Color.rgb(255, 255, 255));
      customization.setProgressBarSelectedColor(Color.rgb(0, 0, 0));
      customization.setCustomNavigationBarBackgroundColor(Color.rgb(99, 5, 220));
      customization.setCustomNavigationBarTextColor(Color.rgb(0, 0, 0));
      customization.setCustomNavigationBarButtonsTextColor(Color.rgb(255, 255, 255));
```

 <br/>

### **Screen customization**

Screens can be customized individually by creating CustomizableScreens and adding them to the Customization parameter. In the following snippet you can see an example on how to customize the Start Screen.

```java

      Customization customization = new Customization();

      Map<String, String> customStartCopy = new HashMap<>();
      customStartCopy.put(ScreenCustomizationKeys.start_screen.Copy.title, "My custom title");
      customStartCopy.put(ScreenCustomizationKeys.start_screen.Copy.description, "My custom description");

      Map<String, Drawable> images = new HashMap<>();
      // kitten image
      images.put(ScreenCustomizationKeys.start_screen.Elements.image, context.getDrawable(R.drawable.kitten));

      CustomizableScreen screen = new CustomizableScreen();
      screen.screenType = ScreenType.start_screen;
      screen.backgroundColor = Color.rgb(255, 0, 0);
      screen.textAccentColor = Color.rgb(255, 255, 0);
      screen.textColor = Color.rgb(255, 255, 255);
      screen.images = images;
      screen.copyStrings = customStartCopy;

      CustomizableScreen[] customScreens = new CustomizableScreen[] {
        screen
      };
      customization.setCustomScreens(customScreens);
```

<br/><br/>

## Tests Selection

The tests to perform should be passed as a parameter in an array of strings. The available tests can be obtained using **ScreenType** as seen in the following snippet. _The order in which the tests are presented is the same order as they are defined in the array._
If no tests are passed as a parameter, then it is assumed that all supported tests will be performed.

```java
      String[] testsToPerform = new String[]{
              ScreenType.buttons_v2,
              ScreenType.charging_v2,
              ScreenType.multi_touch_v2,
              ScreenType.device_front_video_v2
      };
```

For more details on the what the different tests entail please check the [Available Tests](#available-tests) section of this guide.

<br/><br/>

# Perform tests

After all is setup you just need to call the following function with the appropriate parameters and the tests interface will appear and once they're finished a response will be received with all the results. For more details regarding the format of the response check the [Test Results](#tests-results) section.

You can optionally pass the IMEI of the device via parameter but otherwise the SDK will request the IMEI.

```java
// this -> Context, this -> Activity
DeviceHealth.performTests(this, this, "<-your app id->", "<-your api key->", "<-serial number->", "<-device IMEI->", testsToPerform, new TestCallback() {
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

  DeviceHealth.getDeviceInfo(this, this, "<-serial number->", "<-device IMEI->", new DeviceInfoCallback() {
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

| Test        | TestType              | Description                                                                                                                                                             |
| ----------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Multi touch | multi_touch_v2        | This tests if the screen is capable of detecting gestures with multiple touch.                                                                                          |
| Buttons     | buttons_v2            | This test checks if the physical buttons of the phone are working properly                                                                                              |
| Screen      | device_front_video_v2 | This test is performed using a mirror to record a video to detect if there are any anomalies with the screen, if it's broken or if there are dead pixels in the screen. |
| Charging    | charging_v2           | The user is asked to plug in the charging cable.                                                                                                                        |

<br/><br/>

# Results formats

## Tests Results

The response obtained after the tests are finished include the tests_results as well as the device_info_v1 in a JSON object with the following format:

```swift
{
  "tests_results" : {
    "buttons" : {
      "version" : String,
      "volume_down" : {
        "success" : Bool,
        "timestamp" : "<timestamp>"
      },
      "volume_up" : {
        "success" : Bool,
        "timestamp" : "<timestamp>"
      },
      "timestamp" : "<timestamp>"
    },
    "multi_touch" : {
      "version" : String,
      "timestamp" : "<timestamp>",
      "success" : Bool
    },
    "charging" : {
      "version" : String,
      "battery_state" : "charging",
      "battery_level" : 0.99000000953674316,
      "success" : Bool,
      "timestamp" : "<timestamp>"
    },
    "device_front_video" : {
      "version" : String,
      "url" : "<url-to-uploaded-file>",
      "timestamp" : "<timestamp>",
      "success" : Bool
    }
  },
  "device_info" : {
    "device_info_version" : String,
    "used_disk_space" : String,
    "thermal_state" : String,
    "device_model_identifier" : String,
    "app_version" : String,
    "free_disk_space" : String,
    "battery_state" : String,
    "process_name" : String,
    "battery_level" : Float,
    "os_name" : String,
    "ram_bytes" : Int,
    "carriers" : [
      {
        "carrier_name" : String,
        "allows_voip" : Bool,
        "iso_country_code" : String,
        "mobile_country_code" : String,
        "mobile_network_code" : String
      }
    ],
    "timestamp" : String,
    "device_model" : String,
    "os_version" : String,
    "processor_count" : Int,
    "os_version_detailed" : String,
    "detected_ids" : [
      {
        "android_id" : String,
	"serial_number" : String,
        "imei_values" : [ String ]
      }
    ],
    "received_ids" : [
      {
        "serial_number" : String,
        "imei_values" : [ String ],
        "serial_number_image_url" : [ String ],
        "imei_image_url" : [ String ],
      }
    ],
    "uptime" : Timestamp,
    "device_name" : String,
    "device_model_commercial" : String,
    "app_build_number" : String,
    "active_processor_count" : Int,
    "total_disk_space" : String
  }
}
```

</br>

## Device Info

The response for the DeviceInfo is a JSON object in the following format:

```swift
{
  "device_info_version" : String,
    "detected_ids" : [
      {
        "android_id" : String,
	"serial_number" : String,
        "imei_values" : [ String ]
      }
    ],
    "received_ids" : [
      {
        "serial_number" : String,
        "imei_values" : [ String ],
        "serial_number_image_url" : [ String ],
        "imei_image_url" : [ String ]
      }
    ],
    "brand" : String,
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

| Code | Description               |
| ---- | ------------------------- |
| 200  | Test finished             |
| 301  | Test was canceled by user |
| 401  | Invalid App ID            |
| 402  | Invalid API Key           |
| 403  | Invalid IMEI              |
| 403  | Invalid IMEI              |
| 406  | A server error occurred.  |
| 407  | Invalid Serial Number     |

<br/><br/>
