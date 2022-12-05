package com.example.findfriends;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationService extends Service {
    String numerotxt=null;
    public MyLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        numerotxt=intent.getStringExtra("numero");
//        preparer les position gps:
        FusedLocationProviderClient mClient= LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    sendSMS(location);
                }

            }
        });
        //hathi kif chtet7arik ya5ou el position kol 10 min par exapl
//        LocationRequest.Builder builder=new LocationRequest.Builder(100);//pour la version Api
//        LocationRequest request=LocationRequest.create().setSmallestDisplacement(10).setFastestInterval(10000);
//        mClient.requestLocationUpdates(request, new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                Location location=locationResult.getLastLocation();
//                if(location!=null){}
//            }
//        }, null);


        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSMS(Location location) {

        SmsManager manager=SmsManager.getDefault();
        manager.sendTextMessage(numerotxt,
                null,
                "FindFriends: ma position est #"+location.getLongitude()+"#"+location.getAltitude(),
                null,
                null );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}