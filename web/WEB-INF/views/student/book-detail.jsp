<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${book.title}"/>
</jsp:include>

<a href="${pageContext.request.contextPath}/student/books" class="btn btn-link ps-0 mb-3">← Quay lại danh sách</a>

<div class="card form-narrow">
    <div class="card-body">
        <h2 class="h5 card-title">${book.title}</h2>
        <table class="table table-sm mt-3">
            <tr><th>Tác giả</th>     <td>${book.author}</td></tr>
            <tr><th>Thể loại</th>    <td>${book.category}</td></tr>
            <tr><th>Năm XB</th>      <td>${book.publishedYear}</td></tr>
            <tr><th>Còn lại</th>
                <td>
                    <c:choose>
                        <c:when test="${book.quantity > 0}">
                            <span class="badge bg-success">${book.quantity} quyển có thể mượn</span>
                        </c:when>
                        <c:otherwise><span class="badge bg-secondary">Hết sách</span></c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
