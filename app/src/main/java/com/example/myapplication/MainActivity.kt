package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.rememberNavController

// --- Color Palette (Translated to Compose Colors) ---
object AppColors {
    val primary = Color(0xFF3B5998)
    val secondary = Color(0xFF8B9DC3)
    val link = Color(0xFF1877F2)
    val text = Color(0xFF1C1E21)
    val textSecondary = Color(0xFF606770)
    val background = Color(0xFFF0F2F5)
    val cardBackground = Color(0xFFFFFFFF)
    val inputBackground = Color(0xFFFFFFFF)
    val border = Color(0xFFDDDFE2)
    val deleteButton = Color(0xFFD32F2F)
    val white = Color(0xFFFFFFFF)
    val black = Color(0xFF000000)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Use the AppNavigation to handle screens
                AppNavigation()
            }
        }
    }
}

// --- LoginScreen Composable ---
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onCreateAccount: () -> Unit) {
    // Equivalent of useState for state variables
    var rememberMe by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // In a real app, handle navigation and auth logic here or via a ViewModel
    val handleLoginSuccess = {
        // Placeholder for navigation/auth logic
        println("Login Attempt with Email: $email, Password: $password, Remember Me: $rememberMe")
        // Navigate to the main screen
        onLoginSuccess()
    }

    val handleCreateAccount = {
        // Navigate to create account screen
        onCreateAccount()
    }

    // SafeAreaView equivalent handled by Scaffold and padding
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background) // Set background color for the whole screen
    ) { innerPadding ->
        // ScrollView equivalent
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling
                .padding(horizontal = 15.dp), // Add horizontal padding around the card
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            // ContentContainer equivalent with styling
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp) // Add vertical padding around the card
                    .background(AppColors.cardBackground, shape = RoundedCornerShape(8.dp))
                    .padding(20.dp) // Inner padding for content within the card
                // Basic shadow equivalent (Compose does not have direct shadow like React Native)
                // For a more accurate shadow, you might need a custom modifier or library
                // This is a simple elevation effect provided by Material Design components if used
                // But since we are drawing background manually, elevation needs extra steps or different components
            ) {
                // ScreenTitle equivalent
                Text(
                    text = "Welcome Back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                // LoginSubtitle equivalent
                Text(
                    text = "Sign in to continue to your blogs",
                    fontSize = 16.sp,
                    color = AppColors.textSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                )

                // InputContainer and Input equivalent
                OutlinedTextField( // Using OutlinedTextField for border
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }, // Label acts as placeholder initially
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    colors = OutlinedTextFieldDefaults.colors( // Customize colors
                        focusedBorderColor = AppColors.border,
                        unfocusedBorderColor = AppColors.border,
                        cursorColor = AppColors.text,
                        focusedLabelColor = AppColors.textSecondary,
                        unfocusedLabelColor = AppColors.textSecondary,
                        focusedContainerColor = AppColors.inputBackground,
                        unfocusedContainerColor = AppColors.inputBackground
                    ),
                    shape = RoundedCornerShape(6.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                OutlinedTextField( // InputContainer and Input equivalent for password
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.border,
                        unfocusedBorderColor = AppColors.border,
                        cursorColor = AppColors.text,
                        focusedLabelColor = AppColors.textSecondary,
                        unfocusedLabelColor = AppColors.textSecondary,
                        focusedContainerColor = AppColors.inputBackground,
                        unfocusedContainerColor = AppColors.inputBackground
                    ),
                    shape = RoundedCornerShape(6.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation() // Hide password
                )

                // Row equivalent
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // CheckboxContainer equivalent
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = AppColors.primary,
                                uncheckedTrackColor = AppColors.border,
                                checkedThumbColor = AppColors.white,
                                uncheckedThumbColor = AppColors.white // Or a different color
                            )
                        )
                        // CheckboxLabel equivalent
                        Text(
                            text = "Remember me",
                            fontSize = 15.sp,
                            color = AppColors.textSecondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // LinkButton equivalent
                    TextButton(
                        onClick = { /* Handle Forgot Password */ },
                        modifier = Modifier.padding(horizontal = 5.dp) // Add padding to the button area
                    ) {
                        // LinkText equivalent
                        Text(
                            text = "Forgot Password?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.link
                        )
                    }
                }

                // StyledButton equivalent
                Button(
                    onClick = handleLoginSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary)
                ) {
                    // ButtonText equivalent
                    Text(
                        text = "Sign In",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.white,
                        modifier = Modifier.padding(vertical = 4.dp) // Add vertical padding inside the button
                    )
                }

                // LinkButton equivalent for Sign Up
                TextButton(
                    onClick = handleCreateAccount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    // LinkText equivalent
                    Text(
                        text = "Don't have an account? Sign Up",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.link
                    )
                }
            }
        }
    }
}
