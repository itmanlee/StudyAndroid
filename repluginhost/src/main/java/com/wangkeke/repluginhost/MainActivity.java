package com.wangkeke.repluginhost;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnInstall,btnStart,btnStartForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initListener();

        Log.e("wangkeke","pluginclient已经被安装："+RePlugin.isPluginInstalled("pluginclient"));
    }

    private void initListener() {
        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xsyplugin";
                createDir(apkPath);
                Log.e("wangkeke","apkPath = "+apkPath);
                PluginInfo pluginInfo = RePlugin.install(apkPath + "/replugin.apk");
                if(null != pluginInfo){
                    Log.e("wangkeke","pluginInfo : "+pluginInfo.getAlias()+"   "+pluginInfo.getPackageName()+"   "+pluginInfo.getName());
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RePlugin.startActivity(MainActivity.this,RePlugin.createIntent("com.wangkeke.repluginone","com.wangkeke.repluginone.PluginOneActivity"));
            }
        });

        btnStartForResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(RePlugin.isPluginInstalled("pluginclient")){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("pluginclient","com.wangkeke.repluginone.PluginOneActivity"));
                    Bundle bundle = new Bundle();
                    bundle.putString("host","我是host传来的值");
                    intent.putExtra("bundle",bundle);
                    RePlugin.startActivityForResult(MainActivity.this,intent,100,null);
                }else {
                    Toast.makeText(MainActivity.this, "请先安装插件再使用！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        btnInstall = findViewById(R.id.btn_install);
        btnStart = findViewById(R.id.btn_start);
        btnStartForResult = findViewById(R.id.btn_start_result);
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
