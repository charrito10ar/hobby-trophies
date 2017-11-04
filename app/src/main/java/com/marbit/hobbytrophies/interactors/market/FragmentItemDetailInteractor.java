package com.marbit.hobbytrophies.interactors.market;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marbit.hobbytrophies.wishes.dao.WishDAO;
import com.marbit.hobbytrophies.interfaces.market.FragmentItemDetailPresenterInterface;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import java.io.File;
import java.util.ArrayList;

public class FragmentItemDetailInteractor {
    private Context context;
    private ArrayList<File> listFilesItemToEncrypt;
    private FragmentItemDetailPresenterInterface presenter;
    private static final String TYPE_IMAGES_ROOT = "images_root";
    private static final String TYPE_IMAGES_BUCKET = "images_bucket";
    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_VIDEOS_ROOT = "videos_root";
    private static final String TYPE_VIDEOS_BUCKET = "videos_bucket";
    private static final String TYPE_VIDEO = "video";
    private static final String TYPE_AUDIO_ROOT = "audio_root";
    private static final String TYPE_AUDIO = "audio";
    private static final String TYPE_ARTIST = "artist";
    private static final String TYPE_ALBUM = "album";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private UploadTask uploadTask;

    public FragmentItemDetailInteractor(Context context, FragmentItemDetailPresenterInterface presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void performFileSearch() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");

        presenter.startActivityForResult(intent);

    }

    /***
     * Procesa los archivos seleecionados. Tanto si es uno sólo como si son una lista.
     * @param data
     * @return
     */
    public ArrayList<File> getFilesToSend(Intent data) {
        listFilesItemToEncrypt = new ArrayList<>();
        if (data != null) {
            if(data.getData() != null){
                Uri uri = data.getData();
                if(isFolder(data.getDataString())){
                    ArrayList<String> listPaths = getAllShownImagesPath(context, uri);
                    processList(listPaths);
                }else {
                    File file = new File(getPath(context, uri));
                    if(file.isDirectory()){
                        processDirectory(file);
                    }else {
                        addItemToEncrypt(file);
                    }
                }
            }else {
                ClipData clipData = data.getClipData();
                for(int i=0; i < clipData.getItemCount(); i++){
                    Uri uri = clipData.getItemAt(i).getUri();
                    if(isFolder(uri.getPath())){
                        ArrayList<String> listPaths = getAllShownImagesPath(context, uri);
                        processList(listPaths);
                    }else {
                        File file = new File(getPath(context, uri));
                        if(file.isDirectory()){
                            processDirectory(file);
                        }else {
                            addItemToEncrypt(file);
                        }
                    }
                }
            }
        }
        return listFilesItemToEncrypt;
    }

    private void processDirectory(File file) {
        File [] files = file.listFiles();
        for(int i = 0; i < files.length; i++){
            File fileIn = files[i];
            if(fileIn.isDirectory()){
                processDirectory(fileIn);
            }else {
                addItemToEncrypt(fileIn);
            }
        }
    }

    private void processList(ArrayList<String> listPaths) {
        for(int i = 0; i < listPaths.size(); i++){
            File file = new File(listPaths.get(i));
         //   FileItem fileItem = new FileItem(file, file.getName());
            addItemToEncrypt(file);
        }
    }

    private boolean isFolder(String dataString) {
        if(dataString.contains("_bucket")) return true;
        else return false;
    }

    private void addItemToEncrypt(File fileItem){
        listFilesItemToEncrypt.add(fileItem);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if (TYPE_IMAGE.equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (TYPE_VIDEO.equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (TYPE_AUDIO.equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    } // TODO: Hay mas casos que se tienen que contemplar. Por ejemplo Audio, Artistas, Albumnes
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
                else if (isBugReportDocument(uri)) {
                    Toast.makeText(context, "No tienes acceso a este archivo", Toast.LENGTH_SHORT).show();
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return uri.getPath();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isBugReportDocument(Uri uri) {
        return "com.android.shell.documents".equals(uri.getAuthority());
    }


    public ArrayList<String> getAllShownImagesPath(Context context, Uri uri) {
        final String docId;
        ArrayList<String> listOfAllImages = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Images.ImageColumns.BUCKET_ID + "=" + split[1],
                    null,
                    null);
            while (cursor.moveToNext()){
                final int column_index = cursor.getColumnIndexOrThrow("_data");
                String imagePath = cursor.getString(column_index);
                listOfAllImages.add(imagePath);
            }
        }
        return listOfAllImages;
    }

    public void publishNewItem(Item item) {
        StorageMetadata metadata = new StorageMetadata.Builder()
            .setContentType("image/jpg")
            .build();
        String itemId = saveInDataBase(item);
        for(int i=0; i < item.getImages().size(); i++){
            Uri image = Uri.fromFile(item.getImages().get(i));
            saveImageInFirebase(image, itemId, metadata, i);
        }
        presenter.publishItemSuccess(item);
    }

    @SuppressWarnings("VisibleForTests")
    private void saveImageInFirebase(Uri image, String itemId, StorageMetadata metadata, int number) {
        StorageReference storageReference = storage.getReference();
        StorageReference riversRef = storageReference.child("item-images/" + itemId + "/image_0" + number +".jpg");
        uploadTask = riversRef.putFile(image, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // presenter.onFailureUpload(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url = taskSnapshot.getDownloadUrl().toString();
                //presenter.publishItemSuccess();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    private String saveInDataBase(Item item){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_ITEMS).push();
        String itemId = mDatabase.getKey();
        item.setId(itemId);
        mDatabase.setValue(item);
        return itemId;
    }

    public void checkWishesList(Item item) {
        WishDAO wishDAO = new WishDAO();
        wishDAO.checkWishesListWithNewItem(context, item);
    }
}

