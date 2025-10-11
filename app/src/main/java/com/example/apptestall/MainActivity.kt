package com.example.apptestall

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainActivity : AppCompatActivity() {

    private val mutex = Mutex()

    private val TAG = "MainActivityMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent?.data?.let { uri ->
            Log.d("DeepLink", "Received URI: $uri")

            // Ví dụ: bạn có thể parse thêm query
            val target = uri.getQueryParameter("target")
            if (target != null) {
                // Mở màn hình tương ứng
                Toast.makeText(this, "Open target: $target", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun testMutex() = runBlocking {
        // Launch two concurrent coroutines
        val job1 = launch {
            repeat(5) {
                doSomething("$TAG Coroutine 1 -- $it")
            }
        }

        val job2 = launch {
            repeat(5) {
                doSomething("$TAG Coroutine 2  -- $it")
            }
        }

        // Wait for both coroutines to complete
        job1.join()
        job2.join()
    }

    suspend fun doSomething(name: String) {
        println("$TAG $name is trying to enter the critical section")

        // Use `withLock` extension function to acquire and release the mutex automatically
        mutex.withLock {
            println("$TAG $name entered the critical section")
            delay(1000) // Simulate some work
            println("$TAG $name leaving the critical section")
        }
    }
}