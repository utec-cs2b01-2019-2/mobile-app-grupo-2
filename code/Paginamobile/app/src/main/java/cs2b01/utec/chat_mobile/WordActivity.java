package cs2b01.utec.chat_mobile;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class WordActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String username = getIntent().getExtras().getString("username");
        setTitle("@"+username);
        mRecyclerView = findViewById(R.id.main_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMessages();
    }

    public Activity getActivity(){
        return this;
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void getMessages(){
        double random = Math.floor(Math.random() * (8-1)+1 );
        final int userFromId = getIntent().getExtras().getInt("user_from_id");

        String uri = "http://10.0.2.2:5000/words/"+random;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                uri,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        //TODO process response
                        mAdapter = new MessageAdapter(response, getActivity(), userFromId);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

    public void onClickBtnSend(View view) {
        final int userFromId = getIntent().getExtras().getInt("user_from_id");

        EditText txtMessage = (EditText) findViewById(R.id.txtMessage);
        String Message = txtMessage.getText().toString();

        Map<String, String> message = new HashMap<>();
        message.put("response", txtMessage.toString());
        message.put("userFromId", Integer.toString(userFromId));


        JSONObject jsonMessage = new JSONObject(message);


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:80/gabriel/word",
                jsonMessage,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO Qué hacer cuando el server responda
                        showMessage("Send");
                        try {
                            String message = response.getString("message");
                            String response2 = response.getString("reponse");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO Qué hacer cuando ocurra un error
                        showMessage("fail to send");
                    }
                }
        );


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);



    }

}
