<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="h5 mb-0">Danh sách sách</h2>
    <a href="${pageContext.request.contextPath}/admin/books?action=edit&id=0"
       class="btn btn-success btn-sm">+ Thêm sách</a>
</div>

<%-- Flash message --%>
<c:if test="${not empty flash_msg}">
    <div class="alert alert-${flash_type == 'success' ? 'success' : 'danger'} alert-dismissible fade show py-2">
        ${flash_msg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<%-- Search & filter form --%>
<form method="get" action="${pageContext.request.contextPath}/admin/books" class="row g-2 mb-3">
    <div class="col-sm-4">
        <input id="keyword" name="keyword" type="search" class="form-control form-control-sm"
               placeholder="Tên, tác giả, thể loại..." value="${not empty keyword ? keyword : ''}">
    </div>
    <div class="col-sm-3">
        <select id="category" name="category" class="form-select form-select-sm">
            <option value="">-- Tất cả thể loại --</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}" ${cat == category ? 'selected' : ''}>${cat}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-sm-3 d-flex align-items-center">
        <div class="form-check">
            <input id="availableOnly" name="availableOnly" type="checkbox" class="form-check-input"
                   value="1" ${availableOnly ? 'checked' : ''}>
            <label for="availableOnly" class="form-check-label small">Còn sách</label>
        </div>
    </div>
    <div class="col-sm-2">
        <button type="submit" class="btn btn-primary btn-sm w-100">Tìm</button>
    </div>
</form>

<%-- Book table --%>
<div class="table-responsive">
<table class="table table-bordered table-hover table-sm align-middle">
    <thead class="table-dark">
        <tr>
            <th>ID</th><th>Tên sách</th><th>Tác giả</th><th>Thể loại</th>
            <th>Số lượng</th><th>Năm XB</th><th>Thao tác</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty books}">
                <tr><td colspan="7" class="text-center text-muted">Không tìm thấy sách nào.</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="b" items="${books}">
                    <tr>
                        <td>${b.bookId}</td>
                        <td>${b.title}</td>
                        <td>${b.author}</td>
                        <td>${b.category}</td>
                        <td>
                            <c:choose>
                                <c:when test="${b.quantity == 0}"><span class="badge bg-danger">Hết sách</span></c:when>
                                <c:otherwise>${b.quantity}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${b.publishedYear}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/books?action=edit&id=${b.bookId}"
                               class="btn btn-warning btn-sm">Sửa</a>
                            <a href="${pageContext.request.contextPath}/admin/books?action=delete&id=${b.bookId}"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('Bạn có chắc muốn xóa sách này?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
