$("#uid_check").click(function checkId() {
    var uid = $('#uid').val(); //id값이 "id"인 입력란의 값을 저장
    var check = 0

    if (uid.length < 5 || uid.length > 50) {
        check = 1
        alert("아이디는 5자 이상 50자 미만으로 입력해주세요")
    } else {
        check = 0
    }

    $.ajax({
        url: '/user/validation', //Controller에서 요청 받을 주소
        type: 'get', //POST 방식으로 전달
        data: {"uid": uid},
        success: function (result) {
            if (result || check === 1) {
                $('#uid_check_message').css({
                    "color": "red",
                    "visibility": "visible"
                }).text("사용 불가능한 아이디입니다.");
            } else {
                $('#uid').attr("check_result", "success")
                $('#uid_check_message').css({
                    "color": "blue",
                    "visibility": "visible"
                }).text("사용 가능한 아이디입니다.");
            }
        },
        error: function () {
            alert("에러입니다");
        }
    })
});

$('#uid').change(function () {   // 아이디 입력값이 바뀌면 메세지가 사라진다.
    $('#uid').attr("check_result", "fail");
    $('#uid_check_message').css({
        "visibility": "hidden"
    });
})

function submitCheck() {
    var password = $("#password").val();
    var passwordCheck = $("#passwordCheck").val();

    if (password !== passwordCheck) {
        alert("비밀번호를 확인해주세요.")
        return false;
    }

    if ($('#uid').attr("check_result") === "fail") {
        alert("아이디 중복체크를 해주시기 바랍니다.");
        $('#uid').focus();
        return false;
    }
    return true
}
