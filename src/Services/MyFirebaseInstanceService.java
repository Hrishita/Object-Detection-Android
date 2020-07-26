package Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.tensorflow.demo.Homescreen;
import org.tensorflow.demo.Homevol;
import org.tensorflow.demo.R;
import org.tensorflow.demo.Splashmn;

import java.util.Map;

public class MyFirebaseInstanceService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Map<String,String> extraData = remoteMessage.getData();
        String sub = extraData.get("sub");
        Homevol.subscriber = sub;
        String brandID = extraData.get("brandID");
        String category = extraData.get("category");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "TAC")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.weseelg);


//        if (category.equals("shoes")) {
//            intent = new Intent(this, NotificationActivity.class);
//            intent.putExtra("brandID", brandID);
//            intent.putExtra("category", category);
//        }
////        else{
////
////        }

        Intent intent;
        // = new Intent(this, NotificationActivity.class)
        if (Splashmn.ifVol){
            intent = new Intent(this, Homevol.class);
        }else {
            intent = new Intent(this, Homescreen.class);
        }

        intent.putExtra("sub", sub);
        intent.putExtra("brandID", brandID);
        intent.putExtra("category", category);

//      Intent intent = new Intent(this, NotificationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("TAC", "demo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id, notificationBuilder.build());
    }
}
