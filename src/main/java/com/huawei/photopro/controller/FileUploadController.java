package com.huawei.photopro.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.huawei.photopro.config.PhotoConfig;
import com.huawei.photopro.pojo.FilePro;
import com.huawei.photopro.utils.FileUtils;

@Controller
public class FileUploadController {
	
	private final static Logger log=Logger.getLogger(FileUploadController.class);

	@Autowired
	private PhotoConfig config;

	@RequestMapping("/file")
	public String file() {
		log.info("");
		return "/file";
	}

	@RequestMapping("/")
	public String index(Model model) {
		FilePro single = new FilePro("aa", 1);
		List<FilePro> list = new ArrayList<FilePro>();
		FilePro p1 = new FilePro("bb", 2);
		FilePro p2 = new FilePro("cc", 3);
		FilePro p3 = new FilePro("dd", 4);
		list.add(p1);
		list.add(p2);
		list.add(p3);
		model.addAttribute("single", single);
		model.addAttribute("list", list);
		return "index";
	}

	/**
	 * 文件上传具体实现方法;
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping("/upload")
	// @ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest req) {
		if (!file.isEmpty()) {
			String reqUrl = req.getRequestURL().toString();
			String cdnUrl = reqUrl.substring(0, reqUrl.lastIndexOf(req.getServletPath())) + "/";
			String ff = file.getOriginalFilename();
			String path = req.getSession().getServletContext().getRealPath("");
			File fi = new File(path + ff);
			if (fi.exists())
				fi.delete();
			try {
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fi));
				out.write(file.getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				model.addAttribute("msg", "Upload failed: the reason is" + e.getMessage());
				return "fail";
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("msg", "Upload failed: the reason is" + e.getMessage());
				return "fail";
			}
			model.addAttribute("url", cdnUrl + ff);
			model.addAttribute("fileName", ff);
			return "succ";
		} else {
			model.addAttribute("msg", "Upload failed: the file is empty");
			return "fail";
		}
	}
	

	@RequestMapping(value="/showImg",method=RequestMethod.POST)
	public void showImg(HttpServletRequest req, HttpServletResponse res,@RequestBody String cc)
			throws IOException {
		
		// res.setContentType("application/octet-stream;charset=UTF-8");
		String url=null;
		try {
			url = new org.json.JSONObject(cc).getString("url");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		res.setContentType("image/png");
		// req.getContextPath();
		String path = req.getSession().getServletContext().getRealPath("");
		// String path=FileUploadController.class.getResource("/").getPath();
		// path=path.substring(0,path.lastIndexOf("target"));
		String temp = path + "download.jpg";
		String des = path + "pro.jpg";
		try {
			FileUtils.downloadRemoteFile(url, temp);
			FileUtils.makeRoundedCorner(temp, des, 180);
		} catch (Exception e) {
			e.printStackTrace();
		}

		OutputStream os = null;
		File fi = new File(des);
		FileInputStream fis = new FileInputStream(fi);
		byte[] b = new byte[fis.available()];
		fis.read(b);
		fis.close();
		byte[] bb = Base64Utils.encode(b);
		os = res.getOutputStream();
		os.write(bb);
		os.flush();
		os.close();
	}
	
	

}
