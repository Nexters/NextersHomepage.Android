package org.androidtown.nextersapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;

import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private CookieManager cookieManager;
    private String domain="http://nh.maden.kr";
    private HttpClient client = new DefaultHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        cookieManager = CookieManager.getInstance(); // 쿠키매니저 초기화

        String cookie=cookieManager.getCookie(domain);

        if(cookie!=null){
            Intent intent=new Intent(getApplicationContext(),Nexters.class);
            startActivity(intent);
        }






    }





    public void onClickTest(View v) throws IOException {



        new SendPost().execute();












    }

    private class SendPost extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... unused) {
            String content = executeClient();
            return content;
        }

        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        // 실제 전송하는 부분

        public String executeClient() {
            EditText editText = (EditText)findViewById(R.id.editText);
            EditText editText2 = (EditText)findViewById(R.id.editText2);
            String username=editText.getText().toString();
            String password=editText2.getText().toString();
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("username", username));
            post.add(new BasicNameValuePair("password", password));

            // 연결 HttpClient 객체 생성



            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // Post객체 생성
            HttpPost httpPost = new HttpPost(domain+"/login");

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                HttpResponse response=client.execute(httpPost);



                BufferedReader in = new BufferedReader(new InputStreamReader(response
                        .getEntity().getContent()));

                //SB to make a string out of the inputstream
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();

                JSONObject json = null;

                try {

                    json = new JSONObject(sb.toString());
                    if(json.get("result").toString().equals("success")){

                        JSONArray resData= (JSONArray) json.get("resData");

                        JSONObject resData1= (JSONObject) resData.get(0);

                        if(Integer.parseInt( resData1.get("userRoles").toString())<2){


                            List<Cookie> cookies = ((DefaultHttpClient)client).getCookieStore().getCookies();
                            String cookieString="";
                            if (!cookies.isEmpty()) {
                                for (int i = 0; i < cookies.size(); i++) {
                                    // cookie = cookies.get(i);
                                    cookieString = cookies.get(i).getName() + "=\""
                                            + cookies.get(i).getValue()+"\"";


                                    cookieManager.setCookie(domain, cookieString);





                                }
                            }
                            try {
                                Thread.sleep(500);


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(getApplicationContext(),Nexters.class);
                            startActivity(intent);
                        }


                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,"아이디와 비밀번호가 틀렸습니다",Toast.LENGTH_LONG).show();
                        Looper.loop();







                    }
                } catch (JSONException e) {
                    Log.d("errorrr", e.getMessage());
                }




                return EntityUtils.getContentCharSet(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
