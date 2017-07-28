/**
 * File : index_control.js date : 2012-01-07 edit by handong
 */
/*
 * 页面加载之前 删除cookie
 */
$(document).bind("mobileinit", function() {
	$.mobile.defaultPageTransition = "none";
});

$(document).ready(function() {
	// 页面初始化 清空cookie
	android_payquery.clearcookie();
	initControllEvents();
});
/*
 * 控制动作事件
 */
function initControllEvents() {
	// 选择行业
	$("#page_select_industry").click(function(event) {
		var hy_oldid = android_payquery.getcookie("hy_id");// 旧行业ID
		var hy_id = event.target.id;
		if (hy_id > 0) {
			if (hy_oldid != hy_id) {// 行业ID改变 清空职位分类
				android_payquery.clearone("job_pid");
				android_payquery.clearone("job_zid");
				$("#page_index li a").eq(1).text("职位分类");
				android_payquery.setcookie("hy_id", hy_id); // 添加新行业
			}
			var industry = event.target.textContent;
			if (industry) {
				android_payquery.setcookie("hy_name", industry); // 添加新行业
				$("#page_index li a").eq(0).text(industry);
			}
		}

	});
	// 选择职位
	$("#page_index ul li a").eq(1).click(
			function() {
				var hy_id = android_payquery.getcookie("hy_id");
				if (!hy_id) {
					alert("请选择行业");
					return false;
				}
				var job = new Array();
				var getFuntype = "getFuntype" + hy_id + "Json";
				// alert(getFuntype);
				getstrToJson = strToJson(getFuntype);
				var job = getstrToJson();
				var outlist = "";
				// 删除原有列表
				$("#page_select_job_catalog ul li").remove();
				$.each(job, function(key, val) {
					outlist += "<li data-icon='false' id='" + val.id
							+ "'><a href='#page_select_job' id='" + val.id
							+ "' data-transition='none'>" + val.name
							+ "</a></li>";
				});
				$("#page_select_job_catalog ul").append(outlist);
				$("#page_select_job_catalog").page();// 在dom数据加载完成后，重新生成page
				$('#page_select_job_catalog ul').listview('refresh');
			});
	// 细分职位
	$("#page_select_job_catalog")
			.click(
					function(event) {
						var hy_id = android_payquery.getcookie("hy_id");
						var job_id = event.target.id;

						$.cookie("hy_id", hy_id, {
							path : '/'
						});

						if (job_id > 0) {
							var job_pname = event.target.textContent;
							android_payquery.setcookie("job_id", job_id);
							android_payquery.setcookie("job_pname", job_pname);
							var job = new Array();
							var getFuntype = "getFuntype" + hy_id + "Json";
							// alert(getFuntype);
							getstrToJson = strToJson(getFuntype);
							var job = getstrToJson();
							var outlist = "";
							// 删除原有列表
							$("#page_select_job ul li").remove();
							$
									.each(
											job,
											function(key, val) {
												if (val.id == job_id) {
													$
															.each(
																	val.child,
																	function(
																			key2,
																			val2) {
																		outlist += "<li data-icon='false'><a href='#page_index' id='"
																				+ val2.id
																				+ "' data-transition='none'>"
																				+ val2.name
																				+ "</a></li>";
																	});
													return false;
												}
											});
							$("#page_select_job ul").append(outlist);
							$("#page_select_job").page();
							$('#page_select_job ul').listview('refresh');
							// $.mobile.changePage("#page_select_job");
						}
					});
	// 选择职位
	$("#page_select_job").click(function(event) {
		var job_name = event.target.textContent;
		var job_zid = event.target.id;
		if (job_zid > 0 && job_name) {
			android_payquery.setcookie("job_zid", job_zid);
			android_payquery.setcookie("job_name", job_name);
			$("#page_index li a").eq(1).text(job_name);
			// $.mobile.changePage("index_solo.html");
		}
	});
	// 选择城市

	$("#page_select_city ul li a").click(function() {
		var city_id = $(this).attr("id");
		var city_name = $(this).text();
		android_payquery.setcookie("city_id", city_id);
		android_payquery.setcookie("city_name", city_name); //
		$.mobile.changePage("index_solo.html");
		if (city_id && city_name) {
			$("#page_index li a").eq(2).text(city_name);
		}
	});

	// 查询
	$("#onSubmit").click(function() {
		if (!checkForm()) {
			return false;
		}
		android_payquery.setcookie("money_num", $.trim($("#money_num").val()));
	});
}

/*
 * 验证表单
 */
function checkForm() {
	var hy_id = android_payquery.getcookie("hy_id");
	var hy_name = $("#page_index li a").eq(0).text();
	if (!hy_id || !hy_name) {
		alert("请选择行业");
		return false;
	}
	var job_zid = android_payquery.getcookie("job_zid");
	var job_name = $("#page_index li a").eq(1).text();
	if (!job_zid || !job_name) {
		alert("请选择职位分类");
		return false;
	}
	var city_id = android_payquery.getcookie("city_id");
	var city_name = $("#page_index li a").eq(2).text();
	if (!city_id || !city_name) {
		alert("请选择工作地点");
		return false;
	}
	var money_num = $.trim($("#money_num").val());// 去除空格
	if (!money_num) {
		alert("请输入期望薪资");
		return false;
	}
	if (!/^\d+$/.test(money_num)) {
		alert("期望薪资格式有误");
		return false;
	}

	/*
	 * var sta = 'error'; $.ajax({ url:
	 * 'http://api.800hr.com/user/emolument/emif.php', type: 'GET', success:
	 * function(response, status) { sta = status; }, async:false });
	 */
	var state = android_payquery.getnetstate();
	if (state == '1') {
		return true;
	} else {
		alert("查询失败,请检查您的网络连接是否正常。");
		return false;
	}
	return true;
}
/*
 * 字符串转化成对象
 */
function strToJson(str) {
	var json = eval('(' + str + ')');
	return json;
}