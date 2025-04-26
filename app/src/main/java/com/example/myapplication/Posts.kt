package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.MainDestinations

// Define Post data class
data class Post(
    val id: Int,
    val title: String,
    val blogId: Int,
    val date: String,
    val content: String = "" // Add content field with default value
)

// Simple state holder for posts
class PostStateHolder {
    private val _posts = mutableStateListOf(
        Post(
            id = 1,
            title = "Getting Started with Kotlin",
            blogId = 1,
            date = "2023-06-15",
            content = "Kotlin is a modern programming language that makes developers happier. It's concise, safe, interoperable with Java, and offers many great features."
        ),
        Post(
            id = 2,
            title = "Jetpack Compose Basics",
            blogId = 1,
            date = "2023-07-22",
            content = "Jetpack Compose is Android's modern toolkit for building native UI. It simplifies and accelerates UI development on Android with less code and powerful tools."
        ),
        Post(
            id = 3,
            title = "My Trip to Paris",
            blogId = 2,
            date = "2023-05-10",
            content = "Paris, the City of Light, offered amazing cuisine, beautiful architecture, and unforgettable experiences. The Eiffel Tower was breathtaking!"
        )
    )
    
    val posts: List<Post> get() = _posts
    
    fun getPostsByBlogId(blogId: Int): List<Post> {
        return _posts.filter { it.blogId == blogId }
    }
    
    fun addPost(post: Post) {
        _posts.add(post)
    }
    
    fun deletePost(postId: Int) {
        _posts.removeIf { it.id == postId }
    }
    
    fun getNewPostId(): Int {
        return if (_posts.isEmpty()) 1 else _posts.maxOf { it.id } + 1
    }
    
    // Add updatePost function
    fun updatePost(postId: Int, title: String, content: String) {
        val postIndex = _posts.indexOfFirst { it.id == postId }
        if (postIndex != -1) {
            val post = _posts[postIndex]
            _posts[postIndex] = post.copy(title = title, content = content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    navController: NavController,
    blogId: Int,
    blogStateHolder: BlogStateHolder,
    postStateHolder: PostStateHolder = remember { PostStateHolder() }
) {
    // Get the blog details
    val blog = blogStateHolder.blogs.find { it.id == blogId }
    
    // Get the filtered posts
    val posts = postStateHolder.getPostsByBlogId(blogId)
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = blog?.name?.plus(": Posts") ?: "Posts",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Create New Post Button Container
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
                    Button(
                        onClick = {
                            // Navigate to create post screen
                            // This would typically navigate to a dedicated screen, but for now we'll just show a dummy function
                            val newPost = Post(
                                id = postStateHolder.getNewPostId(),
                                title = "New Post ${postStateHolder.getNewPostId()}",
                                blogId = blogId,
                                date = "2023-08-30", // In a real app, use current date
                                content = "" // In a real app, you'd prompt for content
                            )
                            postStateHolder.addPost(newPost)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary)
                    ) {
                        Text(
                            text = "Create New Post",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.white,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
            
            // Post List
            if (blog == null) {
                // Blog not found
                Column(
                    modifier = Modifier.fillMaxSize(),
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
            } else if (posts.isEmpty()) {
                // No posts found
                Text(
                    text = "No posts found for this blog yet.",
                    fontSize = 16.sp,
                    color = AppColors.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                )
            } else {
                // Display posts
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(posts, key = { it.id }) { post ->
                        PostItem(
                            post = post,
                            onEditClick = {
                                // Navigate to edit post screen
                                navController.navigate(
                                    "${MainDestinations.EDIT_POST_ROUTE}/${blogId}/${post.id}"
                                )
                            },
                            onDeleteClick = {
                                postStateHolder.deletePost(post.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = AppColors.cardBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.text,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Published: ${post.date}",
                fontSize = 14.sp,
                color = AppColors.textSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Add content preview
            if (post.content.isNotEmpty()) {
                Text(
                    text = post.content.take(100) + if (post.content.length > 100) "..." else "",
                    fontSize = 15.sp,
                    color = AppColors.text,
                    modifier = Modifier.padding(bottom = 15.dp),
                    lineHeight = 21.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.secondary),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Edit",
                        color = AppColors.white,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.deleteButton),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Delete",
                        color = AppColors.white,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}