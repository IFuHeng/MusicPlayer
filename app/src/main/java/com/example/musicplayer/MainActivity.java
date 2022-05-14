package com.example.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayer.database.MediaDataUtils;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.entity.MusicBean;
import com.example.musicplayer.jni.NativeObject;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.musicplayer.ui.MusicService;
import com.example.musicplayer.ui.vhbean.MusicViewHolderBean;
import com.example.mylibrary.recycler.CustomRecyclerViewAdapter;
import com.example.mylibrary.recycler.OnItemClickListener;
import com.example.mylibrary.recycler.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , ServiceConnection
        , OnItemClickListener<MusicViewHolderBean>
        , OnItemLongClickListener<MusicViewHolderBean> {

    private ActivityMainBinding binding;
    private MainViewModel model;
    private List<MusicViewHolderBean> mMusicList = new ArrayList<>();
    private MusicService.PlayerStateBind musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.root);

        TextView tv = binding.sampleText;
        tv.setText(new NativeObject().stringFromJNI());

        initView();
        initModel();
    }

    private void initModel() {
        this.model = new ViewModelProvider(this).get(MainViewModel.class);

        this.model.getArrMusic().observe(this, musicBeans -> {

            //判断减少
            HashSet<MusicViewHolderBean> setDeletes = new HashSet<>();
            for (MusicViewHolderBean musicViewHolderBean : mMusicList) {
                MusicBean musicBean = musicViewHolderBean.getMusicBean();
                if (musicBeans.contains(musicBean)) {
                    continue;
                }
                setDeletes.add(musicViewHolderBean);
            }
            for (MusicViewHolderBean delBean : setDeletes) {
                int index = mMusicList.indexOf(delBean);
                mMusicList.remove(index);
                binding.listMedia.getAdapter().notifyItemRemoved(index);
            }

            //判断新增
            HashSet<MusicViewHolderBean> setAdds = new HashSet<>();
            for (MusicBean musicBean : musicBeans) {
                for (MusicViewHolderBean musicViewHolderBean : mMusicList) {
                    if (musicViewHolderBean.getMusicBean().equals(musicBean)) {
                        break;
                    }
                }
                setAdds.add(new MusicViewHolderBean(musicBean));
            }
            for (MusicViewHolderBean addBean : setAdds) {
                mMusicList.add(addBean);
                binding.listMedia.getAdapter().notifyItemInserted(musicBeans.size() - 1);
            }

            binding.sampleText.setText("音乐（" + mMusicList.size() + ")");
        });
    }

    private void initView() {
        binding.listMedia.setAdapter(new CustomRecyclerViewAdapter(new GeneralViewHolderFactory(this, this), mMusicList, this, this));
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(new ColorDrawable(Color.DKGRAY));
        binding.listMedia.addItemDecoration(divider);

//        binding.btnNext.setOnClickListener(this);
//        binding.btnPrevious.setOnClickListener(this);
//        binding.btnPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.getArrMusic().postValue(MediaDataUtils.getMusicsFromSysDb(this));
        bindService(new Intent(this, MusicService.class), this, BIND_NOT_FOREGROUND);
    }


    @Override
    protected void onPause() {
        unbindService(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnNext:
//                Toast.makeText(this, "点击下一首", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnPrevious:
//                Toast.makeText(this, "点击上一首", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnPlay:
//                Toast.makeText(this, "点击播放", Toast.LENGTH_SHORT).show();
//                break;
            default:
                if (v.getTag() != null && v.getTag() instanceof Integer) {
                    int clickIndex = (int) v.getTag();

                }
        }
    }

    @Override
    public void onItemClick(View view, int position, MusicViewHolderBean bean) {
        Toast.makeText(this, "点击" + bean.getMusicBean().getTitle(), Toast.LENGTH_SHORT).show();
        if (musicService == null) {
            bindService(new Intent(this, MusicService.class), this, BIND_AUTO_CREATE);
            Toast.makeText(this, "服务尚未启动,请重试", Toast.LENGTH_SHORT).show();
        } else {
            musicService.play(bean.getMusicBean().getUrl());
        }
    }

    @Override
    public void onItemLongClick(View view, int position, MusicViewHolderBean bean) {
        Toast.makeText(this, "长按点击" + bean.getMusicBean().getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        musicService = (MusicService.PlayerStateBind) service;
        Toast.makeText(this, "服务启动成功", Toast.LENGTH_SHORT).show();
        binding.playerView.setPlayer(musicService.getPlayer());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}