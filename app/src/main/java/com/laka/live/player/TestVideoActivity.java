package com.laka.live.player;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.jzvd.Jzvd;

import com.laka.live.R;
import com.laka.live.dialog.ChooseDialog;

/**
 * @Author:summer
 * @Date:2018/11/30
 * @Description:
 */
public class TestVideoActivity extends AppCompatActivity {

    private Jzvd mJzvdStd;
    private ChooseDialog mChooseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video);

//        Jzvd.SAVE_PROGRESS = false;
//        mJzvdStd = (Jzvd) findViewById(R.id.jz_video);
//        String videoUrl = "http://jzvd.nathen.cn/6ea7357bc3fa4658b29b7933ba575008/fbbba953374248eb913cb1408dc61d85-5287d2089db37e62345123a1be272f8b.mp4";
//        JZDataSource jzDataSource = new JZDataSource(videoUrl, "");
//        jzDataSource.looping = true; //循环播放
//        mJzvdStd.setUp(jzDataSource, Jzvd.SCREEN_WINDOW_LIST);

//        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mChooseDialog != null && !mChooseDialog.isShowing()) {
//                    mChooseDialog.show();
//                }
//            }
//        });

        mChooseDialog = new ChooseDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_commit:

                        break;
                    case R.id.dialog_cancel:

                        break;
                }
                mChooseDialog.dismiss();
            }
        });

        mChooseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // finish();
            }
        });


    }
}
