package com.example.nenmott.screens.main.bottomnav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.nenmott.screens.main.abc.ABC
import com.example.nenmott.screens.main.home.HomeScreen
import com.example.nenmott.screens.main.user.UserProfileScreen

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val content: @Composable (navController: NavHostController) -> Unit,
)


val buttonItems = listOf(
    BottomNavigationItem(
        title = "ABC",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle,
        hasNews = false,
        content = { ABC() }
    ),
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = true,
        content = { HomeScreen() }
    ),
    BottomNavigationItem(
        title = "UserProfile",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        hasNews = false,
        content = { navController -> UserProfileScreen(navController) }
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationItemBar(navController: NavHostController) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    val selectedItem = buttonItems[selectedItemIndex]

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            selectedItem.content(navController)
        }
        NavigationBar {
            buttonItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = { selectedItemIndex = index },
                    label = { Text(text = item.title) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount != null) {
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                } else if (item.hasNews) {
                                    Badge()
                                }
                            }) {
                            Icon(
                                imageVector = if (selectedItemIndex == index) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}
