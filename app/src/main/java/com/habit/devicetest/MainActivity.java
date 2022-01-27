package com.habit.devicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.habit.android.devicehealth.DeviceHealth;
import io.habit.android.devicehealth.DeviceInfoCallback;
import io.habit.android.devicehealth.TestCallback;
import io.habit.android.devicehealth.global.Globals;
import io.habit.android.devicehealth.global.ScreenType;

import io.habit.android.devicehealth.model.BorderType;
import io.habit.android.devicehealth.model.ButtonStyle;
import io.habit.android.devicehealth.model.CustomizableScreen;
import io.habit.android.devicehealth.model.Customization;
import io.habit.android.devicehealth.util.Utils;
import io.habit.android.devicehealth.lang.ScreenCustomizationKeys;

public class MainActivity extends AppCompatActivity {

    TextView tvDeviceInfo;
    Button btnGoTesting;
    Button btnDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDeviceInfo = findViewById(R.id.tv_device_info);
        btnGoTesting = findViewById(R.id.btn_go_testing);
        btnDeviceInfo = findViewById(R.id.btn_device_info);

        btnGoTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoTesting();
            }
        });

        btnDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeviceInfo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onClickGoTesting() {

        String appID = ""; // <-your appID here->
        String apiKey = ""; // <-your apiKey here->
        String imei = null; // <-IMEI here->
        String serialNumber = null; // <-device serial number here->

        // Perform Tests
        DeviceHealth.setLanguage(this, "en");
        DeviceHealth.setThemeColor(this, Color.argb(255, 3, 198, 252));
        DeviceHealth.hideStartScreen(false);

        String[] testsToPerform = new String[]{
                ScreenType.buttons_v2,
                ScreenType.charging_v2,
                ScreenType.multi_touch_v2,
                ScreenType.device_front_video_v2
        };

        Customization customization = new Customization();

        ButtonStyle buttonStyle = new ButtonStyle();
        buttonStyle.setBackgroundColor(Color.rgb(254, 242, 79));
        buttonStyle.setForegroundColor(Color.rgb(0, 0, 0));
        buttonStyle.setBorderType(BorderType.Rounded);
        customization.setButtonsStyle(buttonStyle);

        customization.setSkipTestButtonColor(Color.DKGRAY);
        customization.setSkipTestButtonText("<- your custom skip button text ->");
        customization.setNextButtonText("<- your custom next button text ->");
        customization.setProgressBarBackgroundColor(Color.rgb(0, 0, 0));
        customization.setProgressBarSelectedColor(Color.rgb(3, 198, 252));
        customization.setCustomNavigationBarBackgroundColor(Color.rgb(255, 255, 255));
        customization.setCustomNavigationBarTextColor(Color.rgb(0, 0, 0));
        customization.setCustomNavigationBarButtonsTextColor(Color.GRAY);

        Map<String, String> customStartCopy = new HashMap<>();
        customStartCopy.put(ScreenCustomizationKeys.start_screen.Copy.title, "My custom title");
        customStartCopy.put(ScreenCustomizationKeys.start_screen.Copy.description, "My custom description");

//        Map<String, Drawable> images = new HashMap<>();
//        images.put(ScreenCustomizationKeys.start_screen.Elements.image, context.getDrawable(R.drawable.your_custom_image));

        CustomizableScreen screen = new CustomizableScreen();
        screen.screenType = ScreenType.start_screen;
        screen.backgroundColor = Color.rgb(240, 240, 240);
        screen.textAccentColor = Color.rgb(255, 165, 0);
        screen.textColor = Color.GRAY;
//        screen.images = images;
        screen.copyStrings = customStartCopy;

        CustomizableScreen[] customScreens = new CustomizableScreen[]{
                screen
        };

        customization.setCustomScreens(customScreens);

        DeviceHealth.performTests(this, this,
                appID,
                apiKey,
                serialNumber,
                imei,
                testsToPerform, customization, new TestCallback() {

                    @Override
                    public void onResponse(JSONObject obj) {

                        if (obj != null) {

                            tvDeviceInfo.setText(formatString(obj.toString()));

                            try {
                                if (obj.optInt("status_code") == 200) {
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });
    }

    private void showDeviceInfo() {

        // Get DeviceInfo
        DeviceHealth.getDeviceInfo(this, this, null, null, new DeviceInfoCallback() {

            @Override
            public void onResponse(JSONObject obj) {

                if (obj != null) {
                    tvDeviceInfo.setText(formatString(obj.toString()));
                }
            }
        });
    }

    // Parse JSON Object to String
    public String formatString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':

                    if (i < text.length() - 1) {
                        char nextLetter = text.charAt(i + 1);
                        if (nextLetter == '"') {
                            json.append("\n" + indentString + letter + "\n");
                            indentString = indentString + "\t";
                            json.append(indentString);
                        } else {
                            json.append(letter);
                        }
                    } else
                        json.append(letter);
                    break;
                case '[':
                    json.append("\n" + indentString + letter);
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                    if (i < text.length() - 1) {
                        char nextLetter = text.charAt(i + 1);
                        if (nextLetter != '"' && nextLetter != '&') {
                            indentString = indentString.replaceFirst("\t", "");
                            json.append("\n" + indentString + letter);
                        } else {
                            json.append(letter);
                        }
                    } else {
                        indentString = indentString.replaceFirst("\t", "");
                        json.append("\n" + indentString + letter);
                    }
                    break;
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}