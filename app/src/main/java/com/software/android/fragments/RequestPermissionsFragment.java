package com.software.android.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.software.android.cameraapp.Permission;
import com.software.android.cameraapp.SimpleFacebook;
import com.software.android.cameraapp.R;
import com.software.android.listeners.OnNewPermissionsListener;
import com.software.android.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestPermissionsFragment extends BaseFragment {

    private final static String EXAMPLE = "Request new permissions";

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
                showPermissionsSelectDialog();
            }
        });
        return view;
    }

    private void showPermissionsSelectDialog() {
        final List<Permission> newPermissions = new ArrayList<Permission>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select new permissions").setMultiChoiceItems(getAllPermissions(), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Permission permission = Permission.values()[which];
                if (isChecked && !newPermissions.contains(permission)) {
                    newPermissions.add(permission);
                } else if (!isChecked && newPermissions.contains(permission)) {
                    newPermissions.remove(permission);
                }
            }
        }).setPositiveButton("Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Permission[] permissionsArray = newPermissions.toArray(new Permission[newPermissions.size()]);
                SimpleFacebook.getInstance().requestNewPermissions(permissionsArray, new OnNewPermissionsListener() {

                    @Override
                    public void onFail(String reason) {
                        mResult.setText(reason);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        mResult.setText(throwable.getMessage());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSuccess(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                        showGrantedPermissions();
                        if (declinedPermissions != null && declinedPermissions.size() > 0) {
                            Toast.makeText(getActivity(), "User declined few permissions: " + declinedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        builder.create().show();
    }

    private void showGrantedPermissions() {
        String res = "Granted permissions<br>";
        Set<String> grantedPermissions = SimpleFacebook.getInstance().getGrantedPermissions();
        res += Utils.join(grantedPermissions.iterator(), "<br>", new Utils.Process<String>() {
            @Override
            public String process(String permission) {
                return "\u25CF " + permission + " \u25CF";
            }
        });
        res += "<br>";
        mResult.setText(Html.fromHtml(res));
    }

    private CharSequence[] getAllPermissions() {
        Permission[] permissions = Permission.values();
        CharSequence[] charSequences = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            charSequences[i] = permissions[i].getValue();
        }
        return charSequences;
    }
}