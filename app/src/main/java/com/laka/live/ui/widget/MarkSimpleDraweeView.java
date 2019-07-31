package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;

/**
 * Created by luwies on 16/4/11.
 */
public class MarkSimpleDraweeView extends SimpleDraweeView {

    private Bitmap mMarkBmp;

    private Paint mPaint;

    public MarkSimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public MarkSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkSimpleDraweeView(Context context) {
        super(context);
    }

    public MarkSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarkSimpleDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMark(int drawableID) {
        try {
            Drawable drawable = ContextCompat.getDrawable(getContext(), drawableID);
            if (drawable != null && drawable instanceof BitmapDrawable) {
                setMark(((BitmapDrawable) drawable).getBitmap());
            } else {
                setMark(null);
            }
        } catch (Resources.NotFoundException exception) {
            setMark(null);
        }
    }

    public void setMark(Bitmap bmp) {
        mMarkBmp = bmp;
        invalidate();
    }

    public void setMark(int auth, SizeType type) {
        AuthType authType = getAuthType((short) auth);
        setMark(authType, type);
    }

    public void setMark(AuthType auth, SizeType type) {
        int id = getMarkDrawableId(auth, type);
        if (id != 0) {
            setMark(id);
        } else {
            setMark(null);
        }
    }

    public void setMark(int auth, SizeType type, int level) {
        AuthType authType = getAuthType((short) auth);
        setMark(authType, type, level);
    }

    public void setMark(AuthType auth, SizeType type, int level) {
        int id = getMarkDrawableId(auth, type, level);
        if (id != 0) {
            setMark(id);
        } else {
            setMark(null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMark(canvas);
    }

    private void drawMark(Canvas canvas) {
        if (mMarkBmp != null) {
            if (mPaint == null) {
                mPaint = new Paint();
            }
            int width = getWidth();
            int height = getHeight();
            int markWidth = mMarkBmp.getWidth();
            int markHeight = mMarkBmp.getHeight();

            canvas.drawBitmap(mMarkBmp, width - markWidth, height - markHeight, mPaint);
        }
    }



    public enum SizeType {
        SMALL,
        MIDDLE,
        BIG
    }

    public enum AuthType {
        NONE,
        NORMAL,
        STAR
    }



    public static int getMarkDrawableId(AuthType authType, SizeType type) {

        if (authType == AuthType.NONE) {
            //没认证
            return 0;
        }
//        else if (authType == AuthType.NORMAL) {
//            if (type == SizeType.SMALL) {
//                return R.drawable.corner_icon_v02_60;
//            } else {
//                return R.drawable.corner_icon_v02;
//            }
//        }
        else if (authType == AuthType.STAR) {

            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_v_60;
            } else {
                return R.drawable.corner_icon_v;
            }
        }else {
            return 0;
        }
    }

    public static int getMarkDrawableId(AuthType authType, SizeType type, int level) {
        if (authType == AuthType.NONE) {
            //没认证
            return getLevelDrawableId(level, type);
        }
//        else if (authType == AuthType.NORMAL) {
//            if (type == SizeType.SMALL) {
//                return R.drawable.corner_icon_v02_60;
//            } else {
//                return R.drawable.corner_icon_v02;
//            }
//        }
        else if (authType == AuthType.STAR) {

            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_v_60;
            } else {
                return R.drawable.corner_icon_v;
            }
        }else {
            return 0;
        }
    }

    /*public static int getMarkDrawableId(AuthType authType, SizeType type) {
        if (authType == AuthType.NONE) {
            //没认证
//            return getLevelDrawableId(level, type);
        } else if (authType == AuthType.NORMAL) {
            if (type == SizeType.SMALL) {
                return R.drawable.mark_icon_personal03;
            } else if (type == SizeType.MIDDLE) {
                return R.drawable.mark_icon_personal02;
            } else if (type == SizeType.BIG) {
                return R.drawable.mark_icon_personal01;
            }
        } else {
            if (type == SizeType.SMALL) {
                return R.drawable.mark_icon_star03;
            } else if (type == SizeType.MIDDLE) {
                return R.drawable.mark_icon_star02;
            } else if (type == SizeType.BIG) {
                return R.drawable.mark_icon_star01;
            }
        }
        return 0;
    }*/

    public static AuthType getAuthType(String starVerified, String verified) {
        if (TextUtils.isEmpty(starVerified) == false) {
            return AuthType.STAR;
        } else if (TextUtils.isEmpty(verified) == false) {
            return AuthType.NORMAL;
        } else {
            return AuthType.NONE;
        }
    }

    public static AuthType getAuthType(short auth) {
        if (auth == 2) {
            return AuthType.STAR;
        } else if (auth == 1) {
            return AuthType.NORMAL;
        } else if (auth == 0) {
            return AuthType.NONE;
        } else {
            return AuthType.NONE;
        }
    }

    public static int getLevelDrawableId(int level, MarkSimpleDraweeView.SizeType type) {

        if (level < 10) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_leaf_60;
            } else {
                return R.drawable.corner_icon_leaf;
            }
        } else if (level >= 11 && level <= 20) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_star_60;
            } else {
                return R.drawable.corner_icon_star;
            }
        } else if (level >= 21 && level <= 30) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_moon_60;
            } else {
                return R.drawable.corner_icon_moon;
            }
        } else if (level >= 31 && level <= 40) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_sun_60;
            } else {
                return R.drawable.corner_icon_sun;
            }
        } else if (level >= 41 && level <= 45) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_dianm_60;
            } else {
                return R.drawable.corner_icon_dianm;
            }
        } else if (level >= 46 && level <= 50) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king06_60;
            } else {
                return R.drawable.corner_icon_king06;
            }
        } else if (level >= 51 && level <= 55) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king05_60;
            } else {
                return R.drawable.corner_icon_king05;
            }
        } else if (level >= 56 && level <= 60) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king04_60;
            } else {
                return R.drawable.corner_icon_king04;
            }
        } else if (level >= 61 && level <= 65) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king03_60;
            } else {
                return R.drawable.corner_icon_king03;
            }
        } else if (level >= 66 && level <= 99) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king02_60;
            } else {
                return R.drawable.corner_icon_king02;
            }
        } else if (level >= 100) {
            if (type == SizeType.SMALL) {
                return R.drawable.corner_icon_king01_60;
            } else {
                return R.drawable.corner_icon_king01;
            }
        }

        return 0;
    }
}
