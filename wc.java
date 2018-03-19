package wordcount;
import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;

public class wc {
    //����
    static int countChar = 0;
    static int countWord = 0;
    static int countLine = 0;
    static int countCodeLine = 0;
    static int countSpaceLine = 0;
    static int countComment = 0;

    //�����в�����־�����Ҫѹ���ڴ棬���Կ���ʹ����΢��ԭ���еı�־λ�ٺ٣������������
    static int cFlag = 0;
    static int wFlag = 0;
    static int lFlag = 0;
    static int aFlag = 0;
    static int eFlag = 0;
    static int sFlag = 0;
    static int isStarComment=0;//��/**/ע��
    //·��
    static String inputPath = "";
    static String outputPath = "result.txt";
    static String stopListPath = "";
    static String[] stopList;

    //�ж��Ƿ���ע����
    public static boolean isCommentLine(String s){
        //�������У�Ѱ��ע�ͷ���
        for(int i = 0; i < s.length() - 1; i++){
            if(s.charAt(i) == '/'){
                if(s.charAt(i + 1) == '/'){
                    return true;
                }
            	if(s.charAt(i + 1) == '*'){
            		isStarComment=1;
            		return true;
            	}
            }
        }
        return false;
    }
 public static boolean isCommentLineEnd(String s){
        //�������У�Ѱ��*/
        for(int i = 0; i < s.length() - 1; i++){
            if(s.charAt(i) == '/'){
                if(s.charAt(i + 1) == '*'&&isStarComment==1){
                	isStarComment=0;
                    return true;
                }
            }
        }
        return false;
    }

    //�ж��Ƿ��ǿ���
    public static boolean isSpaceLine(String s){
        int nChar = 0;
        //�������У����Ƿ��Ǹ�ʽ���Ʒ�
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

    //�ж��Ƿ���ͣ�ôʱ���
    public static boolean isInStopList(String s){
        for(int i = 0; i < stopList.length; i++){
            if(s.equals(stopList[i])){
                return true;
            }
        }
        return false;
    }

    //��ȡͣ�ôʱ�
    public static void parseStopList(File file) throws IOException{
        //��ȡͣ�ô��ļ�
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
                //System.out.print(s + "\n");
            	
                if(isCommentLine(s)||isStarComment==1){
                    countComment++;
                }
                if(isCommentLineEnd(s)){
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
                //�жϴ��Ƿ���ͣ�ôʱ���
                countLine++;
            }
            isr.close();
            //�ض����ļ�
            String iPath = file.getAbsolutePath();
            if(cFlag == 1){
                System.out.println(iPath + "����������" + countChar);
                countChar = 0;
            }
            if(wFlag == 1){
                System.out.println(iPath + "����������" + countWord);
                countWord = 0;
            }
            if(lFlag == 1){
                System.out.println(iPath + "��������" + countLine);
                countLine = 0;
            }
            if(aFlag == 1){
                System.out.println(iPath + "��������/����/ע���У�" + countCodeLine + "/" + countSpaceLine + "/" + countComment);
                countCodeLine = countSpaceLine = countComment = 0;
            }
        }
    }


    public static void main(String[] args) throws IOException, ParseException {
        //���������б�
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
            //��ȡͣ�ôʱ�
            else if(args[i].equals("-e")){
                //����һλ
                stopListPath = args[i + 1];
                i++;
                eFlag = 1;
                //�������TXT�ļ����򾯸����
                if(!stopListPath.endsWith(".txt")){
                    System.out.println("Error: No stop list!");
                }
                File stopFile = new File(stopListPath);
                parseStopList(stopFile);
            }
            //��ȡ���·��
            else if(args[i].equals("-o")){
                //����һλ
                outputPath = args[i + 1];
                i++;
                if(!outputPath.endsWith(".txt")){
                    System.out.println("Error: No output file!");
                }
            }
            //��ȡ�����ļ�·��
            else{
                inputPath = args[i];
            }
        }
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(outputPath)), true));
        File file = new File(inputPath);
        //������ļ��У���ݹ鴦��������ļ�
        if(sFlag == 1) {
            if (file.isDirectory()) {
                //�����ļ���
                LinkedList<File> list = new LinkedList<File>();
                File[] files = file.listFiles();
                //����·���������
                for (File pFile : files) {
                    if (pFile.isDirectory()) {
                        list.add(pFile);
                    } else {
                        getWC(pFile);
                    }
                }
                //��ͣ���б��е�����������ļ���getWC����������һ��������ȱ���
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
        //�ض���ؿ���̨
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)), true));
    }
}