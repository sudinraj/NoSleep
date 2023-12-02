package com.NoSleep.nosleep

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.NoSleep.nosleep.R
import com.NoSleep.nosleep.ui.theme.NoSleepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoSleepTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val timer = Alarm()
                    timer.audio(this)
                    Start(timer)
                }

            }
        }
    }
}

@Composable
fun Start(timer: Alarm) {
    var colorStart = remember { mutableStateOf(Color.Gray) }//0xffff0000
    var colorEnd = remember { mutableStateOf(Color.Green) }//0xff19ff00
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Press start to \"start\" the timer. Noises will play at random points until \"stop\" is pressed", fontSize = 30.sp)
        Button(onClick = {
            if(!timer.inProcess)
            {
                println("started")
                timer.set()
                timer.stop = false
                colorStart.value=Color.Green
                colorEnd.value = Color.Gray
            }
            },
            modifier = Modifier
                .size(width = 240.dp, height = 80.dp),
            colors = ButtonDefaults.buttonColors(
            containerColor = colorStart.value
            //.background(colorStart.value)
        ))
        {
            Text("Start", fontSize = 30.sp)
        }
        Button(onClick = {
            timer.end()
            colorEnd.value=Color.Green
            colorStart.value = Color.Gray
                         },
            modifier = Modifier.size(width = 240.dp, height = 80.dp),
            colors = ButtonDefaults.buttonColors(
            containerColor = colorEnd.value
            ))
        {
            Text("End",
                fontSize = 30.sp)
            timer.end()
        }
    }
}

class Alarm : Service()
{
    var minutes = 0
    var stop = false
    var inProcess = false
    lateinit var mediaPlayer:MediaPlayer
    fun audio(context: Context)
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.death)
    }
    fun set()
    {
        inProcess=true
        //var mediaPlayer:MediaPlayer = MediaPlayer.create(context, R.raw.death)
        minutes = (1..5).random()
        var until = minutes * 60000
        val time = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long)
            {
                if(stop)
                    this.cancel()
            }
            override fun onFinish()
            {
                println(minutes)
                mediaPlayer.start()
                set()
            }
        }
        time.start()
    }
    fun end()
    {
        inProcess=false
        stop = true
    }

    override fun onBind(p0: Intent?): IBinder? = null;
}