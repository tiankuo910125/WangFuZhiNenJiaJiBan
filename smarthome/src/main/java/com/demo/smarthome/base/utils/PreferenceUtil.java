package com.demo.smarthome.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * sharedPreference tools class
 * @author liukun
 * @date
 */
public class PreferenceUtil {

	private static String TAG="PreferenceUtil";
	private static SharedPreferences mSharedPreferences;
	private static Context mContext;

	private static synchronized SharedPreferences getPreferneces() {
		if (mSharedPreferences == null) {
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			Log.d(TAG,"DefaultSharePreferences path is "+mSharedPreferences.toString());
			Log.d(TAG,"DefaultSharePreferences is "+mSharedPreferences.getAll());
		}
		return mSharedPreferences;
	}

	public static void setContext(final Context context) {
		PreferenceUtil.mContext = context.getApplicationContext();
	}
	/**
	 * 打印�?��
	 */
	public static void print() {
		Log.d(TAG, "preference print: "+getPreferneces().getAll());
	}

	/**
	 * 清空保存在默认SharePreference下的�?��数据
	 */
	public static void clear() {
		getPreferneces().edit().clear().commit();
	}

	/**
	 * 保存字符�?
	 * @return
	 */
	public static void putString(String key, String value) {
		if(value!=null){
			SharedPreferences.Editor editor = getPreferneces().edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	/**
	 * 读取字符�?
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getPreferneces().getString(key, null);

	}

	/**
	 * 读取字符串由默认�?
	 * @param key
	 * @return
	 */
	public static String getString(String key, String defaultStr) {
		return getPreferneces().getString(key, defaultStr);

	}

	/**
	 * 保存整型�?
	 * @return
	 */
	public static void putInt(String key, int value) {
		SharedPreferences.Editor editor = getPreferneces().edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 读取整型�?
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		return getPreferneces().getInt(key, 0);
	}

	public static int getInt(String key,int value) {
		return getPreferneces().getInt(key, value);
	}
	/**
	 * 保存布尔�?
	 * @return
	 */
	public static void putBoolean(String key, Boolean value) {
		SharedPreferences.Editor editor = getPreferneces().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putLong(String key, long value) {
		SharedPreferences.Editor editor = getPreferneces().edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLong(String key) {
		return getPreferneces().getLong(key, 0);
	}

	/**
	 * t 读取布尔�?
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		return getPreferneces().getBoolean(key, defValue);

	}

	/**
	 * 移除字段
	 * @return
	 */
	public static void removeString(String key) {
		getPreferneces().edit().remove(key).commit();
	}
}
