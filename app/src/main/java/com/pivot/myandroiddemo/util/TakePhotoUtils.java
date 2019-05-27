package com.pivot.myandroiddemo.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.zcolin.frame.permission.PermissionHelper;
import com.zcolin.frame.permission.PermissionsResultAction;
import com.zcolin.frame.util.DisplayUtil;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.gui.ZDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 相册/相机图片获取工具类
 */
public class TakePhotoUtils {
    private Uri mCutUri;
    private Activity mActivity;
    private ZDialog.ZDialogSubmitListener listener;

    public static final int TAKECAMERAREQUESTCODE = 301;//打开相机请求码
    public static final int TAKEPHOTOALBUMREQUESTCODE = 302;//打开相册请求码
    public static final int CUTPHOTOREQUESTCODE = 303;//打开裁剪图片请求码

    public TakePhotoUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void uploadHeaderImageListener(ZDialog.ZDialogSubmitListener listener) {
        this.listener = listener;
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        PermissionHelper.requestCameraPermission(mActivity, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                //创建一个file，用来存储拍照后的照片
                File outputfile = new File(mActivity.getExternalCacheDir(), "output.png");
                try {
                    if (outputfile.exists()) {
                        outputfile.delete();//删除
                    }
                    outputfile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri imageuri;
                if (Build.VERSION.SDK_INT >= 24) {
                    imageuri = FileProvider.getUriForFile(mActivity, "com.pivot.myandroiddemo.fileprovider", outputfile);//可以是任意字符串
                } else {
                    imageuri = Uri.fromFile(outputfile);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                mActivity.startActivityForResult(intent, TAKECAMERAREQUESTCODE);
            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    /**
     * 打开相册
     */
    public void openPhotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, TAKEPHOTOALBUMREQUESTCODE);
    }

    /**
     * 处理相机拍摄图片
     */
    public Intent cutForCamera(String camerapath, String imgname) {
        try {
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(), "cutcamera.png"); //随便命名一个
            if (cutfile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = null; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(camerapath, imgname);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(mActivity, "com.pivot.myandroiddemo.fileprovider", camerafile);
            } else {
                imageUri = Uri.fromFile(camerafile);
            }
            outputUri = Uri.fromFile(cutfile);
            //把这个 uri 提供出去，就可以解析成 bitmap了
            mCutUri = outputUri;
            //uri权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", DisplayUtil.dip2px(mActivity, 60));
            intent.putExtra("outputY", DisplayUtil.dip2px(mActivity, 60));
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 处理相册图片
     */
    public Intent cutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(), "cutcamera.png"); //随便命名一个
            if (cutfile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;

            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", DisplayUtil.dip2px(mActivity, 60)); //200dp
            intent.putExtra("outputY", DisplayUtil.dip2px(mActivity, 60));
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 获取裁剪后的图片
     */
    public void getCutPhotoAndUpload() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(mCutUri));
            File imgFile = new File(mActivity.getCacheDir().getPath() + "/head_img.png");
            if (imgFile.exists() && imgFile.isFile()) {
                imgFile.delete();
            }
            FileOutputStream out;
            out = new FileOutputStream(imgFile);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 60, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SPUtil.putString("headImgUrl", mActivity.getCacheDir().getPath() + "/head_img.png");
    }
}
