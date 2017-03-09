/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sudaotech.chatlibrary.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.sudaotech.chatlibrary.constroller.ChatUI;
import com.sudaotech.chatlibrary.model.DefaultEmojiconDatas;
import com.sudaotech.chatlibrary.model.Emojicon;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String ee_1 = "\\uD83D\\uDE00";
    //    public static final String ee_1 = "\\uD83D\\uDE01";
    public static final String ee_2 = "\\uD83D\\uDE02";
    public static final String ee_3 = "\\uD83D\\uDE03";
    public static final String ee_4 = "\\uD83D\\uDE04";
    public static final String ee_5 = "\\uD83D\\uDE09";
    public static final String ee_6 = "\\uD83D\\uDE0A";
    public static final String ee_7 = "\\uD83D\\uDE0C";
    public static final String ee_8 = "\\uD83D\\uDE0D";
    public static final String ee_9 = "\\uD83D\\uDE0F";
    public static final String ee_10 = "\\uD83D\\uDE12";
    public static final String ee_11 = "\\uD83D\\uDE13";
    public static final String ee_12 = "\\uD83D\\uDE14";
    public static final String ee_13 = "\\uD83D\\uDE16";
    public static final String ee_14 = "\\uD83D\\uDE18";
    public static final String ee_15 = "\\uD83D\\uDE1A";
    public static final String ee_16 = "\\uD83D\\uDE1C";
    public static final String ee_17 = "\\uD83D\\uDE1D";
    public static final String ee_18 = "\\uD83D\\uDE1E";
    public static final String ee_19 = "\\uD83D\\uDE20";
    public static final String ee_20 = "\\uD83D\\uDE21";
    public static final String ee_21 = "\\uD83D\\uDE22";
    public static final String ee_22 = "\\uD83D\\uDE23";
    public static final String ee_23 = "\\uD83D\\uDE25";
    public static final String ee_24 = "\\uD83D\\uDE28";
    public static final String ee_25 = "\\uD83D\\uDE2A";
    public static final String ee_26 = "\\uD83D\\uDE2D";
    public static final String ee_27 = "\\uD83D\\uDE30";
    public static final String ee_28 = "\\uD83D\\uDE31";
    public static final String ee_29 = "\\uD83D\\uDE32";
    public static final String ee_30 = "\\uD83D\\uDE33";
    public static final String ee_31 = "\\uD83D\\uDE37";
    public static final String ee_32 = "\\uD83D\\uDC7D";
    public static final String ee_33 = "\\uD83D\\uDC7F";
    public static final String ee_34 = "\\uD83C\\uDF19";
    public static final String ee_35 = "\\uD83D\\uDC94";
    public static final String ee_36 = "\\uD83D\\uDC98";
    public static final String ee_37 = "\\uD83D\\uDC4D";
    public static final String ee_38 = "\\uD83D\\uDC4E";
    public static final String ee_39 = "\\uD83D\\uDC4A";
    public static final String ee_40 = "\\uD83D\\uDC66";
    public static final String ee_41 = "\\uD83D\\uDC67";
    public static final String ee_42 = "\\uD83D\\uDC68";
    public static final String ee_43 = "\\uD83D\\uDC69";
    public static final String ee_44 = "\\uD83D\\uDCA9";
    public static final String ee_45 = "\\uD83C\\uDFB5";
    public static final String ee_46 = "\\uD83D\\uDCA6";
    public static final String ee_47 = "\\uD83D\\uDD25";

    public static final String ee_48 = "\ue002";
    public static final String ee_49 = "\ue005";
    public static final String ee_50 = "\ue004";

    public static final String ee_51 = "\ue04a";
    public static final String ee_52 = "\ue04b";
    public static final String ee_53 = "\ue049";
    public static final String ee_54 = "\ue048";
    public static final String ee_55 = "\ue005";
    public static final String ee_56 = "\ue004";


    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        Emojicon[] emojicons = DefaultEmojiconDatas.getData();
        for (Emojicon emojicon : emojicons) {
            addPattern(emojicon.getEmojiText(), emojicon.getIcon());
        }
        ChatUI.EmojiconInfoProvider emojiconInfoProvider = ChatUI.getInstance().getEmojiconInfoProvider();
        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
                addPattern(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * add text and icon to the map
     *
     * @param emojiText-- text of emoji
     * @param icon        -- resource id or local path
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
