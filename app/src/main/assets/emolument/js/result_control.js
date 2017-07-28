/**
 * File : index_control.js date : 2012-01-07 edit by handong
 */
var spv = spc = 'NaN';
var x = y = 0;
$(document).bind("mobileinit", function() {
	$.mobile.defaultPageTransition = "none";
});

$(document).ready(function() {
	var cookie_id = {
		"hy_id" : android_payquery.getcookie("hy_id"),
		"job_pid" : android_payquery.getcookie("job_pid"),
		"job_zid" : android_payquery.getcookie("job_zid"),
		"city_id" : android_payquery.getcookie("city_id")
	};
	var cookie_name = {};
	// 页面初始化
	initPage(cookie_id, cookie_name);
	// 控制动作事件
	initControllEvents(cookie_id);
	var jobid = android_payquery.getcookie("job_zid");
	var areaid = android_payquery.getcookie("city_id");
	// 画图事件
	setInterval(drawEmolument(cookie_id),2000);

	
	

	/*
	 * $(window).bind('orientationchange',function(e){ $("#jqChart
	 * img").remove(); drawEmolument(cookie_id); });
	 */

});

/*
 * 页面初始化
 */
function initPage(cookie_id, cookie_name) {
	// 隐藏比较条件
	$("#switch_options_compare").css("display", "none");
	$("#options_compare").css("display", "none");

	// 获得推荐职位
	if (cookie_id.hy_id && cookie_id.job_zid && cookie_id.city_id) {
		var outlist = "";
		// method 请求类型 1：推荐职位 2：职位薪酬
		var strpost = 'industry=' + cookie_id.hy_id + '&func='
				+ cookie_id.job_zid + '&area=' + cookie_id.city_id;
		strpost = 'http://api.800hr.com/user/emolument/recommend.php?'
				+ strpost + '&rm=' + Math.random() + '&callback=?';
		$
				.getJSON(
						strpost,
						function(data) {
							// 请求失败
							if (data.error_code) {
								alert("服务器正在维护中，请稍后再试!");
								return false;
							}
							if (data.jobs_list.length > 0) {
								$("#recommend li").remove();
								$
										.each(
												data.jobs_list,
												function(key, val) {
													outlist += "<li data-icon='false'><a data-transition='none' href='javascript:void(0)' id='"
															+ val.job_id
															+ "' name='e_"
															+ val.user_id
															+ "'>"
															+ val.job_name
															+ "</a></li>";
												});
								$("#recommend").append(outlist);
								$('#recommend').listview('refresh');
							} else {
								$("#recmd").css("display", "none");
							}
						});
	}
}

/*
 * 控制动作事件
 */
function initControllEvents(cookie_id) {
	// 选择比较条件前判断验证查询条件是否有效
	$("#select_job").click(function() {
		if (typeof zero == "object") {
			if (confirm("请重新选择查询条件,是否返回?")) {
				// location.href="index_solo.html";
				return false;
			} else {
				return false;
			}
		}
		android_payquery.index_click(1);
	});

	$("#select_city").click(function() {
		if (typeof zero == "object") {
			if (confirm("请重新选择查询条件,是否返回?")) {
				// location.href="index_solo.html";
				return false;
			} else {
				return false;
			}
		}
		android_payquery.index_click(1);
	});

	// 细分职位
	$("#page_select_job_catalog")
			.click(
					function(event) {
						var hy_id = cookie_id.hy_id;
						var job_id = event.target.id;
						if (job_id > 0) {
							var job_pname = event.target.textContent;
							android_payquery.setcookie("job_cpid", job_id);
							android_payquery.setcookie("job_cpname", job_pname);
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
																		outlist += "<li data-icon='false'><a data-transition='none' href='javascript:void(0)' id='"
																				+ val2.id
																				+ "'>"
																				+ val2.name
																				+ "</a></li>";
																	});
													return false;
												}
											});
							$("#page_select_job ul").append(outlist);
							$("#page_select_job").page();
							$('#page_select_job ul').listview('refresh');
						}
					});

	// 选择职位
	$("#page_select_job").click(function(event) {
		var job_name = event.target.textContent;
		var job_zid = event.target.id;
		if (job_zid > 0 && job_name) {
			// $.cookie("compare",1);
			android_payquery.setcookie("job_czid", job_zid);
			android_payquery.setcookie("job_czname", job_name);
			$("#select_job").text(job_name);
		}
		$.mobile.changePage("#page_result");
	});

	// 选择城市
	$("#page_select_city ul li a").click(function() {
		var city_id = $(this).attr("id");
		var city_name = $(this).text();
		if (city_id && city_name) {
			// $.cookie("compare",1);
			android_payquery.setcookie("city_cid", city_id);
			android_payquery.setcookie("city_cname", city_name);
			$("#select_city").text(city_name);
		}
		$.mobile.changePage("#page_result");
	});

	// 推荐职位跳转api
	$("#recommend").click(function(event) {
		var hy_id = parseInt(android_payquery.getcookie("hy_id"));// 行业ID
		var job_id = parseInt(event.target.id);// 职位ID
		var enterprise_id = parseInt(event.target.name.substr(2));// 企业ID
		if (job_id) {
			android_payquery.onclick(hy_id, job_id, enterprise_id);
		}
	});

	// 比较条件跳到指定位置
	$("#options_compare").click(function() {
		$.mobile.silentScroll($("#select_options").offset().top);
		return false;// 返回false可以避免在原链接后加上#
	});

	// 比较薪酬
	$("#compare_button").click(
			function() {
				// 验证内容
				if (!checkForm()) {
					return false;
				}
				android_payquery.index_click(0);
				$("#switch_options_compare").css("display", "");
				$("#options_compare").css("display", "");
				$("#options_compare a").empty();
				$("#options_compare a").append(
						"<h5>职位分类：" + android_payquery.getcookie("job_cpname")
								+ "&nbsp;&nbsp;"
								+ android_payquery.getcookie("job_czname")
								+ "</h5><h5>工作地点："
								+ android_payquery.getcookie("city_cname")
								+ "</h5>");
				$("#jqChart img").remove();
				drawEmolument(cookie_id);// 调用画图方法
				$('#result_content ul').listview('refresh');
				$.mobile.silentScroll($("#result_img").offset().top);
				return false;// 返回false可以避免在原链接后加上#
			});
}

/*
 * 验证表单
 */
function checkForm() {
	var job_czid = android_payquery.getcookie("job_czid");
	if (!job_czid) {
		alert("请选择职位分类");
		return false;
	}
	var city_cid = android_payquery.getcookie("city_cid");
	if (!city_cid) {
		alert("请选择工作地点");
		return false;
	}

	/*
	 * var sta = 'error'; alert(sta); return false; $.ajax({ url:
	 * 'http://api.800hr.com/user/emolument/emif.php', type: 'GET', success:
	 * function(response, status) { sta = status; }, async:false }); alert(sta);
	 * if(sta == 'success') { return true; } else {
	 * alert('查询失败，请检查您的网络连接是否正常。'); return false; }
	 */
	var state = android_payquery.getnetstate();
	if (state == '1') {
		return true;
	} else {
		alert('查询失败，请检查您的网络连接是否正常。');
		return false;
	}
	return true;
}

/*
 * 获取数组两极值
 */
function getSeriesDataInfo(numAry) {

	var min = max = 'NaN';
	var poorSum = 0;
	for ( var i in numAry) {
		if (min == 'NaN' || numAry[i] < min) {
			min = numAry[i];
		}
		if (max == 'NaN' || numAry[i] > max) {
			max = numAry[i];
		}
		if (i > 0 && numAry[i] > numAry[i - 1]) {
			poorSum = poorSum + (numAry[i] - numAry[i - 1]);
		}
	}
	return {
		min : min,
		max : max,
		poorAve : poorSum / (numAry.length)
	};
};

/* 获取坐标轴步长、开始值、结束值 */
function getAxesInfo(numAry) {
	var minData = maxData = minAve = tmpXRangeNum = 'NaN';
	for ( var i in numAry) {
		var dataInfo = getSeriesDataInfo(numAry[i]);
		if (minData == 'NaN' || dataInfo.min < minData) {
			minData = dataInfo.min;
		}
		if (maxData == 'NaN' || dataInfo.max > maxData) {
			maxData = dataInfo.max;
		}
		if (minAve == 'NaN' || dataInfo.poorAve > minAve) {
			minAve = dataInfo.poorAve;
		}
	}

	var info = {};
	var divider = Math.pow(10, String(parseInt(minAve)).length - 1);

	info.step = Math.ceil(minAve / divider) * divider;
	if (!info.step)
		info.step = 2000;
	info.start = (info.step >= minData) ? 0
			: (Math.floor(minData / info.step) - 1) * info.step;
	if (!info.start && info.start != 0)
		info.start = 0;
	info.end = (Math.ceil(maxData / info.step) + 1) * info.step;
	if (!info.end)
		info.end = 10000;

	return info;
}

/*
 * 划期望工资
 */
function drawFlag(value, shape, element, offset) {
	if (shape.context.y == value) {
		y = shape.y;
		x = shape.x;
		spv = spc = 'NaN';
	} else {
		if (shape.context.y < value) {
			spv = shape.context.y;
			spc = [ shape.x, shape.y ]
		} else if (spv != 'NaN' && spc != 'NaN') {
			var sideY = spc[1] - shape.y;
			var sideX = shape.x - spc[0];
			var valuePoor = shape.context.y - spv;
			y = (value - spv) * (sideY / valuePoor);
			x = y * sideX / sideY;
			y = spc[1] - y;
			x = spc[0] + x;

			spv = spc = 'NaN';
		}
	}
	if (x && y) {
		$(element).css({
			'top' : y - offset.y + 'px',
			'left' : x - offset.x + 'px',
			'position' : 'absolute'
		}).appendTo($('#jqChart'))
		x = y = 0;
	}
}
/*
 * 标记期望工资
 */
function drawExpert(expect, shapes) {
	for ( var i in shapes) {
		if (shapes[i].context && shapes[i].context.y >= 1100) {
			drawFlag(parseFloat(expect), shapes[i], $(
					'<img src="js/jqMobile/img/result_icon03.png"/>').addClass(
					'expect_tag').hide().bind('mouseover', function() {
				$('#jqChart').jqChart('highlightData', []);
			}), {
				x : 4,
				y : 4
			});
		}
	}
}
/*
 * 得到图标数据开始画图
 */
function drawAction(arr) {
	var axisColor = '#a6a6a6'; // 轴色
	var gridColor = '#d9d9d9'; // 网格线
	var fontColor = '#333333'; // 字体色
	var seriesColor = [ '#c0504d', '#4f81bd' ];
	var markerType = [ 'rectangle', 'circle' ];
	var xAxisCategories = [ '低端', '中低端', '中端', '中高端', '高端' ];

	var seriesOptions = []; // 曲线配置
	var seriesInfo = arr; // 图表数据
	var chartSize = {
		width : "'" + screen.width - 40 + "px'"
	}; // 图表尺寸

	/* 检测是否动态显示提示标签 */

	var tooltipsDisabled = true; // 动态标签显示
	var pointLabels = {
		font : '10px Lucida Grande,Lucida Sans Unicode,Verdana,Arial,Helvetica,sans-serif'
	};
	var crosshairVisible = [ false, false ];

	var datas = [];
	for ( var i in seriesInfo) {
		datas[i] = [];
		if (seriesInfo[i].data.length) {
			for ( var j in seriesInfo[i].data) {
				datas[i].push(parseFloat(seriesInfo[i].data[j]));
			}
			seriesOptions.push({
				type : 'line',
				title : seriesInfo[i].title,
				data : datas[i],
				strokeStyle : seriesColor[i],
				fillStyle : seriesColor[i],
				markers : {
					size : 8,
					type : markerType[i],
					fillStyle : seriesColor[i],
					lineWidth : 1
				},
				labels : "" // pointLabels
			});
		}
	}

	var yAxesInfo = getAxesInfo(datas); // 获取y轴信息

	var expect = android_payquery.getcookie("money_num"); // 期望薪资
	var multiRange = seriesInfo[0].multiRange; // 众数区间

	$('#jqChart')
			.jqChart(
					{
						width : chartSize.width, // chart width, exp: width :
													// '400px'
						height : chartSize.height, // chart height, exp: height
													// : '300px'
						border : {
							lineWidth : 0
						},
						tooltips : {
							disabled : tooltipsDisabled,
							type : 'shared'
						},
						animation : {
							duration : 0
						},
						axes : [
								{
									strokeStyle : axisColor, // axis lines
																// color
									location : 'bottom',
									type : 'category',
									majorTickMarks : {
										strokeStyle : axisColor
									},
									labels : {
										font : "12px 宋体"
									},
									categories : xAxisCategories
								},
								{
									title : {
										text : '元 / 月',
										font : '12px 宋体',
										fillStyle : fontColor,
										strokeStyle : fontColor
									},
									strokeStyle : axisColor, // axis lines
																// color
									minimum : yAxesInfo.start, // the minimum
																// axis value
									maximum : yAxesInfo.end, // the minimum
																// axis value
									interval : yAxesInfo.step,
									majorTickMarks : {
										strokeStyle : axisColor
									}, // 标记线
									majorGridLines : {
										strokeStyle : gridColor
									}, // 网格线
									labels : {
										font : "10px Lucida Grande,Lucida Sans Unicode,Verdana,Arial,Helvetica,sans-serif"
									},
									crossing : yAxesInfo.start
								// the crossing point
								} ],
						series : seriesOptions,
						/*
						 * crosshairs: { enabled: true, // specifies whether the
						 * crosshairs are visible snapToDataPoints: true, //
						 * specifies whether the crosshairs span to data points
						 * hLine: { visible: crosshairVisible[0], strokeStyle:
						 * gridColor}, // horizontal line options vLine: {
						 * visible: crosshairVisible[1], strokeStyle: gridColor } //
						 * vertical line options },
						 */
						legend : {
							visible : false
						}
					});
	// $('#jqChart').jqChart('option', 'width' , '120px');

	/* 划期望薪资与众数区间 */
	var chart = $('#jqChart').jqChart('chart');

	if ($('.msg').length)
		$('.msg').css({
			'left' : chart.gridArea.x / 2 + 'px',
			'top' : chart.gridArea.y / 2 + 'px'
		}).show();

	var shapes = chart.shapes;

	/* 标记期望薪资 */
	drawExpert(expect, shapes);
	// 屏蔽画图事件 
	$('#jqChart').unbind();
	/* 标记众数区间 */
	/*
	 * var seriesNum = seriesOptions.length; for(var j in multiRange){ var tmp =
	 * seriesNum - 2 + parseFloat(j); if(tmp < 0 ) tmp = 0; for(var k in
	 * multiRange[j]){ var tmpNum = 0; for(var i in shapes) {
	 * if(shapes[i].context && shapes[i].context.y >= 1100) { tmpNum ++;
	 * if(tmpNum <= tmp*5) continue; if(tmpNum > (tmp + 1)*5) break; var imgUrl =
	 * (j==1) ? 'js/jqMobile/img/line_blue.png' :
	 * 'js/jqMobile/img/line_red.png'; var element = $('<img/>').attr('title',shapes[i].context.y + '
	 * 元').addClass('multi_tag').hide().attr('src', imgUrl);
	 * drawFlag(parseFloat(multiRange[j][k]), shapes[i], element, {x:0,y:4});
	 *  } } } }
	 */

	setTimeout(
			"$('#jqChart .expect_tag').fadeIn(1000);$('#jqChart .multi_tag').slideDown(1000)",
			700);
}
/*
 * 画图
 */
function drawEmolument(cookie_id) {
	var jobid = cookie_id.job_zid;
	var areaid = cookie_id.city_id;
	var strpost = '';
	var series_info = new Array();
	if (jobid && areaid) {
		strpost = 'job=' + jobid + '&area=' + areaid;
		var jobid = android_payquery.getcookie("job_czid");
		var areaid = android_payquery.getcookie("city_cid");
		if (jobid && areaid) {
			strpost = strpost + '&job2=' + jobid + '&area2=' + areaid;
		}
		// method 请求类型 1：推荐职位 2：职位薪酬
		strpost = 'http://api.800hr.com/user/emolument/emif.php?' + strpost
				+ '&rm=' + Math.random() + '&callback=?';
		$.getJSON(strpost, function(data) {
			// 请求失败
			if (data.error_code) {
				alert("服务器正在维护中，请稍后再试!");
				return false;
			}
			var series_info = [];
			if (data.salary) {
				$("#table_money tr").eq(1).empty();
				$("#table_money tr").eq(2).empty();
				$.each(data.salary, function(key, val) {
					series_info.push({
						"data" : [ val.salary_lower, val.salary_low,
								val.salary_mid, val.salary_high,
								val.salary_higher ],
						"title" : "",
						"multiRange" : [ "2613", "4035" ]
					});
					$("#table_money tr").eq(1).append(
							"<td data-theme='c'>月薪</td>");
					$("#table_money tr").eq(1).append(
							"<td>" + val.salary_lower + "</td>");
					$("#table_money tr").eq(1).append(
							"<td>" + val.salary_low + "</td>");
					$("#table_money tr").eq(1).append(
							"<td>" + val.salary_mid + "</td>");
					$("#table_money tr").eq(1).append(
							"<td>" + val.salary_high + "</td>");
					$("#table_money tr").eq(1).append(
							"<td>" + val.salary_higher + "</td>");
					return false;
				});
				// drawAction(series_info);//调用画图方法
			}
			if (data.salary2) {
				$.each(data.salary2, function(key, val) {
					series_info.push({
						"data" : [ val.salary_lower, val.salary_low,
								val.salary_mid, val.salary_high,
								val.salary_higher ],
						"title" : "",
						"multiRange" : [ "2613", "4035" ]
					});
					$("#table_money tr").eq(2).append(
							"<td data-theme='c'>比较</td>");
					$("#table_money tr").eq(2).append(
							"<td>" + val.salary_lower + "</td>");
					$("#table_money tr").eq(2).append(
							"<td>" + val.salary_low + "</td>");
					$("#table_money tr").eq(2).append(
							"<td>" + val.salary_mid + "</td>");
					$("#table_money tr").eq(2).append(
							"<td>" + val.salary_high + "</td>");
					$("#table_money tr").eq(2).append(
							"<td>" + val.salary_higher + "</td>");
					return false;
				});
			}

			if (series_info.length > 0) {
				drawAction(series_info);// 调用画图方法
			} else {
				if (typeof zero != "undefined") {
					return false;
				}
				$("#jqChart").css("display", "none");
				$("#padding_left5").css("display", "none");
				$("#result_img").append(
						"<p style='color:red'>您选择的查询条件暂无数据，建议选择其他职位分类或地区</p>");
				eval("window.zero = new Object();");// 定义全局变量
				return false;
			}

		});

	}
	
}
/*
 * 字符串转化成对象
 */
function strToJson(str) {
	var json = eval('(' + str + ')');
	return json;
}