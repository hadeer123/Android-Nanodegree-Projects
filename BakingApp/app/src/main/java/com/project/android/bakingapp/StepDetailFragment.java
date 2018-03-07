package com.project.android.bakingapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.project.android.bakingapp.data.RecipeContract;
import com.squareup.picasso.Picasso;


public class StepDetailFragment extends Fragment implements SimpleExoPlayer.EventListener{

    private static final String TAG = StepDetailFragment.class.getName();
    public static final String ARG_ITEM_ID = "item_id";
    private static final String STATE_PLAYER_POSITION = "state_position";
    private Cursor mCursor;
    private TextView stepDescTxtView;
    private ImageView thumbNail;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;
    private long mPlayerPosition;
    private  String recipeID, stepID;

    private static String stepTitle, stepDesc, stepVideoUrl, stepImageUrl;

    public static final String[] STEP_DETAIL_PROJECTION = {
            RecipeContract.RecipeSteps.COLUMN_STEP_ID,
            RecipeContract.RecipeSteps.COLUMN_RECIPE_ID,
            RecipeContract.RecipeSteps.COLUMN_DESC,
            RecipeContract.RecipeSteps.COLUMN_SHORT_DESC,
            RecipeContract.RecipeSteps.COLUMN_VIDEO_URL,
            RecipeContract.RecipeSteps.COLUMN_THUMBNAIL_URL
    };

    public static final int INDEX_STEP_ID = 0;
    public static final int INDEX_RECIPE_ID = 1;
    public static final int INDEX_STEP_DESC = 2;
    public static final int INDEX_STEP_SHORT_DESC = 3;
    public static final int INDEX_STEP_VIDEO_URL = 4;
    public static final int INDEX_STEP_THUMBNAIL_URL = 5;


    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(STATE_PLAYER_POSITION);
        }
        if (getArguments().containsKey(ARG_ITEM_ID)) {

            queryData();
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =  activity.findViewById(R.id.toolbar_layout);

            if (appBarLayout != null && mCursor!= null) {
                appBarLayout.setTitle(stepTitle);
            }
        }
    }

    private void queryData(){
        try {
            recipeID = getArguments().getString(STEP_DETAIL_PROJECTION[INDEX_RECIPE_ID]);
            stepID = getArguments().getString(ARG_ITEM_ID);
            Uri uri = RecipeContract.RecipeSteps.CONTENT_URI.buildUpon().appendPath(recipeID).appendPath(stepID).build();

            mCursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if(mCursor != null) {
                mCursor.moveToFirst();
                stepTitle = mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_SHORT_DESC]));
                stepDesc = mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_DESC]));
                stepVideoUrl = mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_VIDEO_URL]));
                stepImageUrl = mCursor.getString(mCursor.getColumnIndex(STEP_DETAIL_PROJECTION[INDEX_STEP_THUMBNAIL_URL]));
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }

        if (mTrackSelector !=  null) {
            mTrackSelector = null;
        }
    }

    private void initializePlayer(Uri mediaUri){
        if(mExoPlayer == null){

            LoadControl loadControl = new DefaultLoadControl();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector, loadControl);

            // Attach player to the view
            mExoPlayerView.setPlayer(mExoPlayer);


            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);


            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);

        }

    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

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

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        mExoPlayerView =  rootView.findViewById(R.id.videoView);
        stepDescTxtView = rootView.findViewById(R.id.step_desc_txtView);
        thumbNail = rootView.findViewById(R.id.thumbnailImg);
        if(mCursor!= null){
            if (Integer.parseInt(stepID)> 0) {
                int index = stepDesc.indexOf(". ");
                stepDesc = stepDesc.substring(index + 2);
            }
            stepDescTxtView.setText(stepDesc);
        }

        handleStepMedia();

        return rootView;
    }

    private void handleStepMedia (){
        if(!stepVideoUrl.isEmpty()) {
            initializeMediaSession();
            initializePlayer(Uri.parse(stepVideoUrl));
            thumbNail.setVisibility(View.GONE);
        } else if(!stepImageUrl.isEmpty()) {
            Picasso.with(getContext()).load(stepImageUrl).into(thumbNail);
            mExoPlayerView.setVisibility(View.GONE);
        }


    }
    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_PLAYER_POSITION, mPlayerPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mPlayerPosition = mExoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            handleStepMedia();
        }
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
        String stateString;
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            case ExoPlayer.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;
            case ExoPlayer.STATE_READY:
                stateString = "ExoPlayer.STATE_READY     -";
                if(playWhenReady)
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
                else
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
                break;
            case ExoPlayer.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                mMediaSession.setPlaybackState(mStateBuilder.build());
                showNotification(mStateBuilder.build());
                break;
        }
        Log.d(TAG, "changed state to " + stateString
                + " playWhenReady: " + playWhenReady);
    }



    private void showNotification(PlaybackStateCompat stateCompat){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        int icon;
        String play_pause;
        if(stateCompat.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        }else{
            icon = R.drawable.exo_controls_play;
            play_pause= getString(R.string.play);

        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(icon, play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(), PlaybackStateCompat.ACTION_PLAY_PAUSE));
        NotificationCompat.Action restartAction = new NotificationCompat.Action(R.drawable.exo_controls_previous, getString(R.string.restart), MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), StepDetailActivity.class),0);
        builder.setContentTitle(getString(R.string.app_name)).setContentIntent(contentPendingIntent)
        .setContentText(stepTitle).setSmallIcon(R.drawable.ic_not_found).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).addAction(restartAction).addAction(playPauseAction).setStyle(new android.support.v7.app.NotificationCompat.MediaStyle()
        .setMediaSession(mMediaSession.getSessionToken()).setShowActionsInCompactView(0,1));

        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
