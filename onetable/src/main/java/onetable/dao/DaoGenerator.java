package onetable.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class DaoGenerator {
	// 「daoGeneratorConfig.xml」の「javaClientGenerator targetPackage」と合わせて、ソースファイルのパッケージ名を設定する
	private static String pakageName = "\\src\\main\\java\\jp\\tapf\\vttktd\\common\\dao";
	private static File configFile;
	private static String projectPath = System.getProperty("user.dir");
    static {
        String path = projectPath + "\\src\\main\\resources\\daoGeneratorConfig.xml";
        System.out.println(path);
        configFile = new File(path);
    }
    
	public static void main(String[] args)
            throws IOException,
                    XMLParserException,
                    InvalidConfigurationException,
                    SQLException,
                    InterruptedException {
		
        String folderName = projectPath + pakageName;
        // クリアフォルダー
        File folder=new File(folderName);
        deleteAllFiles(folder);
        
        if(!configFile.exists()) {
            System.out.println("配置ファイルが存在しない");
            return;
        }
        
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        
        // ソースファイル再編集
        traverseFile(folderName);
        System.out.println("自動作成完了しました");
    }
	

	public static void traverseFile(String path){
	    File file=new File(path);
	    if(file.exists()){
	        File[] f=file.listFiles();
	        for(File f2:f){
	            if(f2.isFile()){
	            	// クラス名前变更 Mapper 　→　Repository 
	            	String target1 = "Mapper ";
	            	String dest1 = "Dao ";
	            	boolean finish1 = false;

	            	BufferedReader br=null;
	                PrintWriter pw=null;
	                StringBuffer buff=new StringBuffer();
	                String line= "\r\n";
	                try {
	                    br=new BufferedReader(new FileReader(f2.getAbsolutePath()));
	                    for(String str=br.readLine();str!=null;str=br.readLine()) {
	                    	if(!finish1 && str.contains(target1)) {
	                            str=str.replaceAll(target1, dest1);
	                            finish1 = true;
	                        }
	                    	buff.append(str+line);
	                    }
	                    // ファイル名前变更　Mapper　→　Repository
	                    pw=new PrintWriter(new FileWriter(f2.getAbsolutePath().replaceAll("Mapper","Dao")),true);
	                    pw.println(buff);
	                } catch (FileNotFoundException e) {
	                    e.printStackTrace();
	                }catch (IOException e) {
	                    e.printStackTrace();
	                }finally {
	                    if(br!=null)
	                        try {
	                            br.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    if(pw!=null) 
	                        pw.close();
	                }
	                // 元も作成されたソースファイルを削除する
	                f2.delete();
	            }
	        }
	    }else{
	        System.out.println("ファイルが存在しない！");
	    }
	}
	
	public static void deleteAllFiles(File dirFile) {
		File[] files = dirFile.listFiles();
		 if(files == null) {
			 return;
		 }
		 for (int i = 0; i < files.length; i++) {   
	        if (files[i].isFile()) {
	        	files[i].delete();
	        } 
	    }
	}
}
