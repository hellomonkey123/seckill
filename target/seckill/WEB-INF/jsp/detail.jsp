<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="common/tag.jsp"%>

<!DOCTYPE html>
<html>
   <head>
      <title>秒杀详情页面</title>
      <%@include file="common/head.jsp"%>
   </head>
    <body>

	<div class="container">
		<div class="panel panel-default text-center">
			<div class="panel-heading"><h1>${seckill.name}</h1></div>
		</div>
		<div class="panel-body text-center">
			<h2 class="text-danger">
				<!-- 显示time图标 -->
				<span class="glyphicon glyphicon-time "></span>
				<!-- 展示倒计时 -->
				<span class="glyphicon" id="seckill-box"></span>
			</h2>
		</div>
	</div>
	<!-- 登陆弹出层，输入电话 -->
	<div id="killPhoneModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h3 class="modal-title text-center">
						<span class="glyphicon glyphicon-phone"></span>
					</h3>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-8 clo-xs-offset-2">
							<input type="text" name="killPhone" id="killPhoneKey"
								placeholder="填写手机号！！！" class="form-control"/>
						</div>
					</div>
				</div>
				
				<div class="modal-footer">
					<!-- 验证信息 -->
					<span id="killPhoneMessage" class="glyphicon"></span>
					<button type="button" id="killPhoneBth" class="btn btn-success">
						<span class="glyphicon glyphicon-phone"></span>
						submit
					</button>
				</div>
			</div>
		</div>
	</div>
    </body>
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <!-- 包括所有已编译的插件 -->
   <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- 倒计时插件 -->
   <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
    <!-- cookie插件 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>

    <script type="text/javascript" src="/resources/script/seckill.js"></script>

    <script type="text/javascript">

        $(function(){
            //使用EL表达式传入参数
			/*var seckill;*/
            seckill.detail.init({
                seckillId : ${seckill.seckillId},
                startTime : ${seckill.startTime.time},//毫秒
                endTime : ${seckill.endTime.time}
            })
        })
    </script>

 </html>