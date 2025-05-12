package jo.remind.ui.search

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import jo.remind.R
import jo.remind.ui.RemindNavigation
import jo.remind.ui.component.MovieCard
import jo.remind.viewmodel.MovieSearchViewModel

@Composable
fun MovieSearchTopBar(
    onClick: () -> Unit
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
            )
        }

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.prev),
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
                text = "영화 검색",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@Composable
fun MovieSearchScreen(
    navController: NavHostController,
    viewModel: MovieSearchViewModel = hiltViewModel(),
) {
    val movieList = viewModel.movieList
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = bottomPadding + 56.dp)
    ) {
        MovieSearchTopBar(
            onClick = { navController.popBackStack() }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            MovieSearchInput(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    if (query.isNotBlank()) {
                        viewModel.searchMovies(query)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier.weight(1f),
                focusManager = focusManager
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "추가",
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(RemindNavigation.MovieRegistration.route)
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val isLoading = viewModel.isLoading

        Box(modifier = Modifier.fillMaxSize()) {
            var selectedMovieId by remember { mutableStateOf<Int?>(null) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                items(movieList) { movie ->
                    val genreText = viewModel.mapGenreIdsToNames(movie.genre_ids)
                    val isSelected = selectedMovieId == movie.id

                    MovieCard(
                        title = movie.title,
                        releaseDate = movie.release_date,
                        genreText = genreText,
                        imageUrl = movie.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
                        isSelected = isSelected,
                        onClick = {
                            selectedMovieId = if (selectedMovieId == movie.id) null else movie.id
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF009688))
                }
            }
        }
    }
}

@Composable
fun MovieSearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "영화를 검색해보세요!",
    focusManager: FocusManager
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(70))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "검색 아이콘",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = hint,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onSearch()
                        }
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
