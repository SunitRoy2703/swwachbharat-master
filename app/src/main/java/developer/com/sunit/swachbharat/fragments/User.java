package developer.com.sunit.swachbharat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import developer.com.sunit.swachbharat.LoginActivity;
import developer.com.sunit.swachbharat.PolicyActivity;
import developer.com.sunit.swachbharat.R;

public class User extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final View rootView=inflater.inflate(R.layout.fragment_user, parent, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        TextView userName;
        ImageView displayPic;
        Button logout,policy;
        userName=rootView.findViewById(R.id.name);
        logout=rootView.findViewById(R.id.logout);
        displayPic=rootView.findViewById(R.id.display_pic);
          policy=rootView.findViewById(R.id.policy);
        if(user!=null)
        {

            if(user.getEmail()!=null)
            {
                userName.setText(user.getDisplayName());
                Picasso.get().load(user.getPhotoUrl()).fit().into(displayPic);
            }
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(),"GoodBye!!",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PolicyActivity.class));
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
