package com.qing.tewang.util;

/**
 * Created by 伍燎 on 2015/9/15.
 * 邮箱：dazhengxie@gmail.com
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qing.tewang.model.Location;
import com.qing.tewang.model.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }


    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


    public static void saveHistory(Context context, String str) {
        List<String> history = getHistorySearch(context);

        if (history != null && history.size() > 0) {
            Iterator<String> it = history.iterator();
            while (it.hasNext()) {
                String temp = it.next();
                if (temp.equals(str)) {
                    it.remove();
                }
            }
            history.add(0, str);
            setHistorySearch(context, history);
        } else {
            put(context, "HistorySearch", str);
        }
    }


    public static void setHistorySearch(Context context, List<String> list) {
        if (list != null && list.size() > 0) {
            //过略重复
            list = list.subList(0, list.size() >= 8 ? 8 : list.size());
            StringBuffer buffer = new StringBuffer();
            for (String str : list) {
                buffer.append(str + ",");
            }
            buffer.substring(0, buffer.length() - 1);
            put(context, "HistorySearch", buffer.toString());
        }
    }


    public static List<String> getHistorySearch(Context context) {
        String temp = (String) get(context, "HistorySearch", new String(""));
        if (temp != null && !TextUtils.isEmpty(temp)) {
            List<String> list = new ArrayList<String>();
            String[] temps = temp.split(",");
            for (String str : temps) {
                list.add(str);
            }
            return list;
        }
        return null;
    }

    public static boolean isLogin(Context context) {
        return getUser(context) != null;
    }


    public static void saveUser(Context context, User user) {
        if (user == null) {
            DisplayUtils.getInstance().showMsg(context, "异常!请退出重试!");
            return;
        }

        put(context, "curr_user", new Gson().toJson(user));
    }


    public static void saveLocation(Context context, Location location) {
        if (location == null) {
            DisplayUtils.getInstance().showMsg(context, "异常!请退出重试!");
            return;
        }

        put(context, "location", new Gson().toJson(location));
    }


    public static Location getLocation(Context context) {
        String str = (String) get(context, "location", "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Location location = null;
        try {
            location = new Gson().fromJson(str, Location.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return location;
    }

    public static void clearLocation(Context context) {
        remove(context, "location");
    }


    public static User getUser(Context context) {
        String str = (String) get(context, "curr_user", "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        User user = null;
        try {
            user = new Gson().fromJson(str, User.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void clearUser(Context context) {
        remove(context, "curr_user");
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }


}