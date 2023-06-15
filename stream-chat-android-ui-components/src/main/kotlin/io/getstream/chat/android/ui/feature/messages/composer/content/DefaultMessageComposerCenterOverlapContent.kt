package io.getstream.chat.android.ui.feature.messages.composer.content

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.getstream.sdk.chat.audio.recording.MediaRecorderState
import com.getstream.sdk.chat.audio.recording.StreamMediaRecorder
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.ui.R
import io.getstream.chat.android.ui.common.state.messages.composer.MessageComposerState
import io.getstream.chat.android.ui.common.state.messages.composer.RecordingState
import io.getstream.chat.android.ui.databinding.StreamUiMessageComposerDefaultCenterOverlapContentBinding
import io.getstream.chat.android.ui.feature.messages.composer.MessageComposerContext
import io.getstream.chat.android.ui.utils.PermissionChecker
import io.getstream.chat.android.ui.utils.extensions.dpToPx
import io.getstream.log.taggedLogger
import kotlin.math.abs
import kotlin.math.log10

private const val TAG = "OverlappingContent"

@OptIn(InternalStreamChatApi::class)
public class DefaultMessageComposerOverlappingContent : ConstraintLayout, MessageComposerContent {

    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    private val logger by taggedLogger(TAG)

    private var parentHeight: Int = 0
    private var parentWidth: Int = 0
    private var centerContentHeight: Int = 0

    private lateinit var binding: StreamUiMessageComposerDefaultCenterOverlapContentBinding

    public var onStateChangeListener: ((state: RecordingState) -> Unit)? = null

    public var recordButtonHoldListener: () -> Unit = {}
    public var recordButtonLockListener: () -> Unit = {}
    public var recordButtonCancelListener: () -> Unit = {}
    public var recordButtonReleaseListener: () -> Unit = {}

    public var toggleButtonClickListener: () -> Unit = {} // TODO not used
    public var stopButtonClickListener: () -> Unit = {}
    public var deleteButtonClickListener: () -> Unit = {}
    public var completeButtonClickListener: () -> Unit = {}

    private var lockPopup: PopupWindow? = null
    private val lockBaseRect = Rect()
    private val lockLastRect = Rect()
    private val lockMoveRect = Rect()

    private var micPopup: PopupWindow? = null
    private val micOrigRect = Rect()
    private val micBaseRect = Rect()
    private val micLastRect = Rect()
    private val micMoveRect = Rect()

    private val baseTouch = FloatArray(size = 2)

    private var micW: Int = 64.dpToPx()
    private var micH: Int = 64.dpToPx()

    private val permissionChecker = PermissionChecker()
    // private val mediaRecorder = DefaultStreamMediaRecorder(context.applicationContext, amplitudePollingInterval = 100L)
    // private val extractor by lazy(LazyThreadSafetyMode.NONE) {
    //     WaveformExtractor(context, "key", 100) { extractor, progress ->
    //         if (progress >= 1.0f) {
    //             logger.v { "[onProgress] progress: $progress, sampleData: ${extractor.sampleData}" }
    //             if (childCount > 0) {
    //                 binding.recordingWaveform.waveform = extractor.sampleData
    //             }
    //         }
    //     }
    // }

    private var _state: RecordingState = RecordingState.Idle
        set(value) {
            Log.e(TAG, "[setState] value: $value")
            field = value
        }

    init {
        val inflater = LayoutInflater.from(context)
        binding = StreamUiMessageComposerDefaultCenterOverlapContentBinding.inflate(inflater, this)

        binding.recordingDelete.setOnClickListener {
            deleteButtonClickListener()
        }

        binding.recordingStop.setOnClickListener {
            stopButtonClickListener()
        }
        binding.recordingComplete.setOnClickListener {
            completeButtonClickListener()
        }
    }

    private lateinit var composerContext: MessageComposerContext

    override fun attachContext(messageComposerContext: MessageComposerContext) {
        composerContext = messageComposerContext
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun renderState(state: MessageComposerState) {
        binding.root.isVisible = state.recording != RecordingState.Idle
        val recording = state.recording
        if (recording is RecordingState.Hold) {
            renderHold(recording)
        } else if (recording is RecordingState.Locked) {
            renderLocked(recording)
        } else if (recording is RecordingState.Overview) {
            renderOverview(recording)
        } else if (recording is RecordingState.Idle) {
            renderIdle()
        }
        _state = recording
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        renderIdle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return _state is RecordingState.Idle || _state is RecordingState.Hold
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            Log.e(TAG, "[onVisibilityChanged] VISIBLE")
        } else {
            Log.e(TAG, "[onVisibilityChanged] NOT_VISIBLE")
            resetUI()
        }
    }

    private fun resetUI() {
        Log.e(TAG, "[resetUI] no args")

        binding.recordingWaveform.clearData()
        binding.recordingSlider.translationX = 0f
        binding.recordingSlider.alpha = 1f

        micLastRect.set(micBaseRect)
        lockLastRect.set(lockBaseRect)
        lockPopup?.dismiss()
        lockPopup = null
        micPopup?.dismiss()
        micPopup = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.i(TAG, "[onLayout] w: $width, h: $height")
    }

    private fun renderIdle() {
        val state = _state
        if (state is RecordingState.Idle) {
            logger.w { "[cancel] rejected (state is Idle)" }
            return
        }
        logger.e { "[cancel] no args" }
        resetUI()
        //_state = RecordingState.Idle
        onStateChangeListener?.invoke(RecordingState.Idle)

        // mediaRecorder.release()
        // extractor.stop()
    }

    private fun renderHold(state: RecordingState.Hold) {
        //val state = _state
        // if (state !is RecordingState.Idle) {
        //     logger.w { "[hold] rejected (state is not Idle): $state" }
        //     return
        // }
        //_state = RecordingState.Hold()
        logger.d { "[hold] no args" }

        binding.horizontalGuideline.setGuidelinePercent(1f)
        layoutParams.height = centerContentHeight
        binding.recordingSlider.isVisible = true

        binding.recordingIndicator.setImageResource(R.drawable.stream_ui_ic_mic)
        binding.recordingIndicator.setImageColorRes(R.color.stream_ui_accent_red)
        binding.recordingIndicator.isVisible = true
        binding.recordingIndicator.isClickable = false
        binding.recordingIndicator.isFocusable = false
        binding.recordingWaveform.isVisible = false
        binding.recordingStop.isVisible = false
        binding.recordingDelete.isVisible = false
        binding.recordingComplete.isVisible = false

        binding.recordingTimer.text = formatMillis(state.duration)

        // mediaRecorder.startAudioRecording(recordingName = "audio_recording_${Date()}")
        //
        // mediaRecorder.setOnInfoListener(::onRecorderInfo)
        // mediaRecorder.setOnErrorListener(::onRecorderError)
        // mediaRecorder.setOnMediaRecorderStateChangedListener(::onRecorderStateChanged)
        // mediaRecorder.setOnMaxAmplitudeSampledListener(::onRecorderMaxAmplitudeSampled)
        // mediaRecorder.setOnCurrentRecordingDurationChangedListener(::onRecorderDurationChanged)
        // mediaRecorder.setOnRecordingStoppedListener(::onRecorderStopped)
    }

    private fun onRecorderStopped() {
        Log.i(TAG, "[onRecorderStopped] no args")
    }

    private fun onRecorderDurationChanged(durationMs: Long) {
        logger.v { "[onRecorderDurationChanged] duration: $durationMs" }
        post { binding.recordingTimer.text = formatMillis(durationMs) }
    }

    private fun onRecorderStateChanged(mediaRecorderState: MediaRecorderState) {
        Log.i(TAG, "[onRecorderStateChanged] state: $mediaRecorderState")
    }

    private fun onRecorderInfo(streamMediaRecorder: StreamMediaRecorder, what: Int, extra: Int) {
        Log.i(TAG, "[onRecorderInfo] what: $what, extra: $extra")
    }

    private fun onRecorderError(streamMediaRecorder: StreamMediaRecorder, what: Int, extra: Int) {
        Log.e(TAG, "[onRecorderError] what: $what, extra: $extra")
    }

    private fun onRecorderMaxAmplitudeSampled(maxAmplitude: Int) {
        //val normalized = maxOf(maxAmplitude.toFloat() / 32767f, 0.2f)
        val normalized = maxAmplitude.toFloat() / Short.MAX_VALUE

        val MAX_AMPLITUDE = 32767.0
        val MAX_DB = 90.0 // Maximum decibel level for normalization

        val decibels = 20 * log10(maxAmplitude / MAX_AMPLITUDE)
        //val normalizedValue = maxOf(0.0, minOf(decibels / MAX_DB, 1.0))
        val normalizedValue = abs((50 + decibels) / 50)
        Log.v(
            TAG,
            "[onRecorderMaxAmplitudeSampled] maxAmplitude: $maxAmplitude, decibels: $decibels, normalizedValue: $normalizedValue ($normalized)"
        )
        post {
            binding.recordingWaveform.addValue(normalized.toFloat())
        }
    }

    private fun renderLocked(state: RecordingState.Locked) {
        // val state = _state
        // if (state !is RecordingState.Hold) {
        //     logger.w { "[lock] rejected (state is not Hold): $state" }
        //     return
        // }
        // _state = RecordingState.Locked(state.duration, state.waveform)
        logger.d { "[lock] waveform: ${state.waveform.size}" }

        layoutParams.height = parentHeight * 2
        binding.horizontalGuideline.setGuidelinePercent(0.5f)
        binding.recordingSlider.isVisible = false


        binding.recordingDelete.isVisible = true
        binding.recordingStop.isVisible = true
        binding.recordingComplete.isVisible = true

        binding.recordingTimer.text = formatMillis(state.duration)
        binding.recordingWaveform.isVisible = true
        binding.recordingWaveform.waveform = state.waveform

        micPopup?.dismiss()
        micPopup = null
        (lockPopup?.contentView as? ImageView?)?.setImageResource(R.drawable.stream_ui_ic_mic_locked_light)

        lockPopup?.update(lockBaseRect.left, lockBaseRect.top - 16.dpToPx(), -1, 64.dpToPx())

        onStateChangeListener?.invoke(_state)
    }

    private fun renderOverview(state: RecordingState.Overview) {
        // val state = _state
        // if (state !is RecordingState.Locked) {
        //     logger.w { "[overview] rejected (state is not Locked): $state" }
        //     return
        // }
        // _state = RecordingState.Overview()
        logger.d { "[overview] no args" }

        // val result = mediaRecorder.stopRecording()
        // mediaRecorder.release()

        layoutParams.height = parentHeight * 2
        binding.horizontalGuideline.setGuidelinePercent(0.5f)

        binding.recordingIndicator.setImageResource(R.drawable.stream_ui_ic_play)
        binding.recordingIndicator.setImageColorRes(R.color.stream_ui_accent_blue)
        binding.recordingIndicator.isClickable = true
        binding.recordingIndicator.isFocusable = true
        binding.recordingIndicator.isVisible = true

        binding.recordingSlider.isVisible = false

        binding.recordingTimer.text = formatMillis(0L)
        binding.recordingWaveform.isVisible = true
        binding.recordingWaveform.waveform = state.waveform

        binding.recordingDelete.isVisible = true
        binding.recordingStop.isVisible = false
        binding.recordingComplete.isVisible = true

        micPopup?.dismiss()
        micPopup = null
        lockPopup?.dismiss()
        lockPopup = null

        onStateChangeListener?.invoke(_state)

        // result.onSuccess {
            //Log.i(TAG, "[overview] attachment: $it")
            //extractor.start(it.upload?.absolutePath ?: error("no upload found"))

            //val amplituda = Amplituda(context)
            //val data = MediaDecoder(it.upload?.absolutePath).readShortData()
            //Log.v(TAG, "[overview] data: ${data}")
        // }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val state = _state
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN && state !is RecordingState.Idle) {
            Log.w(TAG, "[onTouchEvent] rejected ACTION_DOWN (state is not Idle): $state")
            return false
        }
        if (action != MotionEvent.ACTION_DOWN && state !is RecordingState.Hold) {
            Log.w(TAG, "[onTouchEvent] rejected ${actionToString(action)} (state is not Hold): $state")
            return false
        }
        Log.v(TAG, "[onTouchEvent] state: $state, event: $event")
        val x = event.rawX
        val y = event.rawY
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "[onTouchEvent] ACTION_DOWN")
                if (!permissionChecker.isGrantedAudioRecordPermission(context)) {
                    permissionChecker.checkAudioRecordPermissions(this)
                    return false
                }
                parentWidth = composerContext.content.asView().width
                parentHeight = composerContext.content.asView().height
                centerContentHeight =
                    composerContext.content.center?.asView()?.height ?: error("no center content found")
                val recordAudioButton =
                    composerContext.content.findViewByKey(MessageComposerContent.RECORD_AUDIO_BUTTON)
                        ?: composerContext.content.trailing?.asView()?.findViewById(R.id.recordAudioButton)
                        ?: error("recordAudioButton not found")
                recordAudioButton.getRectInWindow(micOrigRect)
                showMicPopup()
                showLockPopup()
                recordButtonHoldListener()

                baseTouch[0] = x
                baseTouch[1] = y
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = (x - baseTouch[0]).toInt()
                val deltaY = (y - baseTouch[1]).toInt()

                if (micBaseRect.width() == 0 || lockBaseRect.width() == 0) {
                    Log.v(TAG, "[onTouchEvent] ACTION_MOVE rejected (no popups 1)")
                    return false
                }
                if (micPopup == null || lockPopup == null) {
                    Log.v(TAG, "[onTouchEvent] ACTION_MOVE rejected (no popups 2)")
                    return false
                }

                Log.i(TAG, "[onTouchEvent] ACTION_MOVE: offsetX: $deltaX, offsetY: $deltaY")

                val micX = micBaseRect.left + deltaX
                val micY = micBaseRect.top + deltaY
                micLastRect.left = micX.limitTo(micMoveRect.left, micBaseRect.left)
                micLastRect.top = micY.limitTo(micMoveRect.top, micBaseRect.top)
                micPopup?.update(micLastRect.left, micLastRect.top, -1, -1)

                val lockY = lockBaseRect.top + deltaY
                lockLastRect.top = lockY.limitTo(lockMoveRect.top, lockBaseRect.top)
                lockPopup?.update(lockLastRect.left, lockLastRect.top, -1, -1)

                val progress =
                    (micBaseRect.left - micLastRect.left).toFloat() / (micBaseRect.left - micMoveRect.left).toFloat()
                Log.w(TAG, "[onMove] progress: $progress, diff: ${micLastRect.left - micBaseRect.left}")
                binding.recordingSlider.translationX = micLastRect.left - micBaseRect.left.toFloat()
                binding.recordingSlider.alpha = 1 - progress * 1.5f

                if (micLastRect.left == micMoveRect.left) {
                    Log.w(TAG, "[onMove] cancelled; micLastRect: $micLastRect, micMoveRect: $micMoveRect")
                    renderIdle()
                    recordButtonCancelListener()
                    return false
                }

                if (micLastRect.top == micMoveRect.top) {
                    Log.w(TAG, "[onMove] locked")
                    recordButtonLockListener()
                    return false
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "[onTouchEvent] ACTION_CANCEL")
                recordButtonCancelListener()
                return true
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "[onTouchEvent] ACTION_UP")
                recordButtonReleaseListener()
                return true
            }
        }
        return true
    }

    private fun showMicPopup() {
        Log.d(TAG, "[showMicPopup] orig: $micOrigRect")
        val micW = micW
        val micH = micH
        val micContent = LayoutInflater.from(context).inflate(
            R.layout.stream_ui_message_composer_default_center_overlap_floating_mic, this, false
        )
        micPopup?.dismiss()
        micPopup = PopupWindow(context).apply {
            setBackgroundDrawable(null)
            isClippingEnabled = false

            contentView = micContent
            width = micW
            height = micH

            micBaseRect.set(0, 0, micW, micH)
            val deltaX = micOrigRect.centerX() - micBaseRect.centerX()
            val deltaY = micOrigRect.centerY() - micBaseRect.centerY()
            micBaseRect.offset(deltaX, deltaY)
            micLastRect.set(micBaseRect)
            micMoveRect.apply {
                set(micBaseRect)
                left -= (parentWidth / 3)
                top -= centerContentHeight * 2
            }
            showAtLocation(binding.root, Gravity.TOP or Gravity.START, micBaseRect.left, micBaseRect.top)
        }
    }

    private fun showLockPopup() {
        Log.d(TAG, "[showLockPopup] micOrigRect: $micOrigRect")
        val lockW = 52.dpToPx()
        val lockH = 92.dpToPx()
        val lockContent = LayoutInflater.from(context).inflate(
            R.layout.stream_ui_message_composer_default_center_overlap_floating_lock, this, false
        )
        lockPopup?.dismiss()
        lockPopup = PopupWindow(context).apply {
            setBackgroundDrawable(null)
            isClippingEnabled = true

            contentView = lockContent
            width = lockW
            height = lockH

            lockBaseRect.set(0, 0, lockW, lockH)
            val deltaX = micBaseRect.centerX() - lockBaseRect.centerX()
            val deltaY = micBaseRect.top - lockBaseRect.bottom
            lockBaseRect.offset(deltaX, deltaY)

            lockLastRect.set(lockBaseRect)
            lockMoveRect.apply {
                set(lockBaseRect)
                top -= centerContentHeight * 2
            }
            showAtLocation(binding.root, Gravity.TOP or Gravity.START, lockBaseRect.left, lockBaseRect.top)
        }
    }
}

private fun View.getRectInWindow(out: Rect) {
    val xy = IntArray(2)
    getLocationInWindow(xy)
    out.apply {
        left = xy[0]
        top = xy[1]
        right = xy[0] + width
        bottom = xy[1] + height
    }
}

private fun Int.limitTo(min: Int, max: Int): Int {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

private val ViewParent.id: Int
    get() = (this as? View)?.id ?: ConstraintLayout.NO_ID

private fun formatMillis(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun ImageView.setImageColor(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

private fun ImageView.setImageColorRes(@ColorRes colorResId: Int) {
    val color = ContextCompat.getColor(context, colorResId)
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

private fun actionToString(action: Int): String {
    return when (action) {
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        else -> "ACTION_UNKNOWN"
    }
}