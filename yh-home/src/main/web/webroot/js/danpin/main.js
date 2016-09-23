$(document).ready(function(){
    jQuery.support.cors = true;//跨域
    $(function() {
        $(".buttom_w").click(function () {
            $(".province").toggle();
        })
        $(".sel_data em").click(function(){
            $(this).next("dl").toggle();
        })
        $(".sel_data dd").click(function(){
            $(this).parent("dl").siblings("em").text($(this).text());
            $(this).parent("dl").hide();
        })
    })

    $(function(){
        $(".fomr_type li").last().css({"border-right":"none"})
    })
    
    /*****设置时间*********/
    $(function(){
    	 var date = new Date();
    	 $('#time2').val(getNowFormatDate(date));
    	 date.setDate(date.getDate() - 59);
    	 $('#time').val(getNowFormatDate(date));
    })
    function getNowFormatDate(Date) {
	    var date = Date;
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var year = date.getFullYear();
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = year + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    return currentdate;
    }
    
    /*****省份*********/
    $(function(){
    	$(".buttom_w").val(localStorage.getItem("areaNo"));
        var div = $(".province dd em");
        var _initialize = 1001;
        for(var i= 0,max=div.length;i<max;i++){
            var mm = _initialize + i;
            var nn = _initialize*1000 + i+1;
            var aa = (_initialize+1)*1000 + i+1;
            var bb = (_initialize+2)*1000 + i+1;
            var cc = (_initialize+3)*1000 + i+1;
            var dd = (_initialize+4)*1000 + i+1;

            $("[name=mm]").eq(i).val(mm);
            $("[name=nn]").eq(i).val(nn);
            $("[name=aa]").eq(i).val(aa);
            $("[name=bb]").eq(i).val(bb);
            $("[name=cc]").eq(i).val(cc);
            $("[name=dd]").eq(i).val(dd);

        }
        $("[name=mm]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
        $("[name=nn]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
        $("[name=aa]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
        $("[name=bb]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
        $("[name=cc]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
        $("[name=dd]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
        })
    })

    /*******form********/
    $(function(){
        var pageTotal="";
        var _pageSize = 20;	//每页数量
        var _isPageInit = false;    //是否已初始化分页控件
        var formDiv = $(".seek_list input");
        var selectDiv = $(".selectClass");

        function getParam(page,sortField,sort){
        	  var param ={
                      breedName:formDiv.eq(0).val(),
                      miDiameterMin:new Number(formDiv.eq(1).val()),
                      miDiameterMax:new Number(formDiv.eq(2).val()),
                      updateTimeMin:formDiv.eq(3).val(),
                      updateTimeMax:formDiv.eq(4).val(),
                      heightMin:new Number(formDiv.eq(5).val()),
                      heightMax:new Number(formDiv.eq(6).val()),
                      crownMin:new Number(formDiv.eq(7).val()),
                      crownMax:new Number(formDiv.eq(8).val()),
                      areaNo:$(".buttom_w").val(),
                      sourceNo:$(".selectClass option:selected").val(),
                      invoiceType:selectDiv.eq(1).val(),
                      skip:page,
                      limit:_pageSize,
                      sortField:sortField,
                      sort:sort
                  };
        	  return param;
        }
        
        function add(page,sortField,sort){
	        	if($(".buttom_w").val().trim() == ''){
	        		alert("请选择地区");
	        		return;
	        	}
                $.ajax({
                    type: 'POST',
                    url: "/price/findProduct",
                    data: {"param" : JSON.stringify(getParam(page,sortField,sort))},
                    dataType: "json",
                    success: function(data){
                    	 var trhtml="";
                         trhtml+="<tr>"
                             +"<td>"
                             +"产品名称"
                             +"<div class='sort' data-name='breedName' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"胸径/米径"
                             +"<div class='sort' data-name='midiaMeter' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"高度"
                             +"<div class='sort' data-name='height' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"冠幅"
                             +"<div class='sort' data-name='crown' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"上车价"
                             +"<div class='sort' data-name='startingFare' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"发票类型"
                             +"<div class='sort' data-name='invoiceType' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"到货价"
                             +"<div class='sort' data-name='totalPrice' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"地区"
                             +"<div class='sort' data-name='areaNo' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"供应商"
                             +"</td>"
                             +"<td>"
                             +"联系人"
                             +"</td>"
                             +"<td>"
                             +"手机"
                             +"</td>"
                             +"<td>"
                             +"数据源"
                             +"<div class='sort' data-name='source' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"更新时间"
                             +"<div class='sort' data-name='updateTime' data-sort='1'>"
                             +"<img src='../images/paixun.png'>"
                             +"</div>"
                             +"</td>"
                             +"<td>"
                             +"操作"
                             +"</td>"
                             +"</tr>";
                    	if(data.result.length == 0){
                    		  var topDiv = $(".fomr_top em");
                              pageTotal=0;
                              topDiv.eq(0).text(0);
                              topDiv.eq(1).text(0);
                              topDiv.eq(2).text(0);
                              topDiv.eq(3).text(0);
                              topDiv.eq(4).text(0);
                              topDiv.eq(5).text(0);
                              topDiv.eq(6).text(0);
                              topDiv.eq(7).text(0);
                              topDiv.eq(8).text(0);
                              topDiv.eq(9).text(0);
                    		$(".table-bordered").html(trhtml);
                    	}else{
                        var count=data.count;
                        var topDiv = $(".fomr_top em");
                        pageTotal=count[0].countTotal;
                        topDiv.eq(0).text(count[0].countTotal);
                        topDiv.eq(1).text(count[1].supplierTotal);
                        topDiv.eq(2).text(count[0].miDiameterMin);
                        topDiv.eq(3).text(count[0].miDiameterMax);
                        topDiv.eq(4).text(count[0].FareMin);
                        topDiv.eq(5).text(count[0].FareMax);
                        topDiv.eq(6).text(count[0].FareAvg.toFixed(2));
                        topDiv.eq(7).text(count[0].TotalPriceMin);
                        topDiv.eq(8).text(count[0].TotalPriceMax);
                        topDiv.eq(9).text(count[0].PriceAvg.toFixed(2));
                        var liHTML = "";
                        var data=data.result;
                        var _priceAl = "";//税率
                        var _price = " ";//税率
                        _price = $("#taxRate").val();
                        var _newstr=_price.replace(/%/, "");
                        _decimal = _newstr/100;
                        var tax =(_decimal+1);//税后

                        for (var i= 0,max= data.length;i < max; i++ ) {
                            priceAll= data[i].startingFare*tax;
                            var decimal = priceAll.toFixed(2)
                            liHTML += "<tr>"
                                + "<td>"
                                + data[i].productName
                                + "</td>"
                                + "<td>"
                                + data[i].miDiameter
                                + "</td>"
                                + "<td>"
                                + data[i].height
                                + "</td>"
                                + "<td>"
                                + data[i].crown
                                + "</td>"
                                + "<td name='decimal'>"
                                + /*data[i].startingFare*/decimal
                                + "</td>"
                                + "<td>"
                                + data[i].invoiceType
                                + "</td>"
                                + "<td>"
                                + data[i].totalPrice
                                + "</td>"
                                + "<td>"
                                + data[i].area
                                + "</td>"
                                + "</td>"
                                + "<td>"
                                + data[i].supplier
                                + "</td>"
                                + "<td>"
                                + data[i].contacts
                                + "</td>"
                                + "<td>"
                                + data[i].tel
                                + "</td>"
                                + "<td>"
                                + data[i].source
                                + "</td>"
                                + "<td>"
                                + data[i].updateTime
                                + "</td>"
                                + "<td>"
                                + "<a href='/price/freightPage/"+data[i].area+"' class='badge bg-red'>"
                                + "查询运费"
                                + "</a>"
                                + "<a href='"+data[i].details+"' class='badge bg-red'>"
                                + "详情"
                                + "</a>"
                                + "</td>"
                                + "</tr>";
                        	}
                        	$(".table-bordered").html(trhtml + liHTML);
                    	}
                       
                        //排序
                        $(".sort").click(function(){
                    		add(page,$(this).data("name"),$(this).data("sort"));
                    		sessionStorage.setItem("sortName", $(this).data("name"));
                    		sessionStorage.setItem("sort", $(this).data("sort") == 1?-1:1);
                        })
                        if(sessionStorage.getItem("sortName") != null && sessionStorage.getItem("sort") != null){
                        	$("[data-name='"+sessionStorage.getItem("sortName")+"']").attr("data-sort",sessionStorage.getItem("sort"));
                        }
                        	

                        /*****分页初始化*****/
                        if(!_isPageInit){
                            $("#paging").pagination(pageTotal, {
                                items_per_page: _pageSize,	//每页数量
                                num_display_entries: 5,
                                num_edge_entries: 2,
                                prev_text: "上一页",
                                next_text: "下一页",
                                callback:function(page){
                                    /*console.log("" + page);*/
                                    add(page);
                                }
                            });
                            _isPageInit = true;
                        }
                      
                  	  /****税率***/
                      function rate(data) {
                          var _priceAl = "";//税率
                          var _price = " ";//税率
                          _price = $("#taxRate").val();
                          var _newstr=_price.replace(/%/, "");
                          _decimal = _newstr/100;
                          var mm =(_decimal+1);//税后
                          for(var i= 0,max= data.length;i < max; i++){
                              /* var bb=data[i].startingFar;*/
                              priceAll= data[i].startingFare*mm;
                              /* var nn = Math.floor(priceAll*100)/100;*/
                              $("[name='decimal']").eq(i).html(priceAll.toFixed(2));
                          }
                          /* alert(bb);*/
                      }
                      $("#taxRate").change(function(){
                          rate(data);
                      })
                    },
                    error:function(data,msg){
                        alert(msg);
                    }
                });
        }

        //查询
        $("#search").on("click",function(){
        	_isPageInit = false;
            add(1,null,null);
            /*rate();*/
            $(".fomr_data li").last().css({"border-right":"none"})
            $(".main_form").show();
        })
    })


    /********excel**********/
    $(function(){
        /***获得文件路径***/
        $(".a-upload").on("change","input[type='file']",function(){
            var filePath=$(this).val();
            console.log(filePath);
            if(filePath.indexOf("xls")!=-1 || filePath.indexOf("xlsx")!=-1){
                /*$(".fileerrorTip").html("").hide();*/
                var arr=filePath.split('\\');
                var fileName=arr[arr.length-1];
                $(".showFileName").html(fileName);
                /*$('#button').removeAttr("disabled",'true');*/
            }else{
                $(".showFileName").html("上传文件类型有误！");
                /*return false;*/
                /*$("#uploading").attr('disabled','true');*/
                return false;
            }
        })

        /******上传excel文件*****/
         $("#uploading").click(function(){
        	var file = $("#file").val();
     		//如果文件为空
     		if (file == '') {
     			alert('请上传excel文件!');
     			return;
     		}
     		//如果文件不是xls或者xlsx 提示输入正确的excel文件
    		if ((file.indexOf('.xls') == -1 && file.indexOf('.xlsx') == -1)) {
    			alert('请上传正确的excel,后缀名为xls或xlsx!');
    			return;
    		}
     		var option = {
     				url : "/price/excelData",
     				type : 'POST',
     				dataType : "json",
     				clearForm: true,
     				success : function(data) {
     					 $(".folder").hide();
     					if(data.code == 0){
     						var result = data.result;
     						 var excelHtml="<tr><th>序号</th><th>品种名称</th><th>胸径</th><th>高度</th><th>冠幅</th></tr>";
     						$(result).each(function(index){
     							    if(index ==0) return;
     							 excelHtml+="<tr>"
     								 +"<td>"
      	                             +index
      	                             +"</td>"
     	                             +"<td>"
     	                             +result[index].productName
     	                             +"</td>"
     	                             +"<td>"
    	                             +result[index].midiaMeter
    	                             +"</td>"
    	                             +"<td>"
     	                             +result[index].height
     	                             +"</td>"
     	                             +"<td>"
    	                             +result[index].crown
    	                             +"</td>"
     	                             +"</tr>"
     						});
     						$("#excelTable").html(excelHtml);
     					}else{
     						alert(data.msg);
     					}
     				}
     		};
     		$("#excelUpload").ajaxSubmit(option);
     		return false;
     		
         });
        $("#excel").on("click",function(){
            $(".folder").show();
        })
       
    })

})
