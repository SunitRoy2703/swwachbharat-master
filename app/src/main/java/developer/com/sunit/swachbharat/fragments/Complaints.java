package developer.com.sunit.swachbharat.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.ZeeLoader;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


import developer.com.sunit.swachbharat.R;
import developer.com.sunit.swachbharat.ShowComplain;
import developer.com.sunit.swachbharat.models.ComplainPojo;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class Complaints extends Fragment {

     Picasso picasso;
    DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    FirebaseRecyclerOptions<ComplainPojo> options;
    private FirebaseRecyclerAdapter<ComplainPojo, ComplainHolder> fireBaseAdapter;
    ZeeLoader progressBar=null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("complains");
        mDatabase.keepSynced(true);
        }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_complaints, parent, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView=new RecyclerView(getActivity());
        mRecyclerView=rootView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(layoutManager);
        addProgressBar(getActivity());
        mRecyclerView.setItemViewCacheSize(15);
        mRecyclerView.setDrawingCacheEnabled(true);
        picasso = new Picasso.Builder(getContext())
                .memoryCache(new LruCache(24000))
                .build();


        Query query = mDatabase
                .limitToLast(50);
        options = new FirebaseRecyclerOptions.Builder<ComplainPojo>()
                .setQuery(query, ComplainPojo.class)
                .setLifecycleOwner(this)
                .build();


        fireBaseAdapter = new FirebaseRecyclerAdapter<ComplainPojo, ComplainHolder>(options) {

            @Override
            protected void onBindViewHolder
                    (@NonNull ComplainHolder viewHolder, int position,@NonNull ComplainPojo model) {

                viewHolder.message.setText(model.getMessage());
                viewHolder.name.setText(model.getName());
                viewHolder.location.setText(model.getLocation());
                Picasso.get().load(model.getImageUrl()).fit().into(viewHolder.imageView);
                Picasso.get().load(model.getDpUrl()).fit().into(viewHolder.displayPic);
                viewHolder.time.setText(model.getTimeStamp());


            }

            @Override
            @NonNull
            public ComplainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.complain, parent, false);
                final ComplainHolder complainHolder=new ComplainHolder(view);
                complainHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = complainHolder.getAdapterPosition();
                        if (pos == RecyclerView.NO_POSITION) {
                            return;
                        }

                            ComplainPojo complainPojo = fireBaseAdapter.getItem(pos);
                            String message, name, location, imageview, time, dpUrl;
                            message = complainPojo.getMessage();
                            name = complainPojo.getName();
                            location = complainPojo.getLocation();
                            imageview = complainPojo.getImageUrl();
                            time = complainPojo.getTimeStamp();
                            dpUrl = complainPojo.getDpUrl();
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(getActivity(), ShowComplain.class);
                            bundle.putString("name",name);
                            bundle.putString("message",message);
                            bundle.putString("location",location);
                            bundle.putString("time",time);
                            bundle.putString("image",imageview);
                            bundle.putString("dpUrl",dpUrl);
                            intent.putExtras(bundle);
                            startActivity(intent);

                    }

                });
                return new ComplainHolder(view);
            }




        };
        fireBaseAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(fireBaseAdapter);
        mRecyclerView.setHasFixedSize(true);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            private DataSnapshot dataSnapshot;
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                this.dataSnapshot = dataSnapshot;

                if (dataSnapshot.exists()){
                    Log.v(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());
                  hideProgressBar();
                } else {

                    showProgressBar();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

              hideProgressBar();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                } else {
                    // Scrolling down
                    ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public static class ComplainHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public TextView message,name,location,time;
        public ImageView imageView,displayPic;

        public ComplainHolder(View itemView)
        {
            super(itemView);
            view=itemView;

            this.message =  view.findViewById(R.id.message);
            this.name =  view.findViewById(R.id.name);
            this.location =view.findViewById(R.id.place);
            this.imageView=view.findViewById(R.id.post);
            this.time=view.findViewById(R.id.time);
            this.displayPic=view.findViewById(R.id.userImage);




        }



    }

    @Override
    public void onStart() {
        super.onStart();
        fireBaseAdapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(mRecyclerView==null) {
            mRecyclerView.setAdapter(fireBaseAdapter);
        }
        fireBaseAdapter.startListening();
    }

    public void addProgressBar(Activity activity) {
        ViewGroup rootFrameLayout = (ViewGroup) activity.getWindow().peekDecorView();
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressModal = inflater.inflate(R.layout.progress_spinner, rootFrameLayout, false);
        rootFrameLayout.addView(progressModal, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootFrameLayout.invalidate();
        progressBar = progressModal.findViewById(R.id.ProgressBar);
    }

    public void hideProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    public void showProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }



}
