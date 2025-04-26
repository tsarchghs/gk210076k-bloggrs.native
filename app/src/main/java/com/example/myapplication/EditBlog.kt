package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBlogScreen(
    navController: NavController,
    blogId: Int,
    blogStateHolder: BlogStateHolder
) {
    // Get the blog data
    val blog = blogStateHolder.blogs.find { it.id == blogId }
    
    // State for form fields
    var name by remember { mutableStateOf(blog?.name ?: "") }
    var slug by remember { mutableStateOf(blog?.slug ?: "") }
    var description by remember { mutableStateOf(blog?.description ?: "") }
    var logoUrl by remember { mutableStateOf(blog?.logo_url ?: "") }
    var category by remember { mutableStateOf(blog?.category ?: "") }
    var theme by remember { mutableStateOf(blog?.theme ?: "") }
    
    // Form validation state
    var showValidationError by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Blog",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Cancel"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppColors.background
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
    ) { innerPadding ->
        // If blog not found, show error
        if (blog == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Blog not found",
                    fontSize = 18.sp,
                    color = AppColors.textSecondary
                )
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary)
                ) {
                    Text(
                        text = "Go Back",
                        color = AppColors.white,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            // Edit blog form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Blog edit form in a card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.cardBackground
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Show validation error if needed
                        if (showValidationError) {
                            Text(
                                text = "Please fill in all required fields (Name, Slug, Description)",
                                color = AppColors.deleteButton,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(bottom = 10.dp)
                            )
                        }
                        
                        // Blog name input
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Blog Name *") },
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
                            singleLine = true
                        )
                        
                        // Slug input
                        OutlinedTextField(
                            value = slug,
                            onValueChange = { slug = it },
                            label = { Text("Slug (e.g., my-cool-blog) *") },
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
                            singleLine = true
                        )
                        
                        // Description input
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Short Description *") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
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
                            maxLines = 4,
                            minLines = 3
                        )
                        
                        // Logo URL input (optional)
                        OutlinedTextField(
                            value = logoUrl,
                            onValueChange = { logoUrl = it },
                            label = { Text("Logo URL (Optional)") },
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
                            singleLine = true
                        )
                        
                        // Category input (optional)
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category (Optional)") },
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
                            singleLine = true
                        )
                        
                        // Theme input (optional)
                        OutlinedTextField(
                            value = theme,
                            onValueChange = { theme = it },
                            label = { Text("Theme (Optional)") },
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
                            singleLine = true
                        )
                        
                        // Save button
                        Button(
                            onClick = {
                                if (name.isBlank() || slug.isBlank() || description.isBlank()) {
                                    showValidationError = true
                                } else {
                                    // Update blog
                                    blogStateHolder.updateBlog(
                                        blogId,
                                        name = name,
                                        slug = slug,
                                        description = description,
                                        logoUrl = logoUrl.ifBlank { null },
                                        category = category.ifBlank { null },
                                        theme = theme.ifBlank { null }
                                    )
                                    // Navigate back
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary)
                        ) {
                            Text(
                                text = "Save",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.white,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
} 