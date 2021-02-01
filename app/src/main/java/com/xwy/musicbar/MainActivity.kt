package com.xwy.musicbar

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.xwy.musicbar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    val mLock: Object = Object()


    var isGetVoiceRun: Boolean = true

    val SAMPLE_RATE_IN_HZ = 8000;
    val BUFFER_SIZE = AudioRecord.getMinBufferSize(
        SAMPLE_RATE_IN_HZ,
        AudioFormat.CHANNEL_IN_DEFAULT,
        AudioFormat.ENCODING_PCM_16BIT
    )

    lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mBinding.root)

        getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
    }

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        } else {
            isGetVoiceRun = true
            getNoiseLevel()
        }
        wakeLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                or PowerManager.ON_AFTER_RELEASE, "TAG")
        wakeLock.acquire()

    }

    override fun onPause() {
        super.onPause()
        isGetVoiceRun = false
        wakeLock.release()
    }

    fun getNoiseLevel() {
        var mAudioRecord: AudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE
        )
        Thread {
            mAudioRecord.startRecording()
            val buffer = ShortArray(BUFFER_SIZE)
            while (isGetVoiceRun) {
                mAudioRecord.read(buffer, 0, BUFFER_SIZE)
                var volume = calculateVolume(buffer)
                Log.e("----->", "分贝值 = ${volume}dB")
                runOnUiThread(Runnable {
                    mBinding.myView.voiceHeight =
                        mBinding.myView.height * (((volume.toFloat() - 16) * 1.8f) / 35)
                    mBinding.myView.invalidate()
                })
                synchronized(mLock) {
                    try {
                        mLock.wait(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            mAudioRecord.stop()
            mAudioRecord.release()
        }.start()
    }

    private fun calculateVolume(buffer: ShortArray): Double {
        var sumVolume = 0.0
        var avgVolume = 0.0
        var volume = 0.0
        for (b in buffer) {
            sumVolume += Math.abs(b.toInt()).toDouble()
        }
        avgVolume = sumVolume / buffer.size
        volume = Math.log10(1 + avgVolume) * 10
        return volume
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}