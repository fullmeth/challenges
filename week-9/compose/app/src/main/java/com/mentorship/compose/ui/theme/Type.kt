package com.mentorship.compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mentorship.compose.R

val Caprasimo = FontFamily(Font(R.font.caprasimo_regular, FontWeight.Normal))

val Figtree = FontFamily(
    listOf(400, 600, 700).map { w ->
        Font(
            R.font.figtree_variable, FontWeight(w),
            variationSettings = FontVariation.Settings(FontVariation.weight(w))
        )
    }
)

val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Caprasimo,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(fontFamily = Figtree, fontSize = 15.sp),
    bodyMedium = TextStyle(fontFamily = Figtree, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = Figtree, fontSize = 13.sp),
    labelMedium = TextStyle(fontFamily = Figtree, fontSize = 11.sp),
    labelSmall = TextStyle(fontFamily = Figtree, fontSize = 10.sp),
)