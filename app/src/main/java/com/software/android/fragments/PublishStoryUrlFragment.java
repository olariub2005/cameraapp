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

public class PublishStoryUrlFragment extends BaseFragment {

    private final static String EXAMPLE = "Publish story (object url)";

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
                        .setHostedUrl("http://romkuapps.com/github/simple-facebook/object-apple.html")
                        .setNoun("food")
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
