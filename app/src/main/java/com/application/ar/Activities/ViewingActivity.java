package com.application.ar.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.application.ar.myapplication.R;
import com.application.ar.renderer.SceneRenderer;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.assets.AssetHelper;
import org.artoolkit.ar.base.camera.CameraPreferencesActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.ArrayList;
import java.util.List;

public class ViewingActivity extends ARActivity{

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 133;

    private SceneRenderer sceneRenderer;
    private FrameLayout mainLayout;

    /**
     * Id du projet à charger. On pourrait obtenir cette information en scannant un QRCode à l'avenir.
     **/
    private int projectId = 8;
    private List<Integer> versionIDs;

    /**
     * Permet de définir si les scenes seront chargées sur le serveur ou en local (Deux scènes de test en local)
     * **/
    private boolean ONLINE = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);

        versionIDs = new ArrayList<>();

        Log.v("Step","onCreate");

        mainLayout = (FrameLayout)this.findViewById(R.id.glView);

        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(this.getApplication(), "Data");

        if (!checkCameraPermission()) {
           ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sceneRenderer = (SceneRenderer) renderer;
                int id = v.getId();

                switch(id)
                {
                    case R.id.glView :
                        getGLView().queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                sceneRenderer.click();
                            }
                        });
                        break;
                }

                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(40);
            }
        };

        mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(getApplicationContext(), CameraPreferencesActivity.class);
                startActivity(i);
                return true;
            }
        });

        mainLayout.setOnClickListener(clickListener);
    }

    @Override
    protected ARRenderer supplyRenderer() {
        if (!checkCameraPermission()) {
            Toast.makeText(this, "No camera permission - restart the app", Toast.LENGTH_LONG).show();
            return null;
        }

        Log.v("Step","supplyRenderer");

        sceneRenderer = new SceneRenderer(getApplicationContext(), ONLINE);

        return sceneRenderer;
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout)this.findViewById(R.id.glView);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
