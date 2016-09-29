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
                $(".table_box").css({"min-width":"1920px","_min-width":"1920px"})
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
    	$(".buttom_w").val(localStorage.getItem("areaNo"));
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
                      sort:sort/* != ''?sort:sessionStorage.getItem("sort")*/,
                  };
        	  return param;
        }
        
        function add(page,sortField,sort){
	        	/*if($(".buttom_w").val() == ''){
	        		alert("请选择地区");
	        		return;
	        	}*/
                $.ajax({
                    type: 'POST',
                    url: "/price/findProduct",
                    data: {"param" : JSON.stringify(getParam(page,sortField,sort))},//提交数据到服务器
                    dataType: "json",
                    success: function(data){
                    	 var trhtml="";
                         trhtml+="<tr class='height_tr'>"
                             +"<td>"
                             +"产品名称"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='breedName' data-sort='-1'>升序排列</li><li class='aa' data-name='breedName' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"胸径/米径（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='miDiameterMax' data-sort='-1'>从大到小</li><li class='aa' data-name='miDiameterMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"高度（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='heightMax' data-sort='-1'>从大到小</li><li class='aa' data-name='heightMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"冠幅（cm）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='crownMax' data-sort='-1'>从大到小</li><li class='aa' data-name='crownMin' data-sort='1'>从小到大</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"上车价（元）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='startingFare' data-sort='-1'>从高到低</li><li class='aa' data-name='startingFare' data-sort='1'>从低到高</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"发票类型"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='invoiceType' data-sort='-1'>升序排列</li><li class='aa' data-name='invoiceType' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"到货价（元）"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='totalPrice' data-sort='-1'>从高到低</li><li class='aa' data-name='totalPrice' data-sort='1'>从低到高</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"地区"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='areaNo' data-sort='-1'>升序排列</li><li class='aa' data-name='areaNo' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td class='tabel_td'>"
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
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='source' data-sort='-1'>升序排列</li><li class='aa' data-name='source' data-sort='1'>降序排列</li></ul></div></div>"
                             +"</td>"
                             +"<td>"
                             +"更新时间"
                             +"<div class='sort'><img src='../images/sort_bel.png'><div class='rank'><ul><li class='aa' data-name='updateTime' data-sort='-1'>从远到近</li><li class='aa' data-name='updateTime' data-sort='1'>从近到远</li></ul></div></div>"
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
                    		$("#itemContainer").html(trhtml);
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
                                + data[i].source
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
                    	}
                        //组装
                        function judge(num1,num2){
                            if(num1 == 0 && num2 == 0 ){
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
                        var height = $(".height_tr").height();
                        $(".rank").css({"top":height})
                        $(".sort").click(function(){
                            $(this).children(".rank").toggle();
                        })
                        $("ul .aa").click(function(){
                            /*var _sortName = $(this).data("name");
                            var _sort = $(this).data("sort");*/
                            /*sessionStorage.setItem("sortName", $(this).data("name"));
                            sessionStorage.setItem("sort", $(this).data("sort"));*/
                            add(page,$(this).data("name"),$(this).data("sort"));
                            /*$(this).parents(".rank").attr("data-name",_sortName);*/
                            /*sessionStorage.setItem("sortName", $("#excelDivBox").data("name"));
                            sessionStorage.setItem("sort", $("#excelDivBox").data("sort"));*/
                            $("#excelDivBox").attr("name",$(this).data("name"));
                            $("#excelDivBox").val($(this).data("sort"));
                        })
                        /*if(sessionStorage.getItem("sortName") != null && sessionStorage.getItem("sort") != null){
                        	/!*$("[data-name='"+sessionStorage.getItem("sortName")+"']").attr("data-sort",sessionStorage.getItem("sort"));*!/
                            var Max=new RegExp('Max');
                            var Min=new RegExp('Min');
                            var name= sessionStorage.getItem($(this).data("name"));
                           /!* if(sessionStorage.getItem("sortName"))
                            {

                                $("[data-name='"+name+"']").attr("data-sort",sessionStorage.getItem("sort"));
                            }*!/
                            if(Max.test(name)){
                                var new_name=name.replace("Max","Min");
                                $("[data-name='"+name+"']").attr("data-name",new_name);
                            }
                            if(Min.test(name)){
                                var new_name=name.replace("Min","Max");
                                $("[data-name='"+name+"']").attr("data-name",new_name);
                            }/!*
                            i
                            if(sort==1){
                                $(".sort").addClass("sort_on")
                            }
                            if(sort==-1){
                                $(".sort").removeClass("sort_on");
                            }*!/
                        }*/

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
                                   /* add(page);*/
                                    var bb = "", aa = "";
                                    if($("#excelDivBox").attr("name") != null && $("#excelDivBox").val() != null){
                                        bb = $("#excelDivBox").attr("name");
                                        aa = parseInt($("#excelDivBox").val());
                                    }else{
                                        bb = sortField;
                                        aa = sort;
                                    }
                                    add(page,bb,aa);
                                }
                            });
                            _isPageInit = true;
                        }
                      
                  	  /****税率***/
                      function rate(data) {
                         /* var _priceAl = "";//税率
                          var _price = " ";//税率
                          _price = $("#taxRate").val();
                          var _newstr=_price.replace(/%/, "");
                          _decimal = _newstr/100;
                          var mm =(_decimal+1);//税后*/
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
                var arr=filePath.split('\\');
                var fileName=arr[arr.length-1];
                $(".showFileName").html(fileName);
                $(".showFileName").css({"color":"#888"})
            }else{
                $(".showFileName").html("上传文件类型有误！");
                $(".showFileName").css({"color":"#f00"})
                return false;
            }
        })
        
        $(".folder_cancel").click(function(){
            $(".folder").hide();
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
     		var option = {
     				url : "/price/excelData",
     				type : 'POST',
     				dataType : "json",
     				clearForm: true,
     				success : function(data) {
     					 $(".folder").hide();
     					if(data.code == 0){
     						$(".main_form").hide();
     						var result = data.result;
     						 var excelHtml="<tr><th>序号</th><th>品种名称</th><th>胸径</th><th>高度</th><th>冠幅</th></tr>";
     						$(result).each(function(index){
     							    if(index ==0) return;
     							 excelHtml+="<tr name='a'>"
     								 +"<td>"
      	                             +index
      	                             +"</td>"
     	                             +"<td>"
     	                             +"<a class='appoint'>"     	                          
     	                             +result[index].productName
     	                             +"</a>"
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
     						})
     					}else{
     						$(".showFileName").html(data.msg);
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
