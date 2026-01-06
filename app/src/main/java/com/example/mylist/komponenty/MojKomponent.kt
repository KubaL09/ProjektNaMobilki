package com.example.mylist.komponenty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Modifier.SingleListItem1(
    title: String = "Tytuł elementu",
    description: String = "Opis elementu z dodatkowymi informacjami",
    onClick: () -> Unit = {}
) {
    // Generuj unikalny ID bazując na tytule, aby każda osoba miała inne zdjęcie
    val imageId = title.hashCode().toLong().coerceAtLeast(0)
    val size = 200
    Row(
        modifier = fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Zdjęcie z picsum.photos - parametr random zapobiega cachowaniu
        AsyncImage(
            model = "https://picsum.photos/$size?random=$imageId",
            contentDescription = "Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
//            placeholder = androidx.compose.ui.graphics.painter.ColorPainter(
//                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
//            ),
//            error = androidx.compose.ui.graphics.painter.ColorPainter(
//                MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
//            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Kolumna z tekstem po prawej
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
