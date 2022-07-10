package Q4;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 模拟linux命令处理
 */
public class BasicShell {
    public static void main(String[] args) throws IOException {
        System.out.println("请输入linux命令，输入-1结束");
        Scanner sc = new Scanner(System.in);
        while (true){
            String input = sc.nextLine();
            if (input.equals("-1")) break;
            //获得处理的结果并且输出
            dealCommand(input).forEach(System.out :: println);
        }
    }

    /**
     * 处理命令，获取输出结果的list
     * @param command
     * @return
     * @throws IOException
     */
    public static List<String> dealCommand(String command) throws IOException {
        // 根据|分割命令 把字符串中 含有 on 里边的元素切割掉 并且放到 list 中
        List<String> splitCommandList = Splitter.on("|").trimResults().splitToList(command);
        // 根据命令获取需要读取的文件名
        String fileName = getFileName(splitCommandList.get(0));
        // 读取当前文件
        List<String> readLines = Files.readLines(new File(fileName), Charsets.UTF_8);
        // 默认第一次的输入结果为读取到的文件的内容
        List<String> result = readLines;
        // cat src/main/java/Q4/a.txt
        // grep xml
//        splitCommandList.forEach(System.out::println);
        // 遍历splitCommandList数组，即已经把命令分离的数组
        for (String partCommand:splitCommandList) {
            // 获取到处理的命令用于判断该调用哪一个方法
            String dealCommand = Splitter.on(" ").splitToList(partCommand).get(0);
            if(dealCommand.equals("wc")){
                readLines = dealWcCommand(result);
            }else if(dealCommand.equals("cat")){
                readLines = dealCatCommand(result);
            }else if(dealCommand.equals("grep")){
                readLines = dealGrepCommand(result,Splitter.on(" ").splitToList(partCommand).get(1));
            }
            //上一次命令的结果会作为下一次命令的输入
            result = readLines;
        }
        return result;
    }

    /**
     * 处理cat命令
     * @param input
     * @return
     */
    public static List<String> dealCatCommand(List<String> input){
        return  input;
    }

    /**
     * 处理grep命令
     * @param input 上一次命令的结果的输出作为这次命令的输入
     * @param keyword 关键词
     * @return
     */
    public static List<String> dealGrepCommand(List<String> input,String keyword){
        ArrayList<String> result = Lists.newArrayList();
        for (String str:input) {
            if(str.contains(keyword)){
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 处理 Wc -l 命令
     * @param input
     * @return
     */
    public static List<String> dealWcCommand(List<String> input){
//        ArrayList<String> result = Lists.newArrayList();
        ArrayList<String> result = new ArrayList<>();
        result.add(String.valueOf(input.size()));
        return result;
    }

    /**
     * 获取命令里的文件名
     * @param command linux命令
     * @return 文件名
     */
    public static String getFileName(String command){
        List<String> commandList = Splitter.on(" ").splitToList(command);
        // 根据命令的不同，文件名存储于list的索引也不同
        // cat命令 文件名在第1个位置  其余命令 文件名在第2个位置(从0开始)
        if(commandList.get(0).equals("cat")){
            return commandList.get(1);
        }else {
            return commandList.get(2);
        }
    }


}
