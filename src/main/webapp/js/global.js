var BookIt = BookIt || {};                                                                
                                              $("[data-role='page']").on('pagecreate',function (event, ui)
	{
	$("a.fullSiteLink").click(function(){
		$cookie("fullSiteClicked","true",{path:"/",expires:3600});
		}
	);
	}
	);
                                                                         
        
    