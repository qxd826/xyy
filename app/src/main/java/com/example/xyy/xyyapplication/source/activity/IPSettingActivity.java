package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class IPSettingActivity extends Activity implements View.OnClickListener {
    private final String TAG = "IPSettingActivity";

    @Bind(R.id.ip_setting_back)
    ImageButton ipSettingBack;
    @Bind(R.id.ip_setting_edit)
    EditText ipSettingEdit;
    @Bind(R.id.ip_setting_button)
    Button ipSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("IPSettingActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_setting);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        ipSettingBack.setOnClickListener(this);
        ipSettingButton.setOnClickListener(this);
        ipSettingEdit.setText(MApplication.IP_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ip_setting_back:
                finish();
                break;
            case R.id.ip_setting_button:
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(MApplication.SHARE_PREFERENCE, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(MApplication.SHARE_PREFERENCE_IP_KEY, ipSettingEdit.getText().toString()).commit();
                    MApplication.IP_SERVICE = ipSettingEdit.getText().toString();
                } catch (Exception e) {
                    DebugLog.e("qxd", e.toString());
                }
                finish();
                break;
        }
    }
}
