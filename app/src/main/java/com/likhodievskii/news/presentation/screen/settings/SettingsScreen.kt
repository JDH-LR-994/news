@file:OptIn(ExperimentalMaterial3Api::class)

package com.likhodievskii.news.presentation.screen.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.likhodievskii.news.R
import com.likhodievskii.news.presentation.mapper.toReadableFormat
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.processCommand(SettingsCommand.SetNotificationsEnabled(it))
        }
    )
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val state = viewModel.state.collectAsState()
        when (val currentState = state.value) {
            is SettingsState.Configuration -> {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        SettingsCard(
                            title = stringResource(R.string.search_language),
                            subtitle = stringResource(R.string.select_language_for_news_search)
                        ) {
                            SettingsDropdown(
                                items = currentState.languages,
                                selectedItem = currentState.selectedLanguage,
                                onItemSelected = {
                                    viewModel.processCommand(SettingsCommand.SelectLanguage(it))
                                },
                                itemAsString = {
                                    it.toReadableFormat()
                                }
                            )
                        }
                    }

                    item {
                        SettingsCard(
                            title = stringResource(R.string.update_interval),
                            subtitle = stringResource(R.string.how_often_to_update_news)
                        ) {
                            StepSlider(
                                selectedItem = currentState.selectedInterval,
                                items = currentState.intervals,
                                itemsAsString = { it.toReadableFormat() },
                                onValueChangedFinish = {
                                    viewModel.processCommand(SettingsCommand.SelectInterval(it))
                                }
                            )
                        }
                    }
                    item {
                        SettingsCard(
                            title = stringResource(R.string.notifications),
                            subtitle = stringResource(R.string.show_notifications_about_articles)
                        ) {
                            SettingsSwitch(
                                checked = currentState.notificationsEnabled,
                                onCheckedChange = { enabled ->
                                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                                    } else {
                                        viewModel.processCommand(
                                            SettingsCommand.SetNotificationsEnabled(enabled)
                                        )
                                    }
                                },
                                imageVectorChecked = Icons.Default.Check,
                                imageVectorUnChecked = Icons.Default.Clear
                            )
                        }
                    }
                    item {
                        SettingsCard(
                            title = stringResource(R.string.update_only_via_wi_fi),
                            subtitle = stringResource(R.string.save_mobile_data)
                        ) {
                            SettingsSwitch(
                                checked = currentState.wifiOnly,
                                onCheckedChange = {
                                    viewModel.processCommand(SettingsCommand.SetWifiOnly(it))
                                },
                                imageVectorChecked = Icons.Default.Check,
                                imageVectorUnChecked = Icons.Default.Clear
                            )
                        }
                    }
                }
            }

            SettingsState.Initial -> {}
        }
    }
}


@Composable
private fun SettingsSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    imageVectorChecked: ImageVector,
    imageVectorUnChecked: ImageVector
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            Icon(
                imageVector = if (checked) imageVectorChecked else imageVectorUnChecked,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )
        }
    )
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            content()

        }
    }
}

@Composable
private fun <T> SettingsDropdown(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemAsString: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
            value = itemAsString(selectedItem),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.fastForEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = itemAsString(item))
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )

            }
        }
    }
}


@Composable
private fun <T> StepSlider(
    modifier: Modifier = Modifier,
    selectedItem: T,
    items: List<T>,
    itemsAsString: @Composable (T) -> String,
    onValueChangedFinish: (T) -> Unit
) {
    var sliderPosition by remember(selectedItem) {
        mutableFloatStateOf(items.indexOf(selectedItem).toFloat())
    }
    val currentIndex = sliderPosition.roundToInt()
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = itemsAsString(items[currentIndex]))
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
            },
            onValueChangeFinished = {
                val index = sliderPosition.roundToInt()
                onValueChangedFinish(items[index])
            },
            valueRange = 0f..items.lastIndex.toFloat(),
            steps = items.size - 2
        )
    }
}