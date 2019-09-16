package com.software.android.cameraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.software.android.fragments.MainFragment;
import com.software.android.utils.Utils;

public class PublishToFacebook extends FragmentActivity {
    protected static final String TAG = PublishToFacebook.class.getName();

    private SimpleFacebook mSimpleFacebook;
    private Button browseFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleFacebook = SimpleFacebook.getInstance(this);

        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
        Utils.printHashKey(getApplicationContext());

        setContentView(R.layout.content_publish_to_facebook);
        addFragment();

        browseFiles = (Button) findViewById(R.id.BrowseFiles);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new MainFragment());
        fragmentTransaction.commit();
    }

    public void browseFiles(View view ){
        Intent intent = new Intent(this, BrowseFiles.class);
        startActivity(intent);
    }
}
