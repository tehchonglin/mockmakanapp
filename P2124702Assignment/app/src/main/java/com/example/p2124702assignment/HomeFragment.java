package com.example.p2124702assignment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RestAPI.RestAPIListener{

    //Add RecyclerView member
    private RecyclerView recyclerView;
    private PictureHelper helper = null;
    private PictureListAdapter adapter = null;
    private String postID = null;

    private ArrayList<String> Title, Captions, Location;
    private ArrayList<Double> lat, lon;
    private ArrayList<byte[]> images;
    private ArrayList<Integer> id;

    private RecyclerView hawkerRecycler;
    private HawkerAdapter hawkerAdapter = null;
    private ArrayList<String> hawkerName, hawkerLocation, hawkerStatus, hawkerImage;
    private ArrayList<Integer> hawkerId, hawkerStallAmount;
    private JDBCHelper jdbcHelper = null;
    private ArrayList<Data> dataList = null;
    private RestAPI restAPI;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
        restAPI = new RestAPI();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        helper = new PictureHelper(getContext());
        Title = new ArrayList<>();
        Captions = new ArrayList<>();
        Location = new ArrayList<>();
        images = new ArrayList<>();
        id = new ArrayList<>();
        lat = new ArrayList<>();
        lon = new ArrayList<>();
        adapter = new PictureListAdapter(getContext(),Title,Captions,Location,images, id, lat, lon);
        recyclerView.setAdapter(adapter);

        hawkerRecycler = view.findViewById(R.id.recyclerHawker);
        jdbcHelper = new JDBCHelper();
        hawkerId = new ArrayList<>();
        hawkerImage = new ArrayList<>();
        hawkerLocation = new ArrayList<>();
        hawkerName = new ArrayList<>();
        hawkerStallAmount = new ArrayList<>();
        hawkerStatus = new ArrayList<>();
        hawkerAdapter = new HawkerAdapter(getContext(), hawkerId, hawkerName, hawkerStallAmount, hawkerLocation, hawkerStatus, hawkerImage);
        hawkerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        hawkerRecycler.setAdapter(hawkerAdapter);

        displaydata();

        return view;
    }

    private void displaydata() {
        Cursor cursor = helper.getAll();
        if(cursor.getCount()==0){
        }
        else
        {
            while(cursor.moveToNext()){
                id.add(cursor.getInt(0));
                Title.add(cursor.getString(1));
                Captions.add(cursor.getString(2));
                images.add(cursor.getBlob(3));
                lat.add(cursor.getDouble(4));
                lon.add(cursor.getDouble(5));
                Location.add(String.valueOf(cursor.getDouble(4)+" "+cursor.getDouble(5)));
            }
        }
    }

    private void displayHawker(List<Data> data) {
//        dataList = restAPI.getData();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try  {
//                    hawkerId = jdbcHelper.getID();
//                    hawkerImage = jdbcHelper.getImage();
//                    hawkerLocation = jdbcHelper.getLocation();
//                    hawkerName = jdbcHelper.getName();
//                    hawkerStallAmount = jdbcHelper.getStall();
//                    hawkerStatus = jdbcHelper.getStatus();
//                    // Your code goes here
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        for(Data d: data ){
            hawkerId.add(d.getId());
            hawkerImage.add(d.getImage());
            hawkerLocation.add(d.getLocation());
            hawkerName.add(d.getName());
            hawkerStallAmount.add(d.getStallAmount());
            hawkerStatus.add(d.getStatus());
        }

//        thread.start();
        if (hawkerId==null) {
            Toast.makeText(getContext(),"not connected", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onSuccess(List<Data> data) {
        displayHawker(data);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onFailure(String message) {

    }

//    private void clear(){
//        title.setText("");
//        captions.setText("");
//        postImage.setImageResource(0);
//        location.setText("");
//    }
//
//    private void load(){
//        Cursor c = helper.getById(postID);
//        c.moveToFirst();
//
//        title.setText(helper.getTitle(c));
//        captions.setText(helper.getCaptions(c));
//
//        postImage.setImageBitmap(getImage(helper.getImage(c)));
//
//        double latitude = helper.getLatitude(c);
//        double longitude = helper.getLongitude(c);
//        String locationText = "Lat: " + latitude + " Long:" + longitude;
//        location.setText(locationText);
//    }
}

