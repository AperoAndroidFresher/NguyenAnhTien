package com.example.learnjetpackcompose.Screen.Home.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjetpackcompose.Component.DropdownMenuItemWithIcon
import com.example.learnjetpackcompose.Component.IconButtonCustom
import com.example.learnjetpackcompose.R
import com.example.learnjetpackcompose.Utils.LanguagePreferences
import com.example.learnjetpackcompose.Utils.updateLocale


@Composable
fun SettingScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val languagePreferences = remember { LanguagePreferences(context) }
    var currentLanguage by remember { mutableStateOf(languagePreferences.getLanguage()) }
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        HeaderSetting(
            onBack = onBack,
            onSave = {
                languagePreferences.saveLanguage(selectedLanguage)
                updateLocale(context, selectedLanguage)
                // Recreate activity to apply new resources across the app
                (context as? android.app.Activity)?.recreate()
                onSave()
            },
            showSaveIcon = selectedLanguage != currentLanguage
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
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        if (showSaveIcon) {
            IconButtonCustom(onSave, R.drawable.icon_yes, "Save")
        } else {
            Spacer(modifier = Modifier.height(24.dp).width(24.dp))
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
                text = stringResource(R.string.language),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Box {
            Text(
                text = selectedLanguage,
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
        DropdownMenuItemWithIcon(null, stringResource(R.string.english), stringResource(R.string.english), onEnglishClick)
        DropdownMenuItemWithIcon(null, stringResource(R.string.korean), stringResource(R.string.korean), onKoreanClick)
        DropdownMenuItemWithIcon(null, stringResource(R.string.french), stringResource(R.string.french), onFrenchClick)
        DropdownMenuItemWithIcon(null, stringResource(R.string.vietnamese), stringResource(R.string.vietnamese), onVietnameseClick)
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen()
}