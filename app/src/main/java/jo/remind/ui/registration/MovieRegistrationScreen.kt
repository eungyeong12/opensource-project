package jo.remind.ui.registration

import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jo.remind.R
import java.time.LocalDate

@Composable
fun MovieRegistrationTopBar(
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
                text = "영화 등록",
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
fun MovieRegistrationScreen(
    navController: NavHostController,
) {
    var title by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5.0f) }
    val today = LocalDate.now()
    var year by remember { mutableStateOf(today.year) }
    var month by remember { mutableStateOf(today.monthValue) }
    var day by remember { mutableStateOf(today.dayOfMonth) }

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(Color(0xFFE9E9E9))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val hasImage = imageUri.value != null

                imageUri.value?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(uri)
                                .crossfade(true)
                                .size(coil.size.Size.ORIGINAL)
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

                MovieRegistrationTopBar(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(top = 36.dp, start = 16.dp, end = 16.dp),
                    textColor = if (hasImage) Color.White else Color.Black
                )

                Text(
                    text = "+",
                    fontSize = 70.sp,
                    color = if (hasImage) Color.White else Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            launcher.launch("image/*")
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LabeledInput(label = "영화 제목", value = title) { title = it }
                LabeledInput(label = "영화 감독", value = director) { director = it }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "개봉일",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.width(38.dp))

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        DatePicker(
                            year = year,
                            month = month,
                            day = day
                        ) { y, m, d ->
                            year = y
                            month = m
                            day = d
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "평점",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(42.dp))
                    RatingBar(
                        rating = rating,
                        onRatingChanged = { rating = it }
                    )
                }
            }
        }
    }
}

@Composable
fun LabeledInput(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 12.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFF6F9F2), shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            var isFocused by remember { mutableStateOf(false) }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && !isFocused) {
                        Text(
                            text = "${label.substring(3)}을 입력해주세요",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun DatePicker(
    year: Int,
    month: Int,
    day: Int,
    onDateChanged: (Int, Int, Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            val dp8 = (8 * context.resources.displayMetrics.density).toInt()

            LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER

                val yearPicker = NumberPicker(context).apply {
                    minValue = 1800
                    maxValue = 2125
                    value = year
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(newVal, month, day)
                    }
                }

                val monthPicker = NumberPicker(context).apply {
                    minValue = 1
                    maxValue = 12
                    value = month
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(year, newVal, day)
                    }
                }

                val dayPicker = NumberPicker(context).apply {
                    minValue = 1
                    maxValue = 31
                    value = day
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(year, month, newVal)
                    }
                }

                val spacer1 = View(context).apply {
                    layoutParams = LinearLayout.LayoutParams(dp8, 0)
                }

                val spacer2 = View(context).apply {
                    layoutParams = LinearLayout.LayoutParams(dp8, 0)
                }

                addView(yearPicker)
                addView(spacer1)
                addView(monthPicker)
                addView(spacer2)
                addView(dayPicker)
            }
        }
    )
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
) {
    AndroidView(
        factory = { context ->
            android.widget.RatingBar(context, null, android.R.attr.ratingBarStyleIndicator).apply {
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
