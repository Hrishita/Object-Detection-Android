package org.tensorflow.demo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import morxander.zaman.ZamanTextView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserData> list;
    Context activity;
    public static String URL = "https://fcm.googleapis.com/fcm/send";
    public RequestQueue mRequestQue;

    public UserAdapter(Context context, List<UserData> list) {
        activity = context;
        this.list = list;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mRequestQue = Volley.newRequestQueue(parent.getContext());
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        UserData user = list.get(position);
        holder.msgName.setText(user.msgUserName);
        holder.msgDetails.setText(user.msgBody);
        holder.timeStamp.setTimeStamp(user.timestamp);
        holder.btnlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(user.link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });
        holder.btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject mainObj = new JSONObject();
                try {

                    mainObj.put("to", "/topics/"+user.subID);
                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title","any title");
                    notificationObj.put("body", "any body");

                    JSONObject extraData = new JSONObject();
                    extraData.put("sub", "vol");
                    extraData.put("brandID", "puma");
                    extraData.put("category","shoes");

                    mainObj.put("notification", notificationObj);
                    mainObj.put("data",extraData);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                            mainObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> header = new HashMap<>();
                            header.put("content-type","application/json");
                            header.put("authorization","key=AIzaSyDcjZz_-JrVxswr0TsQ6_3F3S2e85QiSVE");
                            return header;
                        }
                    };

                    mRequestQue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView msgName, msgDetails;
        ZamanTextView timeStamp;
        Button btnHelp, btnlink;

//        Typeface semiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-SemiBold.ttf");
//        Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");

        public UserViewHolder(View itemView) {
            super(itemView);

            btnlink = itemView.findViewById(R.id.btnlink);
            msgName =  (TextView) itemView.findViewById(R.id.msg_userName);
            msgDetails = (TextView) itemView.findViewById(R.id.msg_body);
            timeStamp = (ZamanTextView)itemView.findViewById(R.id.timeStamp);
            btnHelp = (Button)itemView.findViewById(R.id.btnHelp);

//            notificationName.setTypeface(semiBold);
//            notificationDetails.setTypeface(regular);
//            timeStamp.setTypeface(regular);
        }
    }
    public void dismissRequests(int position){
        list.remove(position);
        this.notifyItemRemoved(position);
    }
}
