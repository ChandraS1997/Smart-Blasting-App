package com.smart_blasting_drilling.android.utils;

import android.graphics.Color;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Pair;
import kotlin.TypeCastException;


public class StringUtill {


    public static String formatDouble(double d){
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(d);
    }

    public static String formatDoubleDigitIntValue(long time) {
        NumberFormat f = new DecimalFormat("00");
        return f.format(time);
    }

    public static String formatDoubleDigitInt(int d){
        DecimalFormat format = new DecimalFormat("##");
        return format.format(d);
    }

    public static double getDoubleFromString(String value){
        if(TextUtils.isEmpty(value))
            return 0;
        else
            try {
                return Double.parseDouble(value);
            }
            catch (Exception e){
                e.printStackTrace();
            }
           return 0;
    }

    public static String roundDouble(double d){
        return String.valueOf(Math.round(d));
    }
    public static int roundDoubleInInt(double d){
        return Math.toIntExact(Math.round(d));
    }
    public static String formatDouble2(double d){
        DecimalFormat format = new DecimalFormat("0.0");
        return format.format(d);
    }

    public static String getFirstWord(String text) {

        int index = text.indexOf(' ');

        if (index > -1) { // Check if there is more than one word.

            return text.substring(0, index).trim(); // Extract first word.

        } else {

            return text; // Text is the first word itself.
        }
    }
    public static int parceIntFromString(String s){
        if(TextUtils.isEmpty(s))
            return 0;
        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }
    public static String getSplitString(String txt,String regex,int pos){
        if(TextUtils.isEmpty(txt))
            return "";

        if(txt.contains(regex)) {
            String[] s = txt.split(regex);
            return s[pos];
        }
        return txt;
    }


    public int getIntFromString(String value){
        if(TextUtils.isEmpty(value))
            return 0;
        else return Integer.parseInt(value);
    }
    public static String capitalize(String str)
    {
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isEmpty(String val) {
        return val == null || val.length() == 0;
    }

    public static String getString(String val) {
        return isEmpty(val) ? "" : val;
    }



    public static List<String> getStringListFromString(String val, String expression) {
        List<String> array;
        Type lis = new TypeToken<List<String>>(){}.getType();
        array = new Gson().fromJson(new Gson().toJson(TextUtils.split(val,expression)), lis);
        return array;
    }

    public static TextView createLink(TextView targetTextView, String completeString,
                                      String partToClick, ClickableSpan clickableAction) {

        SpannableString spannableString = new SpannableString(completeString);
        int startPosition = completeString.indexOf(partToClick);
        int endPosition = completeString.lastIndexOf(partToClick) + partToClick.length();

        spannableString.setSpan(clickableAction, startPosition, endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return targetTextView;
    }

    public static void makeLinks(TextView textView, Pair... links) {
        SpannableString spannableString = new SpannableString(textView.getText());
        int startIndexOfLink = -1;

        for(Pair link:links){
            ClickableSpan clickableSpan = new ClickableSpan() {
                public void updateDrawState(@NotNull TextPaint textPaint) {
                    textPaint.setColor(Color.parseColor("#222426"));
                    textPaint.setUnderlineText(false);
                }

                public void onClick(@NotNull View view) {
                    CharSequence charSequence = ((TextView)view).getText();
                    if (charSequence == null) {
                        throw new TypeCastException("null cannot be cast to non-null type android.text.Spannable");
                    } else {
                        Selection.setSelection((Spannable)charSequence, 0);
                        view.invalidate();
                        ((View.OnClickListener)link.getSecond()).onClick(view);
                    }
                }
            };
            startIndexOfLink = textView.getText().toString().indexOf(link.getFirst().toString(),startIndexOfLink+1);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.getFirst().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText((CharSequence)spannableString, TextView.BufferType.SPANNABLE);
    }

    public static List<String> getListFromStringArray(String[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static boolean validateNumberVal(String val) {
        Pattern numberPat = Pattern.compile("\\d+");
        Matcher matcher = numberPat.matcher(val);
        return matcher.find();
    }

    public static boolean validateDoubleVal(String val) {
        if (validateNumberVal(val)) {
            return true;
        }
        Pattern numberPat = Pattern.compile("\\\\d+\\\\.\\\\d+");
        Matcher matcher = numberPat.matcher(val);
        return matcher.find();
    }

}
