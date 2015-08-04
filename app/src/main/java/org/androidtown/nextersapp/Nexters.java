package org.androidtown.nextersapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



public class Nexters extends AppCompatActivity {

    private WebView mWebView;
    private final long	FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nexters);





        mWebView=(WebView)findViewById(R.id.webView);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://nh.maden.kr/admin/Main.html");





        mWebView.setWebViewClient(new WebViewClientClass());





    }
    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            moveTaskToBack(true);

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"'뒤로'버튼을한번더누르시면종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }









    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){

            if(url.equals("http://nh.maden.kr/admin/index.html") || url.equals("http://nh.maden.kr/admin/")){
                CookieManager.getInstance().removeAllCookie();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
            else{
                view.loadUrl(url);
            }




            return true;
        }


    }

   /* @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if((keyCode==KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    } */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nexters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
