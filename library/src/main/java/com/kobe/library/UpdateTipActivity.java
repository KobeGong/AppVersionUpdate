package com.kobe.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kobe.library.entity.UpdateInfo;

public class UpdateTipActivity extends Activity {

    UpdateInfo mUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tip);

        mUpdateInfo = getIntent().getParcelableExtra("data");
        showNoticeDialog();

    }

    private void showNoticeDialog() {
        setFinishOnTouchOutside(false);
        if (mUpdateInfo.title.isEmpty())
            setTitle("软件版本更新");
        else
            setTitle(mUpdateInfo.title);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(mUpdateInfo.newMessage);

        TextView textView = (TextView) findViewById(R.id.download);
        if (isDownload()) {
            textView.setText("安装");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startUpdateService(AppUpdateService.PARAM_INSTALL_APK);
                    finish();
                }
            });
        } else {
            builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            textView.setText("下载");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUpdateService(AppUpdateService.PARAM_START_DOWNLOAD);
                    finish();
                }
            });
        }

        if (mUpdateInfo.required == 1 || mUpdateInfo.required == 0) {
            Button buttonCannel = (Button) findViewById(R.id.cancel);
            buttonCannel.setVisibility(View.VISIBLE);
            buttonCannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startUpdateService(AppUpdateService.PARAM_STOP_SELF);
                    finish();
                }
            });

            Button buttonJump = (Button) findViewById(R.id.jump);
            buttonJump.setVisibility(View.VISIBLE);
            buttonJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences share = getSharedPreferences("version", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putInt("jump", mUpdateInfo.newVersion);
                    editor.commit();
                    startUpdateService(AppUpdateService.PARAM_STOP_SELF);
                    finish();
                }
            });

        }
    }

    private void startUpdateService(int request) {
        Intent intent = new Intent(this, AppUpdateService.class);
        intent.putExtra("data", request);
        startService(intent);
    }

    private boolean isDownload() {
        return mUpdateInfo.apkFile().exists();
    }

}
