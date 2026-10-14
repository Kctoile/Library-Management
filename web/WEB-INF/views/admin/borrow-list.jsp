<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="h5 mb-0">Giao dịch mượn sách</h2>
    <a href="${pageContext.request.contextPath}/admin/borrows?action=add"
       class="btn btn-success btn-sm">+ Tạo giao dịch</a>
</div>

<c:if test="${not empty flash_msg}">
    <div class="alert alert-${flash_type == 'success' ? 'success' : 'danger'} alert-dismissible fade show py-2">
        ${flash_msg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="table-responsive">
<table class="table table-bordered table-hover table-sm align-middle">
    <thead class="table-dark">
        <tr>
            <th>ID</th><th>Sinh viên</th><th>Sách</th>
            <th>Ngày mượn</th><th>Hạn trả</th><th>Ngày trả</th><th>Trạng thái</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty records}">
                <tr><td colspan="7" class="text-center text-muted">Chưa có giao dịch nào.</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="r" items="${records}">
                    <tr>
                        <td>${r.recordId}</td>
                        <td>${r.userFullName}</td>
                        <td>${r.bookTitle}</td>
                        <td>${r.borrowDate}</td>
                        <td>${r.dueDate}</td>
                        <td>${not empty r.returnDate ? r.returnDate : '—'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.displayStatus == 'OVERDUE'}">
                                    <span class="badge badge-overdue">QUÁ HẠN</span>
                                </c:when>
                                <c:when test="${r.displayStatus == 'RETURNED'}">
                                    <span class="badge badge-returned">ĐÃ TRẢ</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-borrowing">ĐANG MƯỢN</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
