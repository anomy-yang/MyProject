package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Pinyin4jUtil {

    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();

    static {
        //设置拼音小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //带v字符
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * @return
     */
    public static boolean containsChinese(String str){

        return str.matches(".*" + CHINESE_PATTERN + ".*");
    }

    /**
     * 简单实现：取每个汉字的第一个，然后拼接在一起返回
     * @param hanyu
     * @return
     */

    public static String[] get(String hanyu) {

        String[] array = new String[2];
        StringBuilder pinyin = new StringBuilder();
        StringBuilder pinyin_first = new StringBuilder();

        for(int i = 0;i < hanyu.length();i++){
            try {

                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i), FORMAT);

                //中文字符返回的字符串数组，可能为null或长度为0
                //返回原始的字符
                if (pinyins == null || pinyins.length == 0) {
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                } else {
                    //可以转换为拼音，只取第一个
                    pinyin.append(pinyins[0]);
                    pinyin_first.append(pinyins[0].charAt(0));
                }
            } catch (Exception e){
                //出现异常，返回原始字符
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0] = pinyin.toString();
        array[1] = pinyin_first.toString();

        return array;
    }




    /**
     *  返回所有拼音的排列组合
     * @param
     * @param fullSpell
     */

    public static String[][] get(String hanyu,boolean fullSpell){
        String[][] array = new String[hanyu.length()][];

        for(int i = 0;i < hanyu.length();i++){
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                if(pinyins == null || pinyins.length == 0){
                    array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                }else {
                    array[i] = unique(pinyins,fullSpell);
                }
            } catch (Exception e){
                array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return array;
    }
    //排列组合 两个合并，两个合并为一个，两两合并，最终合并为一个


    //去重
    //true为全拼，false去首字母
    private static String[] unique(String[] pinyins,boolean fullSpell) {
        Set<String> set = new HashSet<>();
        for(int i = 0;i < pinyins.length;i++){
            if(fullSpell){
                set.add(pinyins[i]);
            }else {
                set.add(String.valueOf(pinyins[i].charAt(0)));
            }
        }
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {

        System.out.println("=========================");
        String[] pinyins = get("中华a人民1共和国");
        System.out.println(Arrays.toString(pinyins));

       /* String[][] array = get("中华a人民1共和国",true);
        for(String[] a:array){
            System.out.println(Arrays.toString(a));
        }*/
        System.out.println("=========================");

        System.out.println(containsChinese("abd"));
        System.out.println(containsChinese("杨 1"));
    }

}
