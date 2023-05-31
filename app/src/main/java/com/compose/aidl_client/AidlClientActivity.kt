package com.compose.aidl_client

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.compose.aidl_server.IComman

@Suppress("DEPRECATION")
class AidlClientActivity : AppCompatActivity() {

    lateinit var  bindButton : Button
    lateinit var calculateButton: Button
    lateinit var icomman : IComman

    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {

           icomman= IComman.Stub.asInterface(iBinder)
        }
        override fun onServiceDisconnected(componentName: ComponentName) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl_client)

        bindButton = findViewById(R.id.btnBind)
        calculateButton = findViewById(R.id.btnCacluate)

        bindButton.setOnClickListener {
            val intent = Intent("MySevice")
            Log.i("binbutton", "Bind button lcicked")
            bindService(convertImplicitIntentToExplicitIntent(intent),serviceConnection, BIND_AUTO_CREATE)
        }

        calculateButton.setOnClickListener {

            Log.i("Additon is",icomman.calculate(10,100).toString())
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun convertImplicitIntentToExplicitIntent(implicitIntent: Intent) : Intent? {

        val pm = packageManager
        val resolveInfoList = pm.queryIntentServices(implicitIntent,0)

        if (resolveInfoList.size!=1){
            return null
        }

        val serviceInfo = resolveInfoList.get(0)
        val component = ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component
        return explicitIntent
    }
}