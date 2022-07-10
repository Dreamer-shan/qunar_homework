package Q1;

import com.google.common.collect.Multiset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CountLog {
    static final String FILEPATH = "src/main/resources/Question 1/access.log";
    public static void main(String args[]) throws FileNotFoundException {
        readFile();
    }

    /**
     * 读取log文件
     */
    public static void readFile() {
        try (FileReader reader = new FileReader(FILEPATH);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            int count = 0;
            int getCount = 0;
            int postCount = 0;
            // 计数用的map，键是uri, value是对应的数量
            Map<String,Integer> countMap = new TreeMap<>((o1,o2)->o1.compareTo(o2));
            // 每个种类的数量
            Map<String, ArrayList<String>> classMap = new HashMap<>();
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                count += 1;
                // 计算get和post的数量
                if ("GET ".equals(line.substring(0,4))){
                    getCount += 1;
                }else if ("POST ".equals(line.substring(0,5))){
                    postCount += 1;
                }
                // 将uri与参数分开
                String[] line1 = line.split("\\?");
                List<String> message = Arrays.asList(line1);
                // 若uri存在则数量+1，否则置为1
                if (!countMap.containsKey(message.get(0))){
                    countMap.put(message.get(0), 1);
                }else {
                    countMap.put(message.get(0),countMap.get(message.get(0)) + 1 );
                }
                // 按 AAA 分类
                String lineCopy = line;
                String[] line2 = lineCopy.split("/");
                if (classMap.containsKey(line2[1])){
                    classMap.get(line2[1]).add(lineCopy);
                }else {
                    classMap.put(line2[1], new ArrayList<String>(Collections.singleton(lineCopy)));
                }
            }
            System.out.println("getCount = " + getCount);
            System.out.println("postCount = " + postCount);
            // 有两个请求是HEAD,因此getCount + postCount = count - 2
            System.out.println("count = " + count);
            System.out.println("*************************");
//            for (String key : countMap.keySet()) {
//                System.out.println("接口: " + key + " 数量为: " + countMap.get(key));
//            }
            // 按值排序，排序后选出数量最多的10个键值对
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(countMap.entrySet());
            Collections.sort(entries, (o1, o2) -> (o2.getValue() - o1.getValue()));
            Iterator<Map.Entry<String, Integer>> it = entries.iterator();
            for (int i = 0; i < 10 && it.hasNext(); i++) {
                System.out.println(it.next().getKey()+ " --- " + it.next().getValue());
            }
            System.out.println("*************************");
            // 每个种类的数量
            int classCount = 0;
            for (String key : classMap.keySet()) {
                classCount += classMap.get(key).size();
                System.out.println("URI种类: " + key + " 包含的URI: " + classMap.get(key).size());
            }
            System.out.println(classCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


