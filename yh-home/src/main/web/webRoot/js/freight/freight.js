$(document).ready(function(){
    // 百度地图API功能
    $(function(){
        var map = new BMap.Map("allmap");
        var start = "";//始点
        var end = "";//终点
        var output = "";//距离
        if(localStorage.getItem("start") !=null) $("#cityBegin").val(localStorage.getItem("start"));
        map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
        //三种驾车策略：最少时间，最短距离，避开高速
       /* var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];*/
        function baidu(){
            $(".distanceCost").show();
            $(".allmap").show();
            //路线
            start = $("#cityBegin").val()/*"天安门"*/;
            end = $("#cityFinish").val()/*"百度大厦"*/;
            localStorage.setItem("start",start);//保存本地地址
            map.clearOverlays();
            /* var i=$("#driving_way select").val();*/
            search(start,end/*,routePolicy*//*[i]*/);
            function search(start,end,route){
                var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true},policy: route});
                driving.search(start,end);
            }

            //总的距离
            var searchComplete = function (results){
                if (transit.getStatus() != BMAP_STATUS_SUCCESS){
                    return ;
                }
                var plan = results.getPlan(0);//获得路线
                output = plan.getDistance(true) //获取距离
                var Nun = "";
                Nun = parseFloat(output);
                //计算费用
                var freightMin = 10;//5.2米货车每公里的费用最少
                var freightMax = 22; //5.2米货车每公里费用最多
                var freightSecondMin = 30;//9.6米货车每公里的费用
                var freightSecondMax = 40; //9.6米货车每公里费用
                var freightThirdlyMin = 50;//13.5米货车每公里的费用
                var freightThirdlyMax = 60; //13.5米货车每公里费用
                var freightFourthlyMin = 70;//17.5米货车每公里的费用
                var freightFourthlyMax = 80; //17.5米货车每公里费用
                var fMin = "";//5.2
                var fMax = "";//5.2
                var fSMin = "";//9.6
                var fSMax = "";//9.6
                var fTMin = "";//13.5
                var fTMax = "";//13.5
                var fFMin = "";//17.5
                var fFMax = "";//17.5
                fMin= freightMin*Nun;//5.2
                fMax = freightMax*Nun;//5.2
                fSMin = freightSecondMin*Nun;//9.6
                fSMax = freightSecondMax*Nun;//9.6
                fTMin = freightThirdlyMin*Nun;//13.5
                fTMax = freightThirdlyMax*Nun;//13.5
                fFMin = freightFourthlyMin*Nun;//17.5
                fFMax = freightFourthlyMax*Nun;//17.5
                $(".distance em").eq(0).text(fMin.toFixed(2));
                $(".distance em").eq(1).text(fMax.toFixed(2));
                $(".distance em").eq(2).text(fSMin.toFixed(2));
                $(".distance em").eq(3).text(fSMax.toFixed(2));
                $(".distance em").eq(4).text(fTMin.toFixed(2));
                $(".distance em").eq(5).text(fTMax.toFixed(2));
                $(".distance em").eq(6).text(fFMin.toFixed(2));
                $(".distance em").eq(7).text(fFMax.toFixed(2));

            }
            //赋值路程
            var transit = new BMap.DrivingRoute(map, {renderOptions: {map: map},
                onSearchComplete: searchComplete,
                onPolylinesSet: function(){
                    $("#allDistance").text(output)
                }
            });
            transit.search(start, end);
        }
        $("#cityInquire").click(function(){
            baidu();
        });
        $("#cityInquire").click(); 
        $(this).keydown(function (e){
            var key = e.which;
            console.log(key);
            if(key == "13"){
                baidu();
            }
        })
        /*$('.cityInquire:first').focus();
        var $inp = $('.cityInquire');
        $inp.bind('keydown', function (e) {
            var key = e.which;
            if (key == 13) {
                e.preventDefault();
                var nxtIdx = $inp.index(this) + 1;
                $(":input:text:eq(" + nxtIdx + ")").focus();
            }
            alert(key)
        });*/
    })
})