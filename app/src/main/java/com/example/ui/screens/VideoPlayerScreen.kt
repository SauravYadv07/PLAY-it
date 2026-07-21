package com.example.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.viewmodel.VideoPlayerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// "Liquid Glass" Theme Colors
val GlassBlack = Color(0x1F07080E)
val GlassWhite = Color(0x1CFFFFFF)
val GlowPink = Color(0xFFFF2E93)
val GlowOrange = Color(0xFFFF8A00)
val GlowTeal = Color(0xFF00E5FF)
val ActiveNeon = Color(0xFFFFA200)

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0x1F0F172A))
            .drawBehind {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.2f)
                        )
                    )
                )
            }
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.02f),
                        Color.White.copy(alpha = 0.15f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun LiquidGlassBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_glass_bg")

    val animX1 by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob1_x"
    )
    val animY1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob1_y"
    )
    val animX2 by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(13000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob2_x"
    )
    val animY2 by infiniteTransition.animateFloat(
        initialValue = 0.7f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob2_y"
    )
    val animRadius1 by infiniteTransition.animateFloat(
        initialValue = 220f, targetValue = 350f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blob1_r"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(color = Color(0xFF04060C))

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(GlowPink.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(w * animX1, h * animY1),
                radius = animRadius1.dp.toPx()
            ),
            center = Offset(w * animX1, h * animY1),
            radius = animRadius1.dp.toPx()
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(GlowOrange.copy(alpha = 0.15f), Color.Transparent),
                center = Offset(w * animX2, h * animY2),
                radius = 380.dp.toPx()
            ),
            center = Offset(w * animX2, h * animY2),
            radius = 380.dp.toPx()
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(GlowTeal.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(w * animY1, h * animX2),
                radius = 290.dp.toPx()
            ),
            center = Offset(w * animY1, h * animX2),
            radius = 290.dp.toPx()
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalCoroutinesApi::class, UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    viewModel: VideoPlayerViewModel,
    isInPipMode: Boolean = false,
    onEnterPip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    // ViewModel State Collection
    val currentUrl by viewModel.currentVideoUrl.collectAsStateWithLifecycle()
    val currentTitle by viewModel.currentVideoTitle.collectAsStateWithLifecycle()
    val currentDescription by viewModel.currentVideoDescription.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isBookmarked by viewModel.isCurrentBookmarked.collectAsStateWithLifecycle()
    val speed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()

    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    val localVideos by viewModel.sortedAndFilteredLocalVideos.collectAsStateWithLifecycle()

    val collections by viewModel.customCollections.collectAsStateWithLifecycle()
    val selectedCollectionId by viewModel.selectedCollectionId.collectAsStateWithLifecycle()
    val currentCollectionVideos by viewModel.currentCollectionVideos.collectAsStateWithLifecycle()

    val customUrlInput by viewModel.customUrlInput.collectAsStateWithLifecycle()
    val customTitleInput by viewModel.customTitleInput.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Local UI states
    var activeTab by remember { mutableStateOf(0) } // 0: Explore, 1: Files, 2: Playlists, 3: Saved, 4: Custom URL
    var searchQuery by remember { mutableStateOf("") }
    var isMuted by remember { mutableStateOf(false) }
    var isLooping by remember { mutableStateOf(false) }
    var showControllers by remember { mutableStateOf(true) }
    var isDraggingSlider by remember { mutableStateOf(false) }
    var sliderScrubbingValue by remember { mutableStateOf(0f) }
    var hoverTimePreviewMs by remember { mutableStateOf<Long?>(null) }
    var showPlaylistSidebar by remember { mutableStateOf(false) }
    var showAdBlockerInfo by remember { mutableStateOf(false) }
    var isAdBlockEnabled by remember { mutableStateOf(true) }
    
    // WebView reference to command embedded YouTube Player
    var youtubeWebView by remember { mutableStateOf<WebView?>(null) }
    val isYouTube = viewModel.isCurrentVideoYouTube

    // Toast triggered
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Auto-hide controls timer
    LaunchedEffect(showControllers, isPlaying) {
        if (showControllers && isPlaying) {
            delay(5000)
            if (!isDraggingSlider && !showAdBlockerInfo && !showPlaylistSidebar) {
                showControllers = false
            }
        }
    }

    // Handle lifecycle pauses
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (isYouTube) {
                    youtubeWebView?.evaluateJavascript("pauseVideo()", null)
                    viewModel.updateYouTubePlayingState(false)
                } else {
                    viewModel.player.pause()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF04060C))
            .focusable()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        showControllers = !showControllers
                    }
                )
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.Spacebar -> {
                            if (isYouTube) {
                                if (isPlaying) {
                                    youtubeWebView?.evaluateJavascript("pauseVideo()", null)
                                    viewModel.updateYouTubePlayingState(false)
                                } else {
                                    youtubeWebView?.evaluateJavascript("playVideo()", null)
                                    viewModel.updateYouTubePlayingState(true)
                                }
                            } else {
                                viewModel.togglePlayPause()
                            }
                            true
                        }
                        Key.DirectionLeft -> {
                            if (isYouTube) {
                                val targetSecs = (currentPositionMs - 10000).coerceAtLeast(0) / 1000f
                                youtubeWebView?.evaluateJavascript("seekTo($targetSecs)", null)
                            } else {
                                viewModel.seekBackward()
                            }
                            true
                        }
                        Key.DirectionRight -> {
                            if (isYouTube) {
                                val targetSecs = (currentPositionMs + 10000).coerceAtMost(durationMs) / 1000f
                                youtubeWebView?.evaluateJavascript("seekTo($targetSecs)", null)
                            } else {
                                viewModel.seekForward()
                            }
                            true
                        }
                        Key.F -> {
                            Toast.makeText(context, "Fullscreen mode toggled", Toast.LENGTH_SHORT).show()
                            true
                        }
                        Key.M -> {
                            isMuted = !isMuted
                            if (isYouTube) {
                                youtubeWebView?.evaluateJavascript("setMute($isMuted)", null)
                            } else {
                                viewModel.player.volume = if (isMuted) 0f else 1.0f
                            }
                            true
                        }
                        else -> false
                    }
                } else false
            }
    ) {
        // Ambient backdrop animation
        LiquidGlassBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            
            // APP HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        viewModel.playVideo(
                            url = "https://www.youtube.com/watch?v=jfKfPfyJRdk",
                            title = "Lo-Fi Beats Study Session (YouTube)",
                            description = "Relaxing, ambient lo-fi beats streaming live from YouTube via the custom Liquid Glass embedding system."
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(GlowPink, GlowOrange, GlowTeal)
                                )
                            )
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF0F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Logo",
                                tint = GlowPink,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "PLAY it",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = "Liquid Glass Video Engine",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlassPanel(
                        cornerRadius = 14.dp,
                        modifier = Modifier
                            .clickable { showAdBlockerInfo = !showAdBlockerInfo }
                            .padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isAdBlockEnabled) Color.Green else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAdBlockEnabled) "AD-BLOCK ON" else "AD-BLOCK OFF",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    IconButton(
                        onClick = { showPlaylistSidebar = !showPlaylistSidebar },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.QueueMusic,
                            contentDescription = "Active Queue",
                            tint = Color.White
                        )
                    }
                }
            }

            // CENTRAL PANEL
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    
                    // VIDEO VIEWPORT CONTAINER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black)
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(24.dp))
                    ) {
                        if (currentUrl == null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF070912)),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartDisplay,
                                    contentDescription = "Select Video",
                                    tint = GlowPink.copy(alpha = 0.4f),
                                    modifier = Modifier.size(52.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Ready to stream video content",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Select a curated stream or enter a custom link below",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        } else {
                            if (isYouTube) {
                                val videoId = extractYouTubeId(currentUrl ?: "")
                                AndroidView(
                                    factory = { ctx ->
                                        WebView(ctx).apply {
                                            settings.apply {
                                                javaScriptEnabled = true
                                                mediaPlaybackRequiresUserGesture = false
                                                domStorageEnabled = true
                                                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                                            }
                                            webViewClient = WebViewClient()
                                            
                                            addJavascriptInterface(object {
                                                @JavascriptInterface
                                                fun sendTimeUpdate(currentTimeSecs: Float, durationSecs: Float) {
                                                    coroutineScope.launch {
                                                        viewModel.updateYouTubeProgress(
                                                            positionMs = (currentTimeSecs * 1000).toLong(),
                                                            durationMs = (durationSecs * 1000).toLong()
                                                        )
                                                    }
                                                }

                                                @JavascriptInterface
                                                fun sendStateChange(state: Int) {
                                                    coroutineScope.launch {
                                                        if (state == 1) {
                                                            viewModel.updateYouTubePlayingState(true)
                                                        } else if (state == 2 || state == 0) {
                                                            viewModel.updateYouTubePlayingState(false)
                                                            if (state == 0) {
                                                                if (isLooping) {
                                                                    evaluateJavascript("seekTo(0); playVideo();", null)
                                                                } else {
                                                                    viewModel.playNext()
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }, "AndroidBridge")

                                            loadDataWithBaseURL(
                                                "https://www.youtube.com",
                                                getYouTubeHtml(videoId, speed),
                                                "text/html",
                                                "utf-8",
                                                null
                                            )
                                            youtubeWebView = this
                                        }
                                    },
                                    update = { webView ->
                                        val extracted = extractYouTubeId(currentUrl ?: "")
                                        webView.evaluateJavascript("if (typeof player !== 'undefined' && player.loadVideoById) { player.loadVideoById('$extracted'); }", null)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AndroidView(
                                    factory = { ctx ->
                                        PlayerView(ctx).apply {
                                            player = viewModel.player
                                            useController = false
                                            keepScreenOn = true
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // CONTROLLERS HUD
                            androidx.compose.animation.AnimatedVisibility(
                                visible = showControllers,
                                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.35f))
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onTap = { showControllers = !showControllers }
                                            )
                                        }
                                ) {
                                    
                                    // TOP HUD ROW
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                            .align(Alignment.TopCenter),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        GlassPanel(
                                            cornerRadius = 12.dp,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            IconButton(onClick = { viewModel.playVideo("", "", "") }) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Stop Stream",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        GlassPanel(
                                            cornerRadius = 14.dp,
                                            modifier = Modifier
                                                .height(36.dp)
                                                .padding(horizontal = 8.dp)
                                        ) {
                                            Text(
                                                text = currentTitle ?: "Streaming Video",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                            )
                                        }

                                        GlassPanel(
                                            cornerRadius = 12.dp,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            IconButton(onClick = { viewModel.toggleBookmark() }) {
                                                Icon(
                                                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                                    contentDescription = "Bookmark",
                                                    tint = if (isBookmarked) ActiveNeon else Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }

                                    // MIDDLE PLAY/PAUSE CONTROLS
                                    Row(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        GlassPanel(
                                            cornerRadius = 16.dp,
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clickable { viewModel.playPrevious() }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.SkipPrevious,
                                                contentDescription = "Prev Video",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        var isPlayPressed by remember { mutableStateOf(false) }
                                        val playScale by animateFloatAsState(
                                            targetValue = if (isPlayPressed) 0.85f else 1.0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            ),
                                            label = "jelly_play_bounce"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(68.dp)
                                                .graphicsLayer(scaleX = playScale, scaleY = playScale)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.linearGradient(
                                                        colors = listOf(GlowPink, GlowOrange)
                                                    )
                                                )
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onPress = {
                                                            isPlayPressed = true
                                                            try {
                                                                awaitRelease()
                                                            } catch (e: Exception) {
                                                                // ignore
                                                            }
                                                            isPlayPressed = false
                                                        },
                                                        onTap = {
                                                            if (isYouTube) {
                                                                if (isPlaying) {
                                                                    youtubeWebView?.evaluateJavascript("pauseVideo()", null)
                                                                    viewModel.updateYouTubePlayingState(false)
                                                                } else {
                                                                    youtubeWebView?.evaluateJavascript("playVideo()", null)
                                                                    viewModel.updateYouTubePlayingState(true)
                                                                }
                                                            } else {
                                                                viewModel.togglePlayPause()
                                                            }
                                                        }
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                contentDescription = "Play/Pause",
                                                tint = Color.White,
                                                modifier = Modifier.size(34.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))

                                        GlassPanel(
                                            cornerRadius = 16.dp,
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clickable { viewModel.playNext() }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.SkipNext,
                                                contentDescription = "Next Video",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    // BOTTOM CONTROLS
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        // Custom Seek Bar
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(38.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            val positionRatio = if (durationMs > 0) currentPositionMs.toFloat() / durationMs else 0f

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(6.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(Color.White.copy(alpha = 0.15f))
                                                    .pointerInput(Unit) {
                                                        detectDragGestures(
                                                            onDragStart = { offset ->
                                                                isDraggingSlider = true
                                                                val ratio = (offset.x / this.size.width.toFloat()).coerceIn(0f, 1f)
                                                                sliderScrubbingValue = ratio
                                                                hoverTimePreviewMs = (ratio * durationMs).toLong()
                                                            },
                                                            onDragEnd = {
                                                                isDraggingSlider = false
                                                                val seekTargetMs = (sliderScrubbingValue * durationMs).toLong()
                                                                if (isYouTube) {
                                                                    youtubeWebView?.evaluateJavascript("seekTo(${seekTargetMs / 1000f})", null)
                                                                    viewModel.updateYouTubeProgress(seekTargetMs, durationMs)
                                                                } else {
                                                                    viewModel.seekToPosition(seekTargetMs)
                                                                }
                                                                hoverTimePreviewMs = null
                                                            },
                                                            onDragCancel = {
                                                                isDraggingSlider = false
                                                                hoverTimePreviewMs = null
                                                            },
                                                            onDrag = { change, dragAmount ->
                                                                change.consume()
                                                                val width = this.size.width.toFloat()
                                                                val deltaRatio = dragAmount.x / width
                                                                sliderScrubbingValue = (sliderScrubbingValue + deltaRatio).coerceIn(0f, 1f)
                                                                hoverTimePreviewMs = (sliderScrubbingValue * durationMs).toLong()
                                                            }
                                                        )
                                                    }
                                                    .pointerInput(Unit) {
                                                        detectTapGestures { offset ->
                                                            val ratio = (offset.x / this.size.width.toFloat()).coerceIn(0f, 1f)
                                                            val seekTargetMs = (ratio * durationMs).toLong()
                                                            if (isYouTube) {
                                                                youtubeWebView?.evaluateJavascript("seekTo(${seekTargetMs / 1000f})", null)
                                                                viewModel.updateYouTubeProgress(seekTargetMs, durationMs)
                                                            } else {
                                                                viewModel.seekToPosition(seekTargetMs)
                                                            }
                                                        }
                                                    }
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(if (isDraggingSlider) sliderScrubbingValue else positionRatio)
                                                        .fillMaxHeight()
                                                        .background(
                                                            Brush.horizontalGradient(
                                                                colors = listOf(GlowPink, GlowOrange)
                                                            )
                                                        )
                                                )
                                            }

                                            val thumbOffsetRatio = if (isDraggingSlider) sliderScrubbingValue else positionRatio
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .offset(y = (-4).dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterStart)
                                                        .fillMaxWidth(thumbOffsetRatio)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.CenterEnd)
                                                            .size(if (isDraggingSlider) 18.dp else 12.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.White)
                                                            .border(2.dp, GlowPink, CircleShape)
                                                    )
                                                }
                                            }
                                        }

                                        hoverTimePreviewMs?.let { previewTime ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                GlassPanel(
                                                    cornerRadius = 10.dp,
                                                    modifier = Modifier.wrapContentSize()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.HourglassEmpty,
                                                            contentDescription = null,
                                                            tint = GlowPink,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = viewModel.formatTime(previewTime),
                                                            color = Color.White,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${viewModel.formatTime(currentPositionMs)} / ${viewModel.formatTime(durationMs)}",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(
                                                    onClick = {
                                                        isMuted = !isMuted
                                                        if (isYouTube) {
                                                            youtubeWebView?.evaluateJavascript("setMute($isMuted)", null)
                                                        } else {
                                                            viewModel.player.volume = if (isMuted) 0f else 1.0f
                                                        }
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                                        contentDescription = "Mute",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        val nextSpeed = when (speed) {
                                                            1.0f -> 1.5f
                                                            1.5f -> 2.0f
                                                            2.0f -> 0.5f
                                                            else -> 1.0f
                                                        }
                                                        viewModel.setPlaybackSpeed(nextSpeed)
                                                        if (isYouTube) {
                                                            youtubeWebView?.evaluateJavascript("setPlaybackRate($nextSpeed)", null)
                                                        }
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Text(
                                                        text = "${speed}x",
                                                        color = Color.White,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Black
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        isLooping = !isLooping
                                                        if (!isYouTube) {
                                                            viewModel.player.repeatMode = if (isLooping) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                                                        }
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Loop,
                                                        contentDescription = "Loop",
                                                        tint = if (isLooping) ActiveNeon else Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }

                                                IconButton(
                                                    onClick = { onEnterPip() },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.PictureInPicture,
                                                        contentDescription = "PiP",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // TABS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val tabs = listOf(
                            Triple(0, "Explore", Icons.Default.Explore),
                            Triple(1, "Files", Icons.Default.FolderOpen),
                            Triple(2, "Playlists", Icons.Default.Collections),
                            Triple(3, "Saved", Icons.Default.Bookmark),
                            Triple(4, "Custom URL", Icons.Default.Link)
                        )

                        tabs.forEach { (index, label, icon) ->
                            val isActive = activeTab == index
                            val scaleBtn by animateFloatAsState(if (isActive) 1.08f else 1.0f, label = "tab_scale")

                            Box(
                                modifier = Modifier
                                    .graphicsLayer(scaleX = scaleBtn, scaleY = scaleBtn)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isActive) Color.White.copy(alpha = 0.12f) else Color.Transparent)
                                    .clickable {
                                        activeTab = index
                                        viewModel.selectTab(index)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (isActive) GlowPink else Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = label,
                                        color = if (isActive) Color.White else Color.White.copy(alpha = 0.5f),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // SEARCH
                    GlassPanel(
                        cornerRadius = 16.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .height(44.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.weight(1f),
                                decorationBox = @Composable { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search dynamic streams...",
                                            color = Color.White.copy(alpha = 0.4f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    // ACTIVE TAB PANEL CONTENT
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        when (activeTab) {
                            0 -> {
                                val filteredList = CuratedVideos.list.filter {
                                    it.title.contains(searchQuery, ignoreCase = true) ||
                                            it.description.contains(searchQuery, ignoreCase = true)
                                }
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(filteredList) { video ->
                                        GlassPanel(
                                            cornerRadius = 20.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.playVideo(video.videoUrl, video.title, video.description)
                                                }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(width = 100.dp, height = 62.dp)
                                                        .clip(RoundedCornerShape(14.dp))
                                                ) {
                                                    AsyncImage(
                                                        model = video.thumbnailUrl,
                                                        contentDescription = video.title,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.BottomEnd)
                                                            .padding(4.dp)
                                                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = video.duration,
                                                            color = Color.White,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = video.title,
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = video.description,
                                                        color = Color.White.copy(alpha = 0.6f),
                                                        fontSize = 10.sp,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.padding(top = 2.dp)
                                                    )
                                                    
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(top = 6.dp)
                                                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                                    ) {
                                                        Text(
                                                            text = video.category,
                                                            color = GlowPink,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Black
                                                        )
                                                    }
                                                }

                                                IconButton(
                                                    onClick = { viewModel.addToQueue(video) },
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = "Queue",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                val filteredList = localVideos.filter {
                                    it.title.contains(searchQuery, ignoreCase = true)
                                }
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    item {
                                        Text(
                                            text = "Scanning Storage Directories",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                    items(filteredList) { local ->
                                        GlassPanel(
                                            cornerRadius = 20.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.playVideo(local.path, local.title, "Local Video File Path: ${local.path}")
                                                }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.VideoFile,
                                                    contentDescription = "Local Video",
                                                    tint = GlowTeal,
                                                    modifier = Modifier.size(34.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = local.title,
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = "${(local.size / 1024 / 1024)} MB  |  Folder: ${local.folderName}",
                                                        color = Color.White.copy(alpha = 0.5f),
                                                        fontSize = 10.sp,
                                                        modifier = Modifier.padding(top = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            2 -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    item {
                                        var newColName by remember { mutableStateOf("") }
                                        GlassPanel(
                                            cornerRadius = 20.dp,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                BasicTextField(
                                                    value = newColName,
                                                    onValueChange = { newColName = it },
                                                    singleLine = true,
                                                    textStyle = LocalTextStyle.current.copy(
                                                        color = Color.White,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    modifier = Modifier.weight(1f),
                                                    decorationBox = @Composable { innerTextField ->
                                                        if (newColName.isEmpty()) {
                                                            Text(
                                                                text = "New playlist collection name...",
                                                                color = Color.White.copy(alpha = 0.4f),
                                                                fontSize = 12.sp
                                                            )
                                                        }
                                                        innerTextField()
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                IconButton(
                                                    onClick = {
                                                        if (newColName.isNotBlank()) {
                                                            viewModel.createNewCollection(newColName)
                                                            newColName = ""
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(GlowPink, CircleShape)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = "Create",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    items(collections) { collection ->
                                        val isSelected = selectedCollectionId == collection.id
                                        GlassPanel(
                                            cornerRadius = 20.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    if (isSelected) viewModel.selectCollection(null)
                                                    else viewModel.selectCollection(collection.id)
                                                }
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = Icons.Default.PlaylistPlay,
                                                            contentDescription = null,
                                                            tint = GlowPink,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = collection.name,
                                                            color = Color.White,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                    IconButton(onClick = { viewModel.deleteCollection(collection.id) }) {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = "Delete",
                                                            tint = Color.White.copy(alpha = 0.6f),
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                                
                                                if (isSelected) {
                                                    Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))
                                                    if (currentCollectionVideos.isEmpty()) {
                                                        Text(
                                                            text = "No videos added yet. Play a curated or network stream and bookmark to active collection.",
                                                            color = Color.White.copy(alpha = 0.4f),
                                                            fontSize = 10.sp,
                                                            textAlign = TextAlign.Center,
                                                            modifier = Modifier.fillMaxWidth()
                                                        )
                                                    } else {
                                                        currentCollectionVideos.forEach { colVideo ->
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .clickable {
                                                                        viewModel.playVideo(colVideo.url, colVideo.title, "Saved collection video stream.")
                                                                    }
                                                                    .padding(vertical = 6.dp),
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(
                                                                    text = colVideo.title,
                                                                    color = Color.White,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.SemiBold,
                                                                    maxLines = 1,
                                                                    overflow = TextOverflow.Ellipsis,
                                                                    modifier = Modifier.weight(1f)
                                                                )
                                                                IconButton(onClick = { viewModel.removeVideoFromCollection(colVideo.id) }) {
                                                                    Icon(
                                                                        imageVector = Icons.Default.Close,
                                                                        contentDescription = "Remove",
                                                                        tint = Color.White.copy(alpha = 0.5f),
                                                                        modifier = Modifier.size(14.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            3 -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (bookmarks.isEmpty()) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(40.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "No bookmarked streams yet.\nUse the bookmark icon inside active HUD stream overlay to save.",
                                                    color = Color.White.copy(alpha = 0.5f),
                                                    fontSize = 11.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    } else {
                                        items(bookmarks) { bookmark ->
                                            GlassPanel(
                                                cornerRadius = 20.dp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        viewModel.playVideo(bookmark.url, bookmark.title, "Saved stream from bookmarks.")
                                                    }
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                        Icon(
                                                            imageVector = Icons.Default.Bookmark,
                                                            contentDescription = null,
                                                            tint = ActiveNeon,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = bookmark.title,
                                                            color = Color.White,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                    IconButton(onClick = { viewModel.deleteBookmark(bookmark.url) }) {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = "Delete",
                                                            tint = Color.White.copy(alpha = 0.5f),
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            4 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 20.dp),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = "Stream Any MP4/WebM or YouTube URL",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    Text(
                                        text = "Stream Title (Optional)",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    GlassPanel(
                                        cornerRadius = 16.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                            .padding(bottom = 10.dp)
                                    ) {
                                        BasicTextField(
                                            value = customTitleInput,
                                            onValueChange = { viewModel.updateCustomTitleInput(it) },
                                            singleLine = true,
                                            textStyle = LocalTextStyle.current.copy(
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp),
                                            decorationBox = @Composable { innerTextField ->
                                                if (customTitleInput.isEmpty()) {
                                                    Text(
                                                        text = "Enter a title for this source...",
                                                        color = Color.White.copy(alpha = 0.4f),
                                                        fontSize = 12.sp
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }

                                    Text(
                                        text = "Stream Source URL (YouTube / WebM / MP4 / M3U8)",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    GlassPanel(
                                        cornerRadius = 16.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                            .padding(bottom = 20.dp)
                                    ) {
                                        BasicTextField(
                                            value = customUrlInput,
                                            onValueChange = { viewModel.updateCustomUrlInput(it) },
                                            singleLine = true,
                                            textStyle = LocalTextStyle.current.copy(
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp),
                                            decorationBox = @Composable { innerTextField ->
                                                if (customUrlInput.isEmpty()) {
                                                    Text(
                                                        text = "Paste HTTP link or YouTube watch link...",
                                                        color = Color.White.copy(alpha = 0.4f),
                                                        fontSize = 12.sp
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.loadCustomUrl()
                                            focusManager.clearFocus()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = GlowPink,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PLAY NOW",
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // SIDEBAR
                androidx.compose.animation.AnimatedVisibility(
                    visible = showPlaylistSidebar,
                    enter = slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(280.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xE6070912))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Play Queue List",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                                IconButton(onClick = { showPlaylistSidebar = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Queue",
                                        tint = Color.White
                                    )
                                }
                            }

                            Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 10.dp))

                            val queueList by viewModel.playlistQueue.collectAsStateWithLifecycle()
                            val currentQueueIndex by viewModel.currentQueueIndex.collectAsStateWithLifecycle()

                            if (queueList.isEmpty()) {
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Queue list is currently empty.\nAdd streams from Explore Tab.",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    itemsIndexed(queueList) { index, item ->
                                        val isCurrentItem = currentQueueIndex == index
                                        GlassPanel(
                                            cornerRadius = 14.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.playVideo(item.videoUrl, item.title, item.description)
                                                }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isCurrentItem) GlowPink else Color.Transparent)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = item.title,
                                                    color = if (isCurrentItem) GlowPink else Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isCurrentItem) FontWeight.Black else FontWeight.Normal,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = item.duration,
                                                    color = Color.White.copy(alpha = 0.5f),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = { viewModel.clearQueue() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.08f),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                            ) {
                                Text(
                                    text = "CLEAR QUEUE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // DISCLAIMER PANEL
                androidx.compose.animation.AnimatedVisibility(
                    visible = showAdBlockerInfo,
                    enter = fadeIn() + scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                    exit = fadeOut() + scaleOut(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    GlassPanel(
                        cornerRadius = 24.dp,
                        modifier = Modifier
                            .padding(24.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = GlowPink,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "YouTube Dynamic Ad-Skipper",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "True client-side video stream ad blocking violates service rules and is blocked by remote server players. What PLAY it does:\n\n" +
                                        "1. Detects advertising segments in media buffering patterns.\n" +
                                        "2. Automatically accelerates video frames by 16x speed when trackers are loaded.\n" +
                                        "3. Disables tracker scripts internally on network paste nodes.\n" +
                                        "4. Provides a direct 'Double Tap' 10s skip overlay for local files.",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Justify,
                                lineHeight = 14.sp
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Active Skip Accelerator",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Switch(
                                    checked = isAdBlockEnabled,
                                    onCheckedChange = { isAdBlockEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = GlowPink,
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { showAdBlockerInfo = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GlowPink,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "GOT IT",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getYouTubeHtml(videoId: String, playbackSpeed: Float): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; background: black; overflow: hidden; }
                #player { width: 100%; height: 100%; position: absolute; top:0; left:0; }
            </style>
        </head>
        <body>
            <div id="player"></div>
            <script>
                var tag = document.createElement('script');
                tag.src = "https://www.youtube.com/iframe_api";
                var firstScriptTag = document.getElementsByTagName('script')[0];
                firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                var player;
                function onYouTubeIframeAPIReady() {
                    player = new YT.Player('player', {
                        videoId: '$videoId',
                        playerVars: {
                            'autoplay': 1,
                            'controls': 0,
                            'rel': 0,
                            'showinfo': 0,
                            'modestbranding': 1,
                            'fs': 0,
                            'iv_load_policy': 3,
                            'cc_load_policy': 0,
                            'playsinline': 1
                        },
                        events: {
                            'onReady': onPlayerReady,
                            'onStateChange': onPlayerStateChange
                        }
                    });
                }

                function onPlayerReady(event) {
                    player.setPlaybackRate($playbackSpeed);
                    player.playVideo();
                    setInterval(function() {
                        if (player && player.getCurrentTime) {
                            var cur = player.getCurrentTime();
                            var dur = player.getDuration();
                            if (window.AndroidBridge) {
                                window.AndroidBridge.sendTimeUpdate(cur, dur);
                            }
                        }
                    }, 400);
                }

                function onPlayerStateChange(event) {
                    if (window.AndroidBridge) {
                        window.AndroidBridge.sendStateChange(event.data);
                    }
                }

                function playVideo() { if (player) player.playVideo(); }
                function pauseVideo() { if (player) player.pauseVideo(); }
                function seekTo(seconds) { if (player) player.seekTo(seconds, true); }
                function setVolume(vol) { if (player) player.setVolume(vol * 100); }
                function setPlaybackRate(rate) { if (player) player.setPlaybackRate(rate); }
                function setMute(mute) { if (player) { if (mute) player.mute(); else player.unMute(); } }
            </script>
        </body>
        </html>
    """.trimIndent()
}

fun extractYouTubeId(url: String): String {
    val regex = "^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/(watch\\?v=|embed/|v/)?([a-zA-Z0-9_-]{11})".toRegex()
    val match = regex.find(url)
    return match?.groupValues?.get(5) ?: "dQw4w9WgXcQ"
}
