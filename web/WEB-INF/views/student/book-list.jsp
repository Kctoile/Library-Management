<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}"/>
</jsp:include>

<h2 class="h5 mb-3">Tìm kiếm sách</h2>

<%-- Search & filter --%>
<form method="get" action="${pageContext.request.contextPath}/student/books" class="row g-2 mb-3">
    <div class="col-sm-5">
        <input id="keyword" name="keyword" type="search" class="form-control form-control-sm"
               placeholder="Tìm theo tên, tác giả..." value="${not empty keyword ? keyword : ''}">
    </div>
    <div class="col-sm-4">
        <select id="category" name="category" class="form-select form-select-sm">
            <option value="">-- Tất cả thể loại --</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat}" ${cat == category ? 'selected' : ''}>${cat}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-sm-3">
        <button type="submit" class="btn btn-primary btn-sm w-100">Tìm kiếm</button>
    </div>
</form>

<%-- Book cards --%>
<div class="row g-3">
    <c:choose>
        <c:when test="${empty books}">
            <div class="col-12 text-muted">Không tìm thấy sách nào.</div>
        </c:when>
        <c:otherwise>
            <c:forEach var="b" items="${books}">
                <div class="col-md-4 col-sm-6">
                    <div class="card h-100 book-card">
                        <div class="card-body">
                            <h5 class="card-title fs-6">${b.title}</h5>
                            <p class="card-text small text-muted mb-1">👤 ${b.author}</p>
                            <p class="card-text small text-muted mb-2">📂 ${b.category}</p>
                            <span class="badge ${b.quantity > 0 ? 'bg-success' : 'bg-secondary'}">
                                ${b.quantity > 0 ? 'Còn '.concat(b.quantity).concat(' quyển') : 'Hết sách'}
                            </span>
                        </div>
                        <div class="card-footer bg-transparent">
                            <a href="${pageContext.request.contextPath}/student/books?action=detail&id=${b.bookId}"
                               class="btn btn-outline-primary btn-sm w-100">Xem chi tiết</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
