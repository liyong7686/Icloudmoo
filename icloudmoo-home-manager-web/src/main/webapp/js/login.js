var Login = {
	clearValue : function(input, isPassword) {
		if (isPassword) {
			input.value = "";
		} else {
			$(input).select()
		}
	},
	refreshValidCode : function() {
		$("#validCodeImg").attr("src",
				"code/getKaptchaImage.do?r=" + Math.floor(Math.random() * 100));
	},
	loginEnhome : function() {
		var loginObj = {};
		var jobj;
		$("input").each(function(index, item) {
					jobj = $(item);
					loginObj[jobj.attr("name")] = jobj.val();
				});

		if ($.trim(loginObj.account).length < 1) {
			alert("请输入用户名。")
			return;
		}

		if ($.trim(loginObj.password).length < 1) {
			alert("请输入密码。")
			return;
		}

		if ($.trim(loginObj.validateCode).length < 1) {
			alert("请输入验证码。")
			return;
		}

		loginObj.password = hex_md5(loginObj.password)
		var request = $.ajax({
					url : "home/loginHome.do",
					type : "POST",
					data : loginObj
				});
		request.done(function(msg) {
					if (msg.success) {
						//window.location = "enhome/employee/vaildResourceList.do";
						alert("登陆成功页面跳转！");
					} else {
						//alert(msg.msg);
						alert("登陆失败返回的信息");
					}
				});
		request.fail(function(jqXHR, textStatus) {
					console.log(textStatus);
				});
	}
};