package tw.nolions.acguy.fingerprintmanager

import android.app.Activity
import android.app.KeyguardManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fingerprint()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun fingerprint() {
        val f = Fingerprint(
            this,
            getSystemService(Activity.KEYGUARD_SERVICE) as KeyguardManager,
            getSystemService(FingerprintManager::class.java) as FingerprintManager,
            CancellationSignal()
        )
        f.fingerprintListener(
            status = { Log.d("test", "status: $it") },
            msg = { Log.d("test", "msg: $it") }
        )
    }
}
