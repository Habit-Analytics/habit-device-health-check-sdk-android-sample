package com.habit.devicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import io.habit.android.devicehealth.DeviceHealth;
import io.habit.android.devicehealth.DeviceInfoCallback;
import io.habit.android.devicehealth.TestCallback;
import io.habit.android.devicehealth.global.TestType;

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

        // Perform Tests
        DeviceHealth.setLanguage(this, "pt");
        DeviceHealth.setThemeColor(this, Color.argb(255, 156, 34, 93));

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

        DeviceHealth.performTests(this, this,
                "",
                "",
                "",
                testsToPerform, new TestCallback() {

                    @Override
                    public void onResponse(JSONObject obj) {

                        if (obj != null) {
                            tvDeviceInfo.setText(formatString(obj.toString()));
                        }
                    }
                });
    }

    private void showDeviceInfo() {

        // Get DeviceInfo
        DeviceHealth.getDeviceInfo(this, this, new DeviceInfoCallback() {

            @Override
            public void onResponse(JSONObject obj) {

                if (obj != null) {
                    tvDeviceInfo.setText(formatString(obj.toString()));
                }
            }
        });
    }

    // Parse JSON Object to String
    public String formatString(String text){

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