package developer.com.sunit.swachbharat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.com.sunit.swachbharat.models.EventPOJO;

public class EventActivity extends AppCompatActivity {
    @BindView(R.id.complain)
    EditText message;
    @BindView(R.id.location)
    TextView location;
    private DatabaseReference mDataReference;
    int PLACE_PICKER_REQUEST = 3;
    @BindView(R.id.upload)
    FloatingActionButton fab;
    String  userText, name,dpUrl,locationText,timeStamp;
    ProgressDialog progressDialog;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        mDataReference = FirebaseDatabase.getInstance().getReference("events");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException ec) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }

        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        dpUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        progressDialog = new ProgressDialog(EventActivity.this);
        progressDialog.setMessage("Please Wait, Adding Event.....");
        progressDialog.setCancelable(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = Calendar.getInstance().getTime();
                 timeStamp = dateFormat.format(date);
                userText = message.getText().toString();
                if (userText.length() > 1) {

                    progressDialog.show();
                    EventPOJO model=new EventPOJO(name,dpUrl,locationText,userText,timeStamp);
                    mDataReference.push().setValue(model);
                    progressDialog.hide();
                    Toast.makeText(EventActivity.this, "Event Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    message.setError("Invalid !");

                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                location.setText(place.getAddress());
                locationText = place.getAddress().toString();

            }

        }
    }
}
