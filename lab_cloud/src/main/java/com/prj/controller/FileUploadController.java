package com.prj.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.prj.service.AccountService;
import com.prj.service.FileUploadService;
import com.prj.service.StudentService;
import com.prj.util.AccountAccess;
import com.prj.util.AuthorityException;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;

@Controller
@RequestMapping("/Upload")
public class FileUploadController {
	@Resource(name = "AccountServiceImpl")
	AccountService as;
	@Resource(name = "StudentServiceImpl")
	StudentService ss;
	@Resource(name = "FileUploadServiceImpl")
	FileUploadService fs;
	
	@SuppressWarnings("rawtypes")
	@AccountAccess
	@RequestMapping(value = "/file/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper uploadFile(DataWrapper<MultipartFile> wrapper, @PathVariable int id, HttpServletRequest request) {
		DataWrapper ret = ss.getStudentById(id);
		if (!ret.getErrorCode().equals(ErrorCodeEnum.No_Error))
			return ret;
		
		MultipartFile file = wrapper.getData();
		String path = request.getSession().getServletContext().getRealPath("/files");
		return fs.saveFileById(path, file, id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/flash_file",method= RequestMethod.POST)
	@ResponseBody
	public DataWrapper uploadForFlash(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		String path = request.getSession().getServletContext().getRealPath("/flash_files");
		return fs.saveFlashFile(path, file);
	}
	
//	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/test", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper uploadFile(HttpServletRequest request) {
//		System.out.println("HERE");
////		System.out.println(request.getParameter("test"));
////		System.out.println(test);
//			try {
//			StringBuffer sb = new StringBuffer() ; 
//			InputStream is = request.getInputStream(); 
//			InputStreamReader isr = new InputStreamReader(is);   
//			BufferedReader br = new BufferedReader(isr); 
//			String s = "" ; 
//			while((s=br.readLine())!=null){ 
//			sb.append(s) ; 
//			} 
//			String str =sb.toString(); 
//			System.out.println(str);
//	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return new DataWrapper();
//	}
	
	@AccountAccess
	@RequestMapping(value = "/file/download", method = RequestMethod.POST)
	public void downloadFile(
			@RequestBody DataWrapper<String> wrapper, HttpServletRequest request, HttpServletResponse response) throws IOException {
		switch (wrapper.getAccountCharacter()) {
			case ADMINISTRATOR:
			case TEACHER:
				break;
			case STUDENT:
				String[] s = wrapper.getData().split("/");
				if (s.length > 3 && 
						!s[2].equals(ss.getStudentById(wrapper.getAccountId()).getData().getNumber()))
					return;
				break;
			default:
				break;
		}
		ServletContext context = request.getSession().getServletContext();
		String filePath = context.getRealPath(wrapper.getData());
		File file = new File(filePath);
		if(file.exists()){
			System.out.println("[Download File]"+filePath);
			String fileName = URLEncoder.encode(file.getName(), "UTF-8");
			
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
//			response.setContentType("application/msword");
			
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(response.getOutputStream());

				byte[] buff = new byte[2048];
				int bytesRead;

				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		}
	}
	
	@ExceptionHandler(AuthorityException.class)
	@ResponseBody
	public DataWrapper<Void> handleAuthorityException(AuthorityException ex) {
		System.out.println(ex.getErrorCode().getLabel());
		DataWrapper<Void> ret = new DataWrapper<Void>();
		ret.setErrorCode(ex.getErrorCode());
		return ret;
	}
}
