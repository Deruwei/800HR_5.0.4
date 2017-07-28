/**
 * File : c_industry.js
 * date : 2012-01-07
 * edit by handong
 */

function getIndustryJson(){
   //var arr={"11":"建筑行业","14":"医药行业","29":"化工行业","22":"机械行业","26":"服装行业","12":"金融行业","15":"教育培训","19":"电子行业","23":"IT行业","13":"传媒行业"};
   var arr=[{"id":"11","name":"建筑行业"},{"id":"14","name":"医药行业"},{"id":"29","name":"化工行业"},{"id":"22","name":"机械行业"},{"id":"26","name":"服装行业"},{"id":"12","name":"金融行业"},{"id":"15","name":"教育培训"},{"id":"19","name":"电子行业"},{"id":"23","name":"IT行业"},{"id":"13","name":"传媒行业"}];
   return arr;
/*   //返回指定行业
   if(typeof id =='number' || typeof id =='string'){
	   return arr[id];
   }else{
   return arr;
   }*/

}