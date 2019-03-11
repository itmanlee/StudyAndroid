package com.wangkeke.virtualapkhost;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

import java.io.File;
import java.io.IOException;

public class HostApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            loadApkplugin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadApkplugin() throws Exception {

        String pluginDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xsyplugin";
        createDir(pluginDir);
        PluginManager pluginManager = PluginManager.getInstance(this);
        File apk = new File(pluginDir, "plugin.apk");

        if (apk.exists()) {
            pluginManager.loadPlugin(apk);
        } else {
            Toast.makeText(getApplicationContext(),
                    "SDcard根目录未检测到plugin.apk插件", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建 文件夹
     *
     * @param dirPath 文件夹路径
     * @return 结果码
     */
    public static void createDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            //文件夹是否已经存在
            if (dir.exists()) {
                return;
            }
            if (!dirPath.endsWith(File.separator)) {//不是以 路径分隔符 "/" 结束，则添加路径分隔符 "/"
                dirPath = dirPath + File.separator;
            }
            //创建文件夹
            if (dir.mkdirs()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
