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
        function add(){
            if($(window).width()<=1440){
                $(".box-body").addClass("box-body_on")
                $(".table_box").css({"min-width":"1620px"})
            }else{
                $(".box-body").removeClass("box-body_on")
            }
        }
        add();
        $(window).resize(function(){
            add();
        })
    })

    $(function(){
        $(".fomr_type li").last().css({"border-right":"none"})
    })
    
    /****清空*****/
    $("#empty").click(function(){
    	  $("[name=breedName]").val('');
    	  $("[name=midiaMeterMin]").val('');
    	  $("[name=midiaMeterMax]").val('');
    	  $("[name=updateTimeMin]").val('');
    	  $("[name=updateTimeMax]").val('');
    	  $("[name=heightMin]").val('');
    	  $("[name=heightMax]").val('');
    	  $("[name=crownMin]").val('');
    	  $("[name=crownMax]").val('');
    	  var date = new Date();
     	  $('#time2').val(getNowFormatDate(date));
     	  date.setDate(date.getDate() - 59);
     	  $('#time').val(getNowFormatDate(date));
     	  $(".buttom_w").val("");
     	  $(".buttom_w").text('选择省份');
     	  localStorage.clear();
    });
    
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
	    var year = date.getFullYear();//年份
	    var month = date.getMonth() + 1;//月份
	    var strDate = date.getDate();//日
        var hours = date.getHours();//小时
        var minutes= date.getMinutes();
        var Seconds = date.getSeconds();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = year + seperator1 + month + seperator1 + strDate;
	    return currentdate;
    }

    /**********省份************/
    /*$(function(){
        $.ajax({
            url:"../json/json.json",
            dataType : "json",
            clearForm: true,
            success:function(data){
                var data = data.result;
                var arr1 = [];
                var arr2 = [];
                var arr3 = [];
                $.each(data,function (index){
                    if(data[index].areaNo.length==4){
                       arr1.push(data[index]);
                    }else{
                       arr2.push(data[index]);
                    }

                });
                $("[name=areaNo]").each(function(i){
                    $(this).click(function(){
                        $(this).val(arr1[i].areaNo);
                        alert($(this).val())
                    })
                })
            }
        })

    })*/
    
    /*****省份*********/
    $(function(){
    	$(".buttom_w").val(localStorage.getItem("areaNo") == null?'':localStorage.getItem("areaNo"));
    	var area = localStorage.getItem("area");
    	$(".buttom_w").text(area == null?'选择省份':area);
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
            localStorage.setItem("area", $(this).text());
        })
        $("[name=nn]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
            localStorage.setItem("area", $(this).text());
        })
        $("[name=aa]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
            localStorage.setItem("area", $(this).text());
        })
        $("[name=bb]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
            localStorage.setItem("area", $(this).text());
        })
        $("[name=cc]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
            localStorage.setItem("area", $(this).text());
        })
        $("[name=dd]").click(function(){
            $(".buttom_w").val($(this).val());
            $(".buttom_w").text($(this).text());
            $(".province").hide();
            localStorage.setItem("areaNo", $(this).val());
            localStorage.setItem("area", $(this).text());
        })
    })
    
    /******* 验证信息 *******/
 	function validation(){
   		var $midiaMeterMin = $("[name=midiaMeterMin]");
   		var $midiaMeterMax = $("[name=midiaMeterMax]");
   		var $updateTimeMin = $("[name=updateTimeMin]");
   		var $updateTimeMax = $("[name=updateTimeMax]");
   		var $heightMin = $("[name=heightMin]");
   		var $heightMax = $("[name=heightMax]");
   		var $crownMin = $("[name=crownMin]");
   		var $crownMax = $("[name=crownMax]");
   		if(isNaN($midiaMeterMin.val()) || ($midiaMeterMin.val() != '' && $midiaMeterMin.val() <=0)){
   			alert("请输入正确的胸径/米径最小值！");
   			$midiaMeterMin.focus();
   			return false;
   		}
   		if(isNaN($midiaMeterMax.val()) || ($midiaMeterMax.val() != '' && $midiaMeterMax.val() <=0)){
   			alert("请输入正确的胸径/米径最大值！");
   			$miDiameterMax.focus();
   			return false;
   		}
   		if(isNaN($heightMin.val()) || ($heightMin.val() != '' && $heightMin.val() <=0)){
   			alert("请输入正确的高度最小值！");
   			$heightMin.focus();
   			return false;
   		}
   		if(isNaN($heightMax.val()) || ($heightMax.val() != '' && $heightMax.val() <=0)){
   			alert("请输入正确的高度最大值！");
   			$heightMax.focus();
   			return false;
   		}
   		if(isNaN($crownMin.val()) || ($crownMin.val() != '' && $crownMin.val() <=0)){
   			alert("请输入正确的冠幅最小值！");
   			$crownMin.focus();
   			return false;
   		}
   		if(isNaN($crownMax.val()) || ($crownMax.val() != '' && $crownMax.val() <=0)){
   			alert("请输入正确的冠幅最大值！");
   			$crownMax.focus();
   			return false;
   		}
   		if($crownMax.val() != '' && $crownMin.val() != '' && new Number($crownMax.val())<new Number($crownMin.val())){
   			alert("冠幅最大值不能小于最小值！");
   			$crownMax.focus();
   			return false;
   		}
   		if($heightMax.val() != '' && $heightMin.val() != '' && new Number($heightMax.val())<new Number($heightMin.val())){
   			alert("高度最大值不能小于最小值！");
   			$heightMax.focus();
   			return false;
   		}
   		if($midiaMeterMax.val() != '' && $midiaMeterMin.val() != '' && new Number($midiaMeterMax.val())<new Number($midiaMeterMin.val())){
   			alert("胸径最大值不能小于最小值！");
   			$midiaMeterMin.focus();
   			return false;
   		}
   		if($updateTimeMin.val() != '' && $updateTimeMax.val() != ''){
   			if(!checkEndTime($updateTimeMin.val(),$updateTimeMax.val())){
   				alert("时间最大值不能小于最小值！");
   				return false;
   			}	
   		}
   		return true;
   	}

    /******* 时间验证 *******/
    function checkEndTime($startTime,$endTime){  
        var startTime=$startTime;  
        var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
        var endTime=$endTime;  
        var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
        if(end<start){  
            return false;  
        }  
        return true;  
    }  
    
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
                      sortField:sortField /*!= ''?sortField:sessionStorage.getItem("sortName")*/,
                      sort:sort/* != ''?sort:sessionStorage.getItem("sort")*/
                  };
        	  return param;
        }
        
        function add(page,sortField,sort,isPage){
                $.ajax({
                    type: 'POST',
                    url: "/price/findProduct",
                    data: {"param" : JSON.stringify(getParam(page,sortField,sort))},//提交数据到服务器
                    dataType: "json",
                    success: function(data){
                    	 var trhtml="";
                         trhtml+="<tr class='height_tr'>"
                             +"<td style='width:95px;'>"
                             +"产品名称"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='breedName' data-sort='-1'>升序排列</li><li class='aa' data-name='breedName' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:150px;'>"
                             +"胸径/米径（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='miDiameterMax' data-sort='-1'>从大到小</li><li class='aa' data-name='miDiameterMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:115px;'>"
                             +"高度（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='heightMax' data-sort='-1'>从大到小</li><li class='aa' data-name='heightMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:115px;'>"
                             +"冠幅（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='crownMax' data-sort='-1'>从大到小</li><li class='aa' data-name='crownMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:125px;'>"
                             +"上车价（元）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='startingFare' data-sort='-1'>从高到低</li><li class='aa' data-name='startingFare' data-sort='1'>从低到高</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:95px;'>"
                             +"发票类型"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='invoiceType' data-sort='-1'>升序排列</li><li class='aa' data-name='invoiceType' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:125px;'>"
                             +"到货价（元）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='totalPrice' data-sort='-1'>从高到低</li><li class='aa' data-name='totalPrice' data-sort='1'>从低到高</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:105px;'>"
                             +"地区"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='areaNo' data-sort='-1'>升序排列</li><li class='aa' data-name='areaNo' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td class='tabel_td'>"
                             +"供应商"
                             +"</td>"
                             +"<td style='width:70px;'>"
                             +"联系人"
                             +"</td>"
                             +"<td style='width:100px;'>"
                             +"手机"
                             +"</td>"
                             +"<td style='width:90px;'>"
                             +"数据源"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='source' data-sort='-1'>升序排列</li><li class='aa' data-name='source' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:100px;'>"
                             +"更新时间"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='updateTime' data-sort='-1'>从远到近</li><li class='aa' data-name='updateTime' data-sort='1'>从近到远</li></ul></div></div>"
                             +"</td>"
                             +"<td style='width:140px;'>"
                             +"操作"
                             +"</td>"
                             +"</tr>";
                       //统计和分页
                       total(isPage,data.body,data.result.length,_isPageInit,_pageSize,sortField,sort);  
                      
					   if(data.result.length > 0){
                        var liHTML = "";
                        var data=data.result;
                        var _priceAl = "";//税率
                        var _price = " ";//税率
                        _price = $("#taxRate").val();
                        var _newstr=_price.replace(/%/, "");
                        _decimal = _newstr/100;
                        var mm =(_decimal+1);//税后

                        for (var i= 0,max= data.length;i < max; i++ ) {
                            priceAll= data[i].startingFare*mm;
                            var decimal = priceAll.toFixed(2)
                            liHTML += "<tr>"
                                + "<td>"
                                + data[i].productName
                                + "</td>"
                                + "<td>"
                                + judge(data[i].miDiameterMax,data[i].miDiameterMin)
                                + "</td>"
                                + "<td>"
                                + judge(data[i].heightMax,data[i].heightMin)
                                + "</td>"
                                + "<td>"
                                + judge(data[i].crownMax,data[i].crownMin)
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
                                + (data[i].source=='1'?"外部数据":"其他数据")
                                + "</td>"
                                + "<td>"
                                + data[i].updateTime
                                + "</td>"
                                + "<td>"
                                + "<a href='/price/freightPage/"+data[i].area+"' target='_Blank' class='badge bg-red'>"
                                + "查询运费"
                                + "</a>"
                                + "<a href='"+data[i].details+"' target='_Blank' class='badge bg-red'>"
                                + "详情"
                                + "</a>"
                                + "</td>"
                                + "</tr>";
                        	}
                        	$("#itemContainer").html(trhtml + liHTML);
                    	}else{
                    		$("#itemContainer").html(trhtml);
                    	}
                      
                        //组装冠幅、高度、米径/胸径的值
                        function judge(num1,num2){
                            if((num1 == 0 && num2 == 0) || (num1 == null && num2 == null)){
                                return "";
                            }
                            if(num1== num2){
                                return num1;
                            }
                            else {
                                return num2+"-"+num1;
                            }
                        }
                        //排序
                        $(".sort").hover(function(){
                            $(this).children(".rank").show();
                        },function(){
                            $(this).children(".rank").hide();
                        })
                        $("ul .aa").click(function(){
                            add(page,$(this).data("name"),$(this).data("sort"),false);
                            $("#excelDivBox").attr("name",$(this).data("name"));
                            $("#excelDivBox").val($(this).data("sort"));
                        })

                      
                  	  /****税率***/
                      function rate(data) {
                          var _priceAl = "";//税率
                          var _price = " ";//税率
                          _price = $("#taxRate").val();
                          var _newstr=_price.replace(/%/, "");
                          _decimal = _newstr/100;
                          var mm =(_decimal+1);//税后
                          for(var i= 0,max= data.length;i < max; i++){
                              var priceAll= data[i].startingFare*mm;
                              $("[name='decimal']").eq(i).html(priceAll.toFixed(2));
                          }
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
        $("#search").click(function(){
        	_isPageInit = false;
        	if(validation()){
        		add(1,null,null,true);
                $(".fomr_data li").last().css({"border-right":"none"})
                $(".main_form").show();
        	}
        })

        /*********统计***********/
     	function total(isPage,body,length,_isPageInit,_pageSize,sortField,sort){
            if(isPage){
           	 if(length == 0){
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
                    $("#paging").hide();
          	}else{
          		   $.ajax({
                        type: 'POST',
                        url: "/price/total",
                        data: {"param" : JSON.stringify(body)},//提交数据到服务器
                        dataType: "json",
                        success: function(data){
                             var count=data.count;
                             var topDiv = $(".fomr_top em");
                             var pageTotal=count[1].productTotal;
                             topDiv.eq(0).text(count[1].productTotal);
                             topDiv.eq(1).text(count[1].supplierTotal);
                             topDiv.eq(2).text(count[0].miDiameterMin);
                             topDiv.eq(3).text(count[0].miDiameterMax);
                             topDiv.eq(4).text(count[0].FareMin);
                             topDiv.eq(5).text(count[0].FareMax);
                             topDiv.eq(6).text(count[0].FareAvg.toFixed(2));
                             topDiv.eq(7).text(count[0].TotalPriceMin);
                             topDiv.eq(8).text(count[0].TotalPriceMax);    
                             /*****分页初始化*****/
                             if(!_isPageInit){
                            	 $("#paging").show();
                                 $("#paging").pagination(pageTotal, {
                                     items_per_page: _pageSize,	//每页数量
                                     num_display_entries: 5,
                                     num_edge_entries: 2,
                                     prev_text: "上一页",
                                     next_text: "下一页",
                                     callback:function(page){
                                         var bb = "", aa = "";
                                         if($("#excelDivBox").attr("name") != null && $("#excelDivBox").val() != null){
                                             bb = $("#excelDivBox").attr("name");
                                             aa = parseInt($("#excelDivBox").val());
                                         }else{
                                             bb = sortField;
                                             aa = sort;
                                         }
                                         add(page+1,bb,aa,false);
                                     }
                                 });
                                 _isPageInit = true;
                             }
                        }
                   }); 
               }	         
           	}
        }      
    })


    
    /********excel**********/
    $(function(){
        /***获得文件路径***/
        $(".a-upload").on("change","input[type='file']",function(){
            $(".main_form").addClass("mb_wd")
            var filePath=$(this).val();
            console.log(filePath);
            if(filePath.indexOf("xls")!=-1 || filePath.indexOf("xlsx")!=-1){
                var arr=filePath.split('\\');
                var fileName=arr[arr.length-1];
            }else{
                $(".showFileName").html("上传文件类型有误！");
                return false;
            }
            if(filePath != null){
                $(".showFileName").html(null);
                $(".excelStoll").show();
                var option = {
                    url : "/price/excelData",
                    type : 'POST',
                    dataType : "json",
                    clearForm: true,
                    success : function(data) {
                        if(data.code == 0){
                            $(".main_form").hide();
                            var result = data.result;
                            var excelHtml="<tr><th>序号</th><th style='width:110px;'>品种名称</th><th>胸径</th><th>高度</th><th>冠幅</th></tr>";
                            $(result).each(function(index){
                                if(index ==0) return;
                                excelHtml+="<tr name='a'>"
                                    +"<td>"
                                    +index
                                    +"</td>"
                                    +"<td>"
                                    +"<a class='appoint'>"
                                    +(result[index].productName == null?'':result[index].productName)
                                    +"</a>"
                                    +"</td>"
                                    +"<td>"
                                    +(result[index].midiaMeter == null?'':result[index].midiaMeter)
                                    +"</td>"
                                    +"<td>"
                                    +(result[index].height == null?'':result[index].height)
                                    +"</td>"
                                    +"<td>"
                                    +(result[index].crown == null?'':result[index].crown)
                                    +"</td>"
                                    +"</tr>"
                            });
                            $("#excelTable").html(excelHtml);
                            $(".appoint").click(function(){
                                $("[name='breedName']").val($(this).text());
                                var nextAll = $(this).parent().nextAll();
                                $(nextAll).each(function(index){
                                    var value = $.trim($(this).text());
                                    var sear=new RegExp('--');
                                    var arr = [];
                                    if(sear.test(value)){
                                        arr = value.split('--');
                                    }else{
                                        arr = value.split('-');    							　　}
                                    if(index == 0){
                                        if(arr.lenght<2){
                                            $("[name='midiaMeterMin']").val(arr[0]);
                                        }else{
                                            if(arr[0]<arr[1]){
                                                $("[name='midiaMeterMin']").val(arr[0]);
                                                $("[name='midiaMeterMax']").val(arr[1]);
                                            }else{
                                                $("[name='midiaMeterMin']").val(arr[1]);
                                                $("[name='midiaMeterMax']").val(arr[0]);
                                            }
                                        }
                                    }
                                    if(index == 1){
                                        if(arr.lenght<2){
                                            $("[name='heightMin']").val(arr[0]);
                                        }else{
                                            if(arr[0]<arr[1]){
                                                $("[name='heightMin']").val(arr[0]);
                                                $("[name='heightMax']").val(arr[1]);
                                            }else{
                                                $("[name='heightMin']").val(arr[1]);
                                                $("[name='heightMax']").val(arr[0]);
                                            }
                                        }
                                    }
                                    if(index == 2){
                                        if(arr.lenght<2){
                                            $("[name='crownMin']").val(arr[0]);
                                        }else{
                                            if(arr[0]<arr[1]){
                                                $("[name='crownMin']").val(arr[0]);
                                                $("[name='crownMax']").val(arr[1]);
                                            }else{
                                                $("[name='crownMin']").val(arr[1]);
                                                $("[name='crownMax']").val(arr[0]);
                                            }
                                        }
                                    }
                                });
                                $("#search").click();
                                $(".main_form").addClass("product_form");
                                $(".box-body").addClass("product_box");
                                $(".table_box").addClass("product_tabel");
                                if($(window).width()<=1440){
                                    $(".mb_wd").css({"width":"1000px"})
                                }
                            })
                        }else{
                            $(".showFileName").html(data.msg);
                        }
                    }
                };
                $("#excelUpload").ajaxSubmit(option);
            }
        })

        /******上传excel文件*****/
         $("#uploading").click(function(){
        	var file = $("#file").val();
     		//如果文件为空
     		if (file == '') {
     			$(".showFileName").html('请上传excel文件!');
     			return;
     		}
     		//如果文件不是xls或者xlsx 提示输入正确的excel文件
    		if ((file.indexOf('.xls') == -1 && file.indexOf('.xlsx') == -1)) {
    			$(".showFileName").html('请上传正确的excel,后缀名为xls或xlsx!');
    			return;
    		}
     		return false;

         });
    })

})
