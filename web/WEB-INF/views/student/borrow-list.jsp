<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<h2 class="h5 mb-3">Lịch sử mượn sách của tôi</h2>

<div class="table-responsive">
<table class="table table-bordered table-hover table-sm align-middle">
    <thead class="table-dark">
        <tr>
            <th>ID</th><th>Sách</th><th>Ngày mượn</th><th>Hạn trả</th><th>Ngày trả</th><th>Trạng thái</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty records}">
                <tr><td colspan="6" class="text-center text-muted">Bạn chưa có giao dịch mượn nào.</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="r" items="${records}">
                    <tr>
                        <td>${r.recordId}</td>
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
