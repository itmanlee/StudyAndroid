package com.wangkeke.hprogressbutton;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


	private MasterLayout masterLayout;
	private CircleProgressImageView circleProgressView;

	private boolean isPlay;
	private boolean isComplete = true;
	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		masterLayout = (MasterLayout) findViewById(R.id.master_layout);
		circleProgressView = (CircleProgressImageView) findViewById(R.id.iv);
		circleProgressView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!isPlay) {
					playAudio();
				} else {
					pauseAudio();
				}
			}
		});
		//Onclick listener of the progress button
		masterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				masterLayout.animation(); //Need to call this method for animation and progression
				
				if (masterLayout.flg_frmwrk_mode == 1) { 
					
					//Start state. Call any method that you want to execute
					
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this,
									"Starting download", Toast.LENGTH_SHORT)
									.show();
						}
					});
					new DownLoadSigTask().execute();
				}
				if (masterLayout.flg_frmwrk_mode == 2) {
					
					//Running state. Call any method that you want to execute
					
					new DownLoadSigTask().cancel(true);
					masterLayout.reset();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this,
									"Download stopped", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				if (masterLayout.flg_frmwrk_mode == 3) {
					
					//End state. Call any method that you want to execute.
					
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this,
									"Download complete", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			}
		});

	}

	class DownLoadSigTask extends AsyncTask<String, Integer, String> {

		
		@Override
		protected void onPreExecute() {

		}

		
		@Override
		protected String doInBackground(final String... args) {
			
			//Creating dummy task and updating progress
			
			for (int i = 0; i <= 100; i++) {
				try {
					Thread.sleep(50);
					
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
				publishProgress(i);

			}

		
			return null;
		}

	
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
			//publishing progress to progress arc
			
			masterLayout.cusview.setupprogress(progress[0]);
		}

	

	}


	private void playAudio() {
		try {
			if (isComplete) {
				mediaPlayer.reset();
				//从asset文件夹下读取MP3文件
				AssetFileDescriptor fileDescriptor = getAssets().openFd("lalalademaxiya.mp3");
				mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(), fileDescriptor.getLength());
				mediaPlayer.prepare();
				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						stopAudio();
					}
				});
				circleProgressView.clearDuration();
				isComplete = false;
			}
			mediaPlayer.start();
			circleProgressView.setDuration(mediaPlayer.getDuration());
			circleProgressView.play();
			isPlay = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pauseAudio() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			circleProgressView.pause();
			isPlay = false;
		}
	}

	private void stopAudio() {
		circleProgressView.stop();
		isPlay = false;
		isComplete = true;
	}

	@Override
	protected void onDestroy() {
		//释放资源
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
		super.onDestroy();
	}
}
