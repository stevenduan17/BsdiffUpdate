package com.steven.bsdiff

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck()
        }

        versionTextView.text="v${BuildConfig.VERSION_NAME}"
        button.setOnClickListener {
            Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
        nextButton.setOnClickListener {
            startActivity(Intent(this,PicActivity::class.java))
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class Task : AsyncTask<Void, Void, File>() {
        override fun doInBackground(vararg params: Void?): File {
            val oldApk = applicationInfo.sourceDir
            val newApk = File(Environment.getExternalStorageDirectory(), "newApk.apk").absolutePath
            val patchFile = File(Environment.getExternalStorageDirectory(), "patch").absolutePath
            patchNewApk(oldApk, patchFile, newApk)
            return File(newApk)
        }

        override fun onPostExecute(file: File) {
            super.onPostExecute(file)
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(this@MainActivity, "$packageName.fileProvider", file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(file)
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionCheck() {
        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 1)
        }
    }

    external fun patchNewApk(oldApk: String, patchFile: String, newApk: String)

}
