package com.prj.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.prj.entity.Account;
import com.prj.service.AccountService;
import com.prj.service.FileUploadService;
import com.prj.util.AccountAccess;
import com.prj.util.AccountCharacter;
import com.prj.util.AuthorityException;
import com.prj.util.CallStatusEnum;
import com.prj.util.DataWrapper;
import com.prj.util.DateUtils;
import com.prj.util.LoginParameter;
import com.prj.util.SearchCriteria;

@Controller
@RequestMapping(value = "/Account")
public class AccountController {

	@Resource(name = "AccountServiceImpl")
	AccountService as;
	@Resource(name = "FileUploadServiceImpl")
	FileUploadService fs;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Account> login(@RequestBody DataWrapper<LoginParameter> loginParameter) {
		return as.login(loginParameter.getData(), loginParameter.getAccountCharacter());
	}
	
	@AccountAccess
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public DataWrapper<Void> logout(@RequestBody DataWrapper<?> wrapper) {
		return as.logout(wrapper.getAccountId(), wrapper.getAccountCharacter());
	}

	@AccountAccess
	@RequestMapping(value = "/search", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<List<? extends Account>> search(@RequestBody DataWrapper<SearchCriteria> wrapper) {
		SearchCriteria sc = wrapper.getData();
		return as.searchAccount(sc);
	}
	
	
	@RequestMapping(value = "/accountlist/role/{role}", method = RequestMethod.GET) 
	@ResponseBody
	public DataWrapper<List<? extends Account>> accountlistrole(@PathVariable AccountCharacter role) {
		
		return as.getAccountByRole(role);
	}
	
	@RequestMapping(value = "/accountlist/status/{status}", method = RequestMethod.GET) 
	@ResponseBody
	public DataWrapper<List<? extends Account>> accountliststatus(@PathVariable Account.Status status) {
		
		return as.getAccountByStatus(status);
	}
	
	@AccountAccess
	@RequestMapping(value = "/upload/icon", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<String> uploadIcon(DataWrapper<MultipartFile> wrapper, HttpServletRequest request) {
		DataWrapper<String> ret = new DataWrapper<String>();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		DataWrapper<String> fsRet = fs.saveIcon(wrapper.getData(), wrapper.getAccountId(), wrapper.getAccountCharacter(), rootPath);
		if (fsRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(fsRet.getErrorCode());
			return ret;
		}
		DataWrapper<Account> asRet = as.getAccountById(wrapper.getAccountId(), wrapper.getAccountCharacter());
		if (asRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(asRet.getErrorCode());
			return ret;
		}
		Account a = asRet.getData();
		a.setIconPath(fsRet.getData());
		asRet = as.updateAccount(a, wrapper.getAccountCharacter());
		if (asRet.getCallStatus().equals(CallStatusEnum.FAILED)) {
			ret.setErrorCode(asRet.getErrorCode());
			return ret;
		}
		ret.setData(fsRet.getData());
		return ret;
	}
	
	@RequestMapping(value = "/date", method = RequestMethod.GET) 
	@ResponseBody
	public DataWrapper<Long> getDate() {
		return new DataWrapper<Long>(DateUtils.getCurrentDateLong());
	}
	
//	@AccountAccess
//	@RequestMapping(value = "/profile", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> getAccount(@RequestBody DataWrapper<?> wrapper) {
//		return as.getAccountById(wrapper.getAccountId(), wrapper.getAccountCharacter());
//	}
//
//	@AccountAccess
//	@RequestMapping(value = "/reset", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> resetPassword(@RequestBody DataWrapper<PasswordReset> wrapper) {
//		wrapper.getData().setId(wrapper.getAccountId());
//		return as.reset(wrapper.getData(), wrapper.getAccountCharacter());
//	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/add/{ac}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> add(@RequestBody DataWrapper<? extends Account> wrapper, @PathVariable AccountCharacter ac) {
//		return as.addAccount((Account) wrapper.getData(), wrapper.getAccountCharacter());
//	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> getAccount(@RequestBody DataWrapper<?> wrapper, @PathVariable int id) {
//		return vs.getAccountById(id);
//	}
//	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/all", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<List<Account>> getAccountList(@RequestBody DataWrapper<?> wrapper) {
//		return vs.getAllAccount();
//	}
//	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
//	@ResponseBody
//	public DataWrapper<Account> deleteAccount(@PathVariable int id) {
//		return vs.disableAccountById(id);
//	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/updateAccountCharacter/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> updateAccountCharacter(@RequestBody DataWrapper<Account> account,  @PathVariable int id) {
//		return vs.updateAccountCharacter(id, account.getData().getAccountCharacter());
//	}
	
//	@AccountAccess(checkAccountCharacter = AccountCharacter.ADMINISTRATOR)
//	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public DataWrapper<Account> updateAccount(@RequestBody DataWrapper<Account> account,  @PathVariable int id) {
//		return vs.updateAccount(account.getData());
//	}
	
	@AccountAccess
	@RequestMapping(value = "/token/test", method = RequestMethod.POST) 
	@ResponseBody
	public DataWrapper<Void> testToken(@RequestBody DataWrapper<Void> wrapper) {
		return new DataWrapper<Void>();
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
