package jo.remind.ui.registration

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import jo.remind.R
import jo.remind.data.model.record.BookRecord
import jo.remind.data.model.record.MovieRecord
import jo.remind.ui.RemindNavigation
import jo.remind.ui.search.formatDateToDotPattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookRegistrationTopBar(
    onClick: () -> Unit,
    onClickSave: () -> Unit,
    modifier: Modifier,
    textColor: Color,
    imageUri: Uri?,
    title: String,
    writer: String
) {
    val context = LocalContext.current
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
                text = "책 등록",
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
                        if (imageUri == null) {
                            Toast.makeText(context, "책 이미지를 추가해주세요.", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        if (title.isBlank()) {
                            Toast.makeText(context, "책 제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        if (writer.isBlank()) {
                            Toast.makeText(context, "책 저자를 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        onClickSave()
                    }
            )
        }

    }
}

@Composable
fun BookRegistrationScreen(
    navController: NavHostController,
) {
    val dateArg = navController.currentBackStackEntry?.arguments?.getString("date")
    val selectedDate = remember(dateArg) {
        try {
            LocalDate.parse(dateArg)
        } catch (e: Exception) {
            LocalDate.now()
        }
    }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
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

                BookRegistrationTopBar(
                    onClick = { navController.popBackStack() },
                    onClickSave = {
                        val publishDate = String.format("%04d-%02d-%02d", year, month, day)
                        val uploadedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

                        val record = BookRecord(
                            date = selectedDate.toString(),
                            title = title,
                            writer = author,
                            publishDate = publishDate,
                            imageUrl = imageUri.value?.toString() ?: "",
                            memo = "",
                            rating = rating,
                            uploadedAt = uploadedAt
                        )

                        navController.currentBackStackEntry?.savedStateHandle?.set("bookRecord", record)
                        navController.navigate(RemindNavigation.BookRecord.route)
                    },
                    modifier = Modifier
                        .padding(top = 36.dp, start = 16.dp, end = 16.dp),
                    textColor = if (hasImage) Color.White else Color.Black,
                    imageUri = imageUri.value,
                    title = title,
                    writer = author
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
                LabeledInput(label = "도서 제목", value = title) { title = it }
                LabeledInput(label = "도서 저자", value = author) { author = it }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "출판일",
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