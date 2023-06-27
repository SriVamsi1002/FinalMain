package com.pennant.prodmtr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pennant.prodmtr.Dao.Interface.UserDao;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.model.view.emailSend;

@Controller
public class MailOtpController {
	String generateotp;
	String finalemail;
	List<User> u = new ArrayList<>();
	@Autowired
	UserDao userdao;

	@RequestMapping(value = "/forgetpassword", method = RequestMethod.GET)
	public String forgotPswd(Model model) {
		System.out.println("mail forget Page");
		return "forgetpassword";
	}

	/*
	 * @RequestMapping(value = "/otpAction") public String sendOTP(@RequestParam("email") String email) {
	 * System.out.println(email); u = userdao.getuserbyemailid(email); generateotp = (new emailSend()).sendEmail(email);
	 * 
	 * return generateotp; // Return the generated OTP as the response }
	 */
	@RequestMapping(value = "/otpActionsend")
	@ResponseBody
	public String sendOTP(@RequestParam("email") String email) {
		System.out.println(email);
		u = userdao.getuserbyemailid(email);
		generateotp = (new emailSend()).sendEmail(email);

		return generateotp;
	}

	@RequestMapping(value = "/emailValid", method = RequestMethod.GET)
	@ResponseBody
	public String verifyEmail(@RequestParam("email") String email) {
		// System.out.println("email valid method");
		finalemail = email;
		System.out.println(finalemail);
		u = userdao.getuserbyemailid(finalemail);
		if (u == null)
			return "no";
		return "yes"; // Return the generated OTP as the response
	}

	@RequestMapping(value = "/validateOTP", method = RequestMethod.GET)
	@ResponseBody
	public String validateOTP(@RequestParam("otp12") String otp) {
		// System.out.println("validate" + otp);

		if (otp.equals(generateotp)) {
			// java.sql.Timestamp timestamp1 = cust.getCustLastLoginDate();
			java.time.LocalDateTime t1 = java.time.LocalDateTime.now(); // Remove the data type declaration here
			java.time.LocalDateTime t2 = java.time.LocalDateTime.now();
			java.time.Duration duration = java.time.Duration.between(t1, t2);
			// System.out.println("t1: " + t1);
			// System.out.println("t2: " + t2);
			// System.out.println();
			long seconds = duration.getSeconds();
			// System.out.println("seconds: " + seconds);
			if (seconds <= 30) {
				return "valid";
			} else {
				return "no";
			}
		} else {
			// OTP is invalid
			return "invalid";
		}
	}

	@RequestMapping(value = "/updatepwd", method = RequestMethod.GET)
	public String usersignup(@RequestParam("psd2") String password, Model model) {
		// cdao.updatePassword(p2, finalemail);
		System.out.println(password + "is password");
		userdao.UpdatePassword(password, finalemail);
		return "login";
	}

}
