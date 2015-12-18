package grupomoviles.quelista.captureCodes;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.BarcodeFormat;

import java.io.Serializable;

/**
 * Created by Nauce on 17/12/15.
 */
public class IntentCaptureActivity implements Serializable {

    public static final int CODE_CAPTURE_ACTIVITY = 0;

    private BarcodeFormat barcodeFormat = BarcodeFormat.EAN_13;
    private boolean beep = true;
    private boolean reverseCamera;
    private String statusText = "Código de barras";

    public void setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBeep(boolean beep) {
        this.beep = beep;
    }

    public boolean isBeep() {
        return beep;
    }

    public void setReverseCamera(boolean reverseCamera) {
        this.reverseCamera = reverseCamera;
    }

    public boolean isReverseCamera() {
        return reverseCamera;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void initScan(Activity activity) {
        Intent i = new Intent(activity, CaptureActivity.class);
        i.putExtra("this", (Serializable) this);

        activity.startActivityForResult(i, CODE_CAPTURE_ACTIVITY);
    }
}
