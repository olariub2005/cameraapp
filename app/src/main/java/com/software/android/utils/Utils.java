package com.software.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;

import com.facebook.GraphResponse;
import com.facebook.share.model.SharePhoto;
import com.google.gson.reflect.TypeToken;
import com.software.android.entities.IdName;
import com.software.android.entities.Photo;
import com.software.android.entities.Story;
import com.software.android.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    public static final String EMPTY = "";
    public static final String CHARSET_NAME = "UTF-8";
    /**
     * Take screenshot of the activity including the action bar
     *
     * @param activity
     * @return The screenshot of the activity including the action bar
     */
    public static Bitmap takeScreenshot(Activity activity) {
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setDrawingCacheEnabled(true);
        decorChild.buildDrawingCache();
        Bitmap drawingCache = decorChild.getDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        decorChild.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * Print hash key
     */
    public static void printHashKey(Context context) {
        try {
            String TAG = "com.software.android.cameraapp";
            PackageInfo info = context.getPackageManager().getPackageInfo(TAG, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(TAG, "keyHash: " + keyHash);
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Update language
     *
     * @param code
     *            The language code. Like: en, cz, iw, ...
     */
    public static void updateLanguage(Context context, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Build alert dialog with properties and data
     *
     * @param pairs
     * @return {@link AlertDialog}
     */
    public static AlertDialog buildProfileResultDialog(Activity activity, Pair<String, String>... pairs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Pair<String, String> pair : pairs) {
            stringBuilder.append(String.format("<h3>%s</h3>", pair.first));
            stringBuilder.append(String.valueOf(pair.second));
            stringBuilder.append("<br><br>");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Html.fromHtml(stringBuilder.toString())).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static String toHtml(Object object) {
        StringBuilder stringBuilder = new StringBuilder(256);
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Object val = field.get(object);
                stringBuilder.append("<b>");
                stringBuilder.append(field.getName().substring(1, field.getName().length()));
                stringBuilder.append(": ");
                stringBuilder.append("</b>");
                stringBuilder.append(val);
                stringBuilder.append("<br>");
            }
        } catch (Exception e) {
            // Do nothing
        }
        return stringBuilder.toString();
    }

    public static byte[] getSampleVideo(Context context, String assetFile) {
        try {
            InputStream inputStream = context.getAssets().open(assetFile);
            return getBytes(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getBytes(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
///////////////////////////Simple fb - Utils/////////////////

    public String getFacebookSDKVersion() {
        String sdkVersion = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class<?> cls = classLoader.loadClass("com.facebook.FacebookSdkVersion");
            Field field = cls.getField("BUILD");
            sdkVersion = String.valueOf(field.get(null));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sdkVersion;
    }

    public static String getHashKey(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterator} into a single String
     * containing the provided elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     * </p>
     *
     * @param iterator
     *            the {@code Iterator} of values to join together, may be null
     * @param separator
     *            the separator character to use
     * @return the joined String, {@code null} if null iterator input
     */
    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? EMPTY : first.toString();
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterator} into a single String
     * containing the provided elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     * </p>
     *
     * @param <T>
     *
     * @param iterator
     *            the {@code Iterator} of values to join together, may be null
     * @param separator
     *            the separator character to use
     * @return the joined String, {@code null} if null iterator input
     */
    public static <T> String join(Iterator<T> iterator, String separator, Process<T> process) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        T first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? EMPTY : process.process(first);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(process.process(first));
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            T obj = iterator.next();
            if (obj != null) {
                buf.append(process.process(obj));
            }
        }
        return buf.toString();
    }

    public static String join(Map<?, ?> map, char separator, char valueStartChar, char valueEndChar) {

        if (map == null) {
            return null;
        }
        if (map.size() == 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(256);
        boolean isFirst = true;
        for (Entry<?, ?> entry : map.entrySet()) {
            if (isFirst) {
                buf.append(entry.getKey());
                buf.append(valueStartChar);
                buf.append(entry.getValue());
                buf.append(valueEndChar);
                isFirst = false;
            } else {
                buf.append(separator);
                buf.append(entry.getKey());
                buf.append(valueStartChar);
                buf.append(entry.getValue());
                buf.append(valueEndChar);
            }
        }

        return buf.toString();
    }

    public static List<String> extract(List<IdName> idNames) {
        List<String> names = new ArrayList<String>();
        for (IdName idName : idNames) {
            names.add(idName.getName());
        }
        return names;
    }

    public static List<User> extractUsers(List<IdName> idNames) {
        List<User> users = new ArrayList<User>();
        for (final IdName idName : idNames) {
            users.add(new User(idName));
        }
        return users;
    }

    public static <T> List<T> typedListFromResponse(GraphResponse response /*, Class<T> cls */) {
        return JsonUtils.fromJson(response.getRawResponse(), new TypeToken<List<T>>() {}.getType());
    }

    public static <T> List<T> typedListFromResponse(String raw) {
        return JsonUtils.fromJson(raw, new TypeToken<List<T>>() {
        }.getType());
    }

    public static class DataResult<T> {
        public List<T> data;
    }

    public static class SingleDataResult<T> {
        public T data;

        @Override
        public String toString() {
            if (data != null) {
                return data.toString();
            }
            return super.toString();
        }
    }

    public static <T> T convert(GraphResponse response, Type type) {
        return JsonUtils.fromJson(response.getRawResponse(), type);
    }

    public static <T> T convert(String json, Type type) {
        return JsonUtils.fromJson(json, type);
    }

    public static User convert(final IdName idName) {
        return new User(idName.getId(), idName.getName());
    }

    public interface Process<T> {
        String process(T t);
    }

    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            Object parameter = parameters.get(key);
            if (!(parameter instanceof String)) {
                continue;
            }

            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(key, CHARSET_NAME)).append("=").append(URLEncoder.encode(parameters.getString(key), CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                Logger.logError(Story.class, "Error enconding URL", e);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("resource")
    public static String encode(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            Formatter formatter = new Formatter(sb);
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.logError(Utils.class, "Failed to create sha256", e);
            return null;
        }
    }

    public static List<SharePhoto> extractBitmaps(List<Photo> photos) {
        List<SharePhoto> bitmaps = new ArrayList<SharePhoto>();
        for (Photo photo : photos) {
            Parcelable parcelable = photo.getParcelable();
            if (parcelable instanceof Bitmap) {
                SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap((Bitmap) parcelable).build();
                bitmaps.add(sharePhoto);
            }
        }
        return bitmaps;
    }

    public static <T> List<T> createSingleItemList(T t) {
        List<T> list = new ArrayList<T>();
        list.add(t);
        return list;
    }
}
