/**
 * File : index_control.js
 * date : 2012-01-07
 * edit by handong
 */
$("#page_result").live('pagebeforecreate',function(event){//alert(location.href);
	if(!$.cookie('hy_id') || !$.cookie('job_pid') || !$.cookie('job_zid') || !$.cookie('city_id') || !$.cookie('money_num')){
		location.href="index_solo.html";
		return false;
	}
 });


$(document).ready(function(){
		var cookie_id={"hy_id":$.cookie("hy_id"),"job_pid":$.cookie("job_pid"),"job_zid":$.cookie("job_zid"),"city_id":$.cookie("city_id")};
		var cookie_name={"job_czname":$.cookie("job_czname"),"city_cname":$.cookie("city_cname")};
		//页面初始化
		initPage(cookie_name);
		initControllEvents(cookie_id);
		var	jobid=$.cookie("job_zid");
		var areaid=$.cookie("city_id");
		drawEmolument();
		
		 
});


/*
 * 页面初始化
 */
function initPage(cookie_name){	
	if(!$.cookie("job_czid") || !$.cookie("city_cid")){
		//隐藏比较条件
		$("#switch_options_compare").css("display","none");
		$("#options_compare").css("display","none");
	}else{
		$("#select_job").text(cookie_name.job_czname);
		$("#select_city").text(cookie_name.city_cname);
	}
}


/*
* 	控制动作事件
*/
function initControllEvents(){
	//细分职位
	$("#page_select_job_catalog").click(function(event){
		var hy_id=$.cookie("hy_id");
		var job_id=event.target.id;
		if(job_id>0){
			var job_pname=event.target.textContent;
			$.cookie("job_cpid",job_id,{path: '/'});
			$.cookie("job_cpname",job_pname,{path:'/'});
	  		var job=new Array();
	  		var getFuntype="getFuntype"+hy_id+"Json";
	  		//alert(getFuntype);
	  		getstrToJson=strToJson(getFuntype);
	    	var job=getstrToJson();
	    	var outlist="";
	    	//删除原有列表
	    	$("#page_select_job ul li").remove();
	    	$.each(job,function(key,val){
	    		if(val.id==job_id){
	    		    $.each(val.child,function(key2,val2){
	    		    	outlist+="<li data-icon='false'><a href='result.html' id='"+val2.id+"'>"+val2.name+"</a></li>";
	    		    });
	    		    return false;
	    		}
		    	});
	    	$("#page_select_job ul").append(outlist);
	    	$("#page_select_job").page();
	    	$('#page_select_job ul').listview('refresh');
		}
	});
	
	//选择职位
	 $("#page_select_job").click(function(event){
		 var job_name=event.target.textContent;
		 var job_zid=event.target.id;
		 if(job_zid>0 && job_name){
			 $.cookie("job_czid",job_zid,{path: '/'});
			 $.cookie("job_czname",job_name,{path: '/'});
			 $("#select_job").text(job_name);
		 }
		 });
	 
		//选择城市
		$("#page_select_city ul li a").click(function(){
			var city_id=$(this).attr("id");
			var city_name=$(this).text();
			 $.cookie("city_cid",city_id,{path: '/'});
			 $.cookie("city_cname",city_name,{path: '/'});
//			 $.mobile.changePage("result.html");
			 if(city_id && city_name){			 
				 $("#select_city").text(city_name);
			 } 
			});
	$("#options_compare").click(function(){
		$("#compare").focus();
	});
	
	
	//比较薪酬
	$("#compare_button").click(function(){
		//验证内容
		if(!checkForm()){return false;}
		$("#switch_options_compare").css("display","");
		$("#options_compare").css("display","");
		$("#options_compare a").empty();
		$("#options_compare a").append("<h5>职位分类："+$.cookie("job_cpname")+"&nbsp;&nbsp;"+$.cookie("job_czname")+"</h5><h5>工作地点："+$.cookie("city_cname")+"</h5>");
		$("#jqChart img").remove();
		drawEmolument();//调用画图方法
		$('#result_content ul').listview('refresh');
	});
}



/*
* 验证表单
*/
function checkForm(){
	var job_czid=$.cookie("job_czid");
	if(!job_czid){
		alert("请选择比较职位");
		return false;
	}
	var city_cid=$.cookie("city_cid");
	if(!city_cid){
		alert("请选择比较城市");
		return false;
	}
	return true;
}


/*
 * 获取数组两极值
 */
function getSeriesDataInfo (numAry){
	
    var min = max = 'NaN';
    var poorSum = 0;
    for(var i in numAry) {
        if(min == 'NaN' || numAry[i] < min) {
            min = numAry[i];
        }
        if(max == 'NaN' || numAry[i] > max) {
            max = numAry[i];
        }
        if(i > 0 && numAry[i] > numAry[i-1]) {
            poorSum = poorSum + (numAry[i] - numAry[i-1]);
        }
    }
    return {min:min, max:max, poorAve: poorSum/(numAry.length)};
};

/*获取坐标轴步长、开始值、结束值*/
function getAxesInfo(numAry) {
	var minData = maxData = minAve = tmpXRangeNum = 'NaN';
	for(var i in numAry) {
    	var dataInfo = getSeriesDataInfo(numAry[i]);
    	if(minData == 'NaN' || dataInfo.min < minData) {
    		minData = dataInfo.min;
        }
    	if(maxData == 'NaN' || dataInfo.max > maxData) {
    		maxData = dataInfo.max;
        }
        if(minAve == 'NaN' || dataInfo.poorAve > minAve) {
            minAve = dataInfo.poorAve;
        }
    }

    var info = {};
	var divider = Math.pow(10,String(parseInt(minAve)).length - 1);
	
	info.step  = Math.ceil(minAve / divider) * divider;
	if(!info.step) info.step = 2000;
	info.start = (info.step >= minData) ? 0 : (Math.floor(minData / info.step)-1) * info.step;
	if(!info.start && info.start != 0) info.start = 0;
	info.end   = (Math.ceil(maxData / info.step) + 1) * info.step;
	if(!info.end) info.end = 10000;

	return info;
}
/*
 * 得到图标数据开始画图
 */
function drawAction(arr){
	var axisColor = '#a6a6a6';	//轴色
	var gridColor = '#d9d9d9';	//网格线
	var fontColor = '#333333';	//字体色
	var seriesColor = ['#c0504d', '#4f81bd'];
	var markerType = ['rectangle', 'circle'];
	var xAxisCategories = ['低端', '中低端', '中端', '中高端', '高端'];

	var seriesOptions = [];    //曲线配置
	var seriesInfo = arr;    //图表数据
	var chartSize = {};    //图表尺寸
    
	/*检测是否动态显示提示标签*/
   
	 var tooltipsDisabled = true; //动态标签显示
	 var pointLabels = {font: '10px Lucida Grande,Lucida Sans Unicode,Verdana,Arial,Helvetica,sans-serif'};
	 var crosshairVisible = [false, false]; 
    	
	var datas = [];
	for(var i in seriesInfo) {
		datas[i] = [];
		if(seriesInfo[i].data.length){
    		for(var j in seriesInfo[i].data) {
    			datas[i].push(parseFloat(seriesInfo[i].data[j]));
    		}
    		seriesOptions.push({
    			type: 'line',
    			title: seriesInfo[i].title,
    			data: datas[i],
    			strokeStyle: seriesColor[i],
    			fillStyle: seriesColor[i],
    			markers: {size: 8, type: markerType[i], fillStyle : seriesColor[i], lineWidth: 1 },
    			labels: pointLabels
    		});
		}
	}

	var yAxesInfo = getAxesInfo(datas);     //获取y轴信息

	var expect = $.cookie("money_num");	//期望薪资
	var multiRange =seriesInfo[0].multiRange;	//众数区间
	
	/*划期望薪资与众数区间*/
	function drawFlag(value, shape, element, offset) {
		var spv = spc =  'NaN';    
		var x = y = 0;
		if(shape.context.y == value) {
			y = shape.y;
			x = shape.x;
			spv = spc =  'NaN';
		}else{
			if(shape.context.y < value) {
				spv = shape.context.y;
				spc = [shape.x, shape.y]
			}else if(spv != 'NaN' && spc != 'NaN'){
				var sideY = spc[1] - shape.y;
				var sideX = shape.x - spc[0];
				var valuePoor = shape.context.y - spv;
				y = (value - spv) * (sideY / valuePoor);
				x = y * sideX / sideY;
				y = spc[1] - y;
				x = spc[0] + x;
				
				spv = spc =  'NaN';
			}
		}
		if(x && y) {
			$(element).css({'top':y-offset.y+'px', 'left':x-offset.x+'px', 'position':'absolute'}).appendTo($('#jqChart'))
			x = y = 0;
		}
	}
	
	$('#jqChart').jqChart({
		width : chartSize.width, // chart width, exp: width : '400px'
	    height : chartSize.height, // chart height, exp: height : '300px'
		border: {lineWidth: 0},
		tooltips: {disabled : tooltipsDisabled, type: 'shared'},
		animation: {duration: 1 },
		axes: [
		    {	
	        	strokeStyle: axisColor, // axis lines color
	            location: 'bottom',
	            type: 'category',
	            majorTickMarks: {strokeStyle: axisColor},
	            labels:{font:"12px 宋体"},
	            categories: xAxisCategories
            },{
            	title:{text: '元 / 月', font: '12px 宋体', fillStyle: fontColor/*, strokeStyle: fontColor*/},
            	strokeStyle: axisColor, // axis lines color
            	minimum : yAxesInfo.start, // the minimum axis value 
            	maximum  : yAxesInfo.end, // the minimum axis value
            	interval: yAxesInfo.step,
            	majorTickMarks: {strokeStyle: axisColor},	//标记线
                majorGridLines: {strokeStyle: gridColor},	//网格线
                labels:{font:"10px Lucida Grande,Lucida Sans Unicode,Verdana,Arial,Helvetica,sans-serif"},
                crossing : yAxesInfo.start // the crossing point
        	}
        ],
		series: seriesOptions,
		crosshairs: {
	          enabled: true, // specifies whether the crosshairs are visible
	          snapToDataPoints: true, // specifies whether the crosshairs span to data points
	          hLine: { visible: crosshairVisible[0], strokeStyle: gridColor}, // horizontal line options
	          vLine: { visible: crosshairVisible[1], strokeStyle: gridColor } // vertical line options
	    },
	    legend: {visible: false}
	});
	//$('#jqChart').jqChart('option', 'width' , '120px');
	
	//点标题 
	$('#jqChart').bind('tooltipFormat', function (e, data) {
		var tooltip = '<table>';
		$(data).each(function () {
	    	tooltip += '<tr><td style="color:' + this.series.fillStyle + '">' + this.series.title + ': </td><td>' + this.y + '元</td></tr>';
	    	//tooltip += '<tr><td style="color:' + this.series.fillStyle + '">期望月薪: </td><td>' + expect + ' 元</td></tr>';
	    	if(this.series.fillStyle == seriesColor[0]) { //第一条线
	    	    //tooltip += '<tr><td style="color:' + this.series.fillStyle + '">薪酬集中区: </td><td>' + multiRange[0][0]+' 元 —— ' + multiRange[0][1] + ' 元</td></tr>';
	    	}else{//第二条线
	    	    //tooltip += '<tr><td style="color:' + this.series.fillStyle + '">薪酬集中区: </td><td>' + multiRange[1][0]+' 元 —— ' + multiRange[1][1] + ' 元</td></tr>';
	    	}
	    });
	    tooltip += '</table>';
	    return tooltip;
	});
	
	var chart = $('#jqChart').jqChart('chart');
	
	if($('.msg').length) $('.msg').css({'left':chart.gridArea.x/2+'px', 'top':chart.gridArea.y/2+'px'}).show();
	
	var shapes = chart.shapes;
	/*标记期望薪资*/
	for(var i in shapes) {
		if(shapes[i].context && shapes[i].context.y >= 1100) {
			drawFlag(parseFloat(expect), shapes[i], $('<img src="js/jqMobile/img/result_icon03.png"/>').addClass('expect_tag').hide().bind('mouseover',function(){
			$('#jqChart').jqChart('highlightData', []);
			}), {x:4,y:4});
		}
	}
	/*标记众数区间*/
	var seriesNum = seriesOptions.length;
	for(var j in multiRange){
	    var tmp = seriesNum - 2 + parseFloat(j);
	    if(tmp < 0 ) tmp = 0;
		for(var k in multiRange[j]){
			var tmpNum = 0;
			for(var i in shapes) {
				if(shapes[i].context && shapes[i].context.y >= 1100) {
					tmpNum ++;
					if(tmpNum <= tmp*5) continue;
					if(tmpNum > (tmp + 1)*5) break;
					var imgUrl = (j==1) ? 'js/jqMobile/img/line_blue.png' : 'js/jqMobile/img/line_red.png';
					var element = $('<img/>').attr('title',shapes[i].context.y + ' 元').addClass('multi_tag').hide().attr('src', imgUrl);
					drawFlag(parseFloat(multiRange[j][k]), shapes[i], element, {x:0,y:4});
					
				}
			}
		}
	}
	
	setTimeout("$('#jqChart .expect_tag').fadeIn(1000);$('#jqChart .multi_tag').slideDown(1000)",700);
}
/*
 * 画图
 */
function drawEmolument(){
	var	jobid=$.cookie("job_zid");
	var areaid=$.cookie("city_id");
	var strpost='';
	var series_info=new Array();
	if(jobid && areaid){
		strpost='job='+jobid+'&area='+areaid;
		var	jobid=$.cookie("job_czid");
		var areaid=$.cookie("city_cid");
		if(jobid && areaid){
			strpost=strpost+'&job2='+jobid+'&area2='+areaid;
		}
//		strpost='http://api.800hr.com/user/emolument/emif.php?'+strpost+'&rm='+Math.random();
		strpost='http://api.800hr.com/user/emolument/emif.php?'+strpost+'&rm='+Math.random()+'&callback=?';
	//	$("#page_result").append("<script type='text/javascript' src='"+strpost+"' ></script>");
		$.getJSON(strpost, function(data) {
			//请求失败
            if(data.error_code){
            	alert("服务器正在维护中，请稍后再试!");
            	return false;
            }
            var series_info = []; 
            if(data.salary){
            	$("#table_money tr").eq(1).empty();
            	$("#table_money tr").eq(2).empty();
            	$.each(data.salary,function(key,val){
            		series_info.push({"data":[val.salary_lower,val.salary_low,val.salary_mid,val.salary_high,val.salary_higher],"title":"\u6c14\u538b\/\u6db2\u538b\u8bbe\u5907 + \u6c5f\u82cf","multiRange":["2613","4035"]});
            		$("#table_money tr").eq(1).append("<td data-theme='c'>月薪</td>");
            		$("#table_money tr").eq(1).append("<td>"+val.salary_lower+"</td>");
            		$("#table_money tr").eq(1).append("<td>"+val.salary_low+"</td>");
            		$("#table_money tr").eq(1).append("<td>"+val.salary_mid+"</td>");
            		$("#table_money tr").eq(1).append("<td>"+val.salary_high+"</td>");
            		$("#table_money tr").eq(1).append("<td>"+val.salary_higher+"</td>");
            		return false;
            	});
            	//drawAction(series_info);//调用画图方法
            }
            if(data.salary2){
            	$.each(data.salary2,function(key,val){
            		series_info.push({"data":[val.salary_lower,val.salary_low,val.salary_mid,val.salary_high,val.salary_higher],"title":"\u6c14\u538b\/\u6db2\u538b\u8bbe\u5907 + \u6c5f\u82cf","multiRange":["2613","4035"]});
            		$("#table_money tr").eq(2).append("<td data-theme='c'>月薪</td>");
            		$("#table_money tr").eq(2).append("<td>"+val.salary_lower+"</td>");
            		$("#table_money tr").eq(2).append("<td>"+val.salary_low+"</td>");
            		$("#table_money tr").eq(2).append("<td>"+val.salary_mid+"</td>");
            		$("#table_money tr").eq(2).append("<td>"+val.salary_high+"</td>");
            		$("#table_money tr").eq(2).append("<td>"+val.salary_higher+"</td>");
            		return false;
            	});
            }
            drawAction(series_info);//调用画图方法
            
     });

	}
}

/*
* 字符串转化成对象
*/
function strToJson(str){  
	      var json = eval('(' + str + ')');  
	      return json;  
	 }