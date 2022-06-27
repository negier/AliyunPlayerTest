package com.example.aliyunplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorCode;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.source.UrlSource;

/**
 * todo 视频在抖音拉的，可能会过期，到时候重新在抖音上找一个来设置进uri就好了。
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 一
        //创建播放器
        AliPlayer aliPlayer = AliPlayerFactory.createAliPlayer(this);
        //埋点日志上报功能默认开启，当traceId设置为DisableAnalytics时，则关闭埋点日志上报。当traceId设置为其他参数时，则开启埋点日志上报。
        //建议传递traceId，便于跟踪日志。traceId为设备或用户的唯一标识符，通常为imei或idfa。
        aliPlayer.setTraceId("traceId");
        // 二
        // 设置监听器
        aliPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            //此回调会在使用播放器的过程中，出现了任何错误，都会回调此接口。

            @Override
            public void onError(ErrorInfo errorInfo) {
                ErrorCode errorCode = errorInfo.getCode(); //错误码
                String errorMsg =errorInfo.getMsg(); //错误描述
                //出错后需要停止掉播放器
                aliPlayer.stop();
            }
        });
        aliPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            // 调用aliPlayer.prepare()方法后，播放器开始读取并解析数据。成功后，会回调此接口。
            @Override
            public void onPrepared() {
                //一般调用start开始播放视频
                aliPlayer.start();
            }
        });
        aliPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            //播放完成之后，就会回调到此接口。
            @Override
            public void onCompletion() {
                //一般调用stop停止播放视频
                aliPlayer.stop();
            }
        });
        aliPlayer.setOnInfoListener(new IPlayer.OnInfoListener() {
            //播放器中的一些信息，包括：当前进度、缓存位置等等
            @Override
            public void onInfo(InfoBean infoBean) {
                InfoCode code = infoBean.getCode(); //信息码
                String msg = infoBean.getExtraMsg();//信息内容
                long value = infoBean.getExtraValue(); //信息值
                //当前进度：InfoCode.CurrentPosition
                //当前缓存位置：InfoCode.BufferedPosition
            }
        });
        aliPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            //播放器的加载状态, 网络不佳时，用于展示加载画面。
            @Override
            public void onLoadingBegin() {
                //开始加载。画面和声音不足以播放。
                //一般在此处显示圆形加载
            }

            @Override
            public void onLoadingProgress(int percent, float netSpeed) {
                //加载进度。百分比和网速。
            }

            @Override
            public void onLoadingEnd() {
                //结束加载。画面和声音可以播放。
                //一般在此处隐藏圆形加载
            }
        });
        // 三
        // 创建DataSource
        // 三.一 点播视频播放
        UrlSource urlSource = new UrlSource();
        urlSource.setUri("https://v26-web.douyinvod.com/4eda85064766cbe25edbe58a5ca8d796/62b91fb0/video/tos/cn/tos-cn-ve-15c001-alinc2/76c98d0e5d414d359e646f6f54cf91d1/?a=6383&ch=5&cr=0&dr=0&lr=all&cd=0%7C0%7C0%7C0&cv=1&br=1681&bt=1681&cs=0&ds=3&ft=X1nbLXvvBQx9Uf8ym8Z.wNnOYZlcJF5lF2bL3d01aiZm&mime_type=video_mp4&qs=0&rc=NDs8OTg1aWQ8NGU1aGQ0NkBpMzU6eWk6Zm9vZDMzNGkzM0A0YTBgXzAxNjAxNi8wMV5hYSNscWBvcjRfMTZgLS1kLTBzcw%3D%3D&l=2022062710103001021214003728ED3980");//播放地址，可以是第三方点播地址，或阿里云点播服务中的播放地址。
        aliPlayer.setDataSource(urlSource);
        // 四
        SurfaceView surfaceView = findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliPlayer.setSurface(holder.getSurface());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                aliPlayer.setSurface(null);
            }
        });
        // 可选
        aliPlayer.setAutoPlay(true);
        aliPlayer.prepare();

    }
}