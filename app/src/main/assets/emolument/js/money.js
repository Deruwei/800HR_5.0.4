$(document).ready(function(){
		$("#switch_options").click(function(){
			if($(this).attr("data-icon")=="arrow-d")
				{$(this).buttonMarkup({ icon: "arrow-u" });}
			else{
				$(this).buttonMarkup({ icon: "arrow-d" });
			}			
			$("#options").toggle();			
		});
		$("#switch_options_compare").click(function(){
			if($(this).attr("data-icon")=="arrow-d")
				{$(this).buttonMarkup({ icon: "arrow-u" });}
			else{
				$(this).buttonMarkup({ icon: "arrow-d" });
			}
			$("#options_compare").toggle();			
		});
});