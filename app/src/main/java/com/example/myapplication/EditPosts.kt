package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    navController: NavController,
    blogId: Int,
    postId: Int,
    blogStateHolder: BlogStateHolder,
    postStateHolder: PostStateHolder
) {
    // Get the blog and post data
    val blog = blogStateHolder.blogs.find { it.id == blogId }
    val post = postStateHolder.posts.find { it.id == postId }
    
    // State for form fields
    var title by remember { mutableStateOf(post?.title ?: "") }
    var content by remember { mutableStateOf(post?.content ?: "") }
    
    // Form validation state
    var showValidationError by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Post",
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
        // If post or blog not found, show error
        if (blog == null || post == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Post not found",
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
            // Edit post form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Post edit form in a card
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
                        // Blog name display
                        Text(
                            text = "Blog: ${blog.name}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.textSecondary,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 15.dp)
                        )
                        
                        // Show validation error if needed
                        if (showValidationError) {
                            Text(
                                text = "Please fill in all fields",
                                color = AppColors.deleteButton,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(bottom = 10.dp)
                            )
                        }
                        
                        // Title input
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Post Title") },
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
                        
                        // Content input
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            label = { Text("Post Content") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
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
                            maxLines = 10,
                            minLines = 5
                        )
                        
                        // Update button
                        Button(
                            onClick = {
                                if (title.isBlank() || content.isBlank()) {
                                    showValidationError = true
                                } else {
                                    // Update post
                                    postStateHolder.updatePost(
                                        postId,
                                        title = title,
                                        content = content
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
                                text = "Update Post",
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