<?php
/*
 * 推荐职位接口
 */
include '/var/webroot/wap_api/inc/Model/userResumeApi.php';
$usrapi = new userResumeApi();
	$job_id=$_GET['func'];
	$hy_id=$_GET['industry'];
	$area=$_GET['area'];
	$result['error_code']=0;
	if(!is_numeric($job_id) || !is_numeric($hy_id) || !is_numeric($area)){
		$result['error_code']=201;
	//	echo json_encode($result);
		echo $_REQUEST["callback"]."(".json_encode($result).")"; 	
		exit;
	}
	//推荐职位
	$post = array(
		'method'=>'job.search',
		'func' =>$job_id,
		'industry' =>$hy_id,
		'area'=>$area,
		'page_nums'=>5  //显示记录数
	);
	$result= $usrapi->postFun($post);
echo $_REQUEST["callback"]."(".json_encode($result).")"; 
exit();	
?>