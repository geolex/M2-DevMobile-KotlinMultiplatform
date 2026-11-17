package com.amonteiro.a09_ynov_lyon_android.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.screens.PictureRowItem

@Composable
actual fun WeatherGallery(
    modifier: Modifier,
    list: List<Weather>,
    onPictureClick: (Weather) -> Unit
) {
    LazyRow(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(list.size) {
            PictureRowItem(
                data = list[it], modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.onTertiary)
                        .fillMaxWidth(),
                onPictureClick = onPictureClick
            )
        }
    }
}