package tw.nolions.acguy.fingerprintmanager

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class Fingerprint(
    private val context: Context,
    private val mKeyguardManager: KeyguardManager,
    private val mFingerprintManager: FingerprintManager,
    private val mCancellationSignal: CancellationSignal
) {
    companion object {
        const val TAG = "Fingerprint"
    }

    /**
     * USE_FINGERPRINT 權限檢查
     *
     * @return Boolean
     */
    private fun checkPermission() = context.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED

    /**
     * 檢查硬體是否支援
     *
     * @return Boolean
     */
    fun isHardwareDetected() = !mFingerprintManager.isHardwareDetected

    /**
     * 檢查是否有至少一個指紋
     *
     * @return Boolean
     */
    fun hasEnrolledFingerprints(): Boolean = !mFingerprintManager.hasEnrolledFingerprints()

    /**
     * TODO
     *
     * @return Boolean
     */
    fun isKeyguardSecure() = !mKeyguardManager.isKeyguardSecure

    /**
     * 指紋辨識 Listener
     *
     * @param status
     * @param msg
     */
    fun fingerprintListener(status: (AuthenticationStatus) -> Unit, msg: (String) -> Unit) {
        if (checkPermission()) {
            mFingerprintManager.authenticate(
                null,
                mCancellationSignal,
                0,
                object : FingerprintManager.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        Log.e(TAG, "error $errorCode $errString")
                        status(AuthenticationStatus.ERROR)
                        msg("$errorCode $errString")
                    }

                    override fun onAuthenticationFailed() {
                        Log.e(TAG, "onAuthenticationFailed")
                        status(AuthenticationStatus.FAIL)
                        msg("Authentication Failed")
                    }

                    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
                        Log.d(TAG, "onAuthenticationSucceeded")
                        status(AuthenticationStatus.SUCCESS)
                        msg("Authentication Success")
                    }
                },
                null
            )
        } else {
            status(AuthenticationStatus.MISS_PERMISSIONS)
            msg("Missing required permissions")
        }
    }


    enum class AuthenticationStatus {
        SUCCESS, FAIL, ERROR, MISS_PERMISSIONS
    }


}
