<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="common/tag.jsp"%>

<!DOCTYPE html>
<html>
   <head>
      <title>秒杀列表页面</title>
      <%@include file="common/head.jsp"%>
   </head>
   <body>
        <div class="container">
            <div class="panel panel-default">
                <div class="panel-heading text-center">
                    <h2>秒杀列表</h2>
                </div>
                <div class="panel-body">
                    <table class="table table-hover">
                        <thread>
                            <tr>
                                <th>名称</th>
                                <th>库名</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>创建时间</th>
                                <th>详情页</th>
                            </tr>
                        </thread>
                        <tbody>
                            <c:forEach var="sk" items="${list}">
                                <tr>
                                    <td>${sk.name}</td>
                                    <td>${sk.number}</td>
                                    <td>
                                        <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td>
                                        <a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">link</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>


   </body>
   <!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
   <script src="https://code.jquery.com/jquery.js"></script>
   <!-- 包括所有已编译的插件 -->
   <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>