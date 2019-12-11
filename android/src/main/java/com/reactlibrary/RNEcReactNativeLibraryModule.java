package com.reactlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import in.juspay.godel.PaymentActivity;

/**
 * Created by kiran.puppala on 8/3/19.
 */

public class RNEcReactNativeLibraryModule extends ReactContextBaseJavaModule {

    private final static String LOG_TAG = "EC_HEADLESS_PLUGIN";
    private final static int REQUEST_CODE = 88;
    private final ReactApplicationContext reactContext;
    private Callback successCallback, errorCallback;
    /**** onActivityResult is called when Payment is finished inside PaymentActivity.class and returns back to
     * called Activity.
     * @param activity
     * @param requestCode   The request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode    The integer result code returned by the child activity through its setResult().
     * @param data          Returned by child activity i.e., PaymentActivity in this case. The PaymentResponse
     *                      can be extracted from intent object called data with key name "payload" and it is sent
     *                      back to JS using previous success and error callbacks.
     */
    private ActivityEventListener activityEventListener = new ActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if(successCallback == null || errorCallback == null){
                return;
            }
            String result = data.getStringExtra("payload");
            try {
                if (result != null) {
                    successCallback.invoke(result);
                } else {
                    errorCallback.invoke("null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorCallback.invoke(e.getMessage());
            }
        }

        @Override
        public void onNewIntent(Intent intent) {

        }
    };

    public RNEcReactNativeLibraryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(activityEventListener);
    }

    /**** Specifies the name of the library which can be imported in react-native's NativeModules */
    @Override
    public String getName() {
        return "EcReactNativeLibrary";
    }

    /**** Core function which constructs the appropriate payload from the incoming js request and initiates the payment by
     * triggering an intent to PaymentActivity.class.
     * @param paramString The stringified json payload to make payment
     * @param success The success callback used to send success response to JavaScript.
     * @param error The error callback used to send success response to JavaScript.
     */
    @ReactMethod
    private void startPayment(String paramString, Callback success, Callback error) {
        try {
            this.successCallback = success;
            this.errorCallback = error;
            JSONObject params = new JSONObject(paramString);
            JSONObject baseParams = params.optJSONObject("baseParams");
            JSONObject serviceParams = params.optJSONObject("serviceParams");
            JSONObject customParams = params.optJSONObject("customParams");

            Log.d(LOG_TAG, params.toString());
            Log.d(LOG_TAG, baseParams.toString());
            Log.d(LOG_TAG, serviceParams.toString());
            Log.d(LOG_TAG, customParams.toString());

            Bundle intentBundle = jsonToBundle(baseParams);
            intentBundle.putAll(jsonToBundle(serviceParams));
            intentBundle.putAll(jsonToBundle(customParams));

            Intent intent = new Intent(this.reactContext, PaymentActivity.class);
            intent.putExtras(intentBundle);

            this.reactContext.startActivityForResult(intent, REQUEST_CODE, intentBundle);

        } catch (Exception e) {
            error.invoke(e.getMessage());
        }
    }

    /**** Helper function which converts JSON Object to Bundle Object
     * @param jsonObject    Input JSON Object
     * @return Returns converted Bundle Object
     * @throws Exception    Throws JSONException and/or NullPointer Exception
     */
    private Bundle jsonToBundle(JSONObject jsonObject) throws Exception {
        Bundle bundle = new Bundle();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()){
            String key = (String) keys.next();
            Object value = jsonObject.getString(key);

            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                String[] stringArray = null;
                if (jsonArray != null) {
                    stringArray = new String[jsonArray.length()];
                    for (int j = 0; j < jsonArray.length(); j += 1) {
                        Object jsonElement = jsonArray.opt(j);
                        if (jsonElement instanceof String)
                            stringArray[j] = (String) jsonElement;
                    }
                }
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));
                bundle.putStringArrayList(key, arrayList);
            } else {
                if (value instanceof JSONObject) {
                    Log.d(LOG_TAG, value.toString());
                    bundle.putString(key, value.toString());
                } else {
                    Log.d(LOG_TAG, "Value is non Json Object");
                    bundle.putString(key, (String) value);
                }
            }
        }


        return bundle;
    }
}