package com.example.findfriends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MySMSReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
         Toast.makeText(context,"new SMS",Toast.LENGTH_LONG).show();
        String messageBody, phoneNumber;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();

                    Toast.makeText(context,
                                    "Message : " + messageBody + "Reçu de la part de;" + phoneNumber,
                                    Toast.LENGTH_LONG)
                            .show();

                    if (messageBody.contains("FindFriends: envoyer votre positon plz")) {
//                        Lancer le service
                        Intent i=new Intent(context,MyLocationService.class);
                        i.putExtra("numero",phoneNumber);
                        context.startActivity(i);
                    }
                    if (messageBody.contains("#FindFriends: ma position est ")){
                        String[] t=messageBody.split("#");
                        String Longitude=t[1];
                        String Altitude=t[2];

                        //elli 9renah fel class yet7att linna haka elli fel pdf chapitre 3
                        // instance du gestionnaire des notifications de l'appareil
                        NotificationCompat.Builder mynotif = new NotificationCompat.Builder(
                                context,
                                "findfriends_channel");
                        mynotif.setContentTitle("FindFriends!");
                        mynotif.setContentText("ma position est");
                        mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                        mynotif.setAutoCancel(true);

                        mynotif.setVibrate(new long[]{500, 1000, 200, 2000});
                        //action pour la notification
                        Intent map=new Intent(context,MapsActivity.class);
                        map.putExtra("longitude",Longitude);
                        map.putExtra("altitude",Altitude);

                        PendingIntent pi=PendingIntent.getActivity(context,1,map,PendingIntent.FLAG_MUTABLE);
                        mynotif.setContentIntent(pi);
                        // il faut ajouter la permission VIBRATE dans le manifest
                        Uri son = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        mynotif.setSound(son);
                        // instance du gestionnaire des notifications de l'appareil
                        NotificationManagerCompat manager =
                                NotificationManagerCompat.from(context);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            /* creation du canal si la version android de l'appareil est supérieur à
                            Oreo */
                            NotificationChannel canal = new
                                    NotificationChannel("findfriends_channel",
                                    // l'ID exacte du canal
                                    "canal pour lapplication find me",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            AudioAttributes attr = new AudioAttributes.Builder()

                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .build();
                            // ajouter du son pour le canal
                            canal.setSound(son, attr);
                            // creation du canal dans l'appareil
                            manager.createNotificationChannel(canal);
                        }
                        // lancement de la notification
                        manager.notify(0, mynotif.build());

                    }
                }
            }

        }
    }
}