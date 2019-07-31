package com.laka.live.util;

import android.view.View;

import com.laka.live.R;
import com.laka.live.shopping.widget.MaterialRippleLayout;


/**
 * Created by zhangwulin on 2016/2/15.
 * email 1501448275@qq.com
 */
public class RippleEffectHelper {

    public static MaterialRippleLayout addRippleEffectInView(View view) {
        return MaterialRippleLayout.on(view)
                .rippleColor(ResourceHelper.getColor(R.color.lgray))
                .rippleAlpha(0.1f)
                .rippleOverlay(true)
                .rippleInAdapter(false)
                .create();
    }

    public static MaterialRippleLayout addRippleEffectInView(View view, boolean isInAdapter) {
        return MaterialRippleLayout.on(view)
                .rippleColor(ResourceHelper.getColor(R.color.lgray))
                .rippleAlpha(0.1f)
                .rippleOverlay(true)
                .rippleInAdapter(isInAdapter)
                .create();
    }
}
