package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class ShareImage extends Godot.SingletonBase {

    static public Godot.SingletonBase initialize(Activity pActivity) {
        return new ShareImage(pActivity);
    }

    public ShareImage(Activity pActivity) {
        //register class name and functions to bind
        registerClass("ShareImage", new String[] {
            "init", "getAndroidExternalPath", "shareImage", "shareImageWithText"
        });
        mActivity = pActivity;
    }

    private Activity mActivity;
    private int mInstanceId;
    private String mShareTitle;
    private boolean mDebug;
    
    public void init(final int instanceId, final String shareTitle, final boolean debug) {
        mInstanceId = instanceId;
        mShareTitle = shareTitle;
        mDebug = debug;
    }

    public void getAndroidExternalPath(final String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);    
        showDebugToast("Returning path: " + file.toString());
        GodotLib.calldeferred(mInstanceId, "_on_path_returned", new Object[]{file.toString()});
    }

    public void shareImage(final String path){ 
        mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);  
                    showDebugToast("Sharing image with path: " + path);
                    mActivity.startActivity(Intent.createChooser(share, mShareTitle));
                }
        });
    }

    public void shareImageWithText(final String path, final String shareText){ 
        mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));
                    share.putExtra(Intent.EXTRA_TEXT, shareText);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    showDebugToast("Sharing image with path: " + path + "\n" + "Custom text: " + shareText);
                    mActivity.startActivity(Intent.createChooser(share, mShareTitle));
                }
        });
    }

    private void showDebugToast(final String message) {
        if(mDebug) {
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
