package jo.remind.ui.record

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jo.remind.R

@Composable
fun MovieRecordTopBar(
    onClick: () -> Unit,
    modifier: Modifier,
    textColor: Color
) {
    val backIcon = if (textColor == Color.White) {
        R.drawable.prev_white
    } else {
        R.drawable.prev
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.cancel),
                contentDescription = "취소",
                modifier = Modifier
                    .size(16.dp)
                    .alpha(0f)
            )
        }

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = backIcon),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "영화 제목",
                color = textColor,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "저장",
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                    }
            )
        }

    }
}

@Composable
fun MovieRecordScreen(
    navController: NavHostController,
) {
    var rating by remember { mutableStateOf(5.0f) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var memoText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = bottomPadding + 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(Color(0xFFE9E9E9))
        ) {
            val hasImage = imageUri != null

            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(uri)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "선택된 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }

            MovieRecordTopBar(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(top = 36.dp, start = 16.dp, end = 16.dp),
                textColor = if (hasImage) Color.White else Color.Black
            )

            Text(
                text = "+",
                fontSize = 60.sp,
                color = if (hasImage) Color.White else Color.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        launcher.launch("image/*")
                    }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .border(2.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                MovieRatingBar(rating = rating, onRatingChanged = { rating = it })
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF6F9F2)),
            contentAlignment = Alignment.TopStart
        ) {
            if (memoText.isEmpty()) {
                Text(
                    text = "가장 기억에 남는 장면이나 대사를 기록해보세요.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            BasicTextField(
                value = memoText,
                onValueChange = { memoText = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun MovieRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
) {
    AndroidView(
        factory = { context ->
            android.widget.RatingBar(context, null, android.R.attr.ratingBarStyleSmall).apply {
                numStars = 5
                stepSize = 0.5f
                this.rating = rating
                setIsIndicator(false)

                progressDrawable.setTint(android.graphics.Color.BLACK)

                scaleX = 0.9f
                scaleY = 0.9f

                setOnRatingBarChangeListener { _, newRating, _ ->
                    onRatingChanged(newRating)
                }
            }
        },
        update = { rb ->
            if (rb.rating != rating) {
                rb.rating = rating
            }
        }
    )
}
