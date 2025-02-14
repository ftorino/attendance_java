package com.zk.action;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.AttLog;
import com.zk.pushsdk.po.AttPhoto;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.FileOperateUtil;
import com.zk.util.PagenitionUtil;

public class AttLogAction implements ServletRequestAware,ServletResponseAware {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	/**
	 * Gets the list of Attendance logs
	 * @return
	 */
	public String attLogList() {
		int recCount = 0;
		int pageCount = 0;
		/**Gets the parameters of interface*/
		String deviceSn = request.getParameter("deviceSn");
		String userPin = request.getParameter("userPin");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Gets the count of attendance logs*/
		recCount = ManagerFactory.getAttLogManager().getAttLogCount(deviceSn, userPin);
		/**Gets the count of pages and the current page*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Gets the start logs*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Gets the list of logs by the page condition and page information*/
		List<AttLog> list = ManagerFactory.getAttLogManager().getAttLogList(deviceSn, userPin, startRec, PagenitionUtil.getPageSize());
		
		/** Sets the parameters of interface*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("attList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("user.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		if (null == userPin || userPin.isEmpty()) {
			userPin = resource.getString("user.serach.input.name.or.userpin");
		}
		request.setAttribute("byUserPin21", userPin);
		return "attLogList";
	}
	
	/**
	 * According to the attendance record ID from Interface,removes the specified attendance record in server.
	 * @return
	 */
	public String delById() {
		String logIds = request.getParameter("logId");
		if (null == logIds || logIds.isEmpty()) {
			return "";
		}
		String[] ids = logIds.split(",");
		ManagerFactory.getAttLogManager().deleteAttLogByIds(ids);
		return "";
	}
	/**
	 * Clear all attendance records from the server.
	 * @return
	 */
	public String clearAll() {
		ManagerFactory.getAttLogManager().clearAllAttLog();
		return "";
	}
	
	/**
	 * Clear all attendance photos from the server.
	 * @return
	 */
	public String clearAllPhoto(){
		new Thread(new Runnable() {
			public void run() {
				/**Delete database records*/
				ManagerFactory.getAttPhotoManager().clearAllPhoto();
			}
		}).start();
		return "";
	}
	

}
