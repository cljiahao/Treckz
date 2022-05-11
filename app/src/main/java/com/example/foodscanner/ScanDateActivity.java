package com.example.foodscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanDateActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final static String TAG = "ScanDateActivity";

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ImageView flashBut;
    private TextView dateCaptured;
    private AppCompatButton captureDateBut;
    private Boolean flash = false;
    private TextRecognizer recognizer;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_date);

        init();

        startCamera();

        flashBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashUnit();
            }
        });

        captureDateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });

    }

    private void init() {

        previewView = findViewById(R.id.previewView);
        flashBut = findViewById(R.id.flashBut);
        captureDateBut = findViewById(R.id.captureDateBut);
        dateCaptured = findViewById(R.id.dateCaptured);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        surfaceHolder.addCallback(ScanDateActivity.this);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalyser(imageAnalysis);

        camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    private void capturePicture() {

        imageCapture.takePicture(ContextCompat.getMainExecutor(ScanDateActivity.this)
                , new ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeOptInUsageError")
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        super.onCaptureSuccess(image);
                        Image imgDate = image.getImage();
                        Log.d(TAG, String.valueOf(imgDate.getFormat()));
                        if (imgDate != null) {
                            Bitmap bmp = toBitmap(imgDate);

                            int height = bmp.getHeight();
                            int width = bmp.getWidth();

                            int[] dimen = rectDimension(height, width);

                            Bitmap bitmap = Bitmap.createBitmap(bmp, dimen[0], dimen[1], dimen[4], dimen[5]);

                            Task<Text> task = recognizer.process(InputImage.fromBitmap(bitmap, image.getImageInfo().getRotationDegrees()))
                                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                                        @Override
                                        public void onSuccess(Text text) {
                                            String resultText = text.getText();
                                            Bundle extras = getIntent().getExtras();
                                            if (extras != null) {
                                                String value = extras.getString("barcodeNum");
                                                Intent intent = new Intent(ScanDateActivity.this,RegiFoodForm.class);
                                                intent.putExtra("barcodeNum",value);
                                                intent.putExtra("dateScanned", resultText);
                                                startActivity(intent);
                                            }
                                            image.close();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            image.close();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        super.onError(exception);
                        Log.d(TAG, exception.toString());
                    }
                });
    }

    private void flashUnit() {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (flash.equals(false)) {
                flash = true;
                flashBut.setImageResource(R.drawable.ic_baseline_flash_off);
                camera.getCameraControl().enableTorch(true);
            } else if (flash.equals(true)) {
                flash = false;
                flashBut.setImageResource(R.drawable.ic_baseline_flash_on);
                camera.getCameraControl().enableTorch(false);
            }
        } else {
            flashBut.setImageResource(R.drawable.ic_baseline_flash_off);
            Toast.makeText(this, "No Flash Available!", Toast.LENGTH_SHORT).show();
        }
    }

    private void DrawFocusRect(int color) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = previewView.getHeight();
        int width = previewView.getWidth();

        int[] dimen = rectDimension(height, width);
        // TODO Add blur to the surrounding box
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        //border's properties
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(5);

        RectF rectf = new RectF(dimen[0], dimen[1], dimen[2], dimen[3]);
        canvas.drawRoundRect(rectf, 50, 50, paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void imageAnalyser(ImageAnalysis imageAnalysis) {
        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                Image mediaImage = imageProxy.getImage();
                if (mediaImage != null) {
                    Bitmap bmp = toBitmap(mediaImage);
                    if (bmp != null) {
                        int height = bmp.getHeight();
                        int width = bmp.getWidth();

                        int[] dimen = rectDimension(height, width);

                        Bitmap bitmap = Bitmap.createBitmap(bmp, dimen[0], dimen[1], dimen[4], dimen[5]);

                        Task<Text> task = recognizer.process(InputImage.fromBitmap(bitmap, imageProxy.getImageInfo().getRotationDegrees()))
                                .addOnSuccessListener(new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text text) {
                                        String resultText = text.getText();
                                        dateCaptured.setText(resultText);
                                        imageProxy.close();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        imageProxy.close();
                                    }
                                });
                    }
                }
            }
        });
    }

    private Bitmap toBitmap(Image image) {

        Bitmap bitmap = null;

        if (image.getFormat() == 1) {
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * image.getWidth();
            bitmap = Bitmap.createBitmap(image.getWidth() + rowPadding / pixelStride,
                    image.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
        } else if (image.getFormat() == 256) {
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byteBuffer.rewind();
            byte[] bytes = new byte[byteBuffer.capacity()];
            byteBuffer.get(bytes);
            byte[] clonedBytes = bytes.clone();
            bitmap = BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
        }

        return bitmap;
    }

    private int[] rectDimension(int height, int width) {

        int diameter;
        int[] dimen = new int[6];

        diameter = Math.min(height, width);

        int offset = (int) (0.05 * diameter);
        diameter -= offset;

        // Measurements for the box size
        dimen[0] = width / 2 - diameter / 3;     // Left
        dimen[1] = height / 2 - diameter / 3;    // Top
        dimen[2] = width / 2 + diameter / 3;     // Right
        dimen[3] = height / 2 + diameter / 5;    // Bottom

        dimen[4] = dimen[3] - dimen[1];         // Box Height
        dimen[5] = dimen[2] - dimen[0];          // Box Width

        return dimen;

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        DrawFocusRect(Color.parseColor("#b3dabb"));
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}