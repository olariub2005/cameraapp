package com.software.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.software.android.cameraapp.SimpleFacebook;
import com.software.android.entities.Privacy;
import com.software.android.entities.Privacy.PrivacySettings;
import com.software.android.entities.Video;
import com.software.android.cameraapp.R;
import com.software.android.utils.Utils;
import com.software.android.listeners.OnPublishListener;

public class PublishVideoFragment extends BaseFragment {

    private final static String EXAMPLE = "Publish video";

    private TextView mResult;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(EXAMPLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);
        mResult = (TextView) view.findViewById(R.id.result);
        view.findViewById(R.id.load_more).setVisibility(View.GONE);
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setText(EXAMPLE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final byte[] videoBytes = Utils.getSampleVideo(getActivity().getApplicationContext(), "sample_video.mp4");

                // set privacy
                Privacy privacy = new Privacy.Builder().setPrivacySettings(PrivacySettings.SELF).build();

                // create Photo instance and add some properties
                Video video = new Video.Builder()
                        .setVideo("Sample video", videoBytes)
                        .setTitle("Video generated with CrazySpeedRecorder")
                        .setPrivacy(privacy)
                        .build();

                SimpleFacebook.getInstance().publish(videoBytes, video, new OnPublishListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        hideDialog();
                        mResult.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        hideDialog();
                        mResult.setText(reason);
                    }

                    @Override
                    public void onThinking() {
                        showDialog();
                    }

                    @Override
                    public void onComplete(String response) {
                        hideDialog();
                        mResult.setText(response);
                    }
                });

            }
        });
        return view;
    }

}
