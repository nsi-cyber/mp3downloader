package com.nsicyber.mp3downloader.screens

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nsicyber.mp3downloader.R
import com.nsicyber.mp3downloader.formatBytesToMb
import com.nsicyber.mp3downloader.formatNumber
import com.nsicyber.mp3downloader.getLastDirectoryName
import com.nsicyber.mp3downloader.models.VideoInfo
import com.nsicyber.mp3downloader.toJson
import com.nsicyber.mp3downloader.ui.theme.JakartaSans
import com.nsicyber.mp3downloader.viewModels.DownloadScreenViewModel
import com.nsicyber.mp3downloader.viewModels.InitialScreenViewModel
import kotlinx.coroutines.launch


@Composable
fun DownloadScreen(
    navController: NavController,
    model: VideoInfo?,
) {


    var viewModel = hiltViewModel<DownloadScreenViewModel>()

    LaunchedEffect(Unit) {
        viewModel.startDownload(
            model?.url!!, model.folder!!, model.title!!
        )
    }




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
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clip(
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .background(color = Color(0xFFFAFAFA))
            ) {
                Column(modifier = Modifier.padding(14.dp, 14.dp, 14.dp, 16.dp)) {
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .aspectRatio(1.78f)
                            .fillMaxWidth(), model = model?.thumbnail, contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        fontFamily = JakartaSans,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF1A0D0D),
                        text = model?.title.toString(),
                        fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.view_icon),
                            contentDescription = "image description",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(16.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            fontFamily = JakartaSans,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight(600),
                            color = Color(0xFF858181),
                            text = formatNumber(model?.views!!),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.width(32.dp))


                        Image(
                            painter = painterResource(id = R.drawable.like_icon),
                            contentDescription = "image description",
                            contentScale = ContentScale.None,
                            modifier = Modifier

                                .height(16.dp)
                                .padding(end = 8.dp)
                        )


                        Text(
                            fontFamily = JakartaSans,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight(600),
                            color = Color(0xFF858181),
                            text = formatNumber(model?.likes!!),
                            fontSize = 12.sp
                        )


                    }
                }
            }


            Row(modifier = Modifier.padding(top = 32.dp)) {
                Text(
                    text = if (viewModel.downloadProgress.value != 100) "Downloading" else "Finished",
                    fontSize = 12.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1A0D0D),
                    fontFamily = JakartaSans,
                )

                Spacer(Modifier.weight(0.9f))
                Text(
                    text = formatBytesToMb(viewModel.currByte.value!!) + " / " + formatBytesToMb(
                        viewModel.totalByte.value!!.toInt()
                    ).toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF1A0D0D),
                    fontFamily = JakartaSans,
                )

            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(1f),
                progress = viewModel.downloadProgress.value!!.toFloat() / 100,

                color = Color(android.graphics.Color.parseColor("#28C6D0")),
                trackColor = Color(android.graphics.Color.parseColor("#EFEFEF"))
            )


            if (viewModel.downloadProgress.value == 100) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info_icon),
                        contentDescription = "image description",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(12.dp)
                            .padding(end = 16.dp)
                    )
                    Text(
                        text = "MP3 successfully saved into selected folder",
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF1A0D0D),
                        fontFamily = JakartaSans,
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                Button(modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(1f)
                    .clip(
                        shape = RoundedCornerShape(size = 8.dp)
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF892EFF)),

                    onClick = {
                        navController.popBackStack()


                    }) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {


                        Text(
                            text = "Download Another MP3",
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

