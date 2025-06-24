package com.example.aire.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Ensure you're using Material Design 3 components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable // Function responsible for rendering the bottom navigation bar
fun Bottom(navController: NavController) { // Receives NavController to manage navigation
    val navBackStackEntry = navController.currentBackStackEntryAsState().value // Gets the last element of the back stack
    val currentRoute = navBackStackEntry?.destination?.route // Gets the current route

    // IMPORTANT: Update these references to match the new route names defined in Nav.kt
    // For example, if you renamed Screen1 to Map and Screen2 to Summary, use those.
    val items = listOf(Screen.Home, Screen.Map, Screen.Summary) // List of Screen objects

    Surface( // Defines the container for the bottom navigation
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp), // Fixed height for the bottom bar
        color = MaterialTheme.colorScheme.tertiary, // Background color from your theme
        shadowElevation = 8.dp // Adds a shadow for depth
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly, // Distributes items evenly horizontally
            verticalAlignment = Alignment.CenterVertically // Centers items vertically
        ) {
            items.forEach { screen -> // Iterates over the list of navigation items
                val selected = currentRoute == screen.route // Checks if the current item is selected
                Column(
                    modifier = Modifier
                        .weight(1f) // Each item takes equal weight
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally, // Centers content horizontally within the column
                    verticalArrangement = Arrangement.Center // Centers content vertically within the column
                ) {
                    IconButton(
                        onClick = { // Defines the action when an icon is clicked
                            navController.navigate(screen.route) { // Navigates to the selected screen
                                launchSingleTop = true // Ensures only one instance of the screen is in the stack
                                restoreState = true // Restores the state of the screen if revisited
                                // Clears the back stack up to the start destination of the graph
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true // Saves the state of the popped screens
                                }
                            }
                        },
                        modifier = Modifier.size(32.dp) // Size of the clickable icon area
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.label,
                            modifier = Modifier.size(22.dp), // Size of the icon graphic
                            tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary // Icon color based on selection and theme
                        )
                    }

                    Text(
                        text = screen.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal, // Text weight based on selection
                        color = if (selected) MaterialTheme.colorScheme.secondary else Color(0xFF9E9E9E), // Text color based on selection
                        maxLines = 2, // Allows up to two lines for the label
                        lineHeight = 11.sp, // Line height for the label
                        modifier = Modifier.padding(top = 2.dp) // Padding above the text
                    )
                }
            }
        }
    }
}