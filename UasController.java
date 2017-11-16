package com.cg.uas.controller;

import java.sql.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cg.uas.dto.Application;
import com.cg.uas.dto.Programs_Offered;
import com.cg.uas.dto.Programs_Scheduled;
import com.cg.uas.exception.UasException;
import com.cg.uas.service.IServiceUas;

@Controller
public class UasController {

	@Autowired
	IServiceUas service;

	@RequestMapping("/showHomePage")
	public String showHomepage() {
		return "Homepage";

	}

	@RequestMapping("/applicant")
	public String ApplicantUI() {
		return "ApplicantPage";

	}

	@RequestMapping("/viewallscheduledpgms")
	public ModelAndView ViewAllScheduledPgm() {

		ModelAndView viewall = new ModelAndView("ApplicantPage");
		List<Programs_Scheduled> list = null;
		try {
			list = service.allPrograms();
		} catch (UasException e) {

			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			System.out.println("error conroller");
			return errorpage;

		}

		viewall.addObject("list", list);
		return viewall;
	}

	@RequestMapping("/applyforscheduledpgm")
	public ModelAndView ApplyForProgram( @RequestParam("opt") int opt,
			HttpServletRequest request) {
	
		Application applicant=new Application();

		ModelAndView apply = new ModelAndView("ApplicantPage");
		int appid = 0;
		if (opt == 0) {
			apply.addObject("formdisplay", true);
			return apply;

		}

		if (opt == 1) {
			
			
			System.out.println(request.getParameter("address"));
		
			applicant.setAddress(request.getParameter("address"));
			applicant.setApplication_Id(0);
			LocalDate date=LocalDate.parse(request.getParameter("date_Of_Birth"));
			applicant.setDate_Of_Birth(Date.valueOf(date));
			applicant.setDate_Of_Interview(Date.valueOf(LocalDate.now()));
			applicant.setEmail_Id(request.getParameter("email_Id"));
			applicant.setFull_Name(request.getParameter("full_Name"));
			applicant.setGoals(request.getParameter("goals"));
			applicant.setHighest_Qualification(request.getParameter("highest_Qualification"));
			applicant.setMarks_Obtained(Integer.parseInt(request.getParameter("marks_Obtained")));
			applicant.setScheduled_Program_Id(request.getParameter("scheduled_Program_Id"));
			applicant.setStatus("applied");

			try {
				System.out.println("1");
				appid = service.apply(applicant);
				System.out.println(appid);
				
				apply.addObject("appid", appid);
			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;
			}
			return apply;

		}

		return null;
	}

	@RequestMapping("/viewappstatus")
	public ModelAndView ViewStatus(@RequestParam("viewvalue") int opt,
			HttpServletRequest request) {

		ModelAndView viewall = new ModelAndView("ApplicantPage");
		switch (opt) {
		case 0:

			viewall.addObject("value", 1);
			return viewall;

		case 1:
			viewall = new ModelAndView("ApplicantPage");
			String status = null;

			try {
				status = service.status(Integer.parseInt(request
						.getParameter("id")));

			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;

			}

			viewall.addObject("status", status);
			return viewall;

		}
		return null;

	}

	// -----------------------------------MAC
	// FUNCTIONS-------------------------------------//

	@RequestMapping("/mac")
	public String MacUI(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.setAttribute("logmac",false);
		return "MacPage";

	}

	@RequestMapping(value = "/macLogin", method = RequestMethod.POST)
	public ModelAndView Maclogin(HttpServletRequest request,
			@RequestParam("username") String username,
			@RequestParam("pwd") String pwd) {

		ModelAndView mac = new ModelAndView("MacPage");
		boolean valid = false;
		try {
			valid = service.macLogin(username, pwd);
		} catch (UasException e) {

			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}

		if (valid) {
			request.getSession(false).setAttribute("credentials", true);
			request.getSession(false).setAttribute("logmac", true);
			mac.addObject("logsuccess", true);
			return mac;
		}

		else {
			mac.addObject("credentials", false);
			return mac;
		}

	}

	@RequestMapping("/viewapplications")
	public ModelAndView viewAllApplicants(@RequestParam("viewvalue") int func,
			HttpServletRequest request) {
		ModelAndView viewall = new ModelAndView("MacPage");
		switch (func) {
		case 0:

			viewall.addObject("value", 1);
			return viewall;

		case 1:
			viewall = new ModelAndView("MacPage");
			List<Application> list = null;
			try {
				String programName = request.getParameter("name");
				list = service.allApplications(programName);
			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;

			}
			viewall.addObject("list", list);
			return viewall;

		}
		return null;

	}

	@RequestMapping("/acceptapplication")
	public ModelAndView AcceptApplication(@RequestParam("viewvalue") int func,
			HttpServletRequest request) {
		ModelAndView viewall = new ModelAndView("MacPage");
		switch (func) {
		case 2:

			viewall.addObject("value", 3);
			return viewall;

		case 3:
			viewall = new ModelAndView("MacPage");
			String msg = null;
			boolean ans = false;
			try {
				ans = service.acceptApplication(
						Integer.parseInt(request.getParameter("appid")),
						LocalDate.parse(request.getParameter("intdate")));
				if (ans)
					msg = "Application Status successfully updated";
				else
					msg = "Application Status not updated";
			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;

			}

			viewall.addObject("msg", msg);
			return viewall;

		}
		return null;

	}

	@RequestMapping("/rejectapplication")
	public ModelAndView RejectApplication(@RequestParam("viewvalue") int func,
			HttpServletRequest request) {
		ModelAndView viewall = new ModelAndView("MacPage");
		switch (func) {
		case 3:

			viewall.addObject("value", 4);
			return viewall;

		case 4:
			viewall = new ModelAndView("MacPage");
			String msg = null;
			boolean ans = false;
			try {
				ans = service.rejectApplication(Integer.parseInt(request
						.getParameter("rejid")));
				if (ans)
					msg = "Application Rejected ";
				else
					msg = "Application could not be rejected";
			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;

			}

			viewall.addObject("rejmsg", msg);
			return viewall;

		}
		return null;

	}

	@RequestMapping("/confirmstatus")
	public ModelAndView ConfirmApplication(@RequestParam("viewvalue") int func,
			HttpServletRequest request) {
		ModelAndView viewall = new ModelAndView("MacPage");
		switch (func) {
		case 4:

			viewall.addObject("value", 5);
			return viewall;

		case 5:
			viewall = new ModelAndView("MacPage");
			String msg = null;
			boolean ans = false;
			try {
				ans = service.confirmApplication(Integer.parseInt(request
						.getParameter("statid")));
				if (ans)
					msg = "Application Status  Confirmed";
				else
					msg = "Application status could not be confirmed";
			} catch (UasException e) {
				ModelAndView errorpage = new ModelAndView("error");
				errorpage.addObject("err", e);
				System.out.println("error conroller");
				return errorpage;

			}

			viewall.addObject("statmsg", msg);
			return viewall;

		}
		return null;

	}
	
	
	
	@RequestMapping("/logoutmac")
	public ModelAndView Logoutmac(HttpServletRequest request) {
	request.getSession().invalidate();
	ModelAndView v=new ModelAndView("Homepage");
	return v;
	}
	
	

	// --------------------------------------ADMIN------------------------------------------//
	@RequestMapping("/admin")
	public String AdminLogin() {
		return "AdminLogin";
	}

	@RequestMapping(value = "/adminUI", method = RequestMethod.POST)
	public ModelAndView AdminLoginUI(HttpServletRequest request,
			@RequestParam("username") String username,
			@RequestParam("password") String pwd) {

		System.out.println(username + pwd);
		ModelAndView adminUI = new ModelAndView("AdminUI");
		ModelAndView adminLogin = new ModelAndView("AdminLogin");
		boolean valid;
		try {
			valid = service.adminLogin(username, pwd);
		} catch (UasException e) {

			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("error", e);
			return errorpage;
		}
		System.out.println(valid);
		if (valid) {
			HttpSession session = request.getSession(true);
			session.setAttribute("hello", "hello");
			return adminUI;
		} else {
			System.out.println("his");
			adminLogin.addObject("credentials", false);
			return adminLogin;
		}
	}

	@RequestMapping("/adminUi")
	public String AdminUI() {
		return "AdminUI";
	}

	// ------------------offered progs---------------------------//
	@RequestMapping("/adminOffProgs")
	public String AdminOfferedProgs() {
		return "ProgsOffered";
	}

	@RequestMapping("/adminAddOffProgs")
	public String AdminAddOfferedProgs() {
		return "AddProgsOffered";
	}

	@RequestMapping(value = "/adminAddOffProgs", method = RequestMethod.POST)
	public ModelAndView AdminaddOfferedProgs(
			@ModelAttribute("Program_offered") Programs_Offered program_Offered) {

		ModelAndView addOffProgs = new ModelAndView("Success");
		try {
			boolean valid = service.addProgramOffered(program_Offered);
		} catch (UasException e) {
			System.out.println("hello error");
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}
		addOffProgs.addObject("message", "Succesfully added program");
		return addOffProgs;
	}

	@RequestMapping("/adminDelOffProgs")
	public String AdminDelOfferedProgs() {
		return "DelProgsOffered";
	}

	@RequestMapping(value = "/adminDelOffProgs", method = RequestMethod.POST)
	public ModelAndView AdminDelOfferedProgs(
			@RequestParam("programName") String programName) {

		ModelAndView delOffProgs = new ModelAndView("Success");
		try {

			boolean valid = service.delProgramOffered(programName);
		} catch (UasException e) {
			System.out.println("hello error");
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}
		delOffProgs.addObject("message", "Succesfully deleted offered program");
		return delOffProgs;
	}

	// ----------------------------------scheduled
	// progs-----------------------//

	@RequestMapping("/adminSchProgs")
	public String AdminScheduledProgs() {
		return "ProgScheduled";
	}

	@RequestMapping("/adminAddSchProgs")
	public String AdminaddScheduledProgs() {
		return "AddProgScheduled";
	}

	@RequestMapping(value = "/adminAddScProgs", method = RequestMethod.POST)
	public ModelAndView AdminaddScheduledProgs(
			@ModelAttribute("Programs_Scheduled") Programs_Scheduled program_Scheduled) {

		boolean valid=false;
		System.out.println("hello");
		ModelAndView addSchProgs = new ModelAndView("Success");
		System.out.println(program_Scheduled.toString());
		try {
			 valid = service.addProgramScheduled(program_Scheduled);
		} catch (UasException e) {
			System.out.println("hello error");
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}
		if(valid)
		addSchProgs.addObject("message", "Succesfully added program");
		else
			addSchProgs.addObject("message", "Sorry! The program name is not found in database");
		return addSchProgs;
	}

	@RequestMapping("/adminDelSchProgs")
	public String AdminDelScheduledProgs() {

		return "DelProgScheduled";
	}

	@RequestMapping(value = "/adminDelSchProgs", method = RequestMethod.POST)
	public ModelAndView AdminDelScheduledProgs(
			@RequestParam("programId") String programId) {

		boolean valid=false;
		System.out.println("Hello 1" + programId);
		ModelAndView delSchProgs = new ModelAndView("Success");
		try {
			System.out.println("Hello 2");
			 valid = service.delProgramScheduled(programId);
			System.out.println("Hello 3");
		} catch (UasException e) {
			System.out.println("hello error");
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}
		if(valid)
		delSchProgs.addObject("message", "Succesfully deleted offered program");
		else
			delSchProgs.addObject("message", "Sorry! Program not found in database");
		return delSchProgs;
	}

	// ---------------------------------generate
	// reports-------------------------//

	@RequestMapping("/adminGenerate")
	public String AdminGenerateReports() {
		System.out.println("hello 1");
		return "Generate";
	}

	@RequestMapping(value = "/adminGenerate", method = RequestMethod.POST)
	public ModelAndView AdminGenerateReports(HttpServletResponse response,
			HttpServletRequest request) {
		System.out.println("hello");

		List<Application> applicantList;
		try {
			applicantList = service.viewApplicants();
		} catch (UasException e) {
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", e);
			return errorpage;
		}

		String file = request.getParameter("fileName");
		System.out.println(file);
		response.setHeader("Content-type", "application/csv");
		response.setHeader("Content-disposition", "inline; filename=" + file
				+ ".csv");
		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			ModelAndView errorpage = new ModelAndView("error");
			errorpage.addObject("err", "Print writer initialising failed");
			return errorpage;
		}

		out.println("application_Id,full_Name,date_Of_Birth,highest_Qualification,marks_Obtained,goals,email_Id,scheduled_Program_Id,status,date_Of_Interview,address\n");
		for (Application applicant : applicantList) {
			out.println(applicant.toString2());
		}

		out.flush();
		out.close();

		ModelAndView generate = new ModelAndView("Success");
		return generate;

	}

}
