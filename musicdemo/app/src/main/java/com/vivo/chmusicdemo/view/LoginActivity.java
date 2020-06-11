package com.vivo.chmusicdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vivo.chmusicdemo.activity.LoginUI;
import com.vivo.chmusicdemo.activity.MainActivity;
import com.vivo.chmusicdemo.activity.RegisterActivity;
import com.vivo.chmusicdemo.utils.login.UserService;
import com.vivo.chmusicdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG ="LoginActivity";

    private Button mLogin;
    private Button mRegister;
    private TextView mAccount;
    private TextView mPassword;
    private CheckBox mRemPassword;
    private CheckBox mAutoLogin;

    //private SharedPreferences mPref;
    //private SharedPreferences.Editor mEditor;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(LoginActivity.this, LoginUI.class);
            startActivity(intent);
            Log.d(TAG, "sleep for 3 seconds");
        }

    };

    private UserService mUserService = null;

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(R.layout.loginui_login);

        init();


        /*使用SharedPreference实现
        //判断是否自动登录
        boolean autoLogin = mPref.getBoolean("autoLogin", false);
        //判断是否记住密码
        boolean isRemembered = mPref.getBoolean("rememberPassword", false);
        if(isRemembered) {
            String account = mPref.getString("account", "");
            String password = mPref.getString("password", "");
            if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password))
            {
                mAccount.setText(account);
                mPassword.setText(password);
                mRemPassword.setChecked(true);
                Toast.makeText(this, "显示已记住密码", Toast.LENGTH_SHORT).show();
            }
        }
        if(autoLogin) {
            mAutoLogin.setChecked(true);
            SleepThread sleep = new SleepThread();
            new Thread(sleep).start();
        }
        Log.d(TAG, "oncreate()");
        */
    }

    public void init() {
        mAccount = (TextView)findViewById(R.id.login_edit_user);
        mPassword = (TextView)findViewById(R.id.login_edit_password);
        mRemPassword = (CheckBox)findViewById(R.id.remember_password);
        mRemPassword.setOnCheckedChangeListener(this);
        mAutoLogin = (CheckBox)findViewById(R.id.auto_login);
        mAutoLogin.setOnCheckedChangeListener(this);
        mLogin = (Button) findViewById(R.id.login_login);
        mLogin.setOnClickListener(this);
        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        //mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mUserService = new UserService(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login:
                String account = mAccount.getText().toString();
                String password = mPassword.getText().toString();
                boolean flag = mUserService.exiUser(account, password);
                if(flag) {
                    Log.d(TAG, "登录成功");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "登录失败");
                }
                break;

                /*
                if(TextUtils.equals(account, "hao") && TextUtils.equals(password, "123456")) {
                    mEditor = mPref.edit();
                    if(mRemPassword.isChecked()) {
                        mEditor.putBoolean("rememberPassword",true);
                        mEditor.putString("account", account);
                        mEditor.putString("password", password);
                        mEditor.apply();
                        Log.d(TAG, "save the account information");
                    } else {
                        mEditor.putBoolean("rememberPassword",false);
                        mEditor.apply();
                    }
                    if(mAutoLogin.isChecked()) {
                        mEditor.putBoolean("autoLogin",true);
                        mEditor.apply();
                        Log.d(TAG, "pick the autoLogin");
                    } else {
                        mEditor.putBoolean("autoLogin",false);
                        mEditor.apply();
                    }
                    Intent intent = new Intent(this, LoginUI.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                    mAccount.setText("");
                    mPassword.setText("");
                }
                break;
                */
            case R.id.register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                Log.d(TAG, "进行注册");
            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton check, boolean isChecked) {
        switch (check.getId()) {

            case R.id.remember_password:
                if(isChecked) {
                    mRemPassword.setChecked(true);
                    Log.d(TAG,"选中记住密码");
                } else {
                    mRemPassword.setChecked(false);
                    Log.d(TAG,"取消记住密码");
                }
                break;
            case R.id.auto_login:
                if(isChecked) {
                    mAutoLogin.setChecked(true);
                    Log.d(TAG,"选中自动登录");
                } else {
                    mAutoLogin.setChecked(false);
                    Log.d(TAG,"取消自动登录");
                }
                break;
            default:
                break;
        }
    }
    
}
