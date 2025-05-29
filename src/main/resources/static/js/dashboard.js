// 1. 날짜 표시 (today-date, today-date2)
function updateDate() {
    const today = new Date();
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const formatted = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}/${days[today.getDay()]}`;
    document.getElementById("today-date").textContent = formatted;
    document.getElementById("today-date2").textContent = formatted;
}
updateDate();

// 2. 데이터 변수 (실제 API 연동 시 대체)
const userData = {
    member_id: 1,
    target_assets: null,
    total_assets: 45000000,
    monthlyIncome: { "1": 12300000 },
    monthlyCardSpending: { "1": 4560000 },
    topTodayCardTrades: [
        { card_store_name: "스타벅스", card_trade_money: 1200000 },
        { card_store_name: "이마트", card_trade_money: 800000 },
        { card_store_name: "편의점", card_trade_money: 500000 },
    ],
    topAccountTrades: [
        { account_source: "급여", account_trade_money: 4000000 },
        { account_source: "이자", account_trade_money: 1500000 },
        { account_source: "기타", account_trade_money: 500000 },
    ],
    topCategories: [
        { category_name: "식비", money: 3000000 },
        { category_name: "교통", money: 1500000 },
        { category_name: "문화", money: 1200000 },
    ],
    monthlyCardTradeSum: { "1": 1200000, "2": 1500000, "3": 1800000, "4": 2000000, "5": 2500000 },
    monthlyAccountTradeSum: { "1": 5000000, "2": 4700000, "3": 4500000, "4": 4800000, "5": 5100000 }
};

// 3. 수입, 소비, 남은 금액 계산 (월 1월 기준 예시)
const income = userData.monthlyIncome["1"] || 0;
const spending = userData.monthlyCardSpending["1"] || 0;
const remaining = income - spending;
const spendingPercent = income > 0 ? ((spending / income) * 100).toFixed(1) : 0;

// 4. 도넛 차트 중앙 텍스트 플러그인
const centerTextPlugin = {
    id: 'centerTextPlugin',
    beforeDraw(chart) {
        const { width, height, ctx } = chart;
        ctx.restore();
        const fontSize = (height / 150).toFixed(2);
        ctx.font = `${fontSize}em sans-serif`;
        ctx.textBaseline = "middle";

        const text = `${spendingPercent}%`;
        const textX = Math.round((width - ctx.measureText(text).width) / 2);
        const textY = height / 2 + 15;

        ctx.fillStyle = '#333';
        ctx.fillText(text, textX, textY);
        ctx.save();
    }
};

// 5. 수입 vs 소비 도넛 차트 생성
const ctx = document.getElementById('incomeSpendingChart').getContext('2d');
new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ['소비', '남은 금액'],
        datasets: [{
            data: [spending, remaining],
            backgroundColor: ['#6750A4', '#EBE1F5'],
            hoverOffset: 10
        }]
    },
    options: {
        responsive: true,
        layout: { padding: { top: 0, bottom: 20 } },
        plugins: {
            title: { display: true, /* text: '이번달 수입 대비 소비 비율' */ },
            tooltip: {
                callbacks: {
                    label(context) {
                        const total = spending + remaining;
                        const value = context.raw;
                        const percent = ((value / total) * 100).toFixed(1);
                        return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`;
                    }
                }
            }
        }
    },
    plugins: [centerTextPlugin]
});

// 6. 월별 수입/소비 라인 차트
const monthlyLabels = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];

// 실제 데이터 매핑 (1~5월 데이터, 이후 null)
const monthlyIncomeArr = monthlyLabels.map((label, i) => {
    const month = (i + 1).toString();
    return userData.monthlyAccountTradeSum[month] ?? null;
});
const monthlySpendingArr = monthlyLabels.map((label, i) => {
    const month = (i + 1).toString();
    return userData.monthlyCardTradeSum[month] ?? null;
});

const lineCtx = document.getElementById('monthlyLineChart').getContext('2d');
new Chart(lineCtx, {
    type: 'line',
    data: {
        labels: monthlyLabels,
        datasets: [
            {
                label: '수입',
                data: monthlyIncomeArr,
                borderColor: '#36A2EB',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                fill: false,
                tension: 0.4,
                spanGaps: false
            },
            {
                label: '소비',
                data: monthlySpendingArr,
                borderColor: '#FF6384',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                fill: false,
                tension: 0.4,
                spanGaps: false
            }
        ]
    },
    options: {
        responsive: false,
        plugins: {
            tooltip: {
                callbacks: {
                    label(context) {
                        if (context.raw === null) return context.dataset.label + ': 데이터 없음';
                        return `${context.dataset.label}: ${context.raw.toLocaleString()}원`;
                    }
                }
            },
            legend: { position: 'top' }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback(value) {
                        return value.toLocaleString() + '원';
                    }
                }
            }
        }
    }
});

// 7. 상위 3개 카테고리 파이 차트 (실제 데이터 기준)
const categories = userData.topCategories;
// 상위 3개 정렬 (이미 정렬됐으면 생략 가능)
const top3Categories = categories.slice(0, 3);
const totalCategorySum = top3Categories.reduce((acc, c) => acc + c.money, 0);
const pieLabels = top3Categories.map(c => c.category_name);
const pieData = top3Categories.map(c => c.money);

const pieCtx = document.getElementById('top3PieChart').getContext('2d');
new Chart(pieCtx, {
    type: 'pie',
    data: {
        labels: pieLabels,
        datasets: [{
            data: pieData,
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            hoverOffset: 15
        }]
    },
    options: {
        responsive: false,
        plugins: {
            tooltip: {
                callbacks: {
                    label(context) {
                        const value = context.raw;
                        const percent = ((value / totalCategorySum) * 100).toFixed(1);
                        return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`;
                    }
                }
            }
        }
    }
});

// 8. 목표 자산 대비 현황 도넛 차트 (AnyChart 활용)
anychart.onDocumentReady(function () {
    const target = userData.target_assets || 0;
    const total = userData.total_assets || 0;
    const remainingAssets = Math.max(target - total, 0);

    const data = [
        { x: "현재 자산", value: total },
        { x: "목표까지 남은 금액", value: remainingAssets }
    ];

    const chart = anychart.pie(data);
    chart.innerRadius('65%');
    chart.labels().position("outside");
    chart.legend(true);
    chart.container("target-assets-chart");
    chart.draw();
});

// 9. 목표 자산 모달 열기/닫기

function updateTotalBalance() {
    const totalBalanceElem = document.querySelector(".totalBalance");
    if (totalBalanceElem) {
        totalBalanceElem.textContent = userData.total_assets.toLocaleString() + "원";
    }
}

let goalChartInstance = null;

function renderGoalChart() {
    if (!userData.target_assets || userData.target_assets <= 0) {
        if (goalChartInstance) {
            goalChartInstance.destroy();
            goalChartInstance = null;
        }
        document.getElementById("goal-stats").innerHTML = '';
        return;
    }

    const current = userData.total_assets || 0;
    const target = userData.target_assets;
    const remaining = Math.max(target - current, 0);

    const ctx = document.getElementById("goalChart").getContext("2d");

    if (goalChartInstance) {
        goalChartInstance.destroy();
    }

    const percentage = Math.min((current / target) * 100, 100).toFixed(1); // 소수점 1자리
    const percent = ((current / target) * 100).toFixed(1); // 소수점 1자리까지 표시
    document.getElementById("goal-stats").innerHTML =
        `<div style="font-weight: bold; margin-top: 5px; ">목표 대비 ${percent}%</div>`+
        `<div>${current.toLocaleString()} 원 / ${target.toLocaleString()} 원</div>`;
    goalChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['현재 자산', '남은 금액'],
            datasets: [{
                data: [current, remaining],
                backgroundColor: ['#36A2EB', '#EBE1F5'],
                borderWidth: 0
            }]
        },
        options: {
            rotation: -90,
            circumference: 180,
            cutout: '65%',
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom'
                },
                // 중앙 텍스트 표시 플러그인
                afterDraw: (chart) => {
                    const {ctx, chartArea: {width, height}} = chart;
                    ctx.save();

                    const centerX = width / 2;
                    const centerY = height / 2; // 일단 중앙으로 고정

                    // 배경색이 흰색이면 글씨가 잘 보임
                    ctx.font = 'bold 28px Arial';
                    ctx.fillStyle = '#333';
                    ctx.textAlign = 'center';
                    ctx.textBaseline = 'middle';
                    ctx.fillText(`${percentage}%`, centerX, centerY - 10);

                    ctx.font = '16px Arial';
                    ctx.fillStyle = '#666';
                    ctx.fillText(`${current.toLocaleString()}원`, centerX, centerY + 20);

                    ctx.restore();
                }


            }
        }
    });

}

const goalModal = document.getElementById("goal-modal");
const openGoalBtn = document.getElementById("goal-button");
const closeGoalBtn = document.getElementById("close-modal");
const setGoalBtn = document.getElementById("set-modal");
const goalInput = document.getElementById("goalInput");

// 총자산 표시 업데이트 (모달 열 때마다 반영)
function openGoalModal() {
    updateTotalBalance();
    goalInput.value = userData.target_assets ? userData.target_assets.toLocaleString() : "";
    goalModal.style.display = "block";
}

openGoalBtn.addEventListener("click", openGoalModal);

closeGoalBtn.addEventListener("click", () => {
    goalModal.style.display = "none";
});

window.addEventListener("click", (e) => {
    if (e.target === goalModal) {
        goalModal.style.display = "none";
    }
});

// 콤마 자동포맷팅 (숫자만 허용)
goalInput.addEventListener("input", (e) => {
    let value = e.target.value.replace(/[^0-9]/g, ''); // 숫자 외 제거
    if (value) {
        value = parseInt(value, 10).toLocaleString();
    }
    e.target.value = value;
});

// 목표 자산 저장 처리
setGoalBtn.addEventListener("click", () => {
    let rawValue = goalInput.value.replace(/,/g, '');
    let newTarget = parseInt(rawValue, 10);

    if (isNaN(newTarget) || newTarget <= 0) {
        alert("목표 자산은 0원 이상 숫자여야 합니다.");
        return;
    }

    userData.target_assets = newTarget;
    goalModal.style.display = "none";
    alert(`새 목표 자산: ${newTarget.toLocaleString()}원 으로 저장 처리(예시)`);

    renderGoalChart();
});

// 페이지 로드 시 목표 자산 차트 렌더링
window.addEventListener("DOMContentLoaded", () => {
    renderGoalChart();
});

