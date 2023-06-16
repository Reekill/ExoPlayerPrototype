package stanleyd.exoplayerprototype;

import static com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy.DEFAULT_MIN_LOADABLE_RETRY_COUNT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    EditText url;
    Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText)findViewById(R.id.url);
        play = (Button) findViewById(R.id.play);

        playerView = findViewById(R.id.playerView);

        player = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(new DefaultTrackSelector(this))
                .setLoadControl(new DefaultLoadControl.Builder().build())
                .build();

        playerView.setPlayer(player);

        DefaultHttpDataSource.Factory httpDataSourceFactory =
                new DefaultHttpDataSource.Factory()
                        .setAllowCrossProtocolRedirects(true);

        DefaultHlsDataSourceFactory hlsDataSourceFactory =
                new DefaultHlsDataSourceFactory(httpDataSourceFactory);

        HlsMediaSource.Factory mediaSourceFactory =
                new HlsMediaSource.Factory(hlsDataSourceFactory)
                        .setLoadErrorHandlingPolicy(
                                new DefaultLoadErrorHandlingPolicy(
                                        DefaultLoadErrorHandlingPolicy.DEFAULT_MIN_LOADABLE_RETRY_COUNT
                                )
                        );




        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = url.getText().toString().trim();
                if (!TextUtils.isEmpty(videoUrl)) {
                    HlsMediaSource mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(videoUrl));
                    player.setMediaSource(mediaSource);
                    player.prepare();
                    player.setPlayWhenReady(true);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid video URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        player = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(new DefaultTrackSelector(this))
                .setLoadControl(new DefaultLoadControl.Builder().build())
                .build();

        playerView.setPlayer(player);

        DefaultHttpDataSource.Factory httpDataSourceFactory =
                new DefaultHttpDataSource.Factory()
                        .setAllowCrossProtocolRedirects(true);

        DefaultHlsDataSourceFactory hlsDataSourceFactory =
                new DefaultHlsDataSourceFactory(httpDataSourceFactory);

        HlsMediaSource.Factory mediaSourceFactory =
                new HlsMediaSource.Factory(hlsDataSourceFactory)
                        .setLoadErrorHandlingPolicy(
                                new DefaultLoadErrorHandlingPolicy(
                                        DefaultLoadErrorHandlingPolicy.DEFAULT_MIN_LOADABLE_RETRY_COUNT
                                )
                        );

        String videoUrl = "http://qkefcowx.russtv.net/iptv/2T7G3ZAV6CY88K/127/index.m3u8";

        HlsMediaSource mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(videoUrl));

        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
    }*/

    @Override
    protected void onStop() {
        super.onStop();

        player.setPlayWhenReady(false);
        player.release();
    }
}