package developer.com.sunit.swachbharat.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import developer.com.sunit.swachbharat.ComplainActivity;
import developer.com.sunit.swachbharat.EventActivity;
import developer.com.sunit.swachbharat.R;

public class New extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_new, parent, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        CardView createComplain=rootView.findViewById(R.id.register);
        CardView createEvent=rootView.findViewById(R.id.event);
        createComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(getContext(), ComplainActivity.class));

                }else{
                    ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CAMERA}, 0);
                }




            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),EventActivity.class));
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
}
