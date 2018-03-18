package wordcount;
import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

public class wc {
    static int countChar = 0;
    static int countWord = 0;
    static int countLine = 0;
    static int countCode = 0;
    static int countSpaceLine = 0;
    static int countComment = 0;

    //判断是否是注释行
    public static boolean isCommentLine(String s){
        //遍历整行，寻找注释符号
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '/'){
                if(s.charAt(i + 1) == '/'){
                    return true;
                }
            }
        }
        return false;
    }

    //判断是否是空行
    public static boolean isSpaceLine(String s){
        int nChar = 0;
        //遍历整行，看是否是格式控制符
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' ' && s.charAt(i) != '\n' && s.charAt(i) != '\r' && s.charAt(i) != '\t'){
                nChar++;
            }
            if(nChar > 1){
                return false;
            }
        }
        return true;
    }

    //判断是否是代码行
    public static boolean isCodeLine(String s){
        return true;
    }

    //读取停用词表
    public static void parseStopList(File file, String[] stopList){
        //
    }

    public static void getWC(File file) throws IOException {
        if (file.isFile() && file.getName().endsWith(".c")) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file.getAbsolutePath()));
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while ((s = br.readLine()) != null)
            {
                System.out.print(s + "\n");
                countChar += s.length();
                countWord += s.split(" |,").length;
                countLine++;
            }
            isr.close();
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        //参数开关，可以扩展
        int cFlag = 0, wFlag = 0, lFlag = 0;
        String inputPath = "", outputPath = "result.txt", stopListPath = "";

        //解析参数列表
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-c")){
                cFlag = 1;
            }
            if(args[i].equals("-w")){
                wFlag = 1;
            }
            if(args[i].equals("-l")){
                lFlag = 1;
            }
            //读取输入文件路径
            if(args[i].endsWith(".c")){
                inputPath = args[i];
            }
            //读取停用词表
            if(args[i].equals("-e")){
                //往后看一位
                stopListPath = args[i + 1];
                //如果不是TXT文件，则警告出错
                if(!stopListPath.endsWith(".txt")){
                    System.out.println("Error: No stop list!");
                }
            }
            //读取输出路径
            if(args[i].equals("-o")){
                //往后看一位
                outputPath = args[i + 1];
                if(!outputPath.endsWith(".txt")){
                    System.out.println("Error: No output file!");
                }
            }
        }
        File file = new File(inputPath);
        getWC(file);
        //重定向到文件
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(outputPath)), true));
        if(cFlag == 1){
            System.out.println(inputPath + "，字数数：" + countChar);
        }
        if(wFlag == 1){
            System.out.println(inputPath + "，单词数：" + countWord);
        }
        if(lFlag == 1){
            System.out.println(inputPath + "，行数：" + countLine);
        }
        //重定向回控制台
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true));
        System.out.println("wordcount successful!");
    }
}