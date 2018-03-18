package wordcount;
import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;

public class wc {
    //计数
    static int countChar = 0;
    static int countWord = 0;
    static int countLine = 0;
    static int countCodeLine = 0;
    static int countSpaceLine = 0;
    static int countComment = 0;

    //命令行参数标志，如果要压缩内存，可以考虑使用像微机原理中的标志位嘿嘿，但这儿就算了
    static int cFlag = 0;
    static int wFlag = 0;
    static int lFlag = 0;
    static int aFlag = 0;
    static int eFlag = 0;
    static int sFlag = 0;

    //路径
    static String inputPath = "";
    static String outputPath = "result.txt";
    static String stopListPath = "";
    static String[] stopList;

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

    //判断是否在停用词表中
    public static boolean isInStopList(String s){
        for(int i = 0; i < stopList.length; i++){
            if(s.equals(stopList[i])){
                return true;
            }
        }
        return false;
    }

    //读取停用词表
    public static void parseStopList(File file) throws IOException{
        //读取停用词文件
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file.getAbsolutePath()));
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        stopList = s.split(" ");
        isr.close();
    }

    public static void getWC(File file) throws IOException {
        String[] tStr;
        int nStopWord = 0;
        if (file.isFile() && file.getName().endsWith(".c")) {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file.getAbsolutePath()));
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while ((s = br.readLine()) != null)
            {
                System.out.print(s + "\n");
                if(isCommentLine(s)){
                    countComment++;
                }
                else if(isSpaceLine(s)){
                    countSpaceLine++;
                }
                else{
                    countCodeLine++;
                }
                countChar += s.length();
                tStr = s.split(" |,");
                if(eFlag == 1){
                    for(int i = 0; i < tStr.length; i++){
                        if(!isInStopList(tStr[i])){
                            countWord++;
                        }
                    }
                }
                else{
                    countWord += tStr.length;
                }
                //判断词是否在停用词表中
                countLine++;
            }
            isr.close();
            //重定向到文件
            String iPath = file.getAbsolutePath();
            //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(outputPath)), true));
            if(cFlag == 1){
                System.out.println(iPath + "，字数数：" + countChar);
                countChar = 0;
            }
            if(wFlag == 1){
                System.out.println(iPath + "，单词数：" + countWord);
                countWord = 0;
            }
            if(lFlag == 1){
                System.out.println(iPath + "，行数：" + countLine);
                countLine = 0;
            }
            if(aFlag == 1){
                System.out.println(iPath + "，代码行/空行/注释行：" + countCodeLine + "/" + countSpaceLine + "/" + countComment);
                countCodeLine = countSpaceLine = countComment = 0;
            }
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        //解析参数列表
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-c")){
                cFlag = 1;
            }
            else if(args[i].equals("-w")){
                wFlag = 1;
            }
            else if(args[i].equals("-l")){
                lFlag = 1;
            }
            else if(args[i].equals("-a")){
                aFlag = 1;
            }
            else if(args[i].equals("-s")){
                sFlag = 1;
            }
            //读取停用词表
            else if(args[i].equals("-e")){
                //往后看一位
                stopListPath = args[i + 1];
                i++;
                eFlag = 1;
                //如果不是TXT文件，则警告出错
                if(!stopListPath.endsWith(".txt")){
                    System.out.println("Error: No stop list!");
                }
                File stopFile = new File(stopListPath);
                parseStopList(stopFile);
            }
            //读取输出路径
            else if(args[i].equals("-o")){
                //往后看一位
                outputPath = args[i + 1];
                i++;
                if(!outputPath.endsWith(".txt")){
                    System.out.println("Error: No output file!");
                }
            }
            //读取输入文件路径
            else{
                inputPath = args[i];
            }
        }
        File file = new File(inputPath);
        //如果是文件夹，则递归处理下面的文件
        if(sFlag == 1) {
            if (file.isDirectory()) {
                //处理文件夹
                LinkedList<File> list = new LinkedList<File>();
                File[] files = file.listFiles();
                //遍历路径，如果是
                for (File pFile : files) {
                    if (pFile.isDirectory()) {
                        list.add(pFile);
                    } else {
                        getWC(pFile);
                    }
                }
                //不停从列表中弹出，如果是文件就getWC，本质上是一个宽度优先遍历
                File tFile;
                while (!list.isEmpty()) {
                    tFile = list.removeFirst();
                    files = tFile.listFiles();
                    for (File pFile : files) {
                        if (pFile.isDirectory()) {
                            list.add(pFile);
                        } else {
                            getWC(pFile);
                        }
                    }
                }
            }
        }
        else{
            getWC(file);
        }
        //重定向回控制台
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true));
    }
}