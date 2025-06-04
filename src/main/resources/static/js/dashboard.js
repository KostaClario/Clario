// const memberId = document.getElementById("memberId").value;
const today2 = new Date();
const yyyy = today2.getFullYear();
const mm = String(today2.getMonth() + 1).padStart(2, '0');
const dd = String(today2.getDate()).padStart(2, '0');

// 브라우저 시간 기준으로 할당
const userData = {
    // memberId: parseInt(memberId),
    memberId: 21,
    yearDate: yyyy.toString(),
    monthDate: mm,
    todayDate: `${yyyy}-${mm}-${dd}` // "YYYY-MM-DD" 형태의 문자열
};

function updateDate() {
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const dateObj = new Date(userData.todayDate);
    const dayName = days[dateObj.getDay()];
    const formatted = `${userData.yearDate}/${userData.monthDate}/${dd} ${dayName}`;
    document.getElementById("today-date").textContent = formatted;
}
function updateTopCategoryMonth() {
    const month = today2.getMonth() + 1;
    document.getElementById("topCategoryMonth").textContent = `${month}월`;
}
function updateYearTitle() {
    document.getElementById("currentYear").textContent = yyyy;
}
function formatCurrency(num) {
    if (typeof num !== 'number' || isNaN(num)) {
        console.warn('formatCurrency에 잘못된 값:', num);
        return '0원';
    }
    return num.toLocaleString('ko-KR') + '원';
}

function fetchTodayIncome() {
    const requestData = {
        memberId: userData.memberId,
        todayDate: userData.todayDate
    };
    return axios.post('/api/dashboard/today-income', requestData)
        .then(res => res.data)
        .catch(err => {
            console.error('오늘 수입 데이터 로딩 실패:', err);
            return [];
        });
}

function fetchTodayExpense() {
    const requestData = {
        memberId: userData.memberId,
        todayDate: userData.todayDate
    };
    return axios.post('/api/dashboard/today-expense', requestData)
        .then(res => res.data)
        .catch(err => {
            console.error('오늘 지출 데이터 로딩 실패:', err);
            return [];
        });
}

function renderTodayRecords(incomeList, expenseList) {
    const incomeColumn = document.getElementById('income-column');
    const expenseColumn = document.getElementById('expense-column');

    incomeColumn.innerHTML = '';
    expenseColumn.innerHTML = '';

    incomeList.forEach(item => {
        const category = item.accountSource || '분류없음';
        const amount = Number(item.accountTradeMoney) || 0;

        const span = document.createElement('span');
        span.className = 'plus blue';
        span.textContent = `${category} ${formatCurrency(amount)}`;
        incomeColumn.appendChild(span);
    });

    expenseList.forEach(item => {
        const category = item.cardStoreName || '분류없음';
        const amount = Number(item.cardTradeMoney) || 0;

        const span = document.createElement('span');
        span.className = 'minus red';
        span.textContent = `${category} ${formatCurrency(amount)}`;
        expenseColumn.appendChild(span);
    });
}

function displayMonthlyIncomeAndExpense() {
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate,
        monthDate: userData.monthDate
    };

    return axios.all([
        axios.post('/api/dashboard/monthly-income', requestData),
        axios.post('/api/dashboard/monthly-expense', requestData)
    ])
        .then(axios.spread((incomeRes, expenseRes) => {
            const income = incomeRes.data;
            const expense = expenseRes.data;

            document.getElementById('montylyIncome').innerText = formatCurrency(income);
            document.getElementById('montylyExpense').innerText = formatCurrency(expense);

            return { income, expense };
        }))
        .catch(error => {
            console.error("수입/소비 정보 로딩 실패:", error);
        });
}

function drawIncomeSpendingChart(income, expense) {
    let remaining = income - expense;
    let isOver = false;

    if (remaining < 0) {
        isOver = true;
        remaining = Math.abs(remaining);
    }

    let labels, data, backgroundColor;

    if (isOver) {
        labels = ['소비', '초과 금액'];
        data = [income, remaining];
        backgroundColor = ['#FF6B6B', '#D32F2F'];
    } else {
        labels = ['소비', '남은 금액'];
        data = [expense, remaining];
        backgroundColor = ['#6750A4', '#EBE1F5'];
    }

    const centerTextPlugin = {
        id: 'centerTextPlugin',
        beforeDraw(chart) {
            const { width, height, ctx } = chart;
            ctx.restore();
            const fontSize = (height / 150).toFixed(2);
            ctx.font = `${fontSize}em sans-serif`;
            ctx.textBaseline = "middle";

            const incomeSpending = ((expense / income) * 100) || 0;
            const text = `${incomeSpending.toFixed(1)}%`;
            const textX = Math.round((width - ctx.measureText(text).width) / 2);
            const textY = height / 2 + 15;

            ctx.fillStyle = isOver ? '#D32F2F' : '#333';
            ctx.fillText(text, textX, textY);
            ctx.save();
        }
    };

    const ctxRatio = document.getElementById('incomeSpendingChart').getContext('2d');
    new Chart(ctxRatio, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: backgroundColor,
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            layout: { padding: { top: 0, bottom: 20 } },
            plugins: {
                tooltip: {
                    callbacks: {
                        label(context) {
                            const total = income;
                            const value = context.raw;
                            const percent = ((value / total) * 100).toFixed(1);
                            return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`;
                        }
                    }
                },
            }
        },
        plugins: [centerTextPlugin]
    });
}
// 상위 3개 카테고리 파이차트 그리기
function fetchAndDrawTop3Category() {
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate,
        monthDate: userData.monthDate
    };

    return axios.post('/api/dashboard/top3-category', requestData)
        .then(res => {
            const data = res.data;

            if (!Array.isArray(data) || data.length === 0) {
                console.warn('상위 카테고리 데이터가 없습니다.');
                return;
            }

            // categoryName 배열, categoryMoney 배열 생성
            const labels = data.map(item => item.categoryName);
            const values = data.map(item => item.categoryMoney);

            // 전체 합계 구하기 (백분율 계산용)
            const total = values.reduce((acc, val) => acc + val, 0);

            // 백분율 라벨 만들기 (ex: 고정비 55.2%)
            const labelsWithPercent = data.map(item => {
                const percent = ((item.categoryMoney / total) * 100).toFixed(1);
                return `${item.categoryName} ${percent}%`;
            });

            // 차트 그리기
            const ctx = document.getElementById('top3PieChart').getContext('2d');

            // 만약 이전에 차트가 그려졌다면 제거 (차트 겹침 방지)
            if (window.top3PieChartInstance) {
                window.top3PieChartInstance.destroy();
            }

            window.top3PieChartInstance = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labelsWithPercent,
                    datasets: [{
                        data: values,
                        backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'], // 원하는 색상 3가지
                        hoverOffset: 30,
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label(context) {
                                    const value = context.raw;
                                    const percent = ((value / total) * 100).toFixed(1);
                                    return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`;
                                }
                            }
                        },
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });

        })
        .catch(err => {
            console.error('상위 카테고리 데이터 로딩 실패:', err);
        });
}

function drawMonthlyChart(incomeData, expenseData) {
    const ctx = document.getElementById('monthlyLineChart').getContext('2d');

    // 기존 차트 인스턴스 제거 (중복 방지)
    if (window.monthlyChartInstance) {
        window.monthlyChartInstance.destroy();
    }

    window.monthlyChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
            datasets: [
                {
                    label: '월별 수입',
                    data: incomeData,
                    borderColor: '#3b82f6',
                    backgroundColor: 'rgba(59, 130, 246, 0.2)',
                    tension: 0.3,
                    fill: true
                },
                {
                    label: '월별 소비',
                    data: expenseData,
                    borderColor: '#ef4444',
                    backgroundColor: 'rgba(239, 68, 68, 0.2)',
                    tension: 0.3,
                    fill: true
                }
            ]
        },
        options: {
            responsive: false,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                title: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString() + '원';
                        }
                    }
                }
            }
        }
    });
}

//목표자산
document.addEventListener("DOMContentLoaded", async () => {
    const memberId = userData.memberId;
    try {
        const [targetRes, totalRes] = await Promise.all([
            axios.get(`/api/dashboard/target-assets/${memberId}`),
            axios.get(`/api/dashboard/total-assets/${memberId}`)
        ]);

        const targetAssets = targetRes.data;
        const totalAssets = totalRes.data;

        if (!targetAssets || targetAssets === 0) {
            // 목표 자산이 없을 때
            document.getElementById("no-goal-message").style.display = "block";
            document.getElementById("goal-chart-container").style.display = "none";
        } else {
            // 목표 자산이 있을 때
            document.getElementById("no-goal-message").style.display = "none";
            document.getElementById("goal-chart-container").style.display = "block";

            const remaining = Math.max(targetAssets - totalAssets, 0);
            const progress = Math.min((totalAssets / targetAssets) * 100, 100).toFixed(2);

            drawGoalChart(totalAssets, remaining);

            document.getElementById("goal-details").innerText =
                `현재 자산: ${totalAssets.toLocaleString()}원 / 목표 자산: ${targetAssets.toLocaleString()}원\n달성률: ${progress}%`;
        }
    } catch (error) {
        console.error("자산 정보를 불러오는 중 오류 발생:", error);
    }
});

function drawGoalChart(achieved, remaining) {
    const ctxGoal = document.getElementById("goalChart").getContext("2d");

    const total = achieved + remaining;
    const percentage = ((achieved / total) * 100).toFixed(1);
    document.getElementById("goal-details").innerText = `${percentage}%`;

    // 기존 차트 인스턴스가 존재하면 제거
    if (window.goalChartInstance) {
        window.goalChartInstance.destroy();
    }

    window.goalChartInstance = new Chart(ctxGoal, {
        type: 'doughnut',
        data: {
            labels: ['달성 자산', '남은 자산'],
            datasets: [{
                data: [achieved, remaining],
                backgroundColor: ['#4caf50', '#c8e6c9'],
                hoverOffset: 20
            }]
        },
        options: {
            responsive: true,
            cutout: '70%',
            plugins: {
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label(context) {
                            const value = context.raw;
                            const percent = ((value / total) * 100).toFixed(1);
                            return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`;
                        }
                    }
                }
            }
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const goalButton = document.getElementById('goal-button');
    const goalModal = document.getElementById('goal-modal');
    const closeModalBtn = document.getElementById('close-modal');
    const totalBalanceElement = document.querySelector('.totalBalance');
    const goalInput = document.getElementById('goalInput');
    const saveGoalBtn = document.getElementById('save-goal-btn');

    let totalAssets = 0; // 총자산 값을 저장할 변수
    const memberId = userData.memberId;

    // 총자산을 서버에서 가져와서 표시하는 함수 (axios 버전)
    async function fetchTotalAssets() {
        try {
            const res = await axios.get(`/api/dashboard/total-assets/${memberId}`);
            totalAssets = res.data;
            totalBalanceElement.textContent = totalAssets.toLocaleString('ko-KR') + '원';
        } catch (error) {
            console.error("총자산 불러오기 실패:", error);
            totalBalanceElement.textContent = '불러오기 실패';
        }
    }

    // 입력값 유효성 검사 및 메시지 반환 함수
    function validateGoalValue(inputStr) {
        if (!inputStr || inputStr.trim() === '') {
            return { valid: false, message: '목표 금액을 입력해주세요.' };
        }

        // 콤마 제거 후 숫자 변환
        const value = Number(inputStr.replace(/,/g, ''));
        if (isNaN(value) || value <= 0) {
            return { valid: false, message: '목표 금액을 올바른 숫자로 입력해주세요.' };
        }

        if (value < totalAssets) {
            return { valid: false, message: '⚠️ 목표금액이 총자산보다 작습니다.' };
        }

        return { valid: true, value };
    }

    // 숫자만 입력되게 (음수, 문자 입력 방지)
    goalInput.addEventListener('input', function () {
        let value = this.value.replace(/[^0-9]/g, '');

        if (value === '') {
            this.value = '';
            return;
        }

        this.value = Number(value).toLocaleString('ko-KR');
    });

    // 목표 금액 저장 버튼 클릭 이벤트
    saveGoalBtn.addEventListener('click', async function () {
        const inputStr = goalInput.value;
        const validation = validateGoalValue(inputStr);

        if (!validation.valid) {
            alert(validation.message);
            return;
        }

        try {
            const res = await axios.post("/api/dashboard/target-assets", {
                memberId: memberId,
                targetAssets: validation.value
            });

            if (res.data === true) {
                alert('✅ 목표금액이 설정되었습니다.');
                goalModal.style.display = 'none';
                location.reload();
            } else {
                alert("저장 실패 😢");
            }
        } catch (err) {
            alert("에러 발생: " + err);
        }
    });

    // 모달 열기 버튼
    goalButton.addEventListener('click', async function () {
        goalModal.style.display = 'block';
        await fetchTotalAssets();
        goalInput.value = '';
    });

    // 닫기 버튼
    closeModalBtn.addEventListener('click', function () {
        goalModal.style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function (event) {
        if (event.target === goalModal) {
            goalModal.style.display = 'none';
        }
    });
});


// 대시보드 초기화 함수 (연도별 수입/지출 통합됨)
function initDashboard() {
    updateDate();

    displayMonthlyIncomeAndExpense()
        .then(({ income, expense }) => {
            drawIncomeSpendingChart(income, expense);
        })
        .catch(err => {
            console.error('대시보드 초기화 중 오류:', err);
        });

    Promise.all([fetchTodayIncome(), fetchTodayExpense()])
        .then(([incomeList, expenseList]) => {
            renderTodayRecords(incomeList, expenseList);
        })
        .catch(error => {
            console.error('오늘의 수입/지출 렌더링 중 오류:', error);
        });

    fetchAndDrawTop3Category();

    // 연도별 수입/소비 그래프 데이터 로드 추가
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate
    };

    Promise.all([
        axios.post("/api/dashboard/year-incomes", requestData),
        axios.post("/api/dashboard/year-expenses", requestData)
    ]).then(([incomeRes, expenseRes]) => {
        const incomeData = Array(12).fill(0);
        const expenseData = Array(12).fill(0);

        incomeRes.data.forEach(item => {
            const monthIndex = parseInt(item.accountTradeMonth) - 1;
            incomeData[monthIndex] = item.accountTradeMoney;
        });

        expenseRes.data.forEach(item => {
            const monthIndex = parseInt(item.cardTradeMonth) - 1;
            expenseData[monthIndex] = item.cardTradeMoney;
        });

        drawMonthlyChart(incomeData, expenseData);
    }).catch(err => {
        console.error("연도별 수입/지출 데이터 요청 오류:", err);
    });
}

// DOMContentLoaded 시점에 초기화 실행
document.addEventListener('DOMContentLoaded', () => {
    initDashboard();
    updateDate();
    updateTopCategoryMonth();
    updateYearTitle();
});

