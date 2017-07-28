<?php
//include 'test_a.php';
include '/var/webroot/wap_api/inc/Model/userResumeApi.php';
$usrapi = new userResumeApi();

//职位薪酬
	$job_id=$_GET['job'];
	$area_id=$_GET['area'];
	$result['error_code']=0;
	if(!is_numeric($job_id) || !is_numeric($area_id)){
		$result['error_code']=201;
	//	echo json_encode($result);
		echo $_REQUEST["callback"]."(".json_encode($result).")"; 	
		exit;
	}
	//获取薪酬
	
	$post = array(
		'method'=>'job.emolument',
		'job_id' =>$job_id,
		'area_id' =>$area_id,
	);
	$result= $usrapi->postFun($post);
	if(is_numeric($_GET['job2']) && is_numeric($_GET['area2'])){
		$post = array(
			'method'=>'job.emolument', //job.search
			'job_id' =>$_GET['job2'],
			'area_id' =>$_GET['area2'],
		);
		$result2= $usrapi->postFun($post);
		if($result2['error_code']==0 && $result2['salary']){
			$result['salary2']=$result2['salary'];
		}
	}

echo $_REQUEST["callback"]."(".json_encode($result).")"; 
exit();

?>