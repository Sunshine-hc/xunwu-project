/**
 *
 */
var map=null;
var regionList;
var label;//每个区的对象
var regionCountMap = {},//地区数据
    labels = [],//每个区的标签
    thirdlyMkr = [],//小区标签
    params ={
        orderBy:'lastUpdateTime',
        orderDirection:'desc'
    },
    thirdlyData={

    } ,//小区数据
    regionMap={},//地区数据
    districtparams={
        orderBy:'lastUpdateTime',
        orderDirection:'desc'
    };//加载小区房源参数
var styleJson=data2;
//获取当前城市的中英文名
var cityEnName = $('#cityEnName').val();
var cityCnName = $('#cityCnName').val();
var orderby;

// 是否处于画圈状态下
var isInDrawing = false;
// 是否处于鼠标左键按下状态下
var isMouseDown = false;
// 存储画出折线点的数组
var polyPointArray = [];
// 上次操作画出的折线
var lastPolyLine = null;
// 画圈完成后生成的多边形
var polygonAfterDraw = null;
var drawBtn = document.getElementById("draw");
var exitBtn = document.getElementById("exit");
function load(city,regions,aggData){
    // thirdlyMkr = [];

    //画百度地图
    map = new BMap.Map("allmap",{minZoom:12});//创建实例。设置地图最大显示级别为城市
    map.clearOverlays();
    var  point = new BMap.Point(city.baiduMapLongtiude, city.baiduMapLatitude); // 城市中心
    map.centerAndZoom(point, 12); // 初始化地图，设置中心点坐标及地图级别
    map.addControl(new BMap.NavigationControl({enableGeoloation:true}));//添加比例尺控件
    map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_TOP_LEFT}));//左上角
    map.enableScrollWheelZoom(true);//开启鼠标滚轮缩放
    //夜间模式
    // map.setMapStyleV2({
    //     styleId: '1d9b2bf6f424b5eb620bd53ad1eec9b3',
    //     styleJson:styleJson
    // });
    //用来实现显示每个区域有多少套房子
    for (let i = 0; i < aggData.length ; i++) {
        regionCountMap[aggData[i].key] = aggData[i].count;
    }
    this.regionList=regions;
    drawing();
    drawRegion();
    loadHouseData();
    loaddistrict();//加载小区数据
    //缩放到很大时隐匿掉标签，以免影响视野

    //缩放完成加载数据
    map.addEventListener('zoomend',function (event){
        mapResize(event.target);
    });
    //地图拖拽完成事件moveend
    map.addEventListener('dragend',function (event){
        mapResize(event.target);
    });
}

/**
 * 刻画地区
 * @param map
 * @param regionList
 */
function drawRegion(){
    if(labels.length <= 0) {
        //绘画每个区域具体大小
        var boundary = new BMap.Boundary();
        //覆盖物内容
        var polygonContext = {}

        var regionPoint;
        var textLabel;
        for (let i = 0; i < regionList.length; i++) {

            regionPoint = new BMap.Point(regionList[i].baiduMapLongtiude,regionList[i].baiduMapLatitude);
            var houseCount = 0;
            if (regionList[i].en_name in regionCountMap){
                houseCount = regionCountMap[regionList[i].en_name];
            }
            regionMap[regionList[i].en_name]= regionList[i].cn_name;
            var textContent = '<p style="margin-top: 20px;pointer-events: none">'+
                regionList[i].cn_name+'</p>'+'<p style="pointer-events: none">'+houseCount+'套</p>';
            textLabel=new BMap.Label(textContent,{
                position:regionPoint,//标签位置
                offset:new BMap.Size(-40,20)//文本偏移量
            });
            textLabel.setTitle(regionList[i].en_name);
            textLabel.setStyle({
                height:'78px',
                width:'78px',
                color:'#fff',
                backgroundColor:'#0054a5',
                border:'0px solid rgb(255,0,0)',
                borderRadius:'50%',
                fontWeight:'bold',
                display:'inline',
                lineHeight:'normal',
                textAlign:'center',
                opacity:'0.8',//透明度
                zIndex:2,
                overflow:'hidden'
            });
            map.addOverlay(textLabel);//讲标签画在地图上
            labels.push(textLabel);
            //记录行政区域覆盖物
            polygonContext[textContent] = [];//点集合
            (function (textContent){//闭包传参
                boundary.get(city.cn_name+regionList[i].cn_name,function (rs) {
                    //获取行政区域
                    var count = rs.boundaries.length;//行政区域边界点集合长度
                    if (count === 0){
                        alert('未能获取当前输入行政区域');
                        return ;
                    }
                    for (var j =0;j<count;j++){
                        //建立多边形覆盖物
                        var polygon = new BMap.Polygon(
                            rs.boundaries[j],{
                                strokeWeight:2,
                                strokeColor:'#0054a5',
                                fillOpacity:0.3,
                                fillColor:'#0054a5'
                            }
                        );
                        map.addOverlay(polygon);//添加覆盖物
                        polygonContext[textContent].push(polygon);
                        polygon.hide();
                    }

                })
            })(textContent);

            textLabel.addEventListener('mouseover',function (event){
                label = event.target;
                var boundaries = polygonContext[label.getContent()];
                label.setStyle({backgroundColor:'#1AA591'});
                for (var n = 0;n<boundaries.length;n++ ){
                    boundaries[n].show();
                }
            });
            textLabel.addEventListener('mouseout',function (event){
                label = event.target;
                var boundaries = polygonContext[label.getContent()];
                label.setStyle({backgroundColor:'#0054a5'});
                for (var n = 0;n<boundaries.length;n++ ){
                    boundaries[n].hide();
                }
            });
            textLabel.addEventListener('click',function (event){
                //点击地区进行地图放大
                label = event.target;
                var map = label.getMap();
                map.zoomIn();
                map.panTo(event.point);
                mapResize(map);
            });

        }
    }
    addViewLabel(labels);
    // if (!customLayer){
    //     customLayer = new BMap.CustomLayer({
    //         geotableId:215610,
    //         q:'',//检索关键字
    //         tags:'',//空格分隔的字符串
    //         filter:'',
    //         pointDensityType:BMAP_POINT_DENSITY_HIGH
    //     });
    //     map.addTileLayer(customLayer);//添加自定义图层

    //百度v3已不支持lbs云麻点
    // if (!customLayer) {
    //     alert("开始");
    //     customLayer = new BMap.CustomLayer({
    //         geotableId: 215610,
    //         q: '', // 检索关键字
    //         tags: '', // 空格分隔的字符串
    //         filter: '', // 过滤条件，参考：http://lbsyun.baidu.com/index.php?title=lbscloud/api/geosearch
    //         // pointDensityType: BMAP_POINT_DENSITY_HIGH
    //     });
    //     map.addTileLayer(customLayer); // 添加自定义图层
    // }
}

/**
 * 加载第三级小区数据
 * @param {Object} data
 */
function addLable(data) {

    // map.clearOverlays();
    //  thirdlyMkr = [];
    // 首先判断是不是第一次请求
    if(thirdlyMkr.length <= 0) {
        // alert("加载")
        $.each(data, function(index, data) {
            // alert("数据="+data.key+","+data.count+","+data)
            // alert("坐标="+ data.longitude+","+data.latitude)
            var point = new BMap.Point(data.longitude, data.latitude);
            // 自定义label样式
            var tpl = '<div class=" bubble-1 ZLQbubble" data-longitude="' + data.longitude + '"' +
                ' data-latitude="' + data.latitude + '">' +
                '<span class="name" title="' + data.key + '">' + data.key + '</span>&nbsp&nbsp' +
                '<span class="count"><span>' + data.count + '</span>套</span>' +
                '</div>';
            var myLabel = new BMap.Label(tpl, {
                position: point, // label 在此处添加点位位置信息
                offset: new BMap.Size(-42, -42)
            });
            myLabel.setStyle({
                height: "22px", // 高度
                lineHeight: "22px",
                border: "0", // 边
                borderRadius: "2px",
                background: "#0054a5", // 背景颜色#46ACFF
                opacity: 0.9,
                cursor: "pointer",
                zIndex: 2
            });
            myLabel.setTitle(data.key);
            map.addOverlay(myLabel);
            // 直接缓存起来
            thirdlyMkr.push(myLabel);
            myLabel.addEventListener("mouseover", function() {
                myLabel.setStyle({
                    background: "#1AA591",
                    zIndex: 4
                }); // 修改覆盖物背景颜色
            });
            myLabel.addEventListener("mouseout", function() {
                myLabel.setStyle({
                    background: "#0054a5",
                    zIndex: 2
                }); // 修改覆盖物背景颜色
            });

            myLabel.addEventListener("click", function() {
                // alert(data.key)
                districtparams={
                    district:data.key
                }
                loadOneHouseData();
            });

        })
    }
    // 根据视野动态加载
    addViewLabel(thirdlyMkr)
}
/**
 * 根据地图视野动态加载数据，当数据多时此方法用来提高地图加载性能
 * 本次模拟数据较少，看不出太大效果
 * @param {Object} labels
 */
function addViewLabel(mkr) {
    // map.clearOverlays();
    for(var i = 0; i < mkr.length; i++) {
        var result = isPointInRect(mkr[i].point, map.getBounds());
        if(result == true) {
            mkr[i].show();
            // map.addOverlay(mkr[i])
        } else {
            mkr[i].hide();
            // map.removeOverlay(mkr[i]);
        }
    }
}


// 判断地图视野包含哪些点
function isPointInRect(point, bounds) {
    // 检查类型是否正确
    if(!(point instanceof BMap.Point) ||
        !(bounds instanceof BMap.Bounds)) {
        return false;
    }
    var sw = bounds.getSouthWest(); // 西南脚点
    var ne = bounds.getNorthEast(); // 东北脚点
    return(point.lng >= sw.lng && point.lng <= ne.lng && point.lat >= sw.lat && point.lat <= ne.lat);
}
//缩放和拖拽事件后加载数据
function mapResize(_map){
    var bounds = _map.getBounds(),
        southWest = bounds.getSouthWest(),//西南角
        northEast = bounds.getNorthEast();//东北角
    var zoomLevel = _map.getZoom();
    params = {
        level:zoomLevel,
        leftLongitude:southWest.lng,//左上角
        leftLatitude:northEast.lat,
        rightLongitude:northEast.lng,//右下角
        rightLatitude:southWest.lat
    };

    if (zoomLevel<13){
        if (thirdlyMkr.length>0){
            for (var i=0;i<thirdlyMkr.length;i++){
                thirdlyMkr[i].hide();
            }
        }
        // map.clearOverlays();
        // addViewLabel(labels)
        drawRegion();
        // for (var i=0;i<labels.length;i++){
        //     labels[i].show();
        // }

        // for(var j=0;j<thirdlyMkr.length;j++){
        //     thirdlyMkr[i].hide();
        // }
    }else{


        loadHouseData();

        // for (var i=0;i<labels.length;i++){
        //     labels[i].hide();
        // }
        if (labels.length>0){
            for (var i=0;i<labels.length;i++){

                labels[i].hide();
            }
        }
        // if (thirdlyMkr.length>0) {
        //     alert("小区数据=" + thirdlyMkr[0].getTitle());
        //     alert(thirdlyMkr.length)
        // }
        // for (key in thirdlyData){
        //     alert(key);
        // }

        addLable(thirdlyData)

    }
}
/**
 * 加载全部的房源数据 并且进行渲染
 */
function loadHouseData() {
    orderby="load";
    var target = '&'; // 拼接参数
    $.each(params, function (key, value) {

        target += (key + '=' + value + '&');
    });
    var path='/rent/house/map/houses?cityEnName='
    HouseData(path,target);
}

/**
 * 单个小区所有房源数据加载
 */
function loadOneHouseData(){
    orderby="loadOne";
    var target = '&'; // 拼接参数
    $.each(districtparams, function (key, value) {

        target += (key + '=' + value + '&');
    });
    var path = "/rent/house/map/houses/district?cityEnName="
    HouseData(path,target);
    // alert(target);
}

/**
 * 加载小区数据
 */
function loaddistrict(){
    var target = '&'; // 拼接参数
    $.each(params, function (key, value) {
        target += (key + '=' + value + '&');
    });
    // alert(target);
    //加载小区数据
    $.get('/rent/house/map/district?cityEnName='+cityEnName
        +target,
        function (res) {
            thirdlyData={};
            // for (map in res.data){
            //     alert(map.key);
            //     alert(res.data[map])
            // }
            $.each(res.data, function (index,value){
                // alert(value)
                // alert(value.key)
                //将小区数据装载
                thirdlyData[cityCnName+regionMap[value.region]+value.key]= value;
                //初始化小区坐标

                // for (data in thirdlyData){
                //     alert("小区名="+thirdlyData[data].key+",数量="+thirdlyData[data].count+",全部="+data);
                // }
            });
            searchCoord(thirdlyData);
        });
}
/**
 * 基础房源信息加载
 * @param target
 * @constructor
 */
function HouseData(path,target){
    $('#house-flow').html('');
    layui.use('flow', function () {
        var $ = layui.jquery; //不用额外加载jQuery，flow模块本身是有依赖jQuery的，直接用即可。
        var flow = layui.flow;
        flow.load({
            elem: '#house-flow', //指定列表容器
            scrollElem: '#house-flow',
            done: function (page, next) { //到达临界点（默认滚动触发），触发下一页
                //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
                var lis = [],
                    start = (page - 1) * 3;
                $.get( path+ cityEnName + '&start=' + start + '&size=3' + target,
                    function (res) {
                        if (res.code !== 200) {
                            lis.push('<li>数据加载错误</li>');
                        } else {
                            layui.each(res.data, function (index, house) {
                                var direction;
                                switch (house.direction) {
                                    case 1:
                                        direction = '朝东';
                                        break;
                                    case 2:
                                        direction = '朝南';
                                        break;
                                    case 3:
                                        direction = '朝西';
                                        break;
                                    case 4:
                                        direction = '朝北';
                                        break;
                                    case 5:
                                    default:
                                        direction = '南北';
                                        break;
                                };

                                var tags = '';
                                for (var i = 0; i < house.tags.length; i++) {
                                    tags += '<span class="item-tag-color_2 item-extra">' + house.tags[i] + '</span>';
                                }
                                var li = '<li class="list-item"><a href="/rent/house/show/' + house.id + '" target="_blank"'
                                    + ' title="' + house.title + '"data-community="1111027382235"> <div class="item-aside">'
                                    + '<img src="' + house.cover + '?imageView2/1/w/116/h/116"><div class="item-btm">'
                                    + '<span class="item-img-icon"><i class="i-icon-arrow"></i><i class="i-icon-dot"></i>'
                                    + '</span>&nbsp;&nbsp;</div></div><div class="item-main"><p class="item-tle">'
                                    + house.title + '</p><p class="item-des"> <span>' + house.room + '室' + house.parlour + '厅'
                                    + '</span><span>' + house.area + '平米</span> <span>' + direction + '</span>'
                                    + '<span class="item-side">' + house.price + '<span>元/月</span></span></p>'
                                    + '<p class="item-community"><span class="item-replace-com">' + house.district + '</span>'
                                    + '<span class="item-exact-com">' + house.district + '</span></p><p class="item-tag-wrap">'
                                    + tags + '</p></div></a></li>';

                                lis.push(li);
                            });
                        }
                        //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                        //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                        next(lis.join(''), res.more);

                    });
            }
        });
    });
}
// // 排序切换
$('ol.order-select li').on('click', function () {
    $('ol.order-select li.on').removeClass('on');
    $(this).addClass('on');

    if (orderby=="load"){
        params.orderBy = $(this).attr('data-bind');
        params.orderDirection = $(this).attr('data-direction');
        loadHouseData();
    }else if (orderby=="loadOne"){

        loadOneHouseData();
    }

});


/**
 * 查找坐标
 * @param {Object} data
 */
function searchCoord(data) {
    var localSearch = new BMap.LocalSearch(map);
    for(key in data) {
        localSearch.search(key);
        localSearch.setSearchCompleteCallback((searchResult) => {
            var k = searchResult.keyword;
            var poi = searchResult.getPoi(0);
            data[k].longitude = poi.point.lng;
            data[k].latitude = poi.point.lat;
            // alert("坐标="+data[k].longitude+","+data[k].latitude);
        });
        // alert(key)
    }
}
/**
 * 绑定按钮事件
 */
function drawing() {
    // 开始画圈绑定事件
    drawBtn.addEventListener('click', function(e) {
        var zoomLevel = map.getZoom();
        if(zoomLevel < 13) {
            alert("请放大到二级数据进行画图找房");
            return;
        }
        // 禁止地图移动点击等操作
        // map.clearOverlays()
        if (thirdlyMkr.length>0){
            for (var i=0;i<thirdlyMkr.length;i++){
                thirdlyMkr[i].hide();
            }
        }
        map.disableDragging();
        map.disableScrollWheelZoom();
        map.disableDoubleClickZoom();
        map.disableKeyboard();
        // 设置鼠标样式
        map.setDefaultCursor('crosshair');
        // 设置标志位进入画圈状态
        isInDrawing = true;
    });

    // 退出画圈按钮绑定事件
    exitBtn.addEventListener('click', function(e) {
        // 清空地图上画的折线和圈
        map.clearOverlays()
        thirdlyMkr=[];
        labels=[];
        // 恢复地图移动点击等操作
        map.enableDragging();
        map.enableScrollWheelZoom();
        map.enableDoubleClickZoom();
        map.enableKeyboard();
        map.setDefaultCursor('default');
        addLable(thirdlyData);
        // 设置标志位退出画圈状态
        isInDrawing = false;
    })

    // 为地图绑定鼠标按下事件(开始画圈)
    map.addEventListener('mousedown', function(e) {
        // 如果处于画圈状态下,清空上次画圈的数据结构,设置isMouseDown进入画圈鼠标按下状态
        if(isInDrawing) {
            // 清空地图上画的折线和圈
            map.removeOverlay(polygonAfterDraw);
            map.removeOverlay(lastPolyLine);
            polyPointArray = [];
            lastPolyLine = null;
            isMouseDown = true;
        }
    });
    // 为地图绑定鼠标抬起事件(画圈完成)
    map.addEventListener('mouseup', function(e) {
        // 如果处于画圈状态下 且 鼠标是按下状态
        if(isInDrawing && isMouseDown) {
            // 退出画线状态
            isMouseDown = false;
            // 添加多边形覆盖物,设置为禁止点击
            var polygon = new window.BMap.Polygon(polyPointArray, {
                strokeColor: '#46ACFF',
                strokeOpacity: 1,
                fillColor: '#46ACFF',
                fillOpacity: 0.3,
                enableClicking: false
            });
            map.addOverlay(polygon);
            //包含情况
            show(polygon);
        }
    });
    // 为地图绑定鼠标移动事件(触发画图)
    map.addEventListener('mousemove', function(e) {
        // 如果处于鼠标按下状态,才能进行画操作
        if(isMouseDown) {
            // 将鼠标移动过程中采集到的路径点加入数组保存
            polyPointArray.push(e.point);
            // 除去上次的画线
            if(lastPolyLine) {
                map.removeOverlay(lastPolyLine)
            }
            // 根据已有的路径数组构建画出的折线
            var polylineOverlay = new window.BMap.Polyline(polyPointArray, {
                strokeColor: '#46ACFF',
                strokeOpacity: 1,
                enableClicking: false
            });
            // 添加新的画线到地图上
            map.addOverlay(polylineOverlay);
            // 更新上次画线条
            lastPolyLine = polylineOverlay
        }
    })
}


/**
 * 根据画的圈，显示相应的marker
 * @param {Object} polygon
 */
function show(polygon) {
    // 得到多边形的点数组
    var pointArray = polygon.getPath();
    // 获取多边形的外包矩形
    var bound = polygon.getBounds();
    // 在多边形内的点的数组
    var pointInPolygonArray = [];
    // 计算每个点是否包含在该多边形内
    for(var i = 0; i < thirdlyMkr.length; i++) {
        // 该marker的坐标点
        var markerPoint = thirdlyMkr[i].getPosition();
        if(isPointInPolygon(markerPoint, bound, pointArray)) {
            // map.addOverlay(thirdlyMkr[i])
            thirdlyMkr[i].show();
        }
    }
}
// 判定一个点是否包含在多边形内
function isPointInPolygon(point, bound, pointArray) {
    // 首先判断该点是否在外包矩形内，如果不在直接返回false
    if(!bound.containsPoint(point)) {
        return false;
    }
    // 如果在外包矩形内则进一步判断
    // 该点往右侧发出的射线和矩形边交点的数量,若为奇数则在多边形内，否则在外
    var crossPointNum = 0;
    for(var i = 0; i < pointArray.length; i++) {
        // 获取2个相邻的点
        var p1 = pointArray[i];
        var p2 = pointArray[(i + 1) % pointArray.length];
        // 如果点相等直接返回true
        if((p1.lng === point.lng && p1.lat === point.lat) || (p2.lng === point.lng && p2.lat === point.lat)) {
            return true
        }
        // 如果point在2个点所在直线的下方则continue
        if(point.lat < Math.min(p1.lat, p2.lat)) {
            continue;
        }
        // 如果point在2个点所在直线的上方则continue
        if(point.lat >= Math.max(p1.lat, p2.lat)) {
            continue;
        }
        // 有相交情况:2个点一上一下,计算交点
        // 特殊情况2个点的横坐标相同
        var crossPointLng;
        if(p1.lng === p2.lng) {
            crossPointLng = p1.lng;
        } else {
            // 计算2个点的斜率
            var k = (p2.lat - p1.lat) / (p2.lng - p1.lng);
            // 得出水平射线与这2个点形成的直线的交点的横坐标
            crossPointLng = (point.lat - p1.lat) / k + p1.lng;
        }
        // 如果crossPointLng的值大于point的横坐标则算交点(因为是右侧相交)
        if(crossPointLng > point.lng) {
            crossPointNum++;
        }

    }
    // 如果是奇数个交点则点在多边形内
    return crossPointNum % 2 === 1
}