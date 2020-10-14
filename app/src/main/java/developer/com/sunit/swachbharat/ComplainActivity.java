package developer.com.sunit.swachbharat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.com.sunit.swachbharat.models.ComplainPojo;

public class ComplainActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.complain)
    EditText message;
    @BindView(R.id.location)
    TextView location;

    int PLACE_PICKER_REQUEST = 3;
    static final int REQUEST_TAKE_PHOTO = 1;
    private DatabaseReference mDataReference;
    private StorageReference imageReference;
    private UploadTask uploadTask;
    private Uri uri = null;
    @BindView(R.id.upload)
    FloatingActionButton fab;
    String  complainText, name,dpUrl,locationText,userUid;
    ProgressDialog progressDialog;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_complain_activity);
        ButterKnife.bind(this);
        mDataReference = FirebaseDatabase.getInstance().getReference("complains");
        imageReference = FirebaseStorage.getInstance().getReference().child("images");
        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy hh-mm");

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException ec) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }

        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        dpUrl=FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference ref = imageReference.child("complains");
        progressDialog = new ProgressDialog(ComplainActivity.this);
        progressDialog.setMessage("Please Wait, Uploading complaint.....");
        progressDialog.setCancelable(false);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date=Calendar.getInstance().getTime();
                final String timeStamp=dateFormat.format(date);
                complainText = message.getText().toString();
                if(complainText.length()>1)
                {
                    progressDialog.show();
                    uploadTask = ref.putFile(uri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();

                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                ComplainPojo complainModel = new ComplainPojo(userUid,complainText, name, locationText, downloadUri.toString(),dpUrl, timeStamp);
                                mDataReference.push().setValue(complainModel);
                                progressDialog.hide();
                                Toast.makeText(ComplainActivity.this, "Complaint Uploaded", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                }
                else
                {
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
                clickPhoto();

            }

        }
        if (requestCode==REQUEST_TAKE_PHOTO &&resultCode == RESULT_OK)
        {
                File path = new File(getFilesDir(), "swachbharat/garbage/");
                if (!path.exists()) path.mkdirs();
                File imageFile = new File(path, "image.jpg");
                uri=Uri.fromFile(imageFile);
                imageView.setImageURI(uri);


        }

    }

    private void clickPhoto()
    {
        File path = new File(this.getFilesDir(), "swachbharat/garbage/");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, "image.jpg");
        Uri imageUri = FileProvider.getUriForFile
                (this,"developer.com.sunit.swachbharat.provider", image);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }




}
