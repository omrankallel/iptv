package tn.iptv.nextplayer.feature.player

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.media.AudioManager
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Rational
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.view.accessibility.CaptioningManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.CaptionStyleCompat
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import androidx.media3.ui.SubtitleView
import androidx.media3.ui.TimeBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.NextRenderersFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import tn.iptv.nextplayer.core.common.Utils
import tn.iptv.nextplayer.core.common.extensions.convertToUTF8
import tn.iptv.nextplayer.core.common.extensions.deleteFiles
import tn.iptv.nextplayer.core.common.extensions.getFilenameFromUri
import tn.iptv.nextplayer.core.common.extensions.getMediaContentUri
import tn.iptv.nextplayer.core.common.extensions.isDeviceTvBox
import tn.iptv.nextplayer.core.common.extensions.subtitleCacheDir
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.core.model.DecoderPriority
import tn.iptv.nextplayer.core.model.ScreenOrientation
import tn.iptv.nextplayer.core.model.ThemeConfig
import tn.iptv.nextplayer.core.model.VideoZoom
import tn.iptv.nextplayer.feature.player.adpter.ChannelAdapter2
import tn.iptv.nextplayer.feature.player.adpter.OnChannelClickListener
import tn.iptv.nextplayer.feature.player.databinding.ActivityPlayerBinding
import tn.iptv.nextplayer.feature.player.dialogs.PlaybackSpeedControlsDialogFragment
import tn.iptv.nextplayer.feature.player.dialogs.TrackSelectionDialogFragment
import tn.iptv.nextplayer.feature.player.dialogs.VideoZoomOptionsDialogFragment
import tn.iptv.nextplayer.feature.player.dialogs.nameRes
import tn.iptv.nextplayer.feature.player.extensions.audioSessionId
import tn.iptv.nextplayer.feature.player.extensions.getCurrentTrackIndex
import tn.iptv.nextplayer.feature.player.extensions.getLocalSubtitles
import tn.iptv.nextplayer.feature.player.extensions.getSubtitleMime
import tn.iptv.nextplayer.feature.player.extensions.isPortrait
import tn.iptv.nextplayer.feature.player.extensions.next
import tn.iptv.nextplayer.feature.player.extensions.prettyPrintIntent
import tn.iptv.nextplayer.feature.player.extensions.seekBack
import tn.iptv.nextplayer.feature.player.extensions.seekForward
import tn.iptv.nextplayer.feature.player.extensions.setImageDrawable
import tn.iptv.nextplayer.feature.player.extensions.shouldFastSeek
import tn.iptv.nextplayer.feature.player.extensions.skipSilenceEnabled
import tn.iptv.nextplayer.feature.player.extensions.switchTrack
import tn.iptv.nextplayer.feature.player.extensions.toActivityOrientation
import tn.iptv.nextplayer.feature.player.extensions.toSubtitle
import tn.iptv.nextplayer.feature.player.extensions.toTypeface
import tn.iptv.nextplayer.feature.player.extensions.togglePlayPause
import tn.iptv.nextplayer.feature.player.extensions.toggleSystemBars
import tn.iptv.nextplayer.feature.player.model.Subtitle
import tn.iptv.nextplayer.feature.player.model.grouped_media.GroupedMedia
import tn.iptv.nextplayer.feature.player.utils.AppHelper
import tn.iptv.nextplayer.feature.player.utils.BrightnessManager
import tn.iptv.nextplayer.feature.player.utils.PlayerApi
import tn.iptv.nextplayer.feature.player.utils.PlayerGestureHelper
import tn.iptv.nextplayer.feature.player.utils.PlaylistManager
import tn.iptv.nextplayer.feature.player.utils.VolumeManager
import java.nio.charset.Charset
import tn.iptv.nextplayer.core.ui.R as coreUiR

@SuppressLint("UnsafeOptInUsageError")
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {


    lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModels()
    private val currentContext = this
    private val applicationPreferences get() = viewModel.appPrefs.value
    private val playerPreferences get() = viewModel.playerPrefs.value

    private var playWhenReady = true
    private var isPlaybackFinished = false

    var isFileLoaded = false
    var isControlsLocked = false
    private var shouldFetchPlaylist = true
    private var isSubtitleLauncherHasUri = false
    private var isFirstFrameRendered = false
    private var isFrameRendered = false
    private var isPlayingOnScrubStart: Boolean = false
    private var previousScrubPosition = 0L
    private var scrubStartPosition: Long = -1L
    private var currentOrientation: Int? = null
    var currentVideoSize: VideoSize? = null
    private var hideVolumeIndicatorJob: Job? = null
    private var hideBrightnessIndicatorJob: Job? = null
    private var hideInfoLayoutJob: Job? = null

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var adapter: ChannelAdapter2

    private var lastOkClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA = 300


    private val shouldFastSeek: Boolean
        get() = playerPreferences.shouldFastSeek(player.duration)

    /**
     * Player
     */
    private lateinit var player: Player
    private lateinit var playerGestureHelper: PlayerGestureHelper
    private lateinit var playlistManager: PlaylistManager
    private lateinit var trackSelector: DefaultTrackSelector
    private var surfaceView: SurfaceView? = null
    private var mediaSession: MediaSession? = null
    private lateinit var playerApi: PlayerApi
    private lateinit var volumeManager: VolumeManager
    private lateinit var brightnessManager: BrightnessManager
    var loudnessEnhancer: LoudnessEnhancer? = null

    /**
     * Listeners
     */
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private val subtitleFileLauncher = registerForActivityResult(OpenDocument()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            isSubtitleLauncherHasUri = true
            viewModel.externalSubtitles.add(uri)
        }
        playVideo(playlistManager.getCurrent() ?: intent.data!!)
    }

    /**
     * Player controller views
     */
    private lateinit var menuContainer: LinearLayout
    private lateinit var menuTitle: TextView
    private lateinit var imgImageChannel: AppCompatImageView

    private lateinit var audioTrackButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var exoContentFrameLayout: AspectRatioFrameLayout
    private lateinit var lockControlsButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var playbackSpeedButton: ImageButton
    private lateinit var playerLockControls: FrameLayout
    private lateinit var playerUnlockControls: FrameLayout
    private lateinit var playerCenterControls: LinearLayout
    private lateinit var prevButton: ImageButton
    private lateinit var screenRotateButton: ImageButton
    private lateinit var pipButton: ImageButton
    private lateinit var seekBar: TimeBar
    private lateinit var seekBarPlayer: DefaultTimeBar
    private lateinit var subtitleTrackButton: ImageButton
    private lateinit var unlockControlsButton: ImageButton
    private lateinit var videoTitleTextView: TextView
    private lateinit var videoZoomButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var upButton: ImageButton
    private lateinit var downButton: ImageButton
    private lateinit var addButton: ImageButton
    private lateinit var doubleAddButton: ImageButton
    val handler = Handler(Looper.getMainLooper())
    var isHolding = false


    private val isPipSupported: Boolean by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    @SuppressLint("SuspiciousIndentation", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prettyPrintIntent()

        AppCompatDelegate.setDefaultNightMode(
            when (applicationPreferences.themeConfig) {
                ThemeConfig.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeConfig.OFF -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeConfig.ON -> AppCompatDelegate.MODE_NIGHT_YES
            },
        )

        if (applicationPreferences.useDynamicColors) {
            DynamicColors.applyToActivityIfAvailable(this)
        }

        // The window is always allowed to extend into the DisplayCutout areas on the short edges of the screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initializing views
        menuContainer = binding.playerView.findViewById(R.id.menu_container)
        menuTitle = binding.playerView.findViewById(R.id.menu_title)
        imgImageChannel = binding.playerView.findViewById(R.id.img_channel)


        audioTrackButton = binding.playerView.findViewById(R.id.btn_audio_track)
        backButton = binding.playerView.findViewById(R.id.back_button)
        exoContentFrameLayout = binding.playerView.findViewById(R.id.exo_content_frame)
        lockControlsButton = binding.playerView.findViewById(R.id.btn_lock_controls)
        nextButton = binding.playerView.findViewById(R.id.btn_play_next)
        playbackSpeedButton = binding.playerView.findViewById(R.id.btn_playback_speed)
        playerLockControls = binding.playerView.findViewById(R.id.player_lock_controls)
        playerUnlockControls = binding.playerView.findViewById(R.id.player_unlock_controls)
        playerCenterControls = binding.playerView.findViewById(R.id.player_center_controls)
        prevButton = binding.playerView.findViewById(R.id.btn_play_prev)
        screenRotateButton = binding.playerView.findViewById(R.id.screen_rotate)
        pipButton = binding.playerView.findViewById(R.id.btn_pip)
        seekBar = binding.playerView.findViewById(R.id.exo_progress)
        seekBarPlayer = binding.playerView.findViewById(R.id.exo_progress)
        subtitleTrackButton = binding.playerView.findViewById(R.id.btn_subtitle_track)
        unlockControlsButton = binding.playerView.findViewById(R.id.btn_unlock_controls)
        videoTitleTextView = binding.playerView.findViewById(R.id.video_name)
        videoZoomButton = binding.playerView.findViewById(R.id.btn_video_zoom)
        upButton = binding.playerView.findViewById(R.id.btn_up)
        downButton = binding.playerView.findViewById(R.id.btn_down)
        addButton = binding.playerView.findViewById(R.id.btn_add)
        doubleAddButton = binding.playerView.findViewById(R.id.btn_double_add)

        val runnableUp = object : Runnable {
            override fun run() {
                if (isHolding) {
                    adapter.toggleFavoriteUp(recyclerView)
                    handler.postDelayed(this, 200)
                }
            }
        }
        val runnableDown = object : Runnable {
            override fun run() {
                if (isHolding) {
                    adapter.toggleFavoriteDown(recyclerView)
                    handler.postDelayed(this, 200)
                }
            }
        }

        downButton.setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    isHolding = true
                    handler.post(runnableUp)
                }

                ACTION_UP, ACTION_CANCEL -> {
                    isHolding = false
                    handler.removeCallbacks(runnableUp)
                }
            }
            true
        }
        upButton.setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    isHolding = true
                    handler.post(runnableDown)
                }

                ACTION_UP, ACTION_CANCEL -> {
                    isHolding = false
                    handler.removeCallbacks(runnableDown)
                }
            }
            true
        }
        downButton.setOnLongClickListener {
            adapter.toggleFavoriteDown(recyclerView)
            true
        }
        addButton.setOnClickListener {
            adapter.selectedChannel(this, recyclerView)
        }

        doubleAddButton.setOnClickListener {
            adapter.openFavorite(recyclerView)
        }




        viewModel.products.observe(this) { products ->

        }
        viewModel.fetchProducts()


        try {
            val dataOfLiveChannelJsonString = intent.getStringExtra("GROUP_OF_CHANNEL")
            var indexOfCurrentChannel = intent.getIntExtra("INDEX_OF_CHANNEL", 0)


            // Désérialisation de la chaîne JSON en objet GroupedMedia
            val gson = Gson()
            val groupOfChannel: GroupedMedia = gson.fromJson(dataOfLiveChannelJsonString, GroupedMedia::class.java)

            menuContainer.visibility = View.VISIBLE
            val displayMetrics = resources.displayMetrics
            val width = (displayMetrics.widthPixels * 0.5).toInt()
            menuContainer.layoutParams.width = width

            //adjust Container  with data
            menuTitle.text = AppHelper.cleanChannelName(groupOfChannel.labelGenre)
            Glide.with(this).load(groupOfChannel.icon).into(imgImageChannel)

            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = ChannelAdapter2(
                this.applicationContext, groupOfChannel.listSeries, indexOfCurrentChannel, favoriteViewModel,
                object : OnChannelClickListener {

                    override fun onChannelClick(position: Int, mediaItem: tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem) {
                        if (indexOfCurrentChannel == position) {
                            showHidePlayerGesture()
                        } else {
                            indexOfCurrentChannel = position
                            playVideo(uri = Uri.parse(mediaItem.url))

                        }
                    }

                },
                menuContainer,
                dataOfLiveChannelJsonString!!,
            )
            recyclerView.adapter = adapter
            recyclerView.scrollToPosition(indexOfCurrentChannel)
            recyclerView.itemAnimator = null
            seekBarPlayer.visibility = View.GONE

        } catch (error: NullPointerException) {
            menuContainer.visibility = View.GONE
        }


        if (!isPipSupported) {
            pipButton.visibility = View.GONE
        }

        seekBar.addListener(
            object : TimeBar.OnScrubListener {
                override fun onScrubStart(timeBar: TimeBar, position: Long) {
                    if (player.isPlaying) {
                        isPlayingOnScrubStart = true
                        player.pause()
                    }
                    isFrameRendered = true
                    scrubStartPosition = player.currentPosition
                    previousScrubPosition = player.currentPosition
                    scrub(position)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    scrub(position)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    hidePlayerInfo(0L)
                    scrubStartPosition = -1L
                    if (isPlayingOnScrubStart) {
                        player.play()
                    }
                }
            },
        )

        volumeManager = VolumeManager(audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        brightnessManager = BrightnessManager(activity = this)
        playerGestureHelper = PlayerGestureHelper(
            viewModel = viewModel,
            activity = this,
            volumeManager = volumeManager,
            brightnessManager = brightnessManager,
        )

        playlistManager = PlaylistManager()
        playerApi = PlayerApi(this)
    }


    override fun onStart() {
        if (playerPreferences.rememberPlayerBrightness) {
            brightnessManager.setBrightness(playerPreferences.playerBrightness)
        }
        createPlayer()
        //setOrientation()
        initPlaylist()
        initializePlayerView()
        playVideo(uri = playlistManager.getCurrent() ?: intent.data!!)
        super.onStart()
    }

    override fun onStop() {
        binding.volumeGestureLayout.visibility = View.GONE
        binding.brightnessGestureLayout.visibility = View.GONE
        currentOrientation = requestedOrientation
        releasePlayer()
        super.onStop()
    }

    @SuppressLint("MissingSuperCall")
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            isPipSupported &&
            playerPreferences.autoPip &&
            player.isPlaying &&
            !isControlsLocked
        ) {
            try {
                this.enterPictureInPictureMode(updatePictureInPictureParams())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        if (isInPictureInPictureMode) {
            binding.playerView.subtitleView?.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION)
            playerUnlockControls.visibility = View.INVISIBLE
        } else {
            binding.playerView.subtitleView?.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, playerPreferences.subtitleTextSize.toFloat())
            if (!isControlsLocked) {
                playerUnlockControls.visibility = View.VISIBLE
            }
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePictureInPictureParams(): PictureInPictureParams {
        val params: PictureInPictureParams = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(16, 9))
            .build()

        setPictureInPictureParams(params)
        return params
    }

    private fun createPlayer() {
        Timber.d("Creating player")

        val renderersFactory = NextRenderersFactory(applicationContext)
            .setEnableDecoderFallback(true)
            .setExtensionRendererMode(
                when (playerPreferences.decoderPriority) {
                    DecoderPriority.DEVICE_ONLY -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
                    DecoderPriority.PREFER_DEVICE -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                    DecoderPriority.PREFER_APP -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                },
            )

        trackSelector = DefaultTrackSelector(applicationContext).apply {
            this.setParameters(
                this.buildUponParameters()
                    .setPreferredAudioLanguage(playerPreferences.preferredAudioLanguage)
                    .setPreferredTextLanguage(playerPreferences.preferredSubtitleLanguage),
            )
        }

        player = ExoPlayer.Builder(applicationContext)
            .setRenderersFactory(renderersFactory)
            .setTrackSelector(trackSelector)
            .setAudioAttributes(getAudioAttributes(), playerPreferences.requireAudioFocus)
            .setHandleAudioBecomingNoisy(playerPreferences.pauseOnHeadsetDisconnect)
            .build()

        try {
            if (player.canAdvertiseSession()) {
                mediaSession = MediaSession.Builder(this, player).build()
            }
            loudnessEnhancer = if (playerPreferences.shouldUseVolumeBoost) LoudnessEnhancer(player.audioSessionId) else null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        player.addListener(playbackStateListener)
        volumeManager.loudnessEnhancer = loudnessEnhancer
    }

    private fun setOrientation() {
        requestedOrientation = currentOrientation ?: playerPreferences.playerScreenOrientation.toActivityOrientation()
    }

    private fun initializePlayerView() {
        binding.playerView.apply {
            setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
            player = this@PlayerActivity.player
            controllerShowTimeoutMs = 10000000
            setControllerVisibilityListener(
                PlayerView.ControllerVisibilityListener { visibility ->
                    toggleSystemBars(showBars = visibility == View.VISIBLE && !isControlsLocked)
                },
            )

            subtitleView?.apply {
                val captioningManager = getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager
                if (playerPreferences.useSystemCaptionStyle) {
                    val systemCaptionStyle = CaptionStyleCompat.createFromCaptionStyle(captioningManager.userStyle)
                    setStyle(systemCaptionStyle)
                } else {
                    val userStyle = CaptionStyleCompat(
                        Color.WHITE,
                        Color.BLACK.takeIf { playerPreferences.subtitleBackground } ?: Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW,
                        Color.BLACK,
                        Typeface.create(
                            playerPreferences.subtitleFont.toTypeface(),
                            Typeface.BOLD.takeIf { playerPreferences.subtitleTextBold } ?: Typeface.NORMAL,
                        ),
                    )
                    setStyle(userStyle)
                    setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, playerPreferences.subtitleTextSize.toFloat())
                }
                setApplyEmbeddedStyles(playerPreferences.applyEmbeddedStyles)
            }
        }

        audioTrackButton.setOnClickListener {
            trackSelector.currentMappedTrackInfo ?: return@setOnClickListener

            TrackSelectionDialogFragment(
                type = C.TRACK_TYPE_AUDIO,
                tracks = player.currentTracks,
                onTrackSelected = { player.switchTrack(C.TRACK_TYPE_AUDIO, it) },
            ).show(supportFragmentManager, "TrackSelectionDialog")
        }

        subtitleTrackButton.setOnClickListener {
            trackSelector.currentMappedTrackInfo ?: return@setOnClickListener

            TrackSelectionDialogFragment(
                type = C.TRACK_TYPE_TEXT,
                tracks = player.currentTracks,
                onTrackSelected = { player.switchTrack(C.TRACK_TYPE_TEXT, it) },
                onOpenLocalTrackClicked = {
                    subtitleFileLauncher.launch(
                        arrayOf(
                            MimeTypes.APPLICATION_SUBRIP,
                            MimeTypes.APPLICATION_TTML,
                            MimeTypes.TEXT_VTT,
                            MimeTypes.TEXT_SSA,
                            MimeTypes.BASE_TYPE_APPLICATION + "/octet-stream",
                            MimeTypes.BASE_TYPE_TEXT + "/*",
                        ),
                    )
                },
            ).show(supportFragmentManager, "TrackSelectionDialog")
        }

        playbackSpeedButton.setOnClickListener {
            PlaybackSpeedControlsDialogFragment(
                currentSpeed = player.playbackParameters.speed,
                skipSilenceEnabled = player.skipSilenceEnabled,
                onChange = {
                    viewModel.isPlaybackSpeedChanged = true
                    player.setPlaybackSpeed(it)
                },
                onSkipSilenceChanged = {
                    player.skipSilenceEnabled = it
                },
            ).show(supportFragmentManager, "PlaybackSpeedSelectionDialog")
        }

        nextButton.setOnClickListener {
            if (playlistManager.hasNext()) {
                playlistManager.getCurrent()?.let { savePlayerState(it) }
                viewModel.resetAllToDefaults()
                playVideo(playlistManager.getNext()!!)
            }
        }
        prevButton.setOnClickListener {
            if (playlistManager.hasPrev()) {
                playlistManager.getCurrent()?.let { savePlayerState(it) }
                viewModel.resetAllToDefaults()
                playVideo(playlistManager.getPrev()!!)
            }
        }
        lockControlsButton.setOnClickListener {
            playerUnlockControls.visibility = View.INVISIBLE
            playerLockControls.visibility = View.VISIBLE
            isControlsLocked = true
            toggleSystemBars(showBars = false)
        }
        unlockControlsButton.setOnClickListener {
            playerLockControls.visibility = View.INVISIBLE
            playerUnlockControls.visibility = View.VISIBLE
            isControlsLocked = false
            binding.playerView.showController()
            toggleSystemBars(showBars = true)
        }
        videoZoomButton.setOnClickListener {
            val videoZoom = playerPreferences.playerVideoZoom.next()
            applyVideoZoom(videoZoom = videoZoom, showInfo = true)
        }

        videoZoomButton.setOnLongClickListener {
            VideoZoomOptionsDialogFragment(
                currentVideoZoom = playerPreferences.playerVideoZoom,
                onVideoZoomOptionSelected = { applyVideoZoom(videoZoom = it, showInfo = true) },
            ).show(supportFragmentManager, "VideoZoomOptionsDialog")
            true
        }
        screenRotateButton.setOnClickListener {
            requestedOrientation = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
        pipButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isPipSupported) {
                this.enterPictureInPictureMode(updatePictureInPictureParams())
            }
        }
        backButton.setOnClickListener { finish() }
    }

    private fun initPlaylist() = lifecycleScope.launch(Dispatchers.IO) {
        val mediaUri = getMediaContentUri(intent.data!!)

        if (mediaUri != null) {
            val playlist = viewModel.getPlaylistFromUri(mediaUri)
            playlistManager.setPlaylist(playlist)
        }
    }

    fun playVideo(uri: Uri) = lifecycleScope.launch(Dispatchers.IO) {
        playlistManager.updateCurrent(uri)
        val isCurrentUriIsFromIntent = intent.data == uri

        viewModel.initMediaState(uri.toString())
        if (isCurrentUriIsFromIntent && playerApi.hasPosition && viewModel.currentPlaybackPosition == null) {
            viewModel.currentPlaybackPosition = playerApi.position?.toLong()
        }

        // Get all subtitles for current uri
        val apiSubs = if (isCurrentUriIsFromIntent) playerApi.getSubs() else emptyList()
        val localSubs = uri.getLocalSubtitles(currentContext, viewModel.externalSubtitles.toList())
        val externalSubs = viewModel.externalSubtitles.map { it.toSubtitle(currentContext) }

        // current uri as MediaItem with subs
        val subtitleStreams = createExternalSubtitleStreams(apiSubs + localSubs + externalSubs)
        val mediaStream = createMediaStream(uri).buildUpon()
            .setSubtitleConfigurations(subtitleStreams)
            .build()

        withContext(Dispatchers.Main) {
            surfaceView = SurfaceView(this@PlayerActivity).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
            player.setVideoSurfaceView(surfaceView)
            exoContentFrameLayout.addView(surfaceView, 0)

            if (isCurrentUriIsFromIntent && playerApi.hasTitle) {
                videoTitleTextView.text = playerApi.title
            } else {
                videoTitleTextView.text = getFilenameFromUri(uri)
            }

            // Set media and start player
            player.setMediaItem(mediaStream, viewModel.currentPlaybackPosition ?: C.TIME_UNSET)
            player.playWhenReady = playWhenReady
            player.prepare()
        }
    }

    private fun releasePlayer() {
        Timber.d("Releasing player")
        subtitleCacheDir.deleteFiles()
        playWhenReady = player.playWhenReady
        playlistManager.getCurrent()?.let { savePlayerState(it) }
        player.removeListener(playbackStateListener)
        player.release()
        mediaSession?.release()
        mediaSession = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            binding.playerView.keepScreenOn = isPlaying
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            super.onAudioSessionIdChanged(audioSessionId)
            loudnessEnhancer?.release()

            try {
                loudnessEnhancer = LoudnessEnhancer(audioSessionId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @SuppressLint("SourceLockedOrientationActivity")
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            currentVideoSize = videoSize
            applyVideoZoom(videoZoom = playerPreferences.playerVideoZoom, showInfo = false)
            exoContentFrameLayout.scaleX = viewModel.currentVideoScale
            exoContentFrameLayout.scaleY = viewModel.currentVideoScale
            exoContentFrameLayout.requestLayout()

            if (currentOrientation != null) return

            if (playerPreferences.playerScreenOrientation == ScreenOrientation.VIDEO_ORIENTATION &&
                videoSize.width != 0 &&
                videoSize.height != 0
            ) {
                requestedOrientation = when {
                    videoSize.isPortrait -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                    else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }
            super.onVideoSizeChanged(videoSize)
        }

        override fun onPlayerError(error: PlaybackException) {
            Timber.e(error)
            val alertDialog = MaterialAlertDialogBuilder(this@PlayerActivity).apply {
                setTitle(getString(coreUiR.string.error_playing_video))
                setMessage(error.message ?: getString(coreUiR.string.unknown_error))
                setNegativeButton(getString(coreUiR.string.exit)) { _, _ ->
                    finish()
                }
                if (playlistManager.hasNext()) {
                    setPositiveButton(getString(coreUiR.string.play_next_video)) { dialog, _ ->
                        dialog.dismiss()
                        playVideo(playlistManager.getNext()!!)
                    }
                }
            }.create()

            alertDialog.show()
            super.onPlayerError(error)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    Timber.d("Player state: ENDED")
                    isPlaybackFinished = true
                    if (playlistManager.hasNext() && playerPreferences.autoplay) {
                        playlistManager.getCurrent()?.let { savePlayerState(it) }
                        viewModel.resetAllToDefaults()
                        playVideo(playlistManager.getNext()!!)
                    } else {
                        finish()
                    }
                }

                Player.STATE_READY -> {
                    Timber.d("Player state: READY")
                    Timber.d(playlistManager.toString())
                    isFrameRendered = true
                    isFileLoaded = true
                }

                Player.STATE_BUFFERING -> {
                    Timber.d("Player state: BUFFERING")
                }

                Player.STATE_IDLE -> {
                    Timber.d("Player state: IDLE")
                }
            }
            super.onPlaybackStateChanged(playbackState)
        }

        override fun onRenderedFirstFrame() {
            isFirstFrameRendered = true
            binding.playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            super.onRenderedFirstFrame()
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            if (isFirstFrameRendered) return

            if (isSubtitleLauncherHasUri) {
                val textTracks = player.currentTracks.groups
                    .filter { it.type == C.TRACK_TYPE_TEXT && it.isSupported }
                viewModel.currentSubtitleTrackIndex = textTracks.size - 1
            }
            isSubtitleLauncherHasUri = false
            player.switchTrack(C.TRACK_TYPE_AUDIO, viewModel.currentAudioTrackIndex)
            player.switchTrack(C.TRACK_TYPE_TEXT, viewModel.currentSubtitleTrackIndex)
            player.setPlaybackSpeed(viewModel.currentPlaybackSpeed)
            player.skipSilenceEnabled = viewModel.skipSilenceEnabled
        }
    }

    override fun finish() {
        if (playerApi.shouldReturnResult) {
            val result = playerApi.getResult(
                isPlaybackFinished = isPlaybackFinished,
                duration = player.duration,
                position = player.currentPosition,
            )
            setResult(Activity.RESULT_OK, result)
        }
        super.finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        playlistManager.clearQueue()
        viewModel.resetAllToDefaults()
        setIntent(intent)
        prettyPrintIntent()
        shouldFetchPlaylist = true
        playVideo(intent.data!!)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP,
                -> {
                if (::adapter.isInitialized) {
                    adapter.toggleFavoriteDown(recyclerView)
                    return true
                } else if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    volumeManager.increaseVolume(playerPreferences.showSystemVolumePanel)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_VOLUME_UP,
                -> {
                if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    volumeManager.increaseVolume(playerPreferences.showSystemVolumePanel)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_DOWN,
                -> {
                if (::adapter.isInitialized) {
                    adapter.toggleFavoriteUp(recyclerView)
                    return true
                } else if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volumeManager.decreaseVolume(playerPreferences.showSystemVolumePanel)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_VOLUME_DOWN,
                -> {
                if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volumeManager.decreaseVolume(playerPreferences.showSystemVolumePanel)
                    showVolumeGestureLayout()
                    return true
                }
            }

            KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_PAUSE,
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_BUTTON_SELECT,
                -> {
                when {
                    keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE -> player.pause()
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY -> player.play()
                    player.isPlaying -> player.pause()
                    else -> player.play()
                }
                return true
            }

            KeyEvent.KEYCODE_BUTTON_START,
            KeyEvent.KEYCODE_BUTTON_A,
            KeyEvent.KEYCODE_SPACE,
                -> {
                if (!binding.playerView.isControllerFullyVisible) {
                    binding.playerView.togglePlayPause()
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_BUTTON_L2,
            KeyEvent.KEYCODE_MEDIA_REWIND,
                -> {
                if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                    val pos = player.currentPosition
                    if (scrubStartPosition == -1L) {
                        scrubStartPosition = pos
                    }
                    val position = (pos - 10_000).coerceAtLeast(0L)
                    player.seekBack(position, shouldFastSeek)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_BUTTON_R2,
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                -> {
                if (!binding.playerView.isControllerFullyVisible || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                    val pos = player.currentPosition
                    if (scrubStartPosition == -1L) {
                        scrubStartPosition = pos
                    }

                    val position = (pos + 10_000).coerceAtMost(player.duration)
                    player.seekForward(position, shouldFastSeek)
                    showPlayerInfo(
                        info = Utils.formatDurationMillis(position),
                        subInfo = "[${Utils.formatDurationMillisSign(position - scrubStartPosition)}]",
                    )
                    return true
                }
            }

            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER,
                -> {
                if (::adapter.isInitialized) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastOkClickTime < DOUBLE_CLICK_TIME_DELTA) {
                        adapter.openFavorite(recyclerView)
                    } else {
                        adapter.selectedChannel(this, recyclerView)
                    }
                    lastOkClickTime = currentTime
                    return true
                } else if (!binding.playerView.isControllerFullyVisible) {
                    binding.playerView.showController()
                    return true
                }
            }

            KeyEvent.KEYCODE_BACK -> {
                if (binding.playerView.isControllerFullyVisible && player.isPlaying && isDeviceTvBox()) {
                    binding.playerView.hideController()
                    return true
                } else {
                    finish()
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
                -> {
                hideVolumeGestureLayout()
                return true
            }

            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_BUTTON_L2,
            KeyEvent.KEYCODE_MEDIA_REWIND,
            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_BUTTON_R2,
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD,
                -> {
                hidePlayerInfo()
                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    }

    private fun scrub(position: Long) {
        if (isFrameRendered) {
            isFrameRendered = false
            if (position > previousScrubPosition) {
                player.seekForward(position, shouldFastSeek)
            } else {
                player.seekBack(position, shouldFastSeek)
            }
            previousScrubPosition = position
        }
    }

    fun showHidePlayerGesture() {
        playerGestureHelper.showHide()
    }

    fun showVolumeGestureLayout() {
        hideVolumeIndicatorJob?.cancel()
        with(binding) {
            volumeGestureLayout.visibility = View.VISIBLE
            volumeProgressBar.max = volumeManager.maxVolume.times(100)
            volumeProgressBar.progress = volumeManager.currentVolume.times(100).toInt()
            volumeProgressText.text = volumeManager.volumePercentage.toString()
        }
    }

    fun showBrightnessGestureLayout() {
        hideBrightnessIndicatorJob?.cancel()
        with(binding) {
            brightnessGestureLayout.visibility = View.VISIBLE
            brightnessProgressBar.max = brightnessManager.maxBrightness.times(100).toInt()
            brightnessProgressBar.progress = brightnessManager.currentBrightness.times(100).toInt()
            brightnessProgressText.text = brightnessManager.brightnessPercentage.toString()
        }
    }

    fun showPlayerInfo(info: String, subInfo: String? = null) {
        hideInfoLayoutJob?.cancel()
        with(binding) {
            infoLayout.visibility = View.VISIBLE
            infoText.text = info
            infoSubtext.visibility = View.GONE.takeIf { subInfo == null } ?: View.VISIBLE
            infoSubtext.text = subInfo
        }
    }

    fun showTopInfo(info: String) {
        with(binding) {
            topInfoLayout.visibility = View.VISIBLE
            topInfoText.text = info
        }
    }

    fun hideVolumeGestureLayout(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (binding.volumeGestureLayout.visibility != View.VISIBLE) return
        hideVolumeIndicatorJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            binding.volumeGestureLayout.visibility = View.GONE
        }
    }

    fun hideBrightnessGestureLayout(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (binding.brightnessGestureLayout.visibility != View.VISIBLE) return
        hideBrightnessIndicatorJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            binding.brightnessGestureLayout.visibility = View.GONE
        }
        if (playerPreferences.rememberPlayerBrightness) {
            viewModel.setPlayerBrightness(window.attributes.screenBrightness)
        }
    }

    fun hidePlayerInfo(delayTimeMillis: Long = HIDE_DELAY_MILLIS) {
        if (binding.infoLayout.visibility != View.VISIBLE) return
        hideInfoLayoutJob = lifecycleScope.launch {
            delay(delayTimeMillis)
            binding.infoLayout.visibility = View.GONE
        }
    }

    fun hideTopInfo() {
        binding.topInfoLayout.visibility = View.GONE
    }

    private fun savePlayerState(uri: Uri) {
        if (isFirstFrameRendered) {
            viewModel.saveState(
                uri = uri,
                position = player.currentPosition,
                duration = player.duration,
                audioTrackIndex = player.getCurrentTrackIndex(C.TRACK_TYPE_AUDIO),
                subtitleTrackIndex = player.getCurrentTrackIndex(C.TRACK_TYPE_TEXT),
                playbackSpeed = player.playbackParameters.speed,
                skipSilence = player.skipSilenceEnabled,
                videoScale = exoContentFrameLayout.scaleX,
            )
        }
        isFirstFrameRendered = false
    }

    private fun createMediaStream(uri: Uri) = MediaItem.Builder()
        .setMediaId(uri.toString())
        .setUri(uri)
        .build()

    private suspend fun createExternalSubtitleStreams(subtitles: List<Subtitle>): List<MediaItem.SubtitleConfiguration> {
        return subtitles.map {
            val charset = if (with(playerPreferences.subtitleTextEncoding) { isNotEmpty() && Charset.isSupported(this) }) {
                Charset.forName(playerPreferences.subtitleTextEncoding)
            } else {
                null
            }
            MediaItem.SubtitleConfiguration.Builder(
                convertToUTF8(
                    uri = it.uri,
                    charset = charset,
                ),
            ).apply {
                setId(it.uri.toString())
                setMimeType(it.uri.getSubtitleMime())
                setLabel(it.name)
                if (it.isSelected) setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            }.build()
        }
    }

    private fun resetExoContentFrameWidthAndHeight() {
        exoContentFrameLayout.layoutParams.width = LayoutParams.MATCH_PARENT
        exoContentFrameLayout.layoutParams.height = LayoutParams.MATCH_PARENT
        exoContentFrameLayout.scaleX = 1.0f
        exoContentFrameLayout.scaleY = 1.0f
        exoContentFrameLayout.requestLayout()
    }

    private fun applyVideoZoom(videoZoom: VideoZoom, showInfo: Boolean) {
        viewModel.setVideoZoom(videoZoom)
        resetExoContentFrameWidthAndHeight()
        when (videoZoom) {
            VideoZoom.BEST_FIT -> {
                binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                videoZoomButton.setImageDrawable(this, coreUiR.drawable.ic_fit_screen)
            }

            VideoZoom.STRETCH -> {
                binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                videoZoomButton.setImageDrawable(this, coreUiR.drawable.ic_aspect_ratio)
            }

            VideoZoom.CROP -> {
                binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                videoZoomButton.setImageDrawable(this, coreUiR.drawable.ic_crop_landscape)
            }

            VideoZoom.HUNDRED_PERCENT -> {
                currentVideoSize?.let {
                    exoContentFrameLayout.layoutParams.width = it.width
                    exoContentFrameLayout.layoutParams.height = it.height
                    exoContentFrameLayout.requestLayout()
                }
                binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                videoZoomButton.setImageDrawable(this, coreUiR.drawable.ic_width_wide)
            }
        }
        if (showInfo) {
            lifecycleScope.launch {
                binding.infoLayout.visibility = View.VISIBLE
                binding.infoText.text = getString(videoZoom.nameRes())
                delay(HIDE_DELAY_MILLIS)
                binding.infoLayout.visibility = View.GONE
            }
        }
    }

    companion object {
        const val HIDE_DELAY_MILLIS = 1000L
    }
}

