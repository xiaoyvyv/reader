package com.xiaoyv.comic.datasource.jni.codec;

import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;


public class TextWord extends RectF {
    public String w;
    private RectF original;
    public int number;

    public TextWord() {
        super();
        w = "";
    }

    public TextWord(TextChar tc) {
        w = "";
        Add(tc);
    }


    @NonNull
    @Override
    public String toString() {
        return w + left + " " + top + " " + right + " " + bottom;
    }

    public void Add(TextChar tc) {
        super.union(tc);
        try {
            w = w.concat(new String(Character.toChars(tc.c)));
        } catch (Exception e) {
            w = w.concat("");
            Log.wtf("TextWord", e);
        }
    }

    public TextWord(String w, RectF rect) {
        super(rect);
        this.w = w;
    }

    public RectF getOriginal() {
        return original;
    }

    public void setOriginal(RectF original) {
        this.original = new RectF(original);
    }

    @Deprecated
    private String asString(TextWord[][] input, boolean isFiltering) {
        if (input == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (TextWord[] line : input) {

            for (int i = 0; i < line.length; i++) {
                TextWord word = line[i];
                // delete '-' from last word
                if (i == line.length - 1 && word.w.endsWith("-")) {
                    builder.append(word.w.replaceAll("-$", ""));
                } else {
                    builder.append(word.getWord() + " ");
                }
            }

        }
        return isFiltering ? filterString(builder.toString()) : builder.toString();
    }

    public String getWord() {
        return w;
    }


    public static String filterString(String txt) {
        if (txt == null || txt.isEmpty()) {
            return txt;
        }
        Log.d("TextWord", "filterString-begin: " + txt);
        String replaceAll = txt.trim().replace("   ", " ").replace("  ", " ").replaceAll("\\s", " ").trim();
        if (!txt.contains("http")) {
            replaceAll = replaceAll(replaceAll, "(\\w+)(-\\s)", "$1").trim();
        } else {
            replaceAll = replaceAll.replaceAll("\\s", "");
        }


        if (!replaceAll.contains(" ")) {
            //noinspection RegExpRedundantEscape
            String regexp = "[^\\w\\[\\]\\{\\}’']+";
            replaceAll = replaceAll(replaceAll, regexp + "$", "").replaceAll("^" + regexp, "");
        }
        if (replaceAll.endsWith(".]")) {
            replaceAll = replaceAll.replace(".]", "");
        }
        if (replaceAll.endsWith(")")) {
            replaceAll = replaceAll.replace(")", "");
        }
        if (replaceAll.startsWith("[") && !replaceAll.endsWith("]")) {
            replaceAll = replaceAll.replace("[", "");
        }


        Log.d("filterString-end", replaceAll.trim());
        return replaceAll.trim();
    }

    public static String replaceAll(String input, String regex, String replacement) {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                return Pattern.compile(regex, Pattern.UNICODE_CASE).matcher(input).replaceAll(replacement);
            } else {
                return Pattern.compile(regex).matcher(input).replaceAll(replacement);
            }
        } catch (Exception e) {
            Log.wtf("TextWord", e);
            return Pattern.compile(regex).matcher(input).replaceAll(replacement);
        }
    }
}
