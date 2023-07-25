package com.example.swipetodismissmodifierfromscratch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.swipetodismissmodifierfromscratch.ui.theme.SwipeToDismissModifierFromScratchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeToDismissModifierFromScratchTheme {

                val userList = remember {
                    mutableStateListOf(
                       "user 0",
                       "user 1",
                       "user 2",
                       "user 3",
                       "user 4",
                       "user 5",
                    )
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                    }
                    if (userList.isEmpty()) {
                        Button(onClick = {
                            userList.addAll(
                                listOf(
                                    "user 0",
                                    "user 1",
                                    "user 2",
                                    "user 3",
                                    "user 4",
                                    "user 5"
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                        ) {
                            Text(text = "Reset List")
                        }
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


