package developer.com.sunit.swachbharat.fragments;

import android.app.Activity;
import android.content.Context;
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
import developer.com.sunit.swachbharat.models.EventPOJO;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class Events extends Fragment {

    Picasso picasso;
    DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    FirebaseRecyclerOptions<EventPOJO> options;
    private FirebaseRecyclerAdapter<EventPOJO, EventHolder> fireBaseAdapter;
    ZeeLoader progressBar=null;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");
        mDatabase.keepSynced(true);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_events, parent, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView=new RecyclerView(getContext());
        mRecyclerView=rootView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(layoutManager);
        addProgressBar(getActivity());
        mRecyclerView.setItemViewCacheSize(15);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        picasso = new Picasso.Builder(getContext())
                .memoryCache(new LruCache(24000))
                .build();

        Query query = mDatabase
                .limitToLast(50);
        options = new FirebaseRecyclerOptions.Builder<EventPOJO>()
                .setQuery(query, EventPOJO.class)
                .setLifecycleOwner(this)
                .build();


        fireBaseAdapter = new FirebaseRecyclerAdapter<EventPOJO,EventHolder>(options) {

            @Override
            protected void onBindViewHolder
                    (@NonNull EventHolder viewHolder, int position, @NonNull EventPOJO model) {

                viewHolder.message.setText(model.getDescription());
                viewHolder.name.setText(model.getName());
                viewHolder.location.setText(model.getLocation());
                Picasso.get().load(model.getDpUrl()).fit().into(viewHolder.displayPic);
                viewHolder.time.setText(model.getTimeStamp());

            }

            @Override
            @NonNull
            public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event, parent, false);

                return new EventHolder(view);
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
                    hideProgressBar();
                    Log.v(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());

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

    public static class EventHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public TextView message,name,location,time;
        public ImageView displayPic;

        public EventHolder(View itemView)
        {
            super(itemView);
            view=itemView;

            this.message =  view.findViewById(R.id.message);
            this.name =  view.findViewById(R.id.name);
            this.location =view.findViewById(R.id.place);
            this.time=view.findViewById(R.id.time);
            this.displayPic=view.findViewById(R.id.userImage);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
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
