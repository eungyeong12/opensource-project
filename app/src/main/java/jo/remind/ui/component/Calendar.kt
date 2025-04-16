package jo.remind.ui.component

import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import jo.remind.R
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen() {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showNumberPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CalendarHeader(
            yearMonth = currentYearMonth,
            onPrev = { currentYearMonth = currentYearMonth.minusMonths(1) },
            onNext = { currentYearMonth = currentYearMonth.plusMonths(1) },
            onTextClick = { showNumberPicker = true }
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CalendarGrid(
                yearMonth = currentYearMonth,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )
        }
    }

    if (showNumberPicker) {
        NumberPickerDialog(
            initialYearMonth = currentYearMonth,
            onDismiss = { showNumberPicker = false },
            onConfirm = {
                currentYearMonth = it
                val newDay = selectedDate.dayOfMonth
                val maxDay = it.lengthOfMonth()
                selectedDate = it.atDay(minOf(newDay, maxDay))
                showNumberPicker = false
            }
        )
    }
}

@Composable
fun CalendarHeader(
    yearMonth: YearMonth,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onTextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.prev),
            contentDescription = "previous month",
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onPrev() }
        )

        Text(
            text = "${yearMonth.year}년 ${yearMonth.monthValue}월",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onTextClick() }
        )

        Image(
            painter = painterResource(R.drawable.next),
            contentDescription = "next month",
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onNext() }
        )
    }
}

@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7

    val dates = (0 until totalCells).map { index ->
        val dayOffset = index - firstDayOfWeek
        firstDayOfMonth.plusDays(dayOffset.toLong())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(Modifier.fillMaxWidth()) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        val weeks = dates.chunked(7)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            weeks.forEach { week ->
                Row() {
                    week.forEach { date ->
                        val isCurrentMonth = date.month == yearMonth.month
                        val isSelected = date == selectedDate

                        val backgroundColor = when {
                            isSelected && isCurrentMonth -> MaterialTheme.colorScheme.onPrimary
                            !isCurrentMonth -> Color(0x80A5A5A5)
                            else -> Color.Transparent
                        }

                        val borderColor = when {
                            !isCurrentMonth -> Color(0x809A9A9A)
                            else -> Color(0xFFDADADA)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.6f)
                                .clip(RoundedCornerShape(2.dp))
                                .background(backgroundColor)
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(0f, size.height),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (isCurrentMonth) onDateSelected(date)
                                },
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 6.dp, start = 8.dp),
                                color = if (isCurrentMonth) Color.Black else Color.Transparent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPickerDialog(
    initialYearMonth: YearMonth,
    onDismiss: () -> Unit,
    onConfirm: (YearMonth) -> Unit
) {
    var selectedYear by remember { mutableStateOf(initialYearMonth.year) }
    var selectedMonth by remember { mutableStateOf(initialYearMonth.monthValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onConfirm(YearMonth.of(selectedYear, selectedMonth))
                        }
                ) {
                    Text(text = "취소", color = Color(0xFF4CAF50))
                }
                Spacer(modifier = Modifier.width(64.dp))
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onConfirm(YearMonth.of(selectedYear, selectedMonth))
                        }
                ) {
                    Text(text = "확인", color = Color(0xFF4CAF50))
                }
            }
        },
        dismissButton = {},
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AndroidView(
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 1900
                            maxValue = 2125
                            value = selectedYear
                            setOnValueChangedListener { _, _, newVal ->
                                selectedYear = newVal
                            }
                        }
                    }
                )
                AndroidView(
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 1
                            maxValue = 12
                            value = selectedMonth
                            setOnValueChangedListener { _, _, newVal ->
                                selectedMonth = newVal
                            }
                        }
                    }
                )
            }
        }
    )
}
