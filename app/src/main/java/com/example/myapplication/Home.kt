package com.example.myapplication

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Define data class equivalent to Blog interface
data class Blog(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val logo_url: String? = null,
    val BlogCategoryId: Int? = null,
    val BlogThemeId: Int? = null,
    val category: String? = null,
    val theme: String? = null
)

// Simple state holder for blogs (equivalent to useBlogs context for this example)
class BlogStateHolder {
    private val _blogs = mutableStateListOf<Blog>(
        Blog(
            id = 1,
            name = "Tech Blog",
            slug = "tech-blog",
            description = "A blog about technology and programming.",
            category = "Technology",
            theme = "Modern"
        ),
        Blog(
            id = 2,
            name = "Travel Adventures",
            slug = "travel-adventures",
            description = "Documenting my travels around the world.",
            category = "Travel",
            theme = "Explorer"
        )
    )
    val blogs: List<Blog> get() = _blogs

    fun addBlog(blog: Blog) {
        _blogs.add(blog)
    }

    fun deleteBlog(blogId: Int) {
        _blogs.removeIf { it.id == blogId }
    }
    
    fun updateBlog(blogId: Int, name: String, slug: String, description: String, 
                  logoUrl: String? = null, category: String? = null, theme: String? = null) {
        val blogIndex = _blogs.indexOfFirst { it.id == blogId }
        if (blogIndex != -1) {
            val blog = _blogs[blogIndex]
            _blogs[blogIndex] = blog.copy(
                name = name,
                slug = slug,
                description = description,
                logo_url = logoUrl,
                category = category,
                theme = theme
            )
        }
    }
    // Add other blog-related functions as needed (e.g., updateBlog)
}

// Using remember { BlogStateHolder() } in the root composable to manage state

// --- Dashboard Composable (Equivalent of BlogDashboard) ---
@Composable
fun BlogDashboardScreen(
    navController: NavController,
    blogStateHolder: BlogStateHolder // Pass the state holder
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Set status bar color (equivalent to StatusBar in RN)
    // This typically needs to be handled at the Activity level or using a library
    // Here's a basic example using the Activity context and WindowCompat
    activity?.window?.let { window ->
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true // Or false for dark content
        window.statusBarColor = AppColors.background.hashCode() // Set status bar color
    }

    // SafeAreaView equivalent handled by Scaffold and padding
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background) // Set background color for the whole screen
            .windowInsetsPadding(WindowInsets.safeDrawing) // Apply safe area padding
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp), // Add horizontal padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle(title = "My Blogs")

            ContentContainer {
                StyledButton(
                    text = "Create New Blog",
                    onClick = {
                        // Use MainDestinations
                        navController.navigate(MainDestinations.CREATE_BLOG_ROUTE)
                    }
                )
            }

            BlogList(
                blogs = blogStateHolder.blogs,
                onViewPostsClick = { blogId ->
                    // Use MainDestinations
                    navController.navigate("${MainDestinations.POSTS_ROUTE}/$blogId") {
                        // Optional: Add navigation options
                        launchSingleTop = true
                    }
                },
                onEditClick = { blogId ->
                    // Use MainDestinations
                    navController.navigate("${MainDestinations.EDIT_BLOG_ROUTE}/$blogId") {
                        // Optional: Add navigation options
                        launchSingleTop = true
                    }
                },
                onDeleteClick = { blogId ->
                    blogStateHolder.deleteBlog(blogId)
                    // Simulate alert
                    android.widget.Toast.makeText(context, "Blog deleted successfully", android.widget.Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

// --- Composable Equivalents of Styled Components and other RN Components ---

@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.text,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 10.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ContentContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .shadow(
                elevation = 4.dp, // Android shadow equivalent
                shape = RoundedCornerShape(8.dp)
            )
            .background(AppColors.cardBackground, shape = RoundedCornerShape(8.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun StyledButton(
    text: String,
    onClick: () -> Unit,
    bgColor: Color = AppColors.primary
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        contentPadding = PaddingValues(14.dp)
    ) {
        Text(
            text = text,
            color = AppColors.white,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BlogList(
    blogs: List<Blog>,
    onViewPostsClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    if (blogs.isEmpty()) {
        Text(
            text = "No blogs found. Create your first blog!",
            fontSize = 16.sp,
            color = AppColors.textSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp) // Equivalent to contentContainerStyle paddingBottom
        ) {
            items(blogs, key = { it.id }) { blog ->
                BlogItem(
                    blog = blog,
                    onViewPostsClick = onViewPostsClick,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
fun BlogItem(
    blog: Blog,
    onViewPostsClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Half of the original margin-bottom for spacing between items
            .shadow(
                elevation = 4.dp, // Android shadow equivalent
                shape = RoundedCornerShape(8.dp)
            )
            .background(AppColors.cardBackground, shape = RoundedCornerShape(8.dp))
            .padding(18.dp)
    ) {
        Text(
            text = blog.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = blog.slug,
            fontSize = 14.sp,
            color = AppColors.textSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = blog.description,
            fontSize = 15.sp,
            color = AppColors.text,
            modifier = Modifier.padding(bottom = 15.dp),
            lineHeight = 21.sp // Equivalent to lineHeight
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End) // Equivalent to gap and justifyContent: flex-end
        ) {
            ActionButton(
                text = "View Posts",
                onClick = { onViewPostsClick(blog.id) },
                bgColor = AppColors.secondary // Using secondary for View Posts as per RN code (though RN code uses secondary for Edit button) - adjusted to match RN component behavior
            )
            ActionButton(
                text = "Edit",
                onClick = { onEditClick(blog.id) },
                bgColor = AppColors.secondary // Explicitly setting secondary color
            )
            ActionButton(
                text = "Delete",
                onClick = { onDeleteClick(blog.id) },
                bgColor = AppColors.deleteButton
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    bgColor: Color = AppColors.secondary // Default color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = AppColors.white,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


// --- Navigation Setup (Equivalent of React Navigation Stack) ---

// Define navigation routes
object MainDestinations {
    const val LOGIN_ROUTE = "login"
    const val DASHBOARD_ROUTE = "dashboard"
    const val CREATE_BLOG_ROUTE = "CreateBlog"
    const val EDIT_BLOG_ROUTE = "EditBlog"
    const val POSTS_ROUTE = "Posts"
    const val EDIT_POST_ROUTE = "EditPost"
    const val BLOG_ID_KEY = "blogId"
    const val POST_ID_KEY = "postId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val blogStateHolder = remember { BlogStateHolder() } // State holder for blogs
    val postStateHolder = remember { PostStateHolder() } // State holder for posts

    NavHost(navController = navController, startDestination = MainDestinations.LOGIN_ROUTE) {
        composable(MainDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to the dashboard when login is successful
                    navController.navigate(MainDestinations.DASHBOARD_ROUTE) {
                        // Clear the back stack so user can't go back to login
                        popUpTo(MainDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onCreateAccount = {
                    // In a real app, navigate to sign up screen
                    // For now, just show a message
                }
            )
        }
        composable(MainDestinations.DASHBOARD_ROUTE) {
            BlogDashboardScreen(navController = navController, blogStateHolder = blogStateHolder)
        }
        composable(MainDestinations.CREATE_BLOG_ROUTE) {
            // Placeholder for Create Blog Screen
            CreateBlogScreen(navController = navController, blogStateHolder = blogStateHolder)
        }
        composable("${MainDestinations.EDIT_BLOG_ROUTE}/{${MainDestinations.BLOG_ID_KEY}}") { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString(MainDestinations.BLOG_ID_KEY)?.toIntOrNull()
            if (blogId != null) {
                // Use our new EditBlogScreen implementation
                EditBlogScreen(
                    navController = navController,
                    blogId = blogId,
                    blogStateHolder = blogStateHolder
                )
            } else {
                // Handle error or navigate back
                Text("Invalid Blog ID")
            }
        }
        composable("${MainDestinations.POSTS_ROUTE}/{${MainDestinations.BLOG_ID_KEY}}") { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString(MainDestinations.BLOG_ID_KEY)?.toIntOrNull()
            if (blogId != null) {
                // Use the new PostsScreen composable
                PostsScreen(
                    navController = navController,
                    blogId = blogId,
                    blogStateHolder = blogStateHolder,
                    postStateHolder = postStateHolder
                )
            } else {
                // Handle error or navigate back
                Text("Invalid Blog ID")
            }
        }
        // Add EditPost route
        composable(
            "${MainDestinations.EDIT_POST_ROUTE}/{${MainDestinations.BLOG_ID_KEY}}/{${MainDestinations.POST_ID_KEY}}"
        ) { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString(MainDestinations.BLOG_ID_KEY)?.toIntOrNull()
            val postId = backStackEntry.arguments?.getString(MainDestinations.POST_ID_KEY)?.toIntOrNull()
            
            if (blogId != null && postId != null) {
                EditPostScreen(
                    navController = navController,
                    blogId = blogId,
                    postId = postId,
                    blogStateHolder = blogStateHolder,
                    postStateHolder = postStateHolder
                )
            } else {
                // Handle error or navigate back
                Text("Invalid Blog ID or Post ID")
            }
        }
    }
}

// --- Placeholder Screens (to complete navigation) ---

@Composable
fun CreateBlogScreen(navController: NavController, blogStateHolder: BlogStateHolder) {
    // Implement your Create Blog UI here
    Scaffold(modifier = Modifier.fillMaxSize().background(AppColors.background)) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Create Blog Screen", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Example: Add a dummy blog and navigate back
                val newBlog = Blog(id = blogStateHolder.blogs.size + 1, name = "New Blog ${blogStateHolder.blogs.size + 1}", slug = "new-blog-${blogStateHolder.blogs.size + 1}", description = "Description of new blog", category = "Uncategorized", theme = "Default")
                blogStateHolder.addBlog(newBlog)
                navController.popBackStack()
            }) {
                Text("Save Blog (Dummy)")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
        }
    }
}
