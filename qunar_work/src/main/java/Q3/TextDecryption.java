package Q3;

import java.io.*;
import java.util.Arrays;

public class TextDecryption {

    private static final String NATURAL_ORDER = "natureOrder";
    private static final String INDEX_ORDER = "indexOrder";
    private static final String CHAR_ORDER = "charOrder";
    // private static final String CHAR_ORDER_DESC = "charOrderDESC";
    private static final int READ_BUFF_SIZE = 512;
    private static final int WRITE_BUFF_SIZE = 1024;
    private static final int ARRAY_SIZE = 6110;

    // 原小说Reader, 已封装为CharArrayReader
    private static Reader templateReader;
    // 替换词组资源文件Reader, 已封装为CharArrayReader
    private static Reader configReader;
    // 替换完成文件Writer, 已封装为FileWriter
    private static Writer mergedWriter;
    // 输入输出路径
    static {
        try {
            templateReader = new BufferedReader(new FileReader("src/main/resources/Question 3/sdxl_template.txt"));
            configReader = new BufferedReader(new FileReader("src/main/resources/Question 3/sdxl_prop.txt"));
            mergedWriter = new BufferedWriter(new FileWriter("src/main/java/Q3/sdxl.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 自然顺序替换集
    private static String[] naturalOrderRep = new String[ARRAY_SIZE];
    // 索引顺序词条集
    private static String[] indexOrderRep = new String[ARRAY_SIZE];
    // 字符顺序词条集
    private static String[] charOrderRep = new String[ARRAY_SIZE];
    public static void main(String[] args) {
        execute();
//        System.out.println(Arrays.toString(naturalOrderRep));
//        System.out.println(Arrays.toString(indexOrderRep));
//        System.out.println(Arrays.toString(charOrderRep));
    }

    public static void execute() {
        try {
            // 初始化替换顺序
            initOrderRep();
            BufferedReader templateBuffReader = new BufferedReader(templateReader, READ_BUFF_SIZE);
            BufferedWriter mergedBuffWriter = new BufferedWriter(mergedWriter, WRITE_BUFF_SIZE);
            // 读取一行内容
            String s = templateBuffReader.readLine();
            while (s != null) {
                mergedBuffWriter.write(replaceWord(s));
                s = templateBuffReader.readLine();
                if (s != null)
                    mergedBuffWriter.write("\n");
            }
            mergedBuffWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                templateReader.close();
                configReader.close();
                mergedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换单词方法, 此方法假定每一行只有一个需要替换的词组
     * @param originalSentence 待替换的原始句子  唱的曲子是北宋大$natureOrder(0)“蝶恋花”词，写的正
     * @return 替换完成的句子   唱的曲子是北宋大词人欧阳修所作的“蝶恋花”词，写的正
     * @throws IOException
     */
    private static String replaceWord(String originalSentence) throws IOException {
        // 寻找替换位置
        int dollar = originalSentence.indexOf('$');
        // 存在可替换字串
        if (dollar != -1) {
            int leftParenthese = -1;
            int rightParenthese = -1;
            // 获取左右括号坐标
            for (int i = dollar; i < originalSentence.length(); i++) {
                if (originalSentence.charAt(i) == '(') {
                    leftParenthese = i;
                } else if (originalSentence.charAt(i) == ')') {
                    rightParenthese = i;
                    // 找到完整的() 跳出循环
                    break;
                }
            }
            int numStart = leftParenthese + 1; // 编号开始位置
            int numEnd = rightParenthese - 1; // 编号结束位置
            int typeStart = dollar + 1; // 替换类型开始位置
            int typeEnd = leftParenthese - 1; // 替换类型结束位置
            int num = Integer.valueOf(originalSentence.substring(numStart, numEnd + 1)); // 替换单词编号
            String type = originalSentence.substring(typeStart, typeEnd + 1); // 替换单词类型
            String target = originalSentence.substring(dollar, rightParenthese + 1); // 待替换字符串

            if (type.equals(NATURAL_ORDER)) {
                originalSentence = originalSentence.replace(target, naturalOrderRep[num]);
            } else if (type.equals(INDEX_ORDER)) {
                originalSentence = originalSentence.replace(target, indexOrderRep[num]);
            } else if (type.equals(CHAR_ORDER)) {
                originalSentence = originalSentence.replace(target, charOrderRep[num]);
            } else {
                originalSentence = originalSentence.replace(target, charOrderRep[charOrderRep.length - num - 1]);
            }
        }
        return originalSentence;
    }

    /**
     * 初始化替换顺序，为替换数据做准备
     *
     * @throws IOException
     */
    private static void initOrderRep() throws IOException {
        // configReader为替换词组资源文件Reader
        BufferedReader configBuffReader = new BufferedReader(configReader, READ_BUFF_SIZE);
        String s;
        // 自然顺序索引
        int index = 0;
        while ((s = configBuffReader.readLine()) != null) {
            // 获得制表符的索引位置
            int tabIndex = s.indexOf('\t');
            // 得到该行的替换词组和索引
            // 例如1755	词人欧阳修所作的  则word为词人欧阳修所作的，即替换词组，wordIndex即1755
            String word = s.substring(tabIndex + 1, s.length());
            Integer wordIndex = Integer.parseInt(s.substring(0, tabIndex));
            // 将word放入数组
            naturalOrderRep[index] = word;
            indexOrderRep[wordIndex] = word;
            index++;
        }
        // 复制之前charOrderRep是一个全为null的数组
        // 原数组 源数组要复制的起始位置 目的数组 目的数组放置的起始位置 复制的长度
        System.arraycopy(naturalOrderRep, 0, charOrderRep, 0, naturalOrderRep.length);
        // 复制后，按照 java 的字符排序
        Arrays.sort(charOrderRep);
//        System.out.println(Arrays.toString(charOrderRep));
    }
}