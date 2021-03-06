package com.gionee.note.ai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.gionee.aminote.R;
import com.gionee.note.app.view.StandardActivity;
import com.gionee.note.app.view.StandardActivity.StandardAListener;
import com.gionee.note.common.StatisticsModule;
import com.gionee.note.web.NoteWebView;
import com.gionee.note.web.OnlineImageDownloadHandler;

public class WebPageActivity extends StandardActivity implements StandardAListener {
    public static final String DATA_TITLE = "data_title";
    public static final String DATA_URL = "data_url";
    private OnlineImageDownloadHandler mOnlineImageDownloadHandler;
    private NoteWebView mWebView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getExtras().getString(DATA_TITLE);
        String url = intent.getExtras().getString(DATA_URL);
        setTitle(title);
        setStandardAListener(this);
        setNoteContentView(R.layout.web_page_activity_content_ly);
        this.mWebView = (NoteWebView) findViewById(R.id.web_show_view);
        this.mOnlineImageDownloadHandler = new OnlineImageDownloadHandler(this);
        this.mOnlineImageDownloadHandler.register();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.web_load_progress);
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(0);
                view.getSettings().setBlockNetworkImage(true);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(8);
                view.getSettings().setBlockNetworkImage(false);
            }
        });
        this.mWebView.loadUrl(url);
    }

    protected void onResume() {
        super.onResume();
        StatisticsModule.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        StatisticsModule.onPause(this);
    }

    protected void onDestroy() {
        this.mOnlineImageDownloadHandler.destroy();
        super.onDestroy();
    }

    public void onClickHomeBack() {
        finish();
    }

    public void onClickRightView() {
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        if (key != 4 || !this.mWebView.canGoBack()) {
            return super.onKeyDown(key, event);
        }
        this.mWebView.goBack();
        return true;
    }
}
