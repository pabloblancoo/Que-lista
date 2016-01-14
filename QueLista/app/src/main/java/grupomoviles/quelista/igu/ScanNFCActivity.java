package grupomoviles.quelista.igu;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import grupomoviles.quelista.R;
import grupomoviles.quelista.igu.recyclerViewAdapters.TicketAdapter;
import grupomoviles.quelista.logic.DownloadTicketFileTask;
import grupomoviles.quelista.logic.Ticket;

public class ScanNFCActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 5;
    public static final String BUFFERED = "buffered";
    public static final String PRODUCTS = "products";

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private TicketAdapter ticketAdapter;
    private RelativeLayout relativeLayout;
    ProgressDialog p;
    public static final String URLTAG = "URL";

    public TicketAdapter getTicketAdapter() {
        return ticketAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_nfc);
        mTextView = (TextView) findViewById(R.id.txIfno);
        ticketAdapter = new TicketAdapter(this, new Ticket());
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.layoutBotonTicket);

        relativeLayout.setVisibility(View.GONE);

        handleIntent(getIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {

            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);

                    break;
                }
            }
        }
    }

    /**
     * @param activity The corresponding requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(ScanNFCActivity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(ScanNFCActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding;
            if ((payload[0] & 128) == 0)
                textEncoding = "UTF-8";
            else
                textEncoding = "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            p = new ProgressDialog(ScanNFCActivity.this);
//            p.setMessage("Descargando imágenes...");
//            p.show();
//        }
        /**
         * Esto es lo que hace cuando lee
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

//                DownloadTicketFileTask downloadTicketFileTask = new DownloadTicketFileTask();
//                BufferedReader bufferedReader;
//                try {
//
//                    bufferedReader = downloadTicketFileTask.execute(result).get();
//                    ArrayList array = new ArrayList<String>();
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        array.add(line);
//                    }
//                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    FragmentTicket fragmentTicket = new FragmentTicket();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(BUFFERED, array);
//                    fragmentTicket.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.contenedor, fragmentTicket);
//                    fragmentTransaction.commit();
//                    relativeLayout.setVisibility(View.VISIBLE);
//
//                } catch (Exception e) {
//
//                }

                Intent i = new Intent();
                i.putExtra(URLTAG, result);
                setResult(RESULT_OK);
                finish();
//                p.dismiss();
            }
        }




    }

    public void guardarTicket(View view){
        Intent intent = new Intent();
        intent.putExtra(PRODUCTS, (Serializable) getTicketAdapter().getTicket().getProducts());
        setResult(RESULT_OK, intent);
        finish();
    }
}
