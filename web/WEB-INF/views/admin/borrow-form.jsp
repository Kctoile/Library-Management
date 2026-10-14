<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<div class="form-narrow">
    <h2 class="h5 mb-3">Tạo giao dịch mượn sách</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/borrows" novalidate>
        <div class="mb-3">
            <label for="userId" class="form-label">Mã sinh viên (UserID) <span class="text-danger">*</span></label>
            <input id="userId" name="userId" type="number" min="1" class="form-control"
                   placeholder="Nhập UserID của sinh viên" required>
        </div>
        <div class="mb-3">
            <label for="bookId" class="form-label">Sách <span class="text-danger">*</span></label>
            <select id="bookId" name="bookId" class="form-select" required>
                <option value="">-- Chọn sách --</option>
                <c:forEach var="b" items="${books}">
                    <option value="${b.bookId}" ${b.quantity == 0 ? 'disabled' : ''}>
                        ${b.title} — còn ${b.quantity} quyển
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label for="dueDate" class="form-label">Ngày đến hạn <span class="text-danger">*</span></label>
            <input id="dueDate" name="dueDate" type="date" class="form-control" required>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Tạo giao dịch</button>
            <a href="${pageContext.request.contextPath}/admin/borrows" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
