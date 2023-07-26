package com.example.swipetodismissmodifierfromscratch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.swipetodismissmodifierfromscratch.ui.theme.SwipeToDismissModifierFromScratchTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeToDismissModifierFromScratchTheme {

                val listOfUser = listOf(
                    "user 0",
                    "user 1",
                    "user 2",
                    "user 3",
                    "user 4",
                    "user 5"
                )
                val userList = remember {
                    listOfUser.toMutableStateList()
                }
                val userList2 = remember {
                    listOfUser.toMutableStateList()
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    "Custom SwipeToDismiss Modifier ",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                        items(count = userList.size) { i ->
                            val user = userList.getOrNull(i)
                            user?.let {
                                key(user) {
                                    UserCard(
                                        value = user,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .swipeToDismiss {
                                                userList.remove(user)
                                            }
                                            .background(Color.White)
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    "SwipeToDismiss Composable",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                        items(count = userList2.size, key = { userList2[it] }) { i ->
                            val user = userList.getOrNull(i)
                            val dismissState = rememberDismissState(
                                confirmValueChange = { dismissValue ->
                                    if (dismissValue == DismissValue.DismissedToStart) {
                                        userList2.remove(user)
                                    } else {
                                        false
                                    }
                                },
                                positionalThreshold = {
                                    125.dp.toPx()
                                }
                            )
                            user?.let {
                                SwipeToDismiss(
                                    state = dismissState,
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                        DismissDirection.EndToStart
                                    ),
                                    background = {
                                        val direction = dismissState.dismissDirection
                                            ?: return@SwipeToDismiss
                                        val icon = when (direction) {
                                            DismissDirection.StartToEnd -> Icons.Default.Done
                                            DismissDirection.EndToStart -> Icons.Default.Delete
                                        }
                                        val color = when (direction) {
                                            DismissDirection.StartToEnd -> Color.Green
                                            DismissDirection.EndToStart -> Color.Red
                                        }
                                        val alignment = when (direction) {
                                            DismissDirection.StartToEnd -> Alignment.CenterStart
                                            DismissDirection.EndToStart -> Alignment.CenterEnd
                                        }
                                        val scale by animateFloatAsState(
                                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                        )
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "Localized description",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    },
                                    dismissContent = {
                                        UserCard(
                                            value = user,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                        )
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            userList.clear()
                            userList2.clear()
                            userList.addAll(listOfUser)
                            userList2.addAll(listOfUser)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
                    ) {
                        Text(text = "Reset List")
                    }
                }

            }
        }
    }
}

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    value: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }

}


