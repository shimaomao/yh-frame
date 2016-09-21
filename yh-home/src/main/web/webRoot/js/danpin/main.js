$(document).ready(function(){
	var path = getRootPath();
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

    /*****省份*********/
    $(function(){
        $(".province dd em").click(function(){
            $(".buttom_w").text($(this).text());
            $(".province").hide();
        })
    })

    /*******form********/
    $(function(){
        var pageTotal="";
        var _pageSize = 3;	//每页数量
        var _isPageInit = false;    //是否已初始化分页控件
        var formDiv = $(".seek_list input");
        var selectDiv = $(".selectClass");

        function add(page){
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
                area:"1001",
                source:selectDiv.eq(0).val(),
                invoiceType:selectDiv.eq(1).val(),
                skip:page,
                limit:_pageSize,
            };

            $.ajax({
                type: 'POST',
                url: path+"/price/findProduct",
                data: {"param" : JSON.stringify(param)},
                dataType: "json",
                /*contentType:""*/
                success: function(data){
                    var count=data.count;
                    var topDiv = $(".fomr_top em");
                    pageTotal=count[0].countTotal;
                    topDiv.eq(0).text(count[0].countTotal);
                    topDiv.eq(1).text(count[1].supplierTotal);
                    topDiv.eq(2).text(count[0].miDiameterMin);
                    topDiv.eq(3).text(count[0].miDiameterMax);
                    topDiv.eq(4).text(count[0].FareMin);
                    topDiv.eq(5).text(count[0].FareMax);
                    topDiv.eq(6).text(count[0].FareAvg);
                    topDiv.eq(7).text(count[0].TotalPriceMin);
                    topDiv.eq(8).text(count[0].TotalPriceMax);
                    topDiv.eq(9).text(count[0].PriceAvg);
                    var trhtml="";
                    trhtml+="<tr>"
                        +"<td class='location'>"
                        +"产品名称"
                        +"<span class='sort'>"
                            +"<img src='images/paixun.png'>"
                        +"</span>"
                        +"</td>"
                        +"<td>"
                        +"胸径/米径"
                        +"</td>"
                        +"<td>"
                        +"高度"
                        +"</td>"
                        +"<td>"
                        +"冠幅"
                        +"</td>"
                        +"<td>"
                        +"上车价"
                        +"</td>"
                        +"<td>"
                        +"发票类型"
                        +"</td>"
                        +"<td>"
                        +"到货价"
                        +"</td>"
                        +"<td>"
                        +"地区"
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
                        +"</td>"
                        +"<td>"
                        +"更新时间"
                        +"</td>"
                        +"<td>"
                        +"操作"
                        +"</td>"
                        +"</tr>";

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
                            + "<td name='mm'>"
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
                            + "<a href='#' class='badge bg-red'>"
                            + "查询运费"
                            + "</a>"
                            + "<a href='#' class='badge bg-red'>"
                            + "详情"
                            + "</a>"
                            + "</td>"
                            + "</tr>";
                    }
                    $(".table-bordered").html(trhtml + liHTML);

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
                            $("[name='mm']").eq(i).html(priceAll.toFixed(2));
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
            add(1);
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

        /******上传文件*****/
       /* $("#uploading").click(function(){
            var filename = $("#file").val();
            console.log(filename)
            $.ajax({
                url: "http://192.168.200.45:9090/price/excelData",
                type: "POST",
                dataType: "json",
                enctype:"multipart/form-data",
                data: {
                    file: filename
                },
                success: function (data) {
                    console.log(cosole);
                },
                error:function(){
                    alert(11)
                }
            });
        })*/

        $("#excel").on("click",function(){
            $(".folder").show();
        })
    })
    function getRootPath(){
        //获取当前网址
        var curWwwPath=window.document.location.href;
        //获取主机地址之后的目录
        var pathName=window.document.location.pathname;
        var pos=curWwwPath.indexOf(pathName);
        //获取主机地址
        var localhostPaht=curWwwPath.substring(0,pos);
        return(localhostPaht);
    }
})
