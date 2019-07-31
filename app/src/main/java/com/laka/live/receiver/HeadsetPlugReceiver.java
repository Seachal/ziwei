package com.laka.live.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.laka.live.ui.widget.Toast;
import com.laka.live.util.Log;

/**
 * Created by crazyguan on 2016/4/22.
 */
public class HeadsetPlugReceiver extends BroadcastReceiver {
    private static final String TAG = "RoomHeadsetPlugReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("state")){
            if (intent.getIntExtra("state", 0) == 0){
//                Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show();
                Log.d(TAG , "headset not connected");
            }
            else if (intent.getIntExtra("state", 0) == 1){
//                Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show();
                Log.d(TAG , "headset connected");
            }
        }
    }
}
