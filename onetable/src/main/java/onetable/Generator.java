package onetable;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {
	
	private static File configFile;
    static {
    	String projectPath = System.getProperty("user.dir");
        String path = projectPath + "\\src\\main\\resources\\generatorConfig.xml";
        System.out.println(path);
        configFile = new File(path);
    }
    
	public static void main(String[] args)
            throws IOException,
                    XMLParserException,
                    InvalidConfigurationException,
                    SQLException,
                    InterruptedException {
        
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
        System.out.println("自動作成完了しました");
    }
}
