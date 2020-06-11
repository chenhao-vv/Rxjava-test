package com.vivo.chmusicdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vivo.chmusicdemo.utils.login.User;
import com.vivo.chmusicdemo.utils.login.UserService;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.view.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    private EditText mAccount;
    private EditText mPassword;
    private EditText mPhoneNumber;
    private EditText mCode;
    private Button mGetCode;
    private Button mRegister;

    private String mStrPhoneNumber;
    private String mCodeNumber;
    private boolean mFlag = true; //用于判断获取验证码成功与否

    private static final int TIME = 60;
    private static final int PHONENUMBER_LENGTH = 11;
    private static final int CODE_NUMBER = 4;


    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(R.layout.login_register);
        init();
        SMSSDK.registerEventHandler(mEventHandler);
    }

    private EventHandler mEventHandler = new EventHandler(){
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }
    };

    //handler子线程分发message
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "已进入注册子线程获取验证码");
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (boolean)data;
                    if(smart) {
                       Toast.makeText(getApplicationContext(), R.string.phone_has_register, Toast.LENGTH_SHORT).show();
                       mPhoneNumber.requestFocus();
                       return;
                    }
                }
            }
            if(result == SMSSDK.RESULT_COMPLETE) {
                if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Log.d(TAG, "验证码已发送至手机");
                    Toast.makeText(getApplicationContext(), R.string.phone_has_register, Toast.LENGTH_SHORT).show();
                }
            } else {
                if(mFlag) {
                    mGetCode.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.phone_code_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.phone_code_error, Toast.LENGTH_SHORT).show();
                }
            }
        }

    };



    void init() {
        mAccount = (EditText)findViewById(R.id.edittext_regis_account);
        mPassword = (EditText)findViewById(R.id.edittext_regis_password);
        mPhoneNumber = (EditText) findViewById(R.id.phone_text);
        mCode = (EditText) findViewById(R.id.code);
        mGetCode = (Button)findViewById(R.id.getcode);
        mGetCode.setOnClickListener(this);
        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getcode:
                Log.d(TAG, "发送验证码");
                if(judgePhone()) {
                    SMSSDK.getVerificationCode("86", mStrPhoneNumber);
                    mCode.requestFocus();
                }
                break;
            case R.id.register:
                if(judgeCode()) {
                    SMSSDK.submitVerificationCode("86", mStrPhoneNumber, mCodeNumber);
                    mFlag = false;
                    String account = mAccount.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();
                    UserService userService = new UserService(this);
                    if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                        Toast.makeText(this, R.string.input_phone_password, Toast.LENGTH_SHORT).show();
                    } else if(mFlag){
                        User user = new User();
                        user.setmAccount(account);
                        user.setmPassword(password);
                        userService.register(user);
                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        try{
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                } else {
                    return;
                }
                break;
            default:
                break;
        }
    }

    //判断输入的手机号是否符合标准
    private boolean judgePhone() {
        //TextUtils.isEmpty()能判断editText是否为null以及""。
        if(TextUtils.isEmpty(mPhoneNumber.getText().toString().trim())) {
            Toast.makeText(this, R.string.phonenumber, Toast.LENGTH_SHORT).show();
            mPhoneNumber.requestFocus();
            return false;
        } else if(mPhoneNumber.getText().toString().trim().length() != PHONENUMBER_LENGTH) {
            Toast.makeText(this, R.string.phone_number_numberfailed, Toast.LENGTH_SHORT).show();
            mPhoneNumber.requestFocus();
            return false;
        } else {
            mStrPhoneNumber = mPhoneNumber.getText().toString().trim();
            String standardNumber = "[1][358]\\d{9}";//手机号码  通配符
            if(mStrPhoneNumber.matches(standardNumber)) {
                return true;
            } else {
                Toast.makeText(this, R.string.phone_number_error, Toast.LENGTH_SHORT).show();
                mPhoneNumber.requestFocus();
                return false;
            }
        }
    }

    //判断输入的验证码是否正确
    private boolean judgeCode() {
        if(judgePhone()) {
            if(TextUtils.isEmpty(mCode.getText().toString().trim())) {
                Toast.makeText(this, R.string.code, Toast.LENGTH_SHORT).show();
                mCode.requestFocus();
                return false;
            } else if (mCode.getText().toString().trim().length() != CODE_NUMBER) {
                Toast.makeText(this, R.string.phone_code_error, Toast.LENGTH_SHORT).show();
                mCode.requestFocus();
                return false;
            } else {
                mCodeNumber = mCode.getText().toString().trim();
                return true;
            }
        } else {
            Log.d(TAG, "手机号不正确");
            return false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }
}
