package com.osm.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.activities.MergerResponseActivity;
import com.osm.adapters.MergerRequestAdapter;
import com.osm.adapters.RequestsAdapter;
import com.osm.models.MergerRequestModel;
import com.osm.models.RequestModel;

import java.util.ArrayList;
import java.util.List;


public class MergerRequestsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int REQUEST_ACCEPTED = 1;
    public static final int REQUEST_REJECTED = 0;

    public static int REQUEST = -1;

    private String mParam1;
    private String mParam2;

    private ListView lvRequests;
    private MergerRequestAdapter adapter;
    private List<MergerRequestModel> requests;

    private OnFragmentInteractionListener mListener;

    public MergerRequestsFragment() {
        // Required empty public constructor
    }
    public static MergerRequestsFragment newInstance(String param1, String param2) {
        MergerRequestsFragment fragment = new MergerRequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merger_requests, container, false);
        getActivity().setTitle("Merger Requests");

        lvRequests = (ListView) view.findViewById(R.id.lvRequests);
        adapter = new MergerRequestAdapter(getContext(), R.layout.layout_merger_request_row, requests = new ArrayList<>());
        lvRequests.setAdapter(adapter);
        loadRequests();

        setHasOptionsMenu(true);
        return view;
    }

    private void loadRequests(){

        OSM.getInstance(getContext()).getMergerRequests(new APIResponse.MergerRequestListListener() {
            @Override
            public void onSuccess(List<MergerRequestModel> requests) {
                adapter = new MergerRequestAdapter(getContext(), R.layout.layout_merger_request_row, requests);
                adapter.setOnUpdateListener(new MergerRequestAdapter.OnUpdateListener() {
                    @Override
                    public void onUpdate() {
                        if(REQUEST == REQUEST_ACCEPTED){
                            startActivity(new Intent(getActivity(), MergerResponseActivity.class));
                        }
                        else{
                            loadRequests();
                        }
                    }
                });
                lvRequests.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

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
