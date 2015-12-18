package grupomoviles.quelista.captureCodes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import nauce.registrarproductos.R;

public class CaptureActivity extends ActionBarActivity {

    public static final String SCAN_FORMAT = "SCAN_FORMAT";
    public static final String SCAN_CONTENT = "SCAN_CONTENT";

    private CompoundBarcodeView barcodeView;
    private BeepManager beepManager;
    private MenuItem itemFlash;
    private MenuItem itemBeep;
    private IntentCaptureActivity ica;

    private boolean flash;
    private int cameraId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarCaptureActivity));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ica = (IntentCaptureActivity) getIntent().getExtras().get("this");

        barcodeView = (CompoundBarcodeView) findViewById(R.id.view);
        barcodeView.decodeSingle(callback);
        barcodeView.setStatusText(ica.getStatusText());
        beepManager = new BeepManager(this);

        torchOff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture, menu);

        itemFlash = menu.findItem(R.id.action_flash);
        itemBeep = menu.findItem(R.id.action_beep);
        if (ica.isBeep())
            itemBeep.setIcon(R.drawable.icon_beep_on);
        if (!ica.isReverseCamera())
            menu.findItem(R.id.action_camera).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_flash:
                if (flash) {
                    torchOff();
                    flash = false;
                }
                else {
                    barcodeView.setTorchOn();
                    flash = true;
                }
                break;

            case R.id.action_camera:
                if (cameraId == 0) {
                    itemFlash.setVisible(false);
                    torchOff();
                    flash = false;
                    cameraId = 1;
                    barcodeView.pause();
                    barcodeView.getBarcodeView().getCameraSettings().setRequestedCameraId(cameraId);
                    barcodeView.resume();
                }
                else {
                    cameraId = 0;
                    barcodeView.pause();
                    barcodeView.getBarcodeView().getCameraSettings().setRequestedCameraId(cameraId);
                    barcodeView.resume();
                    itemFlash.setVisible(true);
                }
                break;

            case R.id.action_beep:
                if (ica.isBeep()) {
                    ica.setBeep(false);
                    itemBeep.setIcon(R.drawable.icon_beep_off);
                } else {
                    ica.setBeep(true);
                    itemBeep.setIcon(R.drawable.icon_beep_on);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getBarcodeFormat().equals(ica.getBarcodeFormat())
                    && result.getText() != null) {
                handleDecode(result);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public void handleDecode(BarcodeResult rawResult) {
        barcodeView.setTorchOff();
        if (ica.isBeep())
            beepManager.playBeepSoundAndVibrate();

        setResult(RESULT_OK, getIntent());
        getIntent().putExtra(SCAN_FORMAT, rawResult.getBarcodeFormat().toString());
        getIntent().putExtra(SCAN_CONTENT, rawResult.getText());

        barcodeView.pause();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        barcodeView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(true);
        if(flash)
            barcodeView.setTorchOn();
        torchOff();
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
        if(flash)
            barcodeView.setTorchOn();
        torchOff();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        barcodeView.resume();
        if(flash)
            barcodeView.setTorchOn();
        torchOff();
    }

    @Override
    protected void onPause() {
        super.onPause();

        torchOff();
        barcodeView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        torchOff();
        barcodeView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        torchOff();
        barcodeView.pause();
    }

    public void torchOff() {
        barcodeView.setTorchOff();
        flash = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(flash)
            barcodeView.setTorchOn();
        torchOff();
    }
}
