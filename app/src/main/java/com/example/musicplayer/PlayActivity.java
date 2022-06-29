package com.example.musicplayer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayer.database.MediaDataUtils;
import com.example.musicplayer.databinding.ActivityPlayerBinding;
import com.example.musicplayer.ui.adpter.MediaDescriptionAdapter;

import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PlayActivity.class.getSimpleName();
    private ActivityPlayerBinding binding;
    private PlayViewModel model;
    private MediaDescriptionAdapter mAdapter;
    private Handler handler;
    private List<MediaDescriptionCompat> arrayMusic;

    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            Log.d(TAG, "====~ onConnected");
            model.onConnected(PlayActivity.this, getHandler());

            model.setQueue(arrayMusic);
        }

        @Override
        public void onConnectionFailed() {
            Log.d(TAG, "====~ onConnectionFailed");
            model.onConnectionFailed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initModel();
        doConnect();
        initData();
    }

    private void initData() {
        arrayMusic = MediaDataUtils.getMediaDescriptionFromSysDb(PlayActivity.this);
        mAdapter.submitList(arrayMusic);
    }

    private void doConnect() {
        model.connect2MediaService(this, connectionCallback);
    }

    private void initModel() {
        this.model = new ViewModelProvider(this).get(PlayViewModel.class);
    }

    private void initView() {
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.root);
        mAdapter = new MediaDescriptionAdapter(App.getInstance().sIOExecutor, position -> {
            model.skipToQueueItem(position);
        });
        binding.listMedia.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(new ColorDrawable(Color.DKGRAY));
        binding.listMedia.addItemDecoration(divider);

        binding.btnPlay.setOnClickListener(this);
        binding.btnPrevious.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
    }

    private Handler getHandler() {
        if (handler == null) {
            HandlerThread thread = new HandlerThread(getClass().getCanonicalName());
            thread.start();
            handler = new Handler(thread.getLooper());
        }
        return handler;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                Toast.makeText(this, "点击下一首", Toast.LENGTH_SHORT).show();
                model.skipToNext();
                break;
            case R.id.btnPrevious:
                Toast.makeText(this, "点击上一首", Toast.LENGTH_SHORT).show();
                model.skipToPrevious();
                break;
            case R.id.btnPlay:
                Toast.makeText(this, "点击播放", Toast.LENGTH_SHORT).show();
                model.play();
                break;
            default:
                if (v.getTag() != null && v.getTag() instanceof Integer) {
                    int clickIndex = (int) v.getTag();

                }
        }
    }
}