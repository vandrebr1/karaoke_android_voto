package br.com.vandre.KeepCalmAndKaraokeVoto.atividades;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;

import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.CAMPO_LEITORCODIGOBARRAS;
import static br.com.vandre.KeepCalmAndKaraokeVoto.app.Constantes.TAG;


public class LeitorCodigoBarrasActivity extends Activity {

    SurfaceView camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scanbarcode);
        iniciar();
    }

    private void declarar() {
        camera = findViewById(R.id.scanbarcode_camera);

    }

    private void iniciar() {
        declarar();
        iniciarCameraLocal();
    }

    private void iniciarCameraLocal() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        camera.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(LeitorCodigoBarrasActivity.this, "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(LeitorCodigoBarrasActivity.this.camera.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(CAMPO_LEITORCODIGOBARRAS, barcodes.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}