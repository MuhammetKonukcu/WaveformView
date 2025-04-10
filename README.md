# WaveformView
This class is designed to convert data received from a microphone into a visual waveform display.

https://github.com/user-attachments/assets/0d1ee53a-3e41-428f-b78b-5fc85ca27ff0

Properties:
- Values such as bar color, width, spacing and corner radius can be customized via XML.
- Smoothing window size can be adjusted.

# Add style variables
-You must add the following XML code to your res/values/attrs.xml file.

```xml
<declare-styleable name="WaveformView">
        <attr name="barColor" format="color" />
        <attr name="barWidth" format="dimension" />
        <attr name="barSpacing" format="dimension" />
        <attr name="barRadius" format="dimension" />
        <attr name="smoothingWindowSize" format="integer" />
</declare-styleable>
```

# Using

By adding the WaveformView to your layout file, you can use it in a similar way to the following example:

```xml
<com.muhammetkonukcu.android.WaveformView
    android:id="@+id/waveform"
    android:layout_width="match_parent"
    android:layout_height="120dp"
    app:barColor="@color/your_custom_color"
    app:barWidth="12dp"
    app:barSpacing="8dp"
    app:barRadius="4dp"
    app:smoothingWindowSize="2" />
```
<pre>
class ChatRecorderSheet : BottomSheetDialogFragment(), TimerClass.OnTimerTickListener {

    // MediaRecorder instance used for recording audio from the microphone.
    private var mediaRecorder: MediaRecorder? = null

    // TimerClass instance which provides timer tick callbacks to track the recording duration.
    private var timerClass: TimerClass = TimerClass(this)

    // Note: To clear the waveform view, you can call: binding.waveform.clear()

    // Function to start the recording process.
    private fun startRecord() {
        // Determine the local file where the recording will be saved.
        val localFile = setLocalFile()

        // Set up MediaRecorder with the appropriate settings.
        mediaRecorder = setMediaRecorder()
        mediaRecorder?.apply {
            // Use the microphone as the audio source.
            setAudioSource(MediaRecorder.AudioSource.MIC)
            // Specify the output format for the recording (MPEG_4 in this case).
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            // Use AAC for encoding the audio.
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            // Set the file path where the recorded audio will be stored.
            setOutputFile(localFile)
            try {
                // Prepare the MediaRecorder to start recording.
                prepare()
            } catch (e: IOException) {
                // Log an error if the MediaRecorder fails to prepare.
                Log.e("RecorderSheet", "MediaRecorder prepare() failed $e")
            }
            // Start the recording.
            start()
            // Begin the timer to track the duration of the recording.
            timerClass.start()
        }
    }

    // Callback function from TimerClass which is triggered on each timer tick.
    override fun onTimerTick(duration: String) {
        // Update the TextView that displays the recording duration.
        binding.durationTv.text = duration
        // Add the current maximum amplitude from the MediaRecorder to the waveform view.
        // If mediaRecorder is null or maxAmplitude is not available, default to 0f.
        binding.waveform.addAmplitude(mediaRecorder?.maxAmplitude?.toFloat() ?: 0f)
    }
}

