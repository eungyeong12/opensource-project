package jo.remind.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import jo.remind.R
import jo.remind.ui.RemindNavigation
import jo.remind.ui.record.DatePickerDialog
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    navController: NavHostController,
    onRecordRequest: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val currentYearMonth = remember { derivedStateOf { YearMonth.of(selectedDate.year, selectedDate.month) } }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CalendarHeader(
            yearMonth = currentYearMonth.value,
            onPrev = {
                selectedDate = selectedDate.minusMonths(1).withDayOfMonth(1)
            },
            onNext = {
                selectedDate = selectedDate.plusMonths(1).withDayOfMonth(1)
            },
            onTextClick = { showDatePicker = true }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CalendarGrid(
                yearMonth = currentYearMonth.value,
                selectedDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    showDialog = true
                }
            )
        }
    }

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

    if (showDialog) {
        onRecordRequest(selectedDate)
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
            .padding(top = 8.dp),
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
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
                .border(1.1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        val isCurrentMonth = date.month == yearMonth.month
                        val isSelected = date == selectedDate

                        val backgroundColor = when {
                            isSelected && isCurrentMonth -> MaterialTheme.colorScheme.onPrimary
                            !isCurrentMonth -> Color(0xFFF8F8F8)
                            else -> Color.Transparent
                        }

                        val textColor = if (isCurrentMonth) Color.Black else Color(0xFFF8F8F8)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .border(
                                    width = 0.5.dp,
                                    color = Color(0xFFEEEEEE)
                                )
                                .background(backgroundColor)
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
                                modifier = Modifier.padding(6.dp),
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}