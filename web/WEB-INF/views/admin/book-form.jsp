<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<div class="form-narrow">
    <h2 class="h5 mb-3">${book != null && book.bookId != 0 ? 'Sửa sách' : 'Thêm sách mới'}</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/books" novalidate>
        <input type="hidden" name="action" value="${book != null && book.bookId != 0 ? 'update' : 'add'}">
        <c:if test="${book != null && book.bookId != 0}">
            <input type="hidden" name="id" value="${book.bookId}">
        </c:if>

        <div class="mb-3">
            <label for="title" class="form-label">Tên sách <span class="text-danger">*</span></label>
            <input id="title" name="title" type="text" class="form-control"
                   value="${not empty inputTitle ? inputTitle : (book != null ? book.title : '')}" required>
        </div>
        <div class="mb-3">
            <label for="author" class="form-label">Tác giả <span class="text-danger">*</span></label>
            <input id="author" name="author" type="text" class="form-control"
                   value="${not empty inputAuthor ? inputAuthor : (book != null ? book.author : '')}" required>
        </div>
        <div class="mb-3">
            <label for="category" class="form-label">Thể loại <span class="text-danger">*</span></label>
            <input id="category" name="category" type="text" class="form-control"
                   value="${not empty inputCategory ? inputCategory : (book != null ? book.category : '')}" required>
        </div>
        <div class="row g-2 mb-3">
            <div class="col-6">
                <label for="quantity" class="form-label">Số lượng <span class="text-danger">*</span></label>
                <input id="quantity" name="quantity" type="number" min="0" class="form-control"
                       value="${not empty inputQty ? inputQty : (book != null ? book.quantity : '0')}" required>
            </div>
            <div class="col-6">
                <label for="publishedYear" class="form-label">Năm xuất bản <span class="text-danger">*</span></label>
                <input id="publishedYear" name="publishedYear" type="number" min="1000" class="form-control"
                       value="${not empty inputYear ? inputYear : (book != null ? book.publishedYear : '')}" required>
            </div>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
