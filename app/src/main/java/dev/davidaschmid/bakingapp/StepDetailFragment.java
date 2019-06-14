package dev.davidaschmid.BakingApp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import dev.davidaschmid.BakingApp.model.RecipeModel;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String CHANNEL_ID = "notification_channel";
    private final String PLAYER_POSITION = "player_position";
    private final String PLAY_WHEN_READY = "play_when_ready";
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private int position;
    public static TextView mStepInstructionTV;
    String stepInstruction;
    private static SimpleExoPlayer mExoPlayer;
    private static SimpleExoPlayerView mPlayerView;
    private static Context context;
    public static StepDetailFragment context2;
    public static ImageView mErrorImage;
    private FrameLayout mExoPlayerFrame;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private String videoUrl;
    private int heightOrig, widthOrig;
    private long playerPos;
    private boolean playWhenReady2 = true;
    private Bundle savedInstanceStateGlobal;
    ViewGroup.LayoutParams params;
    public StepDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        context2 = this;
        if(savedInstanceState != null){
            playerPos = savedInstanceState.getLong(PLAYER_POSITION, 0L);
            playWhenReady2 = savedInstanceState.getBoolean(PLAY_WHEN_READY, true);
        }
        if(context.getClass() == IngredientsStepsActivity.class){
            IngredientsStepsActivity.mTwoPane = true;
        }else{
            IngredientsStepsActivity.mTwoPane = false;
        }

        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mStepInstructionTV = rootView.findViewById(R.id.recipe_instruction_tv);
        mErrorImage = rootView.findViewById(R.id.error_image);
        mExoPlayerFrame = rootView.findViewById(R.id.exoplayer_frame);
        params = mExoPlayerFrame.getLayoutParams();
        heightOrig = params.height;
        widthOrig = params.width;
        params.height = -1;
        params.width = -1;
        position = IngredientsStepsFragment.posInSteps;
        RecipeModel.Step step = IngredientsStepsActivity.mRecipeModel.getStepGivenIndex(position);
        stepInstruction = step.getDescription();
        mStepInstructionTV.setText(stepInstruction);
        videoUrl = step.getVideoURL();
        mPlayerView = rootView.findViewById(R.id.playerView);
        initializeMediaSession();
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == 2 && !IngredientsStepsActivity.mTwoPane) {//landscape mode
            mExoPlayerFrame.setLayoutParams(params);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            params.height = convertDpToPx(300);
            params.width = -1;
            mExoPlayerFrame.setLayoutParams(params);
        }
        if(!IngredientsStepsActivity.mTwoPane ||
            (IngredientsStepsActivity.mTwoPane && IngredientsStepsFragment.savedInstanceStateGlobal != null))
        {
            if (videoUrl.equals("")) {
                mErrorImage.setVisibility(View.VISIBLE);
                initializePlayer();
            } else {
                mErrorImage.setVisibility(View.INVISIBLE);
                initializePlayer();
                prepareExoPlayer(Uri.parse(videoUrl), playWhenReady2);
                if (savedInstanceState != null) restorePosition();

            }
        } else {
            mStepInstructionTV.setText("Click step to left to view Instructions");
            mErrorImage.setVisibility(View.INVISIBLE);
            initializePlayer();
        }

        return rootView;
    }
    public void restorePosition(){mExoPlayer.seekTo(playerPos);}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d(TAG, "onSaveInstanceState() called");

        if(!videoUrl.equals("")) {
            outState.putLong(PLAYER_POSITION, playerPos);
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady2);
            //outState = savedInstanceStateGlobal;
        }
    }

    int convertDpToPx(int dp){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;
        int px = (int)(dp*dpi/160.0);
        return px;
    }
    private void initializeMediaSession(){
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }
    private void showNotification(PlaybackStateCompat state){
        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID, "Primary", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = "Pause";
        }else {
            icon = R.drawable.exo_controls_play;
            play_pause = "Play";
        }
        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));
        NotificationCompat.Action restartAction = new NotificationCompat.Action(
                R.drawable.exo_controls_previous, "Restart",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                getContext(), 0, new Intent(getContext(), StepDetailFragment.class), 0);
        builder.setContentTitle("Content Title")
                .setContentText("Press play to watch video")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mMediaSession.getSessionToken())
                    .setShowActionsInCompactView(0, 1));
        mNotificationManager.notify(0, builder.build());
    }
    public static void initializePlayer(){
        if(mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(context2);

        }

    }
    public static void prepareExoPlayer(Uri mediaUri, boolean playWhenReady){
        String userAgent = Util.getUserAgent(context, "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                context, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(playWhenReady);
    }

    public static void releasePlayer(){
        //mNotificationManager.cancelAll();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    public static void playerCleanup(){
        releasePlayer();
        //mMediaSession.setActive(false);
    }
    public void onPause() {
        super.onPause();
        //Log.d(TAG, "onPause() called");

        if(!videoUrl.equals("") && mExoPlayer != null) {
            playerPos = mExoPlayer.getCurrentPosition();
            playWhenReady2 = mExoPlayer.getPlayWhenReady();
        }
        releasePlayer();
        mMediaSession.setActive(false);

        if(Util.SDK_INT <= 23) {
        //had to releasePlayer() outside of if
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        //Log.d(TAG, "onStop() called");

        if(Util.SDK_INT > 23){
            //app doesn't work when releasePlayer() here
            //video plays for 1 sec and stops?
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.d(TAG, "onDestroy() called");
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);

        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        int dummy = 0;

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
    public static class MediaReceiver extends BroadcastReceiver{
        public MediaReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }

    }

}
