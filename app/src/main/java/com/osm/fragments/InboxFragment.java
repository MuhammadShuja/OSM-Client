package com.osm.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.activities.AddFriendActivity;
import com.osm.activities.ChatActivity;
import com.osm.adapters.FriendsAdapter;
import com.osm.adapters.InboxAdapter;
import com.osm.models.MergerRequestModel;
import com.osm.models.ThreadModel;
import com.osm.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView lvThreads;
    private TextView tvNotificationCount;
    private int notificationCount = 0;

    private ArrayList<ThreadModel> threads;
    private InboxAdapter adapter;

    private Fragment fragment = null;
    private String fragmentTitle = null;
    private Class fragmentClass = null;

    private OnFragmentInteractionListener mListener;

    public InboxFragment() {
        // Required empty public constructor
    }
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        threads = new ArrayList<>();
        lvThreads = (ListView) view.findViewById(R.id.lvThreads);
        adapter = new InboxAdapter(getContext(), R.layout.layout_home_row, threads);
        lvThreads.setAdapter(adapter);
        lvThreads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OSM.activeChatName = threads.get(i).getName();
                OSM.activeChatUsername = threads.get(i).getUsername();
                getContext().startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        OSM.getInstance(getContext()).getMergerIndex(new APIResponse.MergerIndexListener() {
            @Override
            public void onSuccess(List<ThreadModel> requests) {
                adapter = new InboxAdapter(getContext(), R.layout.layout_home_row, threads = OSM.threads);
                lvThreads.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        });

        OSM.getInstance(getContext()).getMergerRequests(new APIResponse.MergerRequestListListener() {

            @Override
            public void onSuccess(List<MergerRequestModel> requests) {
                notificationCount = OSM.mergerRequestsCount;
                setupBadge();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);
        final MenuItem menuItem = menu.findItem(R.id.menuHomeNotification);

        View actionView = menuItem.getActionView();
        tvNotificationCount = (TextView) actionView.findViewById(R.id.notificationsBadge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuHomeNotification) {
            fragmentClass = MergerRequestsFragment.class;
            loadFragment();
            return true;
        }
        else if (id == R.id.menuAddFriend) {
            startActivity(new Intent(getActivity(), AddFriendActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(){
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setupBadge() {

        if (tvNotificationCount != null) {
            if (notificationCount == 0) {
                if (tvNotificationCount.getVisibility() != View.GONE) {
                    tvNotificationCount.setVisibility(View.GONE);
                }
            } else {
                tvNotificationCount.setText(String.valueOf(Math.min(notificationCount, 99)));
                if (tvNotificationCount.getVisibility() != View.VISIBLE) {
                    tvNotificationCount.setVisibility(View.VISIBLE);
                }
            }
        }
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
