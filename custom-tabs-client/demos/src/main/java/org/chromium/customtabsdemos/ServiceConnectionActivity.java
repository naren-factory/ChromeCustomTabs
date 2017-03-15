// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.chromium.customtabsdemos;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.chromium.customtabsdemos.customTabs.CustomTabsHelper;

/**
 * This Activity connect to the Chrome Custom Tabs Service on startup.
 */
public class ServiceConnectionActivity extends AppCompatActivity
        implements View.OnClickListener, CustomTabsHelper.ConnectionCallback {
    private EditText mUrlEditText;
    private CustomTabsHelper mCustomTabsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceconnection);

        mCustomTabsHelper = new CustomTabsHelper();
        mCustomTabsHelper.setConnectionCallback(this);

        mUrlEditText = (EditText) findViewById(R.id.url);

        findViewById(R.id.start_custom_tab).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomTabsHelper.setConnectionCallback(null);
    }

    @Override
    public void onCustomTabsConnected() {
        Toast.makeText(this, "onCustomTabsConnected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCustomTabsDisconnected() {
        Toast.makeText(this, "onCustomTabsDisconnected", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabsHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabsHelper.unbindCustomTabsService(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Uri uri  = Uri.parse(mUrlEditText.getText().toString());
        switch (viewId) {
            case R.id.start_custom_tab:
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder(mCustomTabsHelper.getSession());
                intentBuilder.setCloseButtonIcon(
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
                CustomTabsHelper.openCustomTab(
                        this, intentBuilder.build(), getGoogleUri(), new WebviewFallback());
                break;
            default:
                //Unkown View Clicked
        }
    }

    private Uri getGoogleUri() {
        Uri.Builder b = Uri.parse("https://accounts.google.com/o/oauth2/auth").buildUpon();
        b.appendQueryParameter("response_type", "code");
        b.appendQueryParameter("client_id", "674719736750-4ajp80j0afbjibtr0k40ad0uds4aamo3.apps.googleusercontent.com");
        b.appendQueryParameter("redirect_uri", "com.googleusercontent.apps.674719736750-4ajp80j0afbjibtr0k40ad0uds4aamo3:/org.chromium.customtabsdemos");
        b.appendQueryParameter("scope", "https://mail.google.com https://www.googleapis.com/auth/userinfo.email");
        b.appendQueryParameter("state", "state");
        return b.build();
    }
}
