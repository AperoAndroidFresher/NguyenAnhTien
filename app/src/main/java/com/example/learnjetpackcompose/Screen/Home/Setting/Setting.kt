package com.example.learnjetpackcompose.Screen.Home.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R

@Preview
@Preview
@Composable
fun SettingScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {}
) {
    var currentLanguage by remember { mutableStateOf("English") }
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        HeaderSetting(
            onBack,
            onSave,
            showSaveIcon = selectedLanguage != currentLanguage // chỉ hiện khi khác
        )
        SelectLanguage(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = {
                selectedLanguage = it
                onLanguageSelected(it)
            }
        )
    }
}


@Composable
fun HeaderSetting(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    showSaveIcon: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButtonCustom(onBack, R.drawable.icon_back, "Back")
        Text(
            text = "Settings", style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        if (showSaveIcon) {
            IconButtonCustom(onSave, R.drawable.icon_yes, "Save")
        }else{
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SelectLanguage(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.icon_language),
                contentDescription = "Language",
                tint = Color.White
            )
            Text(
                "Language",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Box {
            Text(
                selectedLanguage,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable { expanded = true }
            )

            ShowDropDownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onEnglishClick = {
                    onLanguageSelected("English")
                    expanded = false
                },
                onKoreanClick = {
                    onLanguageSelected("Korean")
                    expanded = false
                },
                onFrenchClick = {
                    onLanguageSelected("French")
                    expanded = false
                },
                onVietnameseClick = {
                    onLanguageSelected("Vietnamese")
                    expanded = false
                }
            )
        }
    }
}


@Composable
fun ShowDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEnglishClick: () -> Unit,
    onKoreanClick: () -> Unit,
    onFrenchClick: () -> Unit,
    onVietnameseClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.DarkGray)
    ) {
        DropdownMenuItemWithIcon(null, "English language", "English", onEnglishClick)
        DropdownMenuItemWithIcon(null, "Korean language", "Korean", onKoreanClick)
        DropdownMenuItemWithIcon(null, "French language", "French", onFrenchClick)
        DropdownMenuItemWithIcon(null, "Vietnamese language", "Vietnamese", onVietnameseClick)
    }
}
