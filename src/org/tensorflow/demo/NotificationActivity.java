package org.tensorflow.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
//    String notificationlastUid;
//    private List<UserData> result;
//    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

//        TextView categoryTv = findViewById(R.id.category);
//        TextView brandTv = findViewById(R.id.brand);
//
//        if (getIntent().hasExtra("sub")){
//            String category = getIntent().getStringExtra("sub");
//            String brand = getIntent().getStringExtra("brandID");
//            categoryTv.setText(category);
//            brandTv.setText(brand);
//        }


//        mRef.keepSynced(true);
//
//        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
//        result = new ArrayList<>();
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        lm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(lm);
//
//        adapter = new UserAdapter(result);
//
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        getNotification();
    }

//    private void getNotification(){
//
////        Query notificationRef = mRef.child("notifications");
////
////        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
////        notificationlastUid = sharedPreferences.getString("lastUid","");
////
////        if (notificationlastUid.isEmpty()) {
////            notificationRef = notificationRef.orderByKey().limitToLast(1);
////        }else {
////            notificationRef = notificationRef.orderByKey().startAt(notificationlastUid);
////        }
//        mRef.child("notifications").orderByKey().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                String uName = dataSnapshot.child("username").getValue(String.class);
//                final Integer newState = dataSnapshot.child("newState").getValue(Integer.class);
//                Long timeStamp = dataSnapshot.child("timestamp").getValue(Long.class);
//                String body = dataSnapshot.child("msg").getValue(String.class);
//                String subID = dataSnapshot.child("subID").getValue(String.class);
//
//                result.add(0,new UserData(uName,body,timeStamp,newState, subID));
//                adapter.notifyDataSetChanged();
//
//              //  pushID = dataSnapshot.getKey();
//
////                if (notificationlastUid.isEmpty()){
////                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
////                    SharedPreferences.Editor editor = sharedPreferences.edit();
////                    editor.putString("lastUid", pushID);
////                    editor.apply();
////                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


}
