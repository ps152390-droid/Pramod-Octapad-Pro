package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.view.ViewCompat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public class ConstraintAttribute {
    private static final String TAG = "TransitionLayout";
    boolean mBooleanValue;
    private int mColorValue;
    private float mFloatValue;
    private int mIntegerValue;
    String mName;
    private String mStringValue;
    private AttributeType mType;

    public enum AttributeType {
        INT_TYPE,
        FLOAT_TYPE,
        COLOR_TYPE,
        COLOR_DRAWABLE_TYPE,
        STRING_TYPE,
        BOOLEAN_TYPE,
        DIMENSION_TYPE
    }

    public AttributeType getType() {
        return this.mType;
    }

    public void setFloatValue(float value) {
        this.mFloatValue = value;
    }

    public void setColorValue(int value) {
        this.mColorValue = value;
    }

    public void setIntValue(int value) {
        this.mIntegerValue = value;
    }

    public void setStringValue(String value) {
        this.mStringValue = value;
    }

    /* JADX INFO: renamed from: androidx.constraintlayout.widget.ConstraintAttribute$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType;

        static {
            int[] iArr = new int[AttributeType.values().length];
            $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType = iArr;
            try {
                iArr[AttributeType.COLOR_TYPE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.COLOR_DRAWABLE_TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.INT_TYPE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.FLOAT_TYPE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.STRING_TYPE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.BOOLEAN_TYPE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[AttributeType.DIMENSION_TYPE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public int noOfInterpValues() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                return 4;
            default:
                return 1;
        }
    }

    public float getValueToInterpolate() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 3:
                return this.mIntegerValue;
            case 4:
                return this.mFloatValue;
            case 5:
                throw new RuntimeException("Cannot interpolate String");
            case 6:
                return this.mBooleanValue ? 0.0f : 1.0f;
            case 7:
                return this.mFloatValue;
            default:
                return Float.NaN;
        }
    }

    public void getValuesToInterpolate(float[] ret) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                int i = this.mColorValue;
                int a = (i >> 24) & 255;
                int r = (i >> 16) & 255;
                int g = (i >> 8) & 255;
                int b = i & 255;
                float f_r = (float) Math.pow(r / 255.0f, 2.2d);
                float f_g = (float) Math.pow(g / 255.0f, 2.2d);
                float f_b = (float) Math.pow(b / 255.0f, 2.2d);
                ret[0] = f_r;
                ret[1] = f_g;
                ret[2] = f_b;
                ret[3] = a / 255.0f;
                return;
            case 3:
                ret[0] = this.mIntegerValue;
                return;
            case 4:
                ret[0] = this.mFloatValue;
                return;
            case 5:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 6:
                ret[0] = this.mBooleanValue ? 0.0f : 1.0f;
                return;
            case 7:
                ret[0] = this.mFloatValue;
                return;
            default:
                return;
        }
    }

    public void setValue(float[] value) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                int iHSVToColor = Color.HSVToColor(value);
                this.mColorValue = iHSVToColor;
                this.mColorValue = (iHSVToColor & ViewCompat.MEASURED_SIZE_MASK) | (clamp((int) (value[3] * 255.0f)) << 24);
                return;
            case 3:
                this.mIntegerValue = (int) value[0];
                return;
            case 4:
                this.mFloatValue = value[0];
                return;
            case 5:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 6:
                this.mBooleanValue = ((double) value[0]) > 0.5d;
                return;
            case 7:
                this.mFloatValue = value[0];
                return;
            default:
                return;
        }
    }

    public boolean diff(ConstraintAttribute constraintAttribute) {
        if (constraintAttribute == null || this.mType != constraintAttribute.mType) {
            return false;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                if (this.mColorValue == constraintAttribute.mColorValue) {
                }
                break;
            case 3:
                if (this.mIntegerValue == constraintAttribute.mIntegerValue) {
                }
                break;
            case 4:
                if (this.mFloatValue == constraintAttribute.mFloatValue) {
                }
                break;
            case 5:
                if (this.mIntegerValue == constraintAttribute.mIntegerValue) {
                }
                break;
            case 6:
                if (this.mBooleanValue == constraintAttribute.mBooleanValue) {
                }
                break;
            case 7:
                if (this.mFloatValue == constraintAttribute.mFloatValue) {
                }
                break;
        }
        return false;
    }

    public ConstraintAttribute(String name, AttributeType attributeType) {
        this.mName = name;
        this.mType = attributeType;
    }

    public ConstraintAttribute(String name, AttributeType attributeType, Object value) {
        this.mName = name;
        this.mType = attributeType;
        setValue(value);
    }

    public ConstraintAttribute(ConstraintAttribute source, Object value) {
        this.mName = source.mName;
        this.mType = source.mType;
        setValue(value);
    }

    public void setValue(Object value) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
                this.mColorValue = ((Integer) value).intValue();
                break;
            case 3:
                this.mIntegerValue = ((Integer) value).intValue();
                break;
            case 4:
                this.mFloatValue = ((Float) value).floatValue();
                break;
            case 5:
                this.mStringValue = (String) value;
                break;
            case 6:
                this.mBooleanValue = ((Boolean) value).booleanValue();
                break;
            case 7:
                this.mFloatValue = ((Float) value).floatValue();
                break;
        }
    }

    public static HashMap<String, ConstraintAttribute> extractAttributes(HashMap<String, ConstraintAttribute> base, View view) {
        HashMap<String, ConstraintAttribute> ret = new HashMap<>();
        Class<?> cls = view.getClass();
        for (String name : base.keySet()) {
            ConstraintAttribute constraintAttribute = base.get(name);
            try {
                if (name.equals("BackgroundColor")) {
                    ColorDrawable viewColor = (ColorDrawable) view.getBackground();
                    Object val = Integer.valueOf(viewColor.getColor());
                    ret.put(name, new ConstraintAttribute(constraintAttribute, val));
                } else {
                    Method method = cls.getMethod("getMap" + name, new Class[0]);
                    Object val2 = method.invoke(view, new Object[0]);
                    ret.put(name, new ConstraintAttribute(constraintAttribute, val2));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return ret;
    }

    public static void setAttributes(View view, HashMap<String, ConstraintAttribute> map) {
        Class<?> cls = view.getClass();
        for (String name : map.keySet()) {
            ConstraintAttribute constraintAttribute = map.get(name);
            String methodName = "set" + name;
            try {
                switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[constraintAttribute.mType.ordinal()]) {
                    case 1:
                        Method method = cls.getMethod(methodName, Integer.TYPE);
                        method.invoke(view, Integer.valueOf(constraintAttribute.mColorValue));
                        break;
                    case 2:
                        Method method2 = cls.getMethod(methodName, Drawable.class);
                        ColorDrawable drawable = new ColorDrawable();
                        drawable.setColor(constraintAttribute.mColorValue);
                        method2.invoke(view, drawable);
                        break;
                    case 3:
                        Method method3 = cls.getMethod(methodName, Integer.TYPE);
                        method3.invoke(view, Integer.valueOf(constraintAttribute.mIntegerValue));
                        break;
                    case 4:
                        Method method4 = cls.getMethod(methodName, Float.TYPE);
                        method4.invoke(view, Float.valueOf(constraintAttribute.mFloatValue));
                        break;
                    case 5:
                        Method method5 = cls.getMethod(methodName, CharSequence.class);
                        method5.invoke(view, constraintAttribute.mStringValue);
                        break;
                    case 6:
                        Method method6 = cls.getMethod(methodName, Boolean.TYPE);
                        method6.invoke(view, Boolean.valueOf(constraintAttribute.mBooleanValue));
                        break;
                    case 7:
                        Method method7 = cls.getMethod(methodName, Float.TYPE);
                        method7.invoke(view, Float.valueOf(constraintAttribute.mFloatValue));
                        break;
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, " Custom Attribute \"" + name + "\" not found on " + cls.getName());
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                Log.e(TAG, e2.getMessage());
                Log.e(TAG, " Custom Attribute \"" + name + "\" not found on " + cls.getName());
                Log.e(TAG, cls.getName() + " must have a method " + methodName);
            } catch (InvocationTargetException e3) {
                Log.e(TAG, " Custom Attribute \"" + name + "\" not found on " + cls.getName());
                e3.printStackTrace();
            }
        }
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public void setInterpolatedValue(View view, float[] value) {
        Class<?> cls = view.getClass();
        String methodName = "set" + this.mName;
        try {
            boolean z = true;
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
                case 1:
                    Method method = cls.getMethod(methodName, Integer.TYPE);
                    int r = clamp((int) (((float) Math.pow(value[0], 0.45454545454545453d)) * 255.0f));
                    int g = clamp((int) (((float) Math.pow(value[1], 0.45454545454545453d)) * 255.0f));
                    int b = clamp((int) (((float) Math.pow(value[2], 0.45454545454545453d)) * 255.0f));
                    int a = clamp((int) (value[3] * 255.0f));
                    int color = (a << 24) | (r << 16) | (g << 8) | b;
                    method.invoke(view, Integer.valueOf(color));
                    return;
                case 2:
                    Method method2 = cls.getMethod(methodName, Drawable.class);
                    int r2 = clamp((int) (((float) Math.pow(value[0], 0.45454545454545453d)) * 255.0f));
                    int g2 = clamp((int) (((float) Math.pow(value[1], 0.45454545454545453d)) * 255.0f));
                    int b2 = clamp((int) (((float) Math.pow(value[2], 0.45454545454545453d)) * 255.0f));
                    int a2 = clamp((int) (value[3] * 255.0f));
                    int color2 = (a2 << 24) | (r2 << 16) | (g2 << 8) | b2;
                    ColorDrawable drawable = new ColorDrawable();
                    drawable.setColor(color2);
                    method2.invoke(view, drawable);
                    return;
                case 3:
                    Method method3 = cls.getMethod(methodName, Integer.TYPE);
                    method3.invoke(view, Integer.valueOf((int) value[0]));
                    return;
                case 4:
                    Method method4 = cls.getMethod(methodName, Float.TYPE);
                    method4.invoke(view, Float.valueOf(value[0]));
                    return;
                case 5:
                    throw new RuntimeException("unable to interpolate strings " + this.mName);
                case 6:
                    Method method5 = cls.getMethod(methodName, Boolean.TYPE);
                    Object[] objArr = new Object[1];
                    if (value[0] <= 0.5f) {
                        z = false;
                    }
                    objArr[0] = Boolean.valueOf(z);
                    method5.invoke(view, objArr);
                    return;
                case 7:
                    Method method6 = cls.getMethod(methodName, Float.TYPE);
                    method6.invoke(view, Float.valueOf(value[0]));
                    return;
                default:
                    return;
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "cannot access method " + methodName + "on View \"" + Debug.getName(view) + "\"");
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "no method " + methodName + "on View \"" + Debug.getName(view) + "\"");
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }

    public static void parse(Context context, XmlPullParser parser, HashMap<String, ConstraintAttribute> custom) {
        AttributeSet attributeSet = Xml.asAttributeSet(parser);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomAttribute);
        String name = null;
        Object value = null;
        AttributeType type = null;
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomAttribute_attributeName) {
                name = a.getString(attr);
                if (name != null && name.length() > 0) {
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                }
            } else if (attr == R.styleable.CustomAttribute_customBoolean) {
                value = Boolean.valueOf(a.getBoolean(attr, false));
                type = AttributeType.BOOLEAN_TYPE;
            } else if (attr == R.styleable.CustomAttribute_customColorValue) {
                type = AttributeType.COLOR_TYPE;
                value = Integer.valueOf(a.getColor(attr, 0));
            } else if (attr == R.styleable.CustomAttribute_customColorDrawableValue) {
                type = AttributeType.COLOR_DRAWABLE_TYPE;
                value = Integer.valueOf(a.getColor(attr, 0));
            } else if (attr == R.styleable.CustomAttribute_customPixelDimension) {
                type = AttributeType.DIMENSION_TYPE;
                value = Float.valueOf(TypedValue.applyDimension(1, a.getDimension(attr, 0.0f), context.getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.CustomAttribute_customDimension) {
                type = AttributeType.DIMENSION_TYPE;
                value = Float.valueOf(a.getDimension(attr, 0.0f));
            } else if (attr == R.styleable.CustomAttribute_customFloatValue) {
                type = AttributeType.FLOAT_TYPE;
                value = Float.valueOf(a.getFloat(attr, Float.NaN));
            } else if (attr == R.styleable.CustomAttribute_customIntegerValue) {
                type = AttributeType.INT_TYPE;
                value = Integer.valueOf(a.getInteger(attr, -1));
            } else if (attr == R.styleable.CustomAttribute_customStringValue) {
                type = AttributeType.STRING_TYPE;
                value = a.getString(attr);
            }
        }
        if (name != null && value != null) {
            custom.put(name, new ConstraintAttribute(name, type, value));
        }
        a.recycle();
    }
}
