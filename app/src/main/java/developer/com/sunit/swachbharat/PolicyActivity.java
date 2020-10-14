package developer.com.sunit.swachbharat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PolicyActivity extends AppCompatActivity {

    private WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = new WebView(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.freeprivacypolicy.com/privacy/view/983d34777be4bf9bce44f9a009d0ec79");
        this.setContentView(mWebView);
    }
}
