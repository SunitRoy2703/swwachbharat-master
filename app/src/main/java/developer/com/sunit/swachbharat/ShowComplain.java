package developer.com.sunit.swachbharat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowComplain extends AppCompatActivity {
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.post)
    ImageView post;
    @BindView(R.id.place)
    TextView place;
    @BindView(R.id.userImage)
    ImageView userImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.message)
    TextView message;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcomplain);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        time.setText(bundle.getString("time"));
        name.setText(bundle.getString("name"));
        place.setText(bundle.getString("location"));
        message.setText(bundle.getString("message"));
        Picasso.get().load(bundle.getString("image")).fit().into(post);

        Picasso.get().load(bundle.getString("dpUrl")).fit().into(userImage);



    }
}
