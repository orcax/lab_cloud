package com.prj.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fr.base.FRContext;
import com.fr.base.ModuleContext;
import com.fr.base.Parameter;
import com.fr.dav.LocalEnv;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.WordExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.report.module.EngineModule;
import com.fr.stable.WriteActor;

public class DocumentGenerator {    
//	public static String generate(Long tableId, List<Long> dayIndexList, String name, String abbreviation) {
//		try {
//			WebApplicationContext context = ApplicationContextUtil.getContext();
//	    	Object dao = context.getBean(abbreviation.toUpperCase()+"TableDaoImpl");
//	    	Method m = dao.getClass().getMethod("get"+abbreviation.toUpperCase()+"TableById", Long.class);
//	    	Object table = m.invoke(dao,tableId);
//				m = table.getClass().getMethod("getProject");
//	    	Project p = (Project) m.invoke(table);
//	    	String rootPath = context.getServletContext().getRealPath("/");
//	    	String filePath = "report/" + p.getNumber();
//	    	String fileName = p.getNumber() + '-' + p.getName() + '-' + name;
//	    	return generate(rootPath, filePath, fileName, tableId, dayIndexList, abbreviation);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

    public static String generate(String rootPath, String filePath, String fileName, String abbreviation, String[] args) {
    	try {    
            // 首先需要定义执行所在的环境，这样才能正确读取数据库信息    
            String envPath = rootPath+"/WEB-INF";    
            FRContext.setCurrentEnv(new LocalEnv(envPath));    
            ModuleContext.startModule(EngineModule.class.getName());    
            // 读取模板    
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(),abbreviation.toLowerCase()+".cpt");    

            Parameter[] parameters = workbook.getParameters();  
            java.util.Map parameterMap = new java.util.HashMap();  
            for (int i = 0; i < parameters.length; i++) {
            	System.out.println(parameters[i].getName());
                parameterMap.put(parameters[i].getName(), args[i]);  
            }  
            // 使用paraMap执行生成结果
        	ResultWorkBook result = workbook.execute(parameterMap,new WriteActor());
            return output(rootPath, filePath, fileName, result); 
        } catch (Exception e) {    
            e.printStackTrace();  
            return null;
        }    	
    }
	private static String output(String rootPath, String filePath,
			String fileName, ResultWorkBook result)
			throws FileNotFoundException, Exception, IOException {
		// 使用结果如导出至doc 
		File file = new File(rootPath+'/'+filePath);
		if (!file.exists())
			file.mkdirs();
		String realPath = rootPath+'/'+filePath + '/' + fileName; 
		FileOutputStream outputStream = new FileOutputStream(realPath+".doc");    
		WordExporter wordExporter = new WordExporter();    
		wordExporter.export(outputStream, result);
		outputStream.close();  
		System.out.println("[Report]Create: "+realPath+".doc");
//		outputStream = new FileOutputStream(realPath+".pdf");    
//		PDFExporter pdfExporter = new PDFExporter();    
//		pdfExporter.export(outputStream, result);
//		System.out.println("[Report]Create: "+realPath+".pdf");
//		outputStream.close();    
		ModuleContext.stopModules(); 
		return realPath;
	}
}
