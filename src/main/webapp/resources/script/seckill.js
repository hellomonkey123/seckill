
var seckill={

	URL: {
		now: function () {
			return '/seckill/time/now';
        },
		exposer: function (seckillId) {
			return '/seckill/'+ seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
			return  '/seckill/'+seckillId +'/' + md5 + '/execution';
        }
	},
    handleSeckillStart: function(seckillId, node){
		console.log('准备进入开始秒杀');//TODO
		node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		$.post(seckill.URL.exposer(seckillId),{},function (result) {
			//在回调函数执行交互流程
            console.log('jinru开始秒杀');//TODO
			if (result && result['success']){
				var exposer = result['data'];
				if (exposer['exposed']) {

                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl: ' + killUrl);
                    //只进行一次绑定，防止多次点击提交
                    $('#killBtn').one('click',function () {
						$(this).addClass('disable');//未点击前默认为无效
						$.post(killUrl,{},function (result) { //点击提交到数据库
							//获取提交后的状态信息
							if (result && result['success']){
								var killResult = result['data'];
								//var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];

								node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }

                        });
                    });
                    node.show();
				}else {
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
					//重新计时
                    seckill.countDown(seckillId,now,start,end);
				}
			} else{
				console.log('result' + result)
			}
        });
		
	},
	validatePhone: function(phone){
		return phone && phone.length === 11 && !isNaN(phone);
	},
    countDown: function(seckillId, nowTime, startTime, endTime){
        console.log('准备进入倒计时程序！！！ '); //TODO

		var seckillBox = $('#seckill-box');
        console.log('seckillBox！！！ ' + seckillBox); //TODO
		if (nowTime > endTime ) {
			seckillBox.html("秒杀已结束");
		}else if (nowTime < startTime){
            console.log('判断秒杀中！！！ '); //TODO
			var killTime = new Date(Number(startTime) + 1000);
			seckillBox.countdown(killTime, function (event) {
                console.log('秒杀倒计时！！！ ' + killTime); //TODO
				var format = event.strftime('秒杀倒计时：%D天  %H时  %M分 %S秒 ');
				seckillBox.html(format);
                console.log('返回时间后！！！ ' + format); //TODO
            }).on('finish.countdown', function () {

                seckill.handleSeckillStart(seckillId, seckillBox);
            });
		} else{
			//
			seckill.handleSeckillStart(seckillId, seckillBox);
		}
	},
	//详情页秒杀逻辑
	detail: {
		init: function(params){
			//手机登陆和验证，计时交互
			//规划交互流程
			//在cookie中查找手机号
			var killPhone = $.cookie('killPhone');
			//验证手机号
			if(!seckill.validatePhone(killPhone)){
				//绑定手机号
				var killPhoneModal =  $('#killPhoneModal');
				//显示弹出层
				killPhoneModal.modal({
					show:true, //显示弹出层
					backdrop:'static', //禁止位置关闭
					keyboard:false //关闭键盘事件
				});

				$('#killPhoneBth').click(function(){
					var inputPhone = $('#killPhoneKey').val();
					//console.log('inputPhone= ' +inputPhone); //TODO
					if(seckill.validatePhone(inputPhone)){
						//电话写入cookie
						$.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
						//刷新页面
						window.location.reload();
					}else{
						$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
					}
				});
			}
			//已登录
			//计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
			$.get(seckill.URL.now(), {}, function (result) {
                console.log('判断准备进入countDown调用 ');//TODO
				if (result && result['success']){
					var nowTime = result['data'];
                    console.log('准备进入countDown调用 ');//TODO

					seckill.countDown(seckillId,nowTime,startTime,endTime);
				} else{
					console.log('result: ' + result);
				}
            });
		}
	}
}