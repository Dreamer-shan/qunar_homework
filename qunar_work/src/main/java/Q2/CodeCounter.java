package Q2;

import java.io.*;

public class CodeCounter {

    public static long totalCodeCount;
    public static long totalBlankCount;
    public static long totalRemarkCount;
    //统计代码文件所在的目录
    static final String PATH = "src/main/resources/Question 2";

    public static void main(String[] args) throws IOException {
        File projectFile = new File(PATH);
        countDir(projectFile);
        System.out.println("总行数：" + (totalCodeCount + totalBlankCount + totalRemarkCount));
        System.out.println("空行：" + totalBlankCount);
        System.out.println("注释行：" + totalRemarkCount);
        System.out.println("有效代码行：" + totalCodeCount);
        // 将结果写入validLineCount.txt
        writeFile("src/main/java/Q2/validLineCount.txt");
    }

    /**
     * @param file 要统计的文件夹路径
     * @throws IOException
     */
    private static void countDir(File file) throws IOException {
        if (file.isDirectory()) {
            // 遍历该目录下所有的文件夹
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                countDir(files[i]);
            }
        } else {
            countLines(file);
        }
    }

    /**
     * @param file 需要统计代码行数的文件
     */
    public static void countLines(File file) {
        BufferedReader br = null;
        // 判断此行是否为注释行
        boolean comment = false;
        // 空白行数
        int temp_whiteLines = 0;
        // 注释行数
        int temp_commentLines = 0;
        // 有效行数
        int temp_normalLines = 0;
        try {
            // 读取文件
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                // 去掉每行首尾的空格
                line = line.trim();
                if (line.matches("^[\\s&&[^\\n]]*$")) {
                    // 空行
                    temp_whiteLines++;
                } else if (line.startsWith("/*") && line.endsWith("*/")) {
                    // 判断此行为"/*xxx*/"的注释行
                    temp_commentLines++;
                } else if (line.startsWith("/*") && !line.endsWith("*/")) {
                    // 判断此行为"/*"开头的注释行
                    temp_commentLines++;
                    comment = true;
                } else if (comment == true && !line.endsWith("*/")) {
                    // 为多行注释中的一行（不是开头和结尾）
                    temp_commentLines++;
                } else if (comment == true && line.endsWith("*/")) {
                    // 为多行注释的结束行
                    temp_commentLines++;
                    comment = false;
                } else if (line.startsWith("//")) {
                    // 单行注释行
                    temp_commentLines++;
                } else {
                    // 正常代码行
                    temp_normalLines++;
                }
            }

            totalBlankCount += temp_whiteLines;
            totalRemarkCount += temp_commentLines;
            totalCodeCount += temp_normalLines;
            System.out.println("当前扫描文件：" + file.getAbsoluteFile() + " 有效代码行" + temp_normalLines + " 注释行" + temp_commentLines + " 空行" + temp_whiteLines);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 若文件已存在则直接覆盖，否则建立一个新的文件写入
     *
     * @param filePath 写入的文件路径
     */
    private static void writeFile(String filePath) {
        try {
            File writeName = new File(filePath);
            writeName.createNewFile();
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                // \r\n为换行
                out.write("总行数: " + (totalCodeCount + totalBlankCount + totalRemarkCount) + "\n");
                out.write("空行数: " + totalBlankCount + "\n");
                out.write("注释行数: " + totalRemarkCount + "\n");
                out.write("有效代码行数: " + totalCodeCount + "\n");
                // 把缓存区内容压入文件
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


