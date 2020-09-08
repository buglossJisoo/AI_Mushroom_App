package com.android.tensor2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tensor2.classifier.ImageClassifier;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class CameraMushActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    // 카메라 불러오기 관련
    static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    // 앨범 불러오기 관련
    static final int REQUEST_IMAGE_PICK = 2;
    // 불러온 사진의 파일 경로
    private Uri filePath;

    // 이미지 변수(찍거나 or 불러왔을때 사진 뜨게함)
    ImageView image;


    // db 파일 불러오는 변수들
    public static final String ROOT_DIR = "/data/data/com.android.tensor2/databases/";
    public SQLiteDatabase db;
    public Cursor cursor;
    ProductDBHelper mHelper;
    String DBname = "Mushroom";

    //버섯이름
    TextView mush_name;
    TextView mname;
    //버섯종류
    TextView mush_type;
    TextView mtype;
    //증상
    TextView mush_sym;
    TextView msym;
    //닮은버섯
    TextView mush_sim;
    TextView msim;

    Button upload_button;
    SharedPreferences sharedPreferences;
    int fontSize;

    // tflite모델 관련 변수
    private ImageClassifier imageClassifier;
    private ListView listView;

    // 카메라 통해 찍은 사진 관련 변수
    // 사진 이름 변수
    private String mPhotoFileName = null;
    // 사진 파일
    private File mPhotoFile;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_mush);

        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("Image Classifier Error", "ERROR: " + e);
        }


        // 사진 업로드 버튼 클릭시
        Button upload = (Button) findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 뜨게함
                makeDialog();
            }
        });

        // 버섯 사진 이미지뷰 뜨게함
        image = findViewById(R.id.mush_image);

        // 버섯의 이름 나오게하는 textview
        mush_name= (TextView) findViewById(R.id.mush_name);
        mname = (TextView)findViewById(R.id.mname);

        // 버섯 타입 나오게하는 textview
        mush_type =(TextView)findViewById(R.id.mush_type);
        mtype = (TextView)findViewById(R.id.mtype);

        // 버섯 증상 나오게하는 textview
        mush_sym = (TextView)findViewById(R.id.mush_sym);
        msym = (TextView)findViewById(R.id.msym);

        // 유사 버섯 나오게하는 textview
        mush_sim = (TextView)findViewById(R.id.mush_sim);
        msim = (TextView)findViewById(R.id.msim);

        // 무슨 버섯인지 list 나오는 listview
        // 최대(MAX_SIZE) 1개까지만 나오게 설정함 (classifier folder 참고)
        listView = findViewById(R.id.lv_probabilities);

        upload_button = (Button)findViewById(R.id.upload_button);

        sharedPreferences = getSharedPreferences("SAMPLE_PREFERENCE", MODE_PRIVATE);

        mush_name.setTextSize((float)14);
        mname.setTextSize((float)14);
        mush_type.setTextSize((float)14);
        mtype.setTextSize((float)14);
        mush_sym.setTextSize((float)14);
        msym.setTextSize((float)14);
        mush_sim.setTextSize((float)14);
        msim.setTextSize((float)14);
        upload_button.setTextSize((float)14);

    }


    // DB파일 불러오는 함수
    // assets 폴더에 db파일 넣어주면됨~
    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        // db파일 이름 적어주기
        File outfile = new File(ROOT_DIR+"mush_10.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("mush_10.db", AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {

        }
    }


    // 사진 업로드 버튼 누르면 뜨는 다이얼로그
    // 앨범 선택 vs 사진 촬영 선택하는
    private void makeDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 업로드");
        builder.setMessage("업로드 할 방법을 선택해주세요.");
        builder.setNeutralButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.setPositiveButton("앨범 선택",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // 모든게 다 허용되어있으면 --> 앨범 오픈
                        if(hasAlbumPermission()){
                            openAlbum();
                        } else{
                            // 허용안된거 있으면 권한 요청
                            requestAlbumPermission();
                        }
                    }
                });
        builder.setNegativeButton("사진 촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 모든게 다 허용되어있으면 --> 카메라 오픈
                        if(hasCameraPermission()){
                            openCamera();
                        } else {
                            // 허용안된거 있으면 권한 요청
                            requestCameraPermission();
                        }
                    }
                });
        builder.show();
    }

    // 버섯 사진찍기 작동 시 일어나는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // camera 사용 code 이고 result_ok일시 작동
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // image_w /h (?,224,224,3) shape 3 -> rgb
            // tflite 모델 image가 width/height 각각  224, 224 size
            float[][][][] input = new float[1][224][224][3];

            int batchNum = 0;

            // 사진의 회전 정보 얻어오는 부분
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);


            // 찍은 사진 mPhotoFile을 bitmap으로 decodeFile
            Bitmap cameraphoto = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

            // 찍은 사진 rotate
            Bitmap cameraRotated = rotateBitmap(cameraphoto, orientation);
            // imageview 사진 뜨게함
            image.setImageBitmap(cameraRotated);

            for (int x = 0; x < 224; x++) {
                for (int y = 0; y < 224; y++) {
                    int pixel = cameraRotated.getPixel(x, y);
                    input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                    input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                    input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                }
            }

            // pass this bitmap to classifier to make prediction
            List<ImageClassifier.Recognition> predicitons = imageClassifier.recognizeImage(
                    cameraRotated, 0);
            // creating a list of string to display in list view
            // 리스트 뷰에 리스트 string으로 보여주게 하도록함
            final List<String> predicitonsList = new ArrayList<>();
            // 리스트 뷰 분류 결과 보여주기 위한 array 어댑터 생성
            ArrayAdapter<String> predictionsAdapter = new ArrayAdapter<>(
                    this, R.layout.listview_layout, predicitonsList);
            listView.setAdapter(predictionsAdapter);

            for (ImageClassifier.Recognition recog : predicitons) {
                // 일치 확률 50% 일시 listview 작성
                if (recog.getConfidence() * 100 >= 50) {
                    predicitonsList.add(recog.getName() + "와(과)  " + recog.getConfidence() * 100 + "%  일치");

                    ShowMushDBInfo(recog.getName());

                    // imageUri (찍은사진)이 있으면
                    if (imageUri != null) {
                        /*
                         * firebase기반으로 한 cloud storage -> 사용자가 찍은 image를 cloud storage로 넘김
                         */

                        // image를 cloud로 넘길때 이름
                        String filename = recog.getName() + recog.getConfidence() * 100 + "__" + currentDateFormat()+ ".jpg";

                        // image를 넘길 폴더
                        String foldername = recog.getName() + "/";

                        // reference 생성
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://mushroom-3c972.appspot.com").child(foldername + filename);

                        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            // coloud storage에 image 업로드 성공시
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("Firebase cloud storage","Upload Success!");
                            }
                        })
                                //실패시
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firebase cloud storage","Upload Fail!");
                                    }
                                })
                                //진행중
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.e("Firebase cloud storage","Loading!");
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                // 일치도 50% 미만이면 존재하는 버섯없다고 뜨게함.
                else if(recog.getConfidence() * 100 < 50){
                    mname.setText("없음");
                    mtype.setText("없음");
                    msym.setText("없음");
                    msim.setText("없음");
                }
            }
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            // album 사용 code 이고 result_ok일시 작동
            // image_w /h (?,224,224,3) shape 3 -> rgb
            // tflite 모델 image가 width/height 각각  224, 224 size
            float[][][][] input = new float[1][224][224][3];

            filePath = data.getData();

            // 사진의 절대 경로명
            Uri mPhotoUri = Uri.parse(getRealPathFromURI(filePath));

            ExifInterface exif = null;

            // inpuStream이라서 try / catch문 없애면 안됨
            try {

                int batchNum = 0;
                InputStream buf = getContentResolver().openInputStream(filePath);
                Bitmap albumphoto = BitmapFactory.decodeStream(buf);
                buf.close();

                // 사진의 회전 정보 얻어오는 부분
                exif = new ExifInterface(mPhotoUri.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 선택한 사진 rotate
                Bitmap albumRotated = rotateBitmap(albumphoto, orientation);
                // imageview 사진 뜨게함
                image.setImageBitmap(albumRotated);


                for (int x = 0; x < 224; x++) {
                    for (int y = 0; y < 224; y++) {
                        int pixel = albumRotated.getPixel(x, y);
                        input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                        input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                        input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                    }
                }

                // pass this bitmap to classifier to make prediction
                List<ImageClassifier.Recognition> predicitons = imageClassifier.recognizeImage(
                        albumRotated, 0);
                // creating a list of string to display in list view
                // 리스트 뷰에 리스트 string으로 보여주게 하도록함
                final List<String> predicitonsList = new ArrayList<>();
                // 리스트 뷰 분류 결과 보여주기 위한 array 어댑터 생성
                ArrayAdapter<String> predictionsAdapter = new ArrayAdapter<>(
                        this, R.layout.listview_layout, predicitonsList);
                listView.setAdapter(predictionsAdapter);

                for (ImageClassifier.Recognition recog : predicitons) {

                    if (recog.getConfidence() * 100 >= 50) {
                        predicitonsList.add(recog.getName() + "와(과)  " + recog.getConfidence() * 100 + "%  일치");

                        ShowMushDBInfo(recog.getName());

                        if (filePath != null) {

                            /*
                             * firebase기반으로 한 cloud storage -> 사용자가 찍은 image를 cloud storage로 넘김
                             */

                            // image를 cloud로 넘길때 이름
                            String filename = recog.getName() + recog.getConfidence() * 100 + "__" + currentDateFormat()+ ".jpg";

                            // image를 넘길 폴더
                            String foldername = recog.getName() + "/";

                            // reference 생성
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReferenceFromUrl("gs://mushroom-3c972.appspot.com").child(foldername + filename);


                            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                // coloud storage에 image 업로드 성공시
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("Firebase cloud storage","Upload Success!");
                                }
                            })
                                    //실패시
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firebase cloud storage","Upload Fail!");
                                        }
                                    })
                                    //진행중
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            Log.e("Firebase cloud storage","Loading!");
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(recog.getConfidence() * 100 < 50){
                        mname.setText("없음");
                        mtype.setText("없음");
                        msym.setText("없음");
                        msim.setText("없음");
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 사진의 절대경로명 찾게해줌 (album-rotate함수때 필요)
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx); cursor.close();
        }
        return result;
    }


    // 사진이 회전되어있으면 돌려주는 bitmap 함수
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    // 일치하는 버섯의 정보를 보여주는 함수
    private void ShowMushDBInfo(String name){
        setDB(this);

        mHelper=new ProductDBHelper(this);
        db =mHelper.getWritableDatabase();

        String mushname = null;
        String mushtype= null;
        String mushsym = null;
        String mushsim = null;
        String sql = "Select * FROM " + DBname;
        cursor = db.rawQuery(sql , null);

        while (cursor.moveToNext()) {
            if(cursor.getString(1).equals(name)) {
                mushname = cursor.getString(1);
                mushtype = cursor.getString(2);
                mushsym = cursor.getString(3);
                mushsim = cursor.getString(4);
            }
        }

        mname.setText(mushname);
        mtype.setText(mushtype);
        msym.setText(mushsym);
        msim.setText(mushsim);
    }


    // 현재 날짜 시간 String 함수
    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


    // 카메라 열어서 사진찍는 함수
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                imageUri = FileProvider.getUriForFile(this, "com.android.tensor2.fileprovider", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }


    // 앨범 열어서 사진 pick하는 함수
    private void openAlbum(){
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(albumIntent, REQUEST_IMAGE_PICK);
    }


    // 카메라 허용 권한이 있는지 check 함수
    private boolean hasCameraPermission() {
        //버전이 M 이상이면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // manifest camera 권한 있는지 확인
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    // 앨범 권한 허용이 있는지 check 함수
    private boolean hasAlbumPermission() {
        //버전이 M 이상이면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // manifest 외부저장소 권한 있는지 확인
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    // 카메리 허용 권한 물어보는 함수
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 카메라 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Log.e("Camera", "Camera Permission Required");
            }
            // 허용 안되어있으면
            // 카메라 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    // 앨범 허용 권한 물어보는 함수
    private void requestAlbumPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 외부저장소 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("External Storage", "Storage Permission Required");
            }
            // 허용 안되어있으면
            // 외부저장소 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // requestcode가 카메라 허용 코드이면
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // permission 다 허용되면
            if (hasAllPermissions(grantResults)) {
                // camera 열기
                openCamera();
            } else {
                requestCameraPermission();
            }
        }
        // requestcode가 앨범선택 허용 코드이면
        else if(requestCode == REQUEST_IMAGE_PICK){
            if (hasAllPermissions(grantResults)) {
                openAlbum();
            } else {
                requestAlbumPermission();
            }
        }
    }

    // permission이 다 허용 됐을시 true 반환
    //@param grantResults the permission grant results
    //@return true if all the reqested permission has been granted
    // otherwise returns false
    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        //프레퍼렌스로부터 글자크기 가져오기
        fontSize = sharedPreferences.getInt("FONT_SIZE",14);

        //글자크기변경하기
        mush_name.setTextSize((float)fontSize);
        mname.setTextSize((float)fontSize);
        mush_type.setTextSize((float)fontSize);
        mtype.setTextSize((float)fontSize);
        mush_sym.setTextSize((float)fontSize);
        msym.setTextSize((float)fontSize);
        mush_sim.setTextSize((float)fontSize);
        msim.setTextSize((float)fontSize);
        upload_button.setTextSize((float)fontSize);
    }
}