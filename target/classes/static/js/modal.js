
    // 공통 열기/닫기
    function openModal(id) { document.getElementById(id).style.display = 'flex'; }
    function closeModal(id) {
    const modal = document.getElementById(id);
    modal.style.display = 'none';

    // 입력 필드 초기화
    const inputs = modal.querySelectorAll('input');
    inputs.forEach(input => {
    if (input.type !== 'hidden' && !input.readOnly) {
    input.value = '';
}
});

    // 에러 메시지 숨기기
    const errors = modal.querySelectorAll('.error-msg');
    errors.forEach(error => error.style.display = 'none');

    // 타이머 초기화
    const timer = modal.querySelector('#timer');
    if (timer) {
    timer.textContent = '';
}

    // 인증 버튼 다시 활성화
    const verifyBtn = modal.querySelector('#verifyBtn');
    if (verifyBtn) {
    verifyBtn.disabled = false;
}
}

    function openEmailModalFromPwInput(){
    closeModal('pwInputModal');
    openModal('emailModal');
}

    function openPwInputModal()    { openModal('pwInputModal'); }
    function openEmailModal()      { openModal('emailModal'); }
    function openResetModal()      { openModal('resetModal'); }

    // 1) 현재 비밀번호 검증
    function validatePassword() {
    const password = document.getElementById('currentPassword').value;
    const errorDiv = document.getElementById("pwInputError");
    errorDiv.style.display = 'none';  // 에러 초기화

    axios.post('/account/verify-password',
{ password: password }, // 바디 내용은 JSON 객체
{
    headers: {
    'Content-Type': 'application/json'
}
}
    )
    .then(res => {
    window.location.href = "/account/edit";
})
    .catch(err => {
    errorDiv.style.display = 'block';
    console.error("비밀번호 검증 실패 : ", err);
});
}

    // 이메일 발송
    function sendVerificationEmail() {
    const email = document.getElementById('email').value.trim();

    if (!email) {
    alert("이메일을 불러오지 못했습니다.");
    return;
}


    axios.post('/api/send-code', { email })
    .then(() => {
    alert('인증 코드가 발송되었습니다.');
    // 이메일 모달 요소에 이메일을 저장
    document.getElementById('emailModal').setAttribute('data-email', email);
})
    .catch(() => {
    alert('이메일 발송 실패');
});
}

    // 검증
    function verifyEmailCode() {
    const code = document.getElementById('emailCode').value.trim();
    const email = document.getElementById('emailModal').getAttribute('data-email');
    const errorDiv = document.getElementById('emailError');

    errorDiv.style.display = 'none';

    if (!code) {
    errorDiv.textContent = "인증 코드를 입력해주세요.";
    errorDiv.style.display = 'block';
    return;
}

    axios.post('/api/verify-code', { email, code })
    .then(res => {
    if (res.data.verified) {
    closeModal('emailModal');
    openResetModal();
    alert("인증이 완료되었습니다.");
} else {
    throw new Error("코드 불일치");
}
})
    .catch(() => {
    errorDiv.textContent = "인증에 실패했습니다.";
    errorDiv.style.display = 'block';
});
}

    // 이메일 페이지가 로드되었을 때 실행
    window.addEventListener('DOMContentLoaded', function () {
    axios.get('/api/user/email')  // ← 이메일을 반환하는 API 엔드포인트
        .then(response => {
            const email = response.data.email;
            document.getElementById('email').value = email;
        })
        .catch(error => {
            console.error('이메일 불러오기 실패:', error);
        });
});


    // 3) 비밀번호 변경
    function changePassword() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmNewPassword').value;
    const errorDiv = document.getElementById('resetError');

    if (newPassword !== confirmPassword) {
    errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
    return errorDiv.style.display = 'block';
}

    axios.post('/account/reset-password',
    JSON.stringify({ newPassword: newPassword, confirmPassword: confirmPassword }),
{
    headers: {
    'Content-Type': 'application/json'
}
}
    )
    .then(_ => {
    alert('비밀번호가 변경되었습니다.');
    closeModal('resetModal');
})
    .catch(err => {
    console.error("비밀번호 변경 오류:", err);
    errorDiv.textContent = '변경 중 오류가 발생했습니다.';
    errorDiv.style.display = 'block';
});
}


    let countdownInterval;
    window.onload = function () {
    document.getElementById('sendCodeBtn').addEventListener('click', function () {
        // 5분(300초) 설정
        let timeLeft = 300;

        // 버튼 누르면 타이머 리셋
        clearInterval(countdownInterval);

        const verifyBtn = document.getElementById('verifyBtn');
        verifyBtn.disabled = false; // 시작할 때는 활성화

        countdownInterval = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;

            document.getElementById('timer').textContent =
                `남은 시간: ${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

            timeLeft--;

            // 시간이 다 되면
            if (timeLeft < 0) {
                clearInterval(countdownInterval);
                document.getElementById('timer').textContent = '인증 시간이 만료되었습니다.';
                verifyBtn.disabled = true;
            }
        }, 1000);
    });
};

    window.openModal = openModal;
    window.closeModal = closeModal;
    window.openPwInputModal = openPwInputModal;
    window.openEmailModal = openEmailModal;
    window.openResetModal = openResetModal;
    window.validatePassword = validatePassword;
    window.sendVerificationEmail = sendVerificationEmail;
    window.verifyEmailCode = verifyEmailCode;
    window.changePassword = changePassword;