package com.example.musicplayer.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * can use this method to init this in Application or Activity
 * @code private void initPreferenceUtils() {
 *         try {
 *             Constructor<PreferenceUtils> method = PreferenceUtils.class.getDeclaredConstructor(Context.class);
 *             boolean isAccessible = method.isAccessible();
 *             method.setAccessible(true);
 *             method.newInstance(context);
 *             if (!isAccessible) {
 *                 method.setAccessible(false);
 *             }
 *         } catch (NoSuchMethodException e) {
 *             e.printStackTrace();
 *         } catch (InvocationTargetException e) {
 *             e.printStackTrace();
 *         } catch (IllegalAccessException e) {
 *             e.printStackTrace();
 *         } catch (InstantiationException e) {
 *             e.printStackTrace();
 *         }
 *     }
 *
 * @author aluca
 * @since 2022年5月15日
 */
public class PreferenceUtils {
    private static PreferenceUtils sInstance;
    private final SharedPreferences sharedPreferences;

    public static PreferenceUtils getsInstance() {
        return sInstance;
    }

    public static final String KEY_SORT_TYPE = "music_sort_type";

    private PreferenceUtils(Context context) {
        sInstance = this;
        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putStringSet(String key, Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 批量处理写入
     *
     * @param map V支持的格式是 int long float boolean string Set<String>
     */
    public void put(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
            put(editor, stringObjectEntry);
        }
        editor.commit();
    }

    private void put(SharedPreferences.Editor editor, Map.Entry<String, Object> entry) {
        if (entry == null || entry.getKey() == null || entry.getKey().length() == 0) {
            return;
        }

        if (entry.getValue() == null) {
            editor.remove(entry.getKey());
        }

        if (entry.getValue() instanceof Integer) {
            editor.putInt(entry.getKey(), (Integer) entry.getValue());
        } else if (entry.getValue() instanceof String) {
            editor.putString(entry.getKey(), (String) entry.getValue());
        } else if (entry.getValue() instanceof Set) {
            Set set = ((Set) entry.getValue());
            if (set.isEmpty()) {
                editor.remove(entry.getKey());
            } else if (set.iterator().next() instanceof String) {
                editor.putStringSet(entry.getKey(), set);
            }
        } else if (entry.getValue() instanceof Boolean) {
            editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
        } else if (entry.getValue() instanceof Float) {
            editor.putFloat(entry.getKey(), (Float) entry.getValue());
        } else if (entry.getValue() instanceof Long) {
            editor.putLong(entry.getKey(), (Long) entry.getValue());
        }
    }

}
