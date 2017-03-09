package com.sudaotech.chatlibrary.model;


import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.utils.SmileUtils;

public class DefaultEmojiconDatas {

    private static String[] emojis = new String[]{
            SmileUtils.ee_1,
            SmileUtils.ee_2,
            SmileUtils.ee_3,
            SmileUtils.ee_4,
            SmileUtils.ee_5,
            SmileUtils.ee_6,
            SmileUtils.ee_7,
            SmileUtils.ee_8,
            SmileUtils.ee_9,
            SmileUtils.ee_10,
            SmileUtils.ee_11,
            SmileUtils.ee_12,
            SmileUtils.ee_13,
            SmileUtils.ee_14,
            SmileUtils.ee_15,
            SmileUtils.ee_16,
            SmileUtils.ee_17,
            SmileUtils.ee_18,
            SmileUtils.ee_19,
            SmileUtils.ee_20,
            SmileUtils.ee_21,
            SmileUtils.ee_22,
            SmileUtils.ee_23,
            SmileUtils.ee_24,
            SmileUtils.ee_25,
            SmileUtils.ee_26,
            SmileUtils.ee_27,
            SmileUtils.ee_28,
            SmileUtils.ee_29,
            SmileUtils.ee_30,
            SmileUtils.ee_31,
            SmileUtils.ee_32,
            SmileUtils.ee_33,
            SmileUtils.ee_34,
            SmileUtils.ee_35,
            SmileUtils.ee_36,
            SmileUtils.ee_37,
            SmileUtils.ee_38,
            SmileUtils.ee_39,
            SmileUtils.ee_40,
            SmileUtils.ee_41,
            SmileUtils.ee_42,
            SmileUtils.ee_43,
            SmileUtils.ee_44,
            SmileUtils.ee_45,
            SmileUtils.ee_46,
            SmileUtils.ee_47,
//            SmileUtils.ee_48,
//            SmileUtils.ee_49,
//            SmileUtils.ee_50,
//            SmileUtils.ee_51,
//            SmileUtils.ee_52,
//            SmileUtils.ee_53,
//            SmileUtils.ee_54,
//            SmileUtils.ee_55,
//            SmileUtils.ee_56,

    };

    private static int[] icons = new int[]{
            R.drawable.emo_001,
            R.drawable.emo_002,
            R.drawable.emo_003,
            R.drawable.emo_004,
            R.drawable.emo_005,
            R.drawable.emo_006,
            R.drawable.emo_007,
            R.drawable.emo_008,
            R.drawable.emo_009,
            R.drawable.emo_010,
            R.drawable.emo_011,
            R.drawable.emo_012,
            R.drawable.emo_013,
            R.drawable.emo_014,
            R.drawable.emo_015,
            R.drawable.emo_016,
            R.drawable.emo_017,
            R.drawable.emo_018,
            R.drawable.emo_019,
            R.drawable.emo_020,
            R.drawable.emo_021,
            R.drawable.emo_022,
            R.drawable.emo_023,
            R.drawable.emo_024,
            R.drawable.emo_025,
            R.drawable.emo_026,
            R.drawable.emo_027,
            R.drawable.emo_028,
            R.drawable.emo_029,
            R.drawable.emo_030,
            R.drawable.emo_031,
            R.drawable.emo_032,
            R.drawable.emo_033,
            R.drawable.emo_034,
            R.drawable.emo_035,
            R.drawable.emo_036,
            R.drawable.emo_037,
            R.drawable.emo_038,
            R.drawable.emo_039,
            R.drawable.emo_040,
            R.drawable.emo_041,
            R.drawable.emo_042,
            R.drawable.emo_043,
            R.drawable.emo_044,
            R.drawable.emo_045,
            R.drawable.emo_046,
            R.drawable.emo_047,
//            R.drawable.emo_048,
//            R.drawable.emo_049,
//            R.drawable.emo_050,
//            R.drawable.emo_051,
//            R.drawable.emo_052,
//            R.drawable.emo_053,
//            R.drawable.emo_054,
//            R.drawable.emo_055,
//            R.drawable.emo_056,

    };


    private static final Emojicon[] DATA = createData();

    private static Emojicon[] createData() {
        Emojicon[] datas = new Emojicon[icons.length];
        for (int i = 0; i < icons.length; i++) {
            datas[i] = new Emojicon(icons[i], emojis[i], Emojicon.Type.NORMAL);
        }
        return datas;
    }

    public static Emojicon[] getData() {
        return DATA;
    }
}
