package com.laka.live.ui.room;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.manager.RoomManager;

/**
 * Created by zwl on 2016/6/15.
 * Email-1501448275@qq.com
 */
public class ResponseCodeHelper {
    public static void showTips(Context context, int responseCode) {
        String message = "";
        switch (responseCode) {
            case RoomManager.TLV_E_USER_FORBIDDEN:
                message = context.getString(R.string.live_manager_already_be_forbidden);
                break;
            case RoomManager.TLV_E_USER_NOT_IN_THIS_ROOM:
                message = context.getString(R.string.live_manager_user_not_in_room);
                break;
            case RoomManager.TLV_E_USER_IS_ADMIN:
                message = context.getString(R.string.live_manager_already_be_admin);
                break;
            case RoomManager.TLV_E_USER_NOT_ADMIN:
                message = context.getString(R.string.live_manager_not_be_admin);
                break;
            case RoomManager.TLV_E_TOO_MANY_ADMIN:
                message = context.getString(R.string.live_manager_admin_is_too_much);
                break;
            case RoomManager.TLV_E_USER_NOT_IN_ROOM_FORBID_SAY:
                message = context.getString(R.string.live_manager_user_not_be_forbidden);
                break;
            default:
//                message = "未知错误" + responseCode;
                break;
        }
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
