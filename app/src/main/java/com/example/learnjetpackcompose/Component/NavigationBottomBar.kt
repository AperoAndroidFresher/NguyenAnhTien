package com.example.learnjetpackcompose.Component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.learnjetpackcompose.R

data class NavBottomItems(val label: String, val icon: Int)

@Composable
fun AppNavigationBottomBar(
    selectedIndex: Int,
    onNavigate: (Int) -> Unit
) {
    val navItemsList = listOf(
        NavBottomItems(stringResource(R.string.home), R.drawable.icon_home),
        NavBottomItems(stringResource(R.string.library), R.drawable.icon_library),
        NavBottomItems(stringResource(R.string.playlist), R.drawable.icon_playlist)
    )

    val selectedColor = Color(0xFF00C2CB)
    val unselectedColor = Color.White

    NavigationBar(containerColor = Color.Black) {
        navItemsList.forEachIndexed { index, navItem ->
            val isSelected = selectedIndex == index

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(index) },
                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.label,
                        tint = if (isSelected) selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        text = navItem.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) selectedColor else unselectedColor
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}