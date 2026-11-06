package com.ultracut.pro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UltraCutApp()
        }
    }
}

@Composable
fun UltraCutApp() {
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        videoUri = uri
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101018))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ULTRACUT PRO",
                color = Color(0xFF00E5FF),
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Professional Video Editor",
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { picker.launch("video/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SELECT VIDEO")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (videoUri != null) {
                VideoPreview(videoUri!!)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Color(0xFF252535),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Your video preview will appear here",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("EDITING TOOLS", color = Color(0xFF00E5FF))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Trim  •  Split  •  Audio  •  Text\n\nEffects  •  Filters  •  Transitions",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun VideoPreview(uri: Uri) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}
