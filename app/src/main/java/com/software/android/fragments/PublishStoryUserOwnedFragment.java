package com.software.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.software.android.cameraapp.SimpleFacebook;
import com.software.android.entities.Story;
import com.software.android.entities.Story.StoryAction;
import com.software.android.entities.Story.StoryObject;
import com.software.android.cameraapp.R;
import com.software.android.listeners.OnPublishListener;

public class PublishStoryUserOwnedFragment extends BaseFragment {

    private final static String EXAMPLE = "Publish story (user-owned)";

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

                // set object to be shared
                StoryObject storyObject = new StoryObject.Builder()
                        .setDescription("The apple is the pomaceous fruit of the apple tree, Malus domestica of the rose family. It is one of the most widely cultivated tree fruits.")
                        .setImage("http://en.huashengfruit.com/v15/Upload/2012971553522493.png")
                        .setNoun("food")
                        .setTitle("Apple")
                        .setUrl("https://github.com/sromku/android-simple-facebook")
                        .addProperty("calories", "52")
                        .build();

                // set action to be done
                StoryAction storyAction = new StoryAction.Builder()
                        .setAction("eat")
                        .addProperty("taste", "sweet")
                        .build();

                // build story
                Story story = new Story.Builder()
                        .setObject(storyObject)
                        .setAction(storyAction)
                        .build();

                // publish story
                SimpleFacebook.getInstance().publish(story, new OnPublishListener() {

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
