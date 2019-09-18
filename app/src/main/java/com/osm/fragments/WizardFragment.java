package com.osm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.R;
import com.osm.activities.AuthActivity;
import com.osm.activities.HomeActivity;
import com.osm.API.OSM;


public class WizardFragment extends Fragment {
    private static final String POSITION = "position";
    private int WIZARD_PAGE_POSITION;

    private EditText inputLoginUser, inputLoginPassword, inputRegisterUsername, inputRegisterName, inputRegisterPassword, inputRegisterConfirmPassword;
    private RadioGroup radioGroupSex;
    private RadioButton radioSex;
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    public WizardFragment() {
        // Required empty public constructor
    }

    public static WizardFragment newInstance(int position) {
        WizardFragment fragment = new WizardFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            WIZARD_PAGE_POSITION = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.form_wizard_start, container, false);

        switch (WIZARD_PAGE_POSITION){
            case 0:
                rootView = inflater.inflate(R.layout.form_wizard_start, container, false);
                rootView.findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthActivity.viewPager.setCurrentItem(1, true);
                    }
                });

                break;

            /*--------------------------
             |   Login
             -------------------------*/
            case 1:
                rootView = inflater.inflate(R.layout.form_wizard_login, container, false);
                inputLoginUser = (EditText) rootView.findViewById(R.id.inputLoginUser);
                inputLoginPassword = (EditText) rootView.findViewById(R.id.inputLoginPassword);

                rootView.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = inputLoginUser.getText().toString();
                        String password = inputLoginPassword.getText().toString();
                        if(username.isEmpty() || password.isEmpty()
                                || username.equals(null) || password.equals(null)
                                || username.equals("") || password.equals("")){
                            Toast.makeText(getContext(), "Please provide valid username and password.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            OSM.getInstance(getContext()).login(username, password, new APIResponse.AuthListener() {
                                @Override
                                public void onSuccess(String response) {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

                rootView.findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthActivity.viewPager.setCurrentItem(2, true);
                    }
                });
                break;

            /*--------------------------
             |   Register Account
             -------------------------*/
            case 2:
                rootView = inflater.inflate(R.layout.form_wizard_register, container, false);

                inputRegisterUsername = (EditText) rootView.findViewById(R.id.inputRegisterUsername);
                inputRegisterName = (EditText) rootView.findViewById(R.id.inputRegisterName);
                radioGroupSex = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
                inputRegisterPassword = (EditText) rootView.findViewById(R.id.inputRegisterPassword);
                inputRegisterConfirmPassword = (EditText) rootView.findViewById(R.id.inputRegisterConfirmPassword);

                final View finalRootView = rootView;
                rootView.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = inputRegisterUsername.getText().toString();
                        String name = inputRegisterName.getText().toString();
                        String password = inputRegisterPassword.getText().toString();
                        String confirmPass = inputRegisterConfirmPassword.getText().toString();
                        int selectedId = radioGroupSex.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        radioSex = (RadioButton) finalRootView.findViewById(selectedId);

                        if(username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()
                                || username.equals(null) || password.equals(null) || confirmPass.equals(null)
                                || username.equals("") || password.equals("") || confirmPass.equals("")){
                            Toast.makeText(getContext(), "Please provide valid username and password", Toast.LENGTH_SHORT).show();
                        }
                        else if(!password.equals(confirmPass)){
                            Toast.makeText(getContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mProgressDialog = ProgressDialog.show(getContext(), "",
                                    "Creating a new account, please wait...", true);

                            OSM.getInstance(getContext()).register(username, password, name, (String) radioSex.getText(), new APIResponse.AuthListener() {
                                @Override
                                public void onSuccess(String response) {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            mProgressDialog.dismiss();
                        }

                    }
                });


                rootView.findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthActivity.viewPager.setCurrentItem(1, true);
                    }
                });
                break;
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
