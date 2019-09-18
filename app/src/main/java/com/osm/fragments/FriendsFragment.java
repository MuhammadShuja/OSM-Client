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
import com.osm.R;
import com.osm.activities.AddFriendActivity;
import com.osm.activities.ChatActivity;
import com.osm.adapters.FriendsAdapter;
import com.osm.API.OSM;
import com.osm.models.RequestModel;
import com.osm.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ListView lvFriends;
    private TextView tvNotificationCount;
    private int notificationCount = 0;
    private FriendsAdapter adapter;
    private List<UserModel> friends;

    private Fragment fragment = null;
    private String fragmentTitle = null;
    private Class fragmentClass = null;

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        lvFriends = (ListView) view.findViewById(R.id.lvFriends);

        adapter = new FriendsAdapter(getContext(), R.layout.layout_friends_row, friends = new ArrayList<>());

        lvFriends.setAdapter(adapter);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OSM.activeChatName = ((TextView) view.findViewById(R.id.tvFriendName)).getText().toString();
                OSM.activeChatUsername = friends.get(i).getUsername();
                getContext().startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        OSM.getInstance(getContext()).getFriends(new APIResponse.FriendListListener() {
            @Override
            public void onSuccess(List<UserModel> friendsResponse) {
                adapter = new FriendsAdapter(getContext(), R.layout.layout_friends_row, friends = friendsResponse);
                lvFriends.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        });

        OSM.getInstance(getContext()).getRequests(new APIResponse.FriendRequestListListener() {

            @Override
            public void onSuccess(List<RequestModel> requests) {
                notificationCount = OSM.requestsCount;
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
        inflater.inflate(R.menu.friends, menu);
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
            fragmentClass = RequestsFragment.class;
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
