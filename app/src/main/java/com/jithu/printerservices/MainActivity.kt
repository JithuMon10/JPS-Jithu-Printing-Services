package com.jithu.printerservices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jithu.printerservices.ui.DashboardScreen
import com.jithu.printerservices.ui.OrderListScreen
import com.jithu.printerservices.ui.PinScreen
import com.jithu.printerservices.ui.SettingsScreen
import com.jithu.printerservices.ui.components.ConfirmDeleteDialog
import com.jithu.printerservices.ui.components.OrderEditBottomSheet
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jithu.printerservices.ui.components.CompactSearchBar
import com.jithu.printerservices.ui.theme.DarkGradientSoftBlue
import com.jithu.printerservices.ui.theme.DarkGradientSoftLavender
import com.jithu.printerservices.ui.theme.GradientSoftBlue
import com.jithu.printerservices.ui.theme.GradientSoftLavender
import com.jithu.printerservices.ui.theme.ThemePreferences
import com.jithu.printerservices.ui.theme.JPSTheme
import com.jithu.printerservices.ui.theme.PrimaryAccent
import com.jithu.printerservices.ui.theme.SecondaryAccent
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch

private const val DASHBOARD_PIN = "407345"

// Entities & Data Layer
@Entity
data class AppOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customer: String,
    val fileName: String?,
    val description: String?,
    val quantity: Int,
    val amount: Double,
    val doubleSided: Boolean = true,
    val spiral: Boolean,
    val dueDate: String,
    val addedTime: String,
    val amountReceived: Boolean = false,
    val completed: Boolean = false
)

@Entity
data class PinConfig(
    @PrimaryKey val id: Int = 1,
    val hash: String // hashed PIN
)

@Dao
interface OrderDao {
    @Query("SELECT * FROM AppOrder ORDER BY dueDate ASC")
    suspend fun getAll(): List<AppOrder>
    @Insert suspend fun insert(order: AppOrder)
    @Update suspend fun update(order: AppOrder)
    @Delete suspend fun delete(order: AppOrder)
    // ...search/sort queries to come...
}

@Dao
interface PinDao {
    @Query("SELECT * FROM PinConfig WHERE id=1")
    suspend fun getPin(): PinConfig?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun setPin(pin: PinConfig)
}

@Database(entities = [AppOrder::class, PinConfig::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun pinDao(): PinDao
}

// PIN UI
@Composable
fun PinScreen(onPinSuccess: () -> Unit, db: AppDatabase) {
    // TODO: Implement PIN set/unlock
    Text("[PIN screen placeholder]")
}

// Home/Dashboard UI
@Composable
fun HomeScreen() {
    Text("[Home/Dashboard placeholder]")
}

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Schedule the nightly notification for pending orders
        val workManager = WorkManager.getInstance(applicationContext)
        val now = java.time.LocalDateTime.now()
        val eightPm = now.withHour(20).withMinute(0).withSecond(0).withNano(0)
        val initialDelay = if (now.isAfter(eightPm)) {
            java.time.Duration.between(now, eightPm.plusDays(1)).toMillis()
        } else {
            java.time.Duration.between(now, eightPm).toMillis()
        }
        val workRequest = PeriodicWorkRequestBuilder<PendingOrderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "pending_orders_daily",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "jvs-db")
            .fallbackToDestructiveMigration()
            .build()
        setContent {
            MainContent(db)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(db: AppDatabase) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // App-level UI state
    var currentScreen by remember { mutableStateOf("orders") } // "dashboard", "orders", "settings"
    var dashboardUnlocked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPinSet by remember { mutableStateOf(false) }
    var orders by remember { mutableStateOf(listOf<AppOrder>()) }
    var searchQuery by remember { mutableStateOf("") }
    var searchExpanded by remember { mutableStateOf(false) }
    var editingOrder by remember { mutableStateOf<AppOrder?>(null) }
    var toDeleteOrder by remember { mutableStateOf<AppOrder?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var requireDashboardUnlock by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var showPinDialog by remember { mutableStateOf(false) }
    var dashboardOrders by remember { mutableStateOf(listOf<AppOrder>()) }

    LaunchedEffect(db) {
        withContext(Dispatchers.IO) {
            isPinSet = db.pinDao().getPin() != null
            orders = db.orderDao().getAll()
        }
    }

    fun refreshOrders() {
        scope.launch {
            orders = withContext(Dispatchers.IO) { db.orderDao().getAll() }
        }
    }

    val totalRevenue = orders.filter { it.amountReceived }.sumOf { it.amount }
    val month = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
    val monthRevenue = orders.filter { it.amountReceived && it.addedTime.startsWith(month) }.sumOf { it.amount }
    val pendingOrders = orders.count { !it.completed }
    val totalOrders = orders.size

    fun onSaveOrder(order: AppOrder) {
        scope.launch {
            if (order.id == 0L) {
                // Round to minutes (remove seconds/milliseconds)
                val now = LocalDateTime.now()
                val rounded = now.withSecond(0).withNano(0)
                val timestamp = rounded.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                db.orderDao().insert(order.copy(addedTime = timestamp))
            } else {
                db.orderDao().update(order)
            }
            editingOrder = null
            refreshOrders()
            snackbarHostState.showSnackbar("Order saved")
        }
    }
    
    fun goToHome() {
        // Always return to home: close any sheets, reset to orders screen
        editingOrder = null
        currentScreen = "orders"
        requireDashboardUnlock = false
    }

    fun onToggleAmountReceived(order: AppOrder) {
        scope.launch {
            db.orderDao().update(order.copy(amountReceived = !order.amountReceived))
            refreshOrders()
        }
    }

    fun onToggleCompleted(order: AppOrder) {
        scope.launch {
            db.orderDao().update(order.copy(completed = !order.completed))
            refreshOrders()
            snackbarHostState.showSnackbar(
                if (!order.completed) "Marked as completed" else "Marked as pending"
            )
        }
    }

    fun onDeleteOrderConfirm() {
        toDeleteOrder?.let {
            scope.launch {
                db.orderDao().delete(it)
                refreshOrders()
            }
        }
        toDeleteOrder = null
        showDeleteDialog = false
    }

    fun onEditOrder(order: AppOrder) {
        editingOrder = order
    }

    fun onDeleteOrder(order: AppOrder) {
        toDeleteOrder = order
        showDeleteDialog = true
    }

    fun onChangePin(oldPin: String, newPin: String) {
        scope.launch {
            val pinConfig = db.pinDao().getPin()
            if (pinConfig?.hash == oldPin) {
                db.pinDao().setPin(PinConfig(hash = newPin))
            }
        }
    }

    JPSTheme(darkTheme = false) {
        val gradientBackground = remember {
            Brush.linearGradient(colors = listOf(GradientSoftBlue, GradientSoftLavender))
        }

        BackHandler(enabled = editingOrder != null) {
            editingOrder = null
        }
        BackHandler(enabled = currentScreen != "orders") {
            currentScreen = "orders"
            requireDashboardUnlock = false
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBackground)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    PremiumTopBar(
                        currentScreen = currentScreen,
                        searchExpanded = searchExpanded,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onSearchExpandedChange = { searchExpanded = it },
                        onHome = { goToHome() },
                        onDashboard = {
                            currentScreen = "dashboard"
                            if (!dashboardUnlocked) {
                                requireDashboardUnlock = true
                                errorMessage = null
                            }
                        },
                        onSettings = { currentScreen = "settings" }
                    )
                },
                floatingActionButton = {
                    if (currentScreen == "orders" && editingOrder == null) {
                        AnimatedFAB(
                            onClick = {
                                editingOrder = AppOrder(
                                    customer = "",
                                    fileName = null,
                                    description = null,
                                    quantity = 1,
                                    amount = 0.0,
                                    doubleSided = true,
                                    spiral = false,
                                    dueDate = "",
                                    addedTime = ""
                                )
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        when (currentScreen) {
                            "dashboard" -> {
                                if (!dashboardUnlocked && requireDashboardUnlock) {
                                    PinScreen(
                                        isPinSet = true,
                                        errorMessage = errorMessage,
                                        onPinEntered = { pinInput ->
                                            if (pinInput == DASHBOARD_PIN) {
                                                dashboardUnlocked = true
                                                requireDashboardUnlock = false
                                                errorMessage = null
                                            } else {
                                                errorMessage = "Incorrect PIN. Try again."
                                            }
                                        }
                                    )
                                } else {
                                    DashboardScreen(
                                        totalRevenue = totalRevenue,
                                        monthRevenue = monthRevenue,
                                        totalOrders = totalOrders,
                                        pendingOrders = pendingOrders,
                                        recentOrders = orders.sortedByDescending { it.addedTime }
                                    )
                                }
                            }

                            "orders" -> {
                                OrderListScreen(
                                    orders = orders,
                                    onToggleAmountReceived = { onToggleAmountReceived(it) },
                                    onToggleCompleted = { onToggleCompleted(it) },
                                    onEditOrder = { onEditOrder(it) },
                                    onDeleteOrder = { onDeleteOrder(it) },
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { searchQuery = it }
                                )
                            }

                            "settings" -> {
                                SettingsScreen(
                                    onChangePin = { oldP, newP -> onChangePin(oldP, newP) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        ConfirmDeleteDialog(
                            visible = showDeleteDialog,
                            onConfirm = { onDeleteOrderConfirm() },
                            onDismiss = {
                                showDeleteDialog = false
                                toDeleteOrder = null
                            }
                        )
                    }
                }
            }

            // Bottom sheet for Add/Edit
            if (editingOrder != null) {
                OrderEditBottomSheet(
                    order = editingOrder,
                    onSave = { onSaveOrder(it) },
                    onDismiss = { editingOrder = null }
                )
            }
        }
    }
}

@Composable
fun AnimatedFAB(onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f, // Slightly smaller scale
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fabScale"
    )
    val iconRotation by animateFloatAsState(
        targetValue = if (pressed) 45f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "fabRotation"
    )

    FloatingActionButton(
        onClick = {
            pressed = true
            onClick()
            scope.launch {
                kotlinx.coroutines.delay(220)
                pressed = false
            }
        },
        modifier = Modifier.scale(scale),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 8.dp, // Reduced elevation
            pressedElevation = 4.dp
        )
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add order",
            modifier = Modifier
                .graphicsLayer { rotationZ = iconRotation }
                .size(20.dp) // Slightly smaller icon
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PremiumTopBar(
    currentScreen: String,
    searchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchExpandedChange: (Boolean) -> Unit,
    onHome: () -> Unit,
    onDashboard: () -> Unit,
    onSettings: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            if (!searchExpanded) {
                Text(
                    text = "JPS",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        fontSize = 20.sp
                    )
                )
            }
        },
        navigationIcon = {
            if (!searchExpanded) {
                Row {
                    ScalingIconButton(icon = Icons.Default.Home, contentDescription = "Home", onClick = onHome)
                    Spacer(modifier = Modifier.width(8.dp))
                    ScalingIconButton(icon = Icons.Default.Dashboard, contentDescription = "Dashboard", onClick = onDashboard)
                }
            }
        },
        actions = {
            when (currentScreen) {
                "orders" -> {
                    CompactSearchBar(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onSearch = { },
                        onClear = { onSearchQueryChange("") },
                        expanded = searchExpanded,
                        onExpandedChange = onSearchExpandedChange,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                else -> {
                    // No actions for other screens
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
private fun ScalingIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "iconScale"
    )
    IconButton(
        onClick = {
            pressed = true
            onClick()
            scope.launch {
                kotlinx.coroutines.delay(180)
                pressed = false
            }
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JPSTheme {
        Greeting("Android")
    }
}