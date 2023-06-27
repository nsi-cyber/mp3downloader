package com.nsicyber.mp3downloader.screens

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.nsicyber.mp3downloader.MainActivity
import com.nsicyber.mp3downloader.R
import com.nsicyber.mp3downloader.getLastDirectoryName
import com.nsicyber.mp3downloader.getRealPathFromURI
import com.nsicyber.mp3downloader.toJson
import com.nsicyber.mp3downloader.ui.theme.JakartaSans
import com.nsicyber.mp3downloader.viewModels.InitialScreenViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.Q)

@Composable
fun InitialScreen(
    context: Context,
    navController: NavController
) {
    var scope = rememberCoroutineScope();

    var viewModel = hiltViewModel<InitialScreenViewModel>()

    val interactionSource = remember { MutableInteractionSource() }

    var launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                viewModel.selectedFolder.value = getRealPathFromURI(uri)


            }
        }
    val linkTextField = remember { mutableStateOf("") }
    val isGrabbingInfo = remember { mutableStateOf(false) }
    var alphaModifier = if (isGrabbingInfo.value)
        Modifier
            .alpha(0.5f)
            .pointerInput(Unit) {}
    else
        Modifier
    var interruptModifier = if (isGrabbingInfo.value)
        Modifier.pointerInput(Unit) {}
    else
        Modifier

    Box(
        modifier = Modifier
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 48.dp)
                .background(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    modifier = Modifier
                        .width(24.dp)
                        .height(16.dp),
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "image description",
                )
                Text(
                    modifier = Modifier.padding(start = 14.dp),
                    text = "MP3 Downloader",
                    fontSize = 20.sp,
                    fontFamily = JakartaSans,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1A0D0D)

                )
            }

            Box(
                modifier = interruptModifier.padding(top = 24.dp)
            ) {
                Column() {
                    Text(
                        modifier = Modifier.padding(bottom = 14.dp),
                        text = "YouTube Link",
                        fontSize = 14.sp,
                        fontFamily = JakartaSans,


                        fontWeight = FontWeight(600),
                        color = Color(0xFF1A0D0D)

                    )
                    Box(
                        alphaModifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD3D3D3),
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .fillMaxWidth(1f)


                            .height(56.dp)
                            .background(
                                color = Color(0xFFFAFAFA),
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        Row() {
                            Image(
                                painter = painterResource(id = R.drawable.link_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None, modifier = Modifier

                                    .height(24.dp)
                                    .padding(end = 16.dp)
                            )

                            BasicTextField(
                                modifier = Modifier
                                    .fillMaxSize(0.9f),
                                value = linkTextField.value,
                                onValueChange = {
                                    linkTextField.value = it
                                }, singleLine = true,
                                maxLines = 1, textStyle = TextStyle(
                                    fontFamily = JakartaSans, fontSize = 14.sp,

                                    fontWeight = FontWeight(600),
                                    color = Color(0xFF1A0D0D)
                                )


                            )




                            IconButton(modifier = Modifier.padding(start = 16.dp), onClick = {
                                linkTextField.value = ""
                            }) {
                                Image(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(16.dp),
                                    painter = painterResource(id = R.drawable.x_icon),
                                    contentDescription = "image description",
                                    contentScale = ContentScale.None
                                )
                            }


                        }
                    }

                    Text(
                        modifier = Modifier.padding(top = 14.dp, bottom = 14.dp),
                        text = "Destination Folder",
                        fontSize = 14.sp,
                        fontFamily = JakartaSans,

                        fontWeight = FontWeight(600),
                        color = Color(0xFF1A0D0D)
                    )

                    Box(
                        alphaModifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD3D3D3),
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .fillMaxWidth(1f)

                            .height(56.dp)
                            .background(
                                color = Color(0xFFFAFAFA),
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
//folder picker
                                launcher.launch(viewModel.openFolderPicker())

                            }
                    ) {
                        Row() {
                            Image(
                                modifier = Modifier
                                    .height(24.dp)
                                    .padding(end = 16.dp),
                                painter = painterResource(id = R.drawable.folder_icon),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                            Text(
                                modifier = Modifier,
                                text =
                                getLastDirectoryName(
                                    viewModel.selectedFolder.value.toString()
                                ),
                                fontSize = 14.sp,
                                fontFamily = JakartaSans,

                                fontWeight = FontWeight(600),
                                color = Color(0xFF1A0D0D)
                            )

                        }


                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 14.dp, bottom = 24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.info_icon),
                            contentDescription = "image description",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(12.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            modifier =
                            Modifier,
                            text = "Where you want to save the MP3",
                            fontSize = 12.sp,
                            fontFamily = JakartaSans,

                            fontWeight = FontWeight(500),
                            color = Color(0xFF858181)
                        )

                    }



                    Button(modifier = alphaModifier
                        .height(56.dp)
                        .fillMaxWidth(1f)
                        .clip(
                            shape = RoundedCornerShape(size = 8.dp)
                        ),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF892EFF)),

                        onClick = {

                            if (!linkTextField.value.isNullOrEmpty()) {

                                isGrabbingInfo.value = true

                                scope.launch {
                                    viewModel.getVideoInfo(linkTextField.value)
                                    println(viewModel.videoData.value)
                                    isGrabbingInfo.value = false
                                    if (viewModel.isReceived.value == true)
                                        navController.navigate("download_screen?model=${viewModel.videoData.value.toJson()}")
                                    else
                                        Toast.makeText(
                                            context,
                                            "Grabbing failed",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                }
                            } else
                                Toast.makeText(context, "Fill the link", Toast.LENGTH_SHORT).show()


                        }) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (isGrabbingInfo.value) {
                                CircularProgressIndicator(color = Color.White,
                                    modifier = Modifier
                                        .height(24.dp).width(24.dp)
                                )
                                Spacer(modifier = Modifier.width(11.dp))
                            }

                            Text(
                                text = if (isGrabbingInfo.value) "Grabbing Info ..." else "Download",
                                fontSize = 14.sp,
                                fontFamily = JakartaSans,

                                fontWeight = FontWeight(700),
                                color = Color.White,
                                textAlign = TextAlign.Center

                            )
                        }


                    }


                }


            }
        }
    }

}