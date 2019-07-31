package framework;

import android.app.Application;

import com.laka.live.util.Log;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import framework.ioc.Ioc;

/**
 * Created by Lyf on 2017/3/15.
 * 初始化所有需要初始化的工具
 */
public class FrameWork {

    // 初始化
    public static void init(Application application) {

        // 控制反转初始化
        Ioc.initApplication(application);
        ImageLoaderConfiguration imageConfig = new ImageLoaderConfiguration.Builder( application)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(1500000)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();

        ImageLoader.getInstance().init(imageConfig);
    }

}
