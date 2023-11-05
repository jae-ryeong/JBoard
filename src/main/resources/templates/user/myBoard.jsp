<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <title>내 게시글</title>
</head>
<body>
<header th:replace="~{header::header}">
    <hr>
    헤더 삽입부
</header>

<div class="container">
    <form class="d-flex" role="search" th:action="'/'">
        <div class="col-lg-3 col-md-3 col-sm-12 p-0">
            <label id="search-value" for="search-type" th:text="${searchType}" hidden></label>
            <select class="form-control" id="search-type" name="searchType" style="text-align-last: center; text-align: center;">
                <option id="search-all" value="all">전체</option>
                <option id="search-title" value="title">제목</option>
                <option id="search-content" value="content">본문</option>
                <option id="search-nickname" value="nickname">닉네임</option>
            </select>
        </div>

        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" th:value="${keyword}" th:name="keyword">
        <button class="btn btn-outline-success" type="submit">Search</button>
    </form>

    <div>
        <h1>게시글 리스트</h1>
    </div>

    <div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>게시글 번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>조회수</th>
            </tr>
            </thead>

            <tbody>
            <tr th:each="Article : ${Articles}">
                <td th:text="${Article.getArticleId()}">게시글 번호</td>
                <td><a th:href="@{/detail/{articleId}(articleId=${Article.getArticleId()})}" th:text="${Article.getTitle()}">제목</a></td>
                <td th:text="${Article.getUserAccountDto().nickname()}">작성자</td>
                <td th:text="${Article.getViewCount()}">조회수</td>
            </tr>
            </tbody>

        </table>
    </div>

    <hr>

    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <a class="btn btn-primary pull-right" th:href="@{boardCreateForm}" role="button">글쓰기</a>
    </div>

    <nav id="pagination" aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <li class="page-item"><a th:text="'previous'"
                                     th:href="@{/(page=${number - 1}, searchType=${searchType}, keyword=${keyword})}"
                                     th:class="'page-link' + (${number} <= 0 ? ' disabled' : '')">Previous</a></li>
            <li class="page-item" th:each="pageNumber : ${paginationBarNumbers}">
                <a th:text="${pageNumber+1}"
                   th:href="@{/(page=${pageNumber}, searchType=${searchType}, keyword=${keyword})}"
                   th:class="'page-link' + (${number} == ${pageNumber} ? ' disabled' : '')">1</a></li>
            <li class="page-item"><a th:text="'next'"
                                     th:href="@{/(page=${number + 1}, searchType=${searchType}, keyword=${keyword})}"
                                     th:class="'page-link' + (${number} >= ${totalPages}-1 ? ' disabled' : '')">Next</a></li>
        </ul>
    </nav>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
</html>