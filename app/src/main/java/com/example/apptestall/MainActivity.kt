package com.example.apptestall

import android.net.Uri
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

        val appLinkIntent = intent

        // 2. Lấy dữ liệu URI (URL) từ Intent
        val appLinkData: Uri? = appLinkIntent.data

        if (appLinkData != null) {

            // A. TRÍCH XUẤT QUERY PARAMETER (Tham số Truy vấn)
            // Lấy giá trị của tham số 'source' (ví dụ: 'web')
            val source = appLinkData.getQueryParameter("source")
            println("Nguồn: $source")


            // B. TRÍCH XUẤT PATH SEGMENT (ID Sản phẩm)
            // Giả định URL là: /myapp/product/123
            // segments: [myapp, product, 123]
            val pathSegments = appLinkData.pathSegments

            if (pathSegments.size >= 3) {
                // Lấy phần tử thứ 3 (chỉ số 2) trong đường dẫn (ví dụ: "123")
                val productId = pathSegments[2]

                // Thực hiện logic của ứng dụng, ví dụ: tải dữ liệu sản phẩm
                loadProductDetails(productId)
            }
        }

    }

    private fun loadProductDetails(id: String) {
        // ... Logic hiển thị chi tiết sản phẩm dựa trên ID
        println("Đang tải chi tiết sản phẩm có ID: $id")
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