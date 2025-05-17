package jo.remind.ui.record

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import jo.remind.R
import jo.remind.data.model.record.DailyRecord
import jo.remind.ui.RemindNavigation
import jo.remind.ui.RemindNavigationActions
import jo.remind.ui.registration.DatePicker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.text.BasicTextField as BasicTextField1

@Composable
fun DailyRecordTopBar(
    dateText: String,
    onClickBack: () -> Unit,
    onClickSave: () -> Unit,
    onClickDate: () -> Unit
) {
    Column(
        modifier = Modifier
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
                painter = painterResource(id = R.drawable.prev),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClickBack() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = dateText,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClickDate()
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "저장",
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClickSave()
                    }
            )
        }
    }
}

@Composable
fun DailyRecordScreen(navController: NavHostController) {
    var memoText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val dateArg = navController.currentBackStackEntry?.arguments?.getString("date")
    var selectedDate by remember {
        mutableStateOf(
            try {
                LocalDate.parse(dateArg)
            } catch (e: Exception) {
                LocalDate.now()
            }
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val imageSlots = remember { mutableStateListOf<Uri?>(null, null, null, null, null) }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = bottomPadding + 40.dp)
    ) {
        DailyRecordTopBar(
            dateText = selectedDate.format(DateTimeFormatter.ofPattern("yyyy.M.d")),
            onClickBack = { navController.popBackStack() },
            onClickSave = {
                if (memoText.isBlank()) {
                    Toast.makeText(context, "내용을 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return@DailyRecordTopBar
                }
                isSaving = true

                val firestore = Firebase.firestore
                val userId = Firebase.auth.currentUser?.uid ?: return@DailyRecordTopBar
                val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                val now = LocalDateTime.now().format(dateFormat)

                val record = DailyRecord(
                    date = selectedDate.toString(),
                    memo = memoText,
                    imageUrls = imageSlots.filterNotNull().map { it.toString() },
                    uploadedAt = now
                )

                val navActions = RemindNavigationActions(navController)

                firestore.collection("users")
                    .document(userId)
                    .collection("records")
                    .document(now)
                    .collection("daily")
                    .document()
                    .set(record)
                    .addOnSuccessListener {
                        isSaving = false
                        navActions.navigateTo(RemindNavigation.Home)
                    }
                    .addOnFailureListener {
                        isSaving = false
                        Toast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            },
            onClickDate = { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                initialDate = selectedDate,
                onDismissRequest = { showDatePicker = false },
                onDateSelected = {
                    selectedDate = it
                    showDatePicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ImageCarouselSection(imageSlots = imageSlots)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF6F9F2)),
            contentAlignment = Alignment.TopStart
        ) {
            if (memoText.isEmpty()) {
                Text(
                    text = "무슨 이야기든 자유롭게 작성해보세요!",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            BasicTextField1(
                value = memoText,
                onValueChange = { memoText = it },
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )

            if (isSaving) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCarouselSection(imageSlots: MutableList<Uri?>) {
    var selectedIndex by remember { mutableStateOf<Int?>(0) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && selectedIndex != null && selectedIndex in imageSlots.indices) {
            imageSlots[selectedIndex!!] = uri
            val nextIndex = imageSlots.indexOfFirst { it == null }
            selectedIndex = if (nextIndex != -1) nextIndex else null
        }
    }

    Column(horizontalAlignment = Alignment.Start) {
        LazyRow(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(155.dp)
        ) {
            items(imageSlots.size) { index ->
                val imageUri = imageSlots[index]
                val isSelected = selectedIndex != null && selectedIndex == index
                val backgroundColor = if (imageUri != null) Color.White else Color(0xFFEDEDED)

                val width = if (isSelected) 118.dp else 106.dp
                val height = if (isSelected) 150.dp else 138.dp

                Box(
                    modifier = Modifier
                        .height(155.dp)
                        .width(width),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(height)
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(backgroundColor)
                            .clickable {
                                selectedIndex = index
                                launcher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Box {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "선택된 이미지",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            imageSlots[index] = null
                                            selectedIndex = index
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_cancel_24),
                                        contentDescription = "삭제",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        } else if (isSelected) {
                            Image(
                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "이미지 추가",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            imageSlots.forEachIndexed { index, _ ->
                val isSelected = selectedIndex != null && selectedIndex == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isSelected) Color.Black else Color.LightGray)
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val year = initialDate.year
    val month = initialDate.monthValue
    val day = initialDate.dayOfMonth

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            var selectedYear by remember { mutableStateOf(year) }
            var selectedMonth by remember { mutableStateOf(month) }
            var selectedDay by remember { mutableStateOf(day) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DatePicker(
                    year = selectedYear,
                    month = selectedMonth,
                    day = selectedDay
                ) { y, m, d ->
                    selectedYear = y
                    selectedMonth = m
                    selectedDay = d
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "취소",
                        modifier = Modifier
                            .padding(end = 54.dp)
                            .clickable { onDismissRequest() },
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "확인",
                        modifier = Modifier
                            .clickable {
                                val selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                                onDateSelected(selectedDate)
                            },
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}