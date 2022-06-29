package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.databinding.ActivitySplashBinding;

public class SplashActivity extends FragmentActivity {

    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_STORAGE = 2;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        SplashViewModel mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        mViewModel.getRemaining().setValue(2000);
        mViewModel.getRemaining().observe(this, integer -> {
            binding.sampleText.setText(String.valueOf(integer));
            if (integer <= 0) {
                goNext();
            }
        });
        setContentView(binding.getRoot());
        if (verifyStoragePermission()) {
            new CountDownTimer(1000, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    mViewModel.getRemaining().postValue((int) millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    mViewModel.getRemaining().postValue(0);
                }
            }.start();
        }
    }

    private boolean verifyStoragePermission() {
        //1.检测权限
        int permission = PermissionChecker.PERMISSION_GRANTED;
        for (String s : PERMISSIONS_STORAGE) {
            permission |= ActivityCompat.checkSelfPermission(this, s);
        }
        if (permission != PermissionChecker.PERMISSION_GRANTED) {
            //2.没有权限，弹出对话框申请
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CODE_STORAGE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            int result = PermissionChecker.PERMISSION_GRANTED;
            for (int grantResult : grantResults) {
                result |= grantResult;
            }
            if (result == PermissionChecker.PERMISSION_GRANTED) {
                //权限申请成功
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                goNext();
            } else {
                //权限申请失败
                Toast.makeText(this, "未获取到所有授权，退出。", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //前往下一页
    private void goNext() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(new Intent(this, PlayActivity.class));
        finish();
    }
}
