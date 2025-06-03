// const memberId = document.getElementById("memberId").value;
const today2 = new Date()
const yyyy = today2.getFullYear()
const mm = String(today2.getMonth() + 1).padStart(2, "0")
const dd = String(today2.getDate()).padStart(2, "0")

// 브라우저 시간 기준으로 할당
const userData = {
    // memberId: parseInt(memberId),
    memberId: 21,
    yearDate: yyyy.toString(),
    monthDate: mm,
    todayDate: `${yyyy}-${mm}-${dd}`, // "YYYY-MM-DD" 형태의 문자열
}

function updateDate() {
    const days = ["일", "월", "화", "수", "목", "금", "토"]
    const dateObj = new Date(userData.todayDate)
    const dayName = days[dateObj.getDay()]
    const formatted = `${userData.yearDate}/${userData.monthDate}/${dd} ${dayName}`
    document.getElementById("today-date").textContent = formatted
}

function updateTopCategoryMonth() {
    const month = today2.getMonth() + 1
    document.getElementById("topCategoryMonth").textContent = `${month}월`
}

function updateYearTitle() {
    document.getElementById("currentYear").textContent = yyyy
}

function formatCurrency(num) {
    if (typeof num !== "number" || isNaN(num)) {
        console.warn("formatCurrency에 잘못된 값:", num)
        return "0원"
    }
    return num.toLocaleString("ko-KR") + "원"
}

function fetchTodayIncome() {
    const requestData = {
        memberId: userData.memberId,
        todayDate: userData.todayDate,
    }
    return axios
        .post("/api/dashboard/today-income", requestData)
        .then((res) => res.data)
        .catch((err) => {
            console.error("오늘 수입 데이터 로딩 실패:", err)
            return []
        })
}

function fetchTodayExpense() {
    const requestData = {
        memberId: userData.memberId,
        todayDate: userData.todayDate,
    }
    return axios
        .post("/api/dashboard/today-expense", requestData)
        .then((res) => res.data)
        .catch((err) => {
            console.error("오늘 지출 데이터 로딩 실패:", err)
            return []
        })
}

function renderTodayRecords(incomeList, expenseList) {
    const incomeColumn = document.getElementById("income-column")
    const expenseColumn = document.getElementById("expense-column")

    incomeColumn.innerHTML = ""
    expenseColumn.innerHTML = ""

    incomeList.forEach((item) => {
        const category = item.accountSource || "분류없음"
        const amount = Number(item.accountTradeMoney) || 0

        const span = document.createElement("span")
        span.className = "plus blue"
        span.textContent = `${category} ${formatCurrency(amount)}`
        incomeColumn.appendChild(span)
    })

    expenseList.forEach((item) => {
        const category = item.cardStoreName || "분류없음"
        const amount = Number(item.cardTradeMoney) || 0

        const span = document.createElement("span")
        span.className = "minus red"
        span.textContent = `${category} ${formatCurrency(amount)}`
        expenseColumn.appendChild(span)
    })
}

function displayMonthlyIncomeAndExpense() {
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate,
        monthDate: userData.monthDate,
    }

    return axios
        .all([
            axios.post("/api/dashboard/monthly-income", requestData),
            axios.post("/api/dashboard/monthly-expense", requestData),
        ])
        .then(
            axios.spread((incomeRes, expenseRes) => {
                const income = incomeRes.data
                const expense = expenseRes.data

                document.getElementById("montylyIncome").innerText = formatCurrency(income)
                document.getElementById("montylyExpense").innerText = formatCurrency(expense)

                return { income, expense }
            }),
        )
        .catch((error) => {
            console.error("수입/소비 정보 로딩 실패:", error)
        })
}

// 개선된 도넛 차트 - 크기 증가, 퍼센트 표시, 상태 판단 로직 수정
function drawIncomeSpendingChart(income, expense) {
    const canvas = document.getElementById("incomeSpendingChart")
    if (!canvas) {
        console.error("차트 캔버스를 찾을 수 없습니다.")
        return
    }

    // 기존 차트 인스턴스 제거
    if (window.incomeSpendingChartInstance) {
        window.incomeSpendingChartInstance.destroy()
    }

    let remaining = income - expense
    let isOver = false

    if (remaining < 0) {
        isOver = true
        remaining = Math.abs(remaining)
    }

    const ratio = income > 0 ? (expense / income) * 100 : 0

    // 상태 판단 로직 수정
    let status = "good"
    let statusIcon = "😊"
    let statusText = "건전한 소비"
    let cardClass = ""
    let progressClass = ""

    if (ratio > 100) {
        status = "danger"
        statusIcon = "😰"
        statusText = "예산 초과"
        cardClass = "danger"
        progressClass = "danger"
    } else if (ratio > 90) {
        status = "danger"
        statusIcon = "😰"
        statusText = "위험 수준"
        cardClass = "danger"
        progressClass = "danger"
    } else if (ratio > 70) {
        status = "warning"
        statusIcon = "😐"
        statusText = "주의 필요"
        cardClass = "warning"
        progressClass = "warning"
    }

    // UI 업데이트
    updateUI(income, expense, ratio, statusIcon, statusText, cardClass, progressClass)

    let labels, data, backgroundColor, borderColor

    if (isOver) {
        labels = ["소비", "초과 금액"]
        data = [income, remaining]
        // 기존: 강한 빨간색 → 부드러운 코랄/오렌지 톤으로 변경
        backgroundColor = ["rgba(255, 107, 107, 0.8)", "rgba(255, 154, 158, 0.6)"]
        borderColor = ["rgba(255, 107, 107, 1)", "rgba(255, 154, 158, 1)"]
    } else {
        labels = ["소비", "남은 금액"]
        data = [expense, remaining]
        backgroundColor = [
            status === "good"
                ? "rgba(52, 211, 153, 0.8)" // 부드러운 에메랄드 그린
                : status === "warning"
                    ? "rgba(251, 191, 36, 0.8)" // 부드러운 앰버
                    : "rgba(248, 113, 113, 0.8)", // 부드러운 로즈
            "rgba(226, 232, 240, 0.4)", // 더 부드러운 회색
        ]
        borderColor = [
            status === "good"
                ? "rgba(52, 211, 153, 1)"
                : status === "warning"
                    ? "rgba(251, 191, 36, 1)"
                    : "rgba(248, 113, 113, 1)",
            "rgba(226, 232, 240, 0.8)",
        ]
    }

    // 중앙 텍스트 플러그인 - 퍼센트 표시
    const centerTextPlugin = {
        id: "centerTextPlugin",
        beforeDraw(chart) {
            const { width, height, ctx } = chart
            ctx.restore()

            const centerX = width / 2
            const centerY = height / 2

            // 폰트 크기 증가
            const fontSize = Math.min(width, height) / 8
            ctx.font = `bold ${fontSize}px 'Segoe UI', sans-serif`
            ctx.textAlign = "center"
            ctx.textBaseline = "middle"

            const gradient = ctx.createLinearGradient(0, centerY - fontSize / 2, 0, centerY + fontSize / 2)
            gradient.addColorStop(0, "rgb(104,96,108)")
            // gradient.addColorStop(1, "rgb(104,96,108)")

            ctx.fillStyle = gradient
            ctx.shadowColor = "rgba(0, 0, 0, 0.4)"
            ctx.shadowBlur = 15
            ctx.shadowOffsetY = 3

            const text = `${ratio.toFixed(1)}%`
            ctx.fillText(text, centerX, centerY + 8)

            ctx.font = `500 ${fontSize * 0.3}px 'Segoe UI', sans-serif`
            ctx.fillStyle = "rgba(255, 255, 255, 0.9)"
            ctx.shadowBlur = 8
            // ctx.fillText("소비율", centerX, centerY + fontSize * 0.4)

            ctx.restore()
        },
    }

    const ctx = canvas.getContext("2d")

    window.incomeSpendingChartInstance = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: labels,
            datasets: [
                {
                    data: data,
                    backgroundColor: backgroundColor,
                    borderColor: borderColor,
                    borderWidth: 3, // 4에서 3으로 줄임
                    hoverOffset: 15, // 20에서 15로 줄임
                    hoverBorderWidth: 4, // 6에서 4로 줄임
                    cutout: "60%", // 55%에서 60%로 증가하여 도넛 구멍 키움
                },
            ],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: {
                padding: 10, // 20에서 10으로 줄임
            },
            plugins: {
                legend: {
                    display: false, // 범례 숨김 (하단에 커스텀 범례 사용)
                },
                tooltip: {
                    enabled: true,
                    backgroundColor: "rgba(0, 0, 0, 0.9)",
                    titleColor: "white",
                    bodyColor: "white",
                    borderColor: "rgba(255, 255, 255, 0.3)",
                    borderWidth: 2,
                    cornerRadius: 12,
                    displayColors: true,
                    titleFont: { size: 14, weight: "bold" },
                    bodyFont: { size: 13 },
                    callbacks: {
                        label(context) {
                            const total = income
                            const value = context.raw
                            const percent = total > 0 ? ((value / total) * 100).toFixed(1) : "0.0"
                            return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`
                        },
                    },
                },
            },
            animation: {
                animateRotate: true,
                animateScale: true,
                duration: 2000,
                easing: "easeOutCubic",
            },
            interaction: {
                intersect: false,
                mode: "nearest",
            },
        },
        plugins: [centerTextPlugin],
    })
}

// UI 업데이트 함수
function updateUI(income, expense, ratio, statusIcon, statusText, cardClass, progressClass) {
    const statusIconElement = document.getElementById("statusIcon")
    const statusTextElement = document.getElementById("statusText")
    const incomeValueElement = document.getElementById("incomeValue")
    const expenseValueElement = document.getElementById("expenseValue")
    const progressFillElement = document.getElementById("progressFill")
    const progressPercentElement = document.getElementById("progressPercent")

    if (statusIconElement) statusIconElement.textContent = statusIcon
    if (statusTextElement) statusTextElement.textContent = statusText
    if (incomeValueElement) incomeValueElement.textContent = income.toLocaleString() + "원"
    if (expenseValueElement) expenseValueElement.textContent = expense.toLocaleString() + "원"

    const card = document.querySelector(".ratio-card")
    if (card) {
        card.className = `ratio-card ${cardClass}`
    }

    if (progressFillElement) {
        progressFillElement.className = `progress-fill ${progressClass}`
    }
    if (progressPercentElement) {
        progressPercentElement.textContent = `${ratio.toFixed(1)}%`
    }

    // 프로그레스 바 애니메이션
    setTimeout(() => {
        if (progressFillElement) {
            progressFillElement.style.width = `${Math.min(ratio, 100)}%`
        }
    }, 500)
}

// 상위 3개 카테고리 파이차트 그리기
function fetchAndDrawTop3Category() {
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate,
        monthDate: userData.monthDate,
    }

    return axios
        .post("/api/dashboard/top3-category", requestData)
        .then((res) => {
            const data = res.data

            if (!Array.isArray(data) || data.length === 0) {
                console.warn("상위 카테고리 데이터가 없습니다.")
                // 데이터가 없을 때 빈 상태 표시
                const container = document.querySelector(".category")
                const existingEmpty = container.querySelector(".empty-category")
                if (!existingEmpty) {
                    const emptyDiv = document.createElement("div")
                    emptyDiv.className = "empty-category"
                    emptyDiv.innerHTML = `
                        <div class="empty-icon">📊</div>
                        <p>카테고리 데이터가 없습니다</p>
                    `
                    container.appendChild(emptyDiv)
                }
                return
            }

            // 빈 상태 메시지 제거
            const existingEmpty = document.querySelector(".empty-category")
            if (existingEmpty) {
                existingEmpty.remove()
            }

            // categoryName 배열, categoryMoney 배열 생성
            const labels = data.map((item) => item.categoryName)
            const values = data.map((item) => item.categoryMoney)

            // 전체 합계 구하기 (백분율 계산용)
            const total = values.reduce((acc, val) => acc + val, 0)

            // 세련된 색상 팔레트
            const modernColors = [
                {
                    bg: "rgba(99, 102, 241, 0.8)",
                    border: "rgba(99, 102, 241, 1)",
                    gradient: ["rgba(99, 102, 241, 0.9)", "rgba(99, 102, 241, 0.7)"],
                },
                {
                    bg: "rgba(34, 197, 94, 0.8)",
                    border: "rgba(34, 197, 94, 1)",
                    gradient: ["rgba(34, 197, 94, 0.9)", "rgba(34, 197, 94, 0.7)"],
                },
                {
                    bg: "rgba(251, 146, 60, 0.8)",
                    border: "rgba(251, 146, 60, 1)",
                    gradient: ["rgba(251, 146, 60, 0.9)", "rgba(251, 146, 60, 0.7)"],
                },
            ]

            // 차트 그리기
            const ctx = document.getElementById("top3PieChart").getContext("2d")

            // 만약 이전에 차트가 그려졌다면 제거 (차트 겹침 방지)
            if (window.top3PieChartInstance) {
                window.top3PieChartInstance.destroy()
            }

            // 커스텀 범례 생성
            createCustomLegend(data, total, modernColors)

            window.top3PieChartInstance = new Chart(ctx, {
                type: "doughnut", // pie에서 doughnut으로 변경하여 더 현대적으로
                data: {
                    labels: labels,
                    datasets: [
                        {
                            data: values,
                            backgroundColor: modernColors.map((color) => color.bg),
                            borderColor: modernColors.map((color) => color.border),
                            borderWidth: 3,
                            hoverOffset: 20,
                            hoverBorderWidth: 4,
                            cutout: "45%", // 도넛 구멍 크기
                        },
                    ],
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    layout: {
                        padding: 20,
                    },
                    plugins: {
                        legend: {
                            display: false, // 기본 범례 숨김 (커스텀 범례 사용)
                        },
                        tooltip: {
                            enabled: true,
                            backgroundColor: "rgba(0, 0, 0, 0.9)",
                            titleColor: "white",
                            bodyColor: "white",
                            borderColor: "rgba(255, 255, 255, 0.3)",
                            borderWidth: 2,
                            cornerRadius: 12,
                            displayColors: true,
                            titleFont: { size: 14, weight: "bold" },
                            bodyFont: { size: 13 },
                            callbacks: {
                                title: (context) => context[0].label,
                                label: (context) => {
                                    const value = context.raw
                                    const percent = ((value / total) * 100).toFixed(1)
                                    return [`금액: ${value.toLocaleString()}원`, `비율: ${percent}%`]
                                },
                            },
                        },
                    },
                    animation: {
                        animateRotate: true,
                        animateScale: true,
                        duration: 2000,
                        easing: "easeOutCubic",
                    },
                    interaction: {
                        intersect: false,
                        mode: "nearest",
                    },
                },
            })
        })
        .catch((err) => {
            console.error("상위 카테고리 데이터 로딩 실패:", err)
        })
}

// 커스텀 범례 생성 함수
function createCustomLegend(data, total, colors) {
    const legendContainer = document.getElementById("category-legend")

    // 기존 범례 컨테이너가 없으면 생성
    if (!legendContainer) {
        const categoryCard = document.querySelector(".category")
        const newLegendContainer = document.createElement("div")
        newLegendContainer.id = "category-legend"
        newLegendContainer.className = "category-legend"
        categoryCard.appendChild(newLegendContainer)
    }

    const legend = document.getElementById("category-legend")
    legend.innerHTML = ""

    data.forEach((item, index) => {
        const percent = ((item.categoryMoney / total) * 100).toFixed(1)
        const legendItem = document.createElement("div")
        legendItem.className = "legend-item-custom"

        legendItem.innerHTML = `
            <div class="legend-dot" style="background-color: ${colors[index].border}"></div>
            <div class="legend-content">
                <div class="legend-name">${item.categoryName}</div>
                <div class="legend-details">
                    <span class="legend-amount">${item.categoryMoney.toLocaleString()}원</span>
                    <span class="legend-percent">${percent}%</span>
                </div>
            </div>
        `

        legend.appendChild(legendItem)
    })
}


// 개선된 월별 차트 - 툴팁 추가
function drawMonthlyChart(incomeData, expenseData) {
    const ctxMonth = document.getElementById("monthlyLineChart").getContext("2d")

    // 기존 차트 인스턴스 제거 (중복 방지)
    if (window.monthlyChartInstance) {
        window.monthlyChartInstance.destroy()
    }

    window.monthlyChartInstance = new Chart(ctxMonth, {
        type: "line",
        data: {
            labels: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            datasets: [
                {
                    label: "월별 수입",
                    data: incomeData,
                    borderColor: "#059669",
                    backgroundColor: "rgba(5, 150, 105, 0.2)",
                    borderWidth: 3,
                    tension: 0.4,
                    fill: true,
                    pointBackgroundColor: "#059669",
                    pointBorderColor: "white",
                    pointBorderWidth: 2,
                    pointRadius: 5,
                    pointHoverRadius: 8,
                },
                {
                    label: "월별 소비",
                    data: expenseData,
                    borderColor: "#dc2626",
                    backgroundColor: "rgba(220, 38, 38, 0.2)",
                    borderWidth: 3,
                    tension: 0.4,
                    fill: true,
                    pointBackgroundColor: "#dc2626",
                    pointBorderColor: "white",
                    pointBorderWidth: 2,
                    pointRadius: 5,
                    pointHoverRadius: 8,
                },
            ],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                intersect: false,
                mode: "index",
            },
            plugins: {
                legend: {
                    display: false, // 커스텀 범례 사용
                },
                tooltip: {
                    enabled: true,
                    backgroundColor: "rgba(0, 0, 0, 0.9)",
                    titleColor: "white",
                    bodyColor: "white",
                    borderColor: "rgba(255, 255, 255, 0.3)",
                    borderWidth: 2,
                    cornerRadius: 12,
                    displayColors: true,
                    titleFont: { size: 14, weight: "bold" },
                    bodyFont: { size: 13 },
                    callbacks: {
                        title: (context) => context[0].label + " 현황",
                        label: (context) => {
                            const value = context.raw
                            const formattedValue = value.toLocaleString("ko-KR") + "원"
                            return `${context.dataset.label}: ${formattedValue}`
                        },
                        afterBody: (context) => {
                            if (context.length === 2) {
                                const income = context.find((c) => c.dataset.label.includes("수입"))?.raw || 0
                                const expense = context.find((c) => c.dataset.label.includes("소비"))?.raw || 0
                                const balance = income - expense
                                const balanceText = balance >= 0 ? "흑자" : "적자"
                                return [``, `${balanceText}: ${Math.abs(balance).toLocaleString("ko-KR")}원`]
                            }
                            return []
                        },
                    },
                },
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: "rgba(139, 92, 246, 0.1)",
                        lineWidth: 1,
                    },
                    ticks: {
                        color: "#5b21b6",
                        font: { size: 11 },
                        callback: (value) => {
                            if (value >= 1000000) {
                                return (value / 1000000).toFixed(0) + "백만원"
                            } else if (value >= 10000) {
                                return (value / 10000).toFixed(0) + "만원"
                            }
                            return value.toLocaleString() + "원"
                        },
                    },
                },
                x: {
                    grid: {
                        color: "rgba(139, 92, 246, 0.1)",
                        lineWidth: 1,
                    },
                    ticks: {
                        color: "#5b21b6",
                        font: { size: 11 },
                    },
                },
            },
            animation: {
                duration: 2000,
                easing: "easeOutCubic",
            },
        },
    })
}

//목표자산 - 반도넛 차트로 개선
document.addEventListener("DOMContentLoaded", async () => {
    const memberId = userData.memberId
    try {
        const [targetRes, totalRes] = await Promise.all([
            axios.get(`/api/dashboard/target-assets/${memberId}`),
            axios.get(`/api/dashboard/total-assets/${memberId}`),
        ])

        const targetAssets = targetRes.data
        const totalAssets = totalRes.data

        if (!targetAssets || targetAssets === 0) {
            // 목표 자산이 없을 때
            document.getElementById("no-goal-message").style.display = "block"
            document.getElementById("goal-chart-container").style.display = "none"
        } else {
            // 목표 자산이 있을 때
            document.getElementById("no-goal-message").style.display = "none"
            document.getElementById("goal-chart-container").style.display = "block"

            const remaining = Math.max(targetAssets - totalAssets, 0)
            drawGoalChart(totalAssets, remaining)
        }
    } catch (error) {
        console.error("자산 정보를 불러오는 중 오류 발생:", error)
    }
})

// 개선된 자산목표 차트 - 반도넛 모양, 가운데 정렬, 퍼센트 표시
function drawGoalChart(achieved, remaining) {
    const canvas = document.getElementById("goalChart")
    if (!canvas) return

    const total = achieved + remaining
    const percentage = ((achieved / total) * 100).toFixed(1)

    // 기존 차트 인스턴스가 존재하면 제거
    if (window.goalChartInstance) {
        window.goalChartInstance.destroy()
    }

    // 중앙 텍스트 플러그인
    const centerTextPlugin = {
        id: "centerTextPlugin",
        beforeDraw(chart) {
            const { width, height, ctx } = chart
            ctx.restore()

            const centerX = width / 2
            const centerY = height / 2 + 20 // 반도넛이므로 중심을 약간 아래로

            const fontSize = Math.min(width, height) / 7
            ctx.font = `bold ${fontSize}px 'Segoe UI', sans-serif`
            ctx.textAlign = "center"
            ctx.textBaseline = "middle"

            const gradient = ctx.createLinearGradient(0, centerY - fontSize / 2, 0, centerY + fontSize / 2)
            gradient.addColorStop(0, "rgba(180, 83, 9, 1)")
            gradient.addColorStop(1, "rgba(180, 83, 9, 0.8)")

            ctx.fillStyle = gradient
            ctx.shadowColor = "rgba(0, 0, 0, 0.3)"
            ctx.shadowBlur = 10
            ctx.shadowOffsetY = 2

            ctx.fillText(`${percentage}%`, centerX, centerY + 10)

            ctx.font = `500 ${fontSize * 0.35}px 'Segoe UI', sans-serif`
            ctx.fillStyle = "rgba(180, 83, 9, 0.9)"
            ctx.shadowBlur = 5
            // ctx.fillText("달성률", centerX, centerY + fontSize * 0.3)

            ctx.restore()
        },
    }

    const ctx = canvas.getContext("2d")

    window.goalChartInstance = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["달성 자산", "남은 자산"],
            datasets: [
                {
                    data: [achieved, remaining],
                    backgroundColor: ["rgba(34, 197, 94, 0.9)", "rgba(255, 255, 255, 0.3)"],
                    borderColor: ["rgba(34, 197, 94, 1)", "rgba(255, 255, 255, 0.5)"],
                    borderWidth: 4,
                    hoverOffset: 15,
                    hoverBorderWidth: 6,
                    cutout: "70%",
                    circumference: 180, // 반원 (180도)
                    rotation: 270, // 시작 각도 조정
                },
            ],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: {
                padding: 20,
            },
            plugins: {
                legend: {
                    display: false,
                },
                tooltip: {
                    backgroundColor: "rgba(0, 0, 0, 0.9)",
                    titleColor: "white",
                    bodyColor: "white",
                    borderColor: "rgba(255, 255, 255, 0.3)",
                    borderWidth: 2,
                    cornerRadius: 12,
                    titleFont: { size: 14, weight: "bold" },
                    bodyFont: { size: 13 },
                    callbacks: {
                        label(context) {
                            const value = context.raw
                            const percent = ((value / total) * 100).toFixed(1)
                            return `${context.label}: ${value.toLocaleString()}원 (${percent}%)`
                        },
                    },
                },
            },
            animation: {
                animateRotate: true,
                animateScale: true,
                duration: 2000,
                easing: "easeOutCubic",
            },
        },
        plugins: [centerTextPlugin],
    })

    // 상세 정보 업데이트
    const goalDetailsElement = document.getElementById("goal-details")
    if (goalDetailsElement) {
        goalDetailsElement.innerHTML =
            // `현재 자산: <strong>${achieved.toLocaleString()}원</strong><br>` +
            // `목표 자산: <strong>${total.toLocaleString()}원</strong><br>` +
            // `남은 금액: <strong>${remaining.toLocaleString()}원</strong>`
            `<div className="goal-label"><span className="goal-badge">현재 자산</span><strong> ${achieved.toLocaleString()}원</strong></div>
        <div className="goal-label"><span className="goal-badge">목표 자산</span><strong> ${total.toLocaleString()}원</strong></div>
        <div className="goal-label"><span className="goal-badge">남은 금액</span><strong> ${remaining.toLocaleString()}원</strong></div>`
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const goalButton = document.getElementById("goal-button")
    const goalModal = document.getElementById("goal-modal")
    const closeModalBtn = document.getElementById("close-modal")
    const totalBalanceElement = document.querySelector(".totalBalance")
    const goalInput = document.getElementById("goalInput")
    const saveGoalBtn = document.getElementById("save-goal-btn")

    let totalAssets = 0 // 총자산 값을 저장할 변수
    const memberId = userData.memberId

    // 총자산을 서버에서 가져와서 표시하는 함수 (axios 버전)
    async function fetchTotalAssets() {
        try {
            const res = await axios.get(`/api/dashboard/total-assets/${memberId}`)
            totalAssets = res.data
            totalBalanceElement.textContent = totalAssets.toLocaleString("ko-KR") + "원"
        } catch (error) {
            console.error("총자산 불러오기 실패:", error)
            totalBalanceElement.textContent = "불러오기 실패"
        }
    }

    // 입력값 유효성 검사 및 메시지 반환 함수
    function validateGoalValue(inputStr) {
        if (!inputStr || inputStr.trim() === "") {
            return { valid: false, message: "목표 금액을 입력해주세요." }
        }

        // 콤마 제거 후 숫자 변환
        const value = Number(inputStr.replace(/,/g, ""))
        if (isNaN(value) || value <= 0) {
            return { valid: false, message: "목표 금액을 올바른 숫자로 입력해주세요." }
        }

        if (value < totalAssets) {
            return { valid: false, message: "⚠️ 목표금액이 총자산보다 작습니다." }
        }

        return { valid: true, value }
    }

    // 숫자만 입력되게 (음수, 문자 입력 방지)
    goalInput.addEventListener("input", function () {
        const value = this.value.replace(/[^0-9]/g, "")

        if (value === "") {
            this.value = ""
            return
        }

        this.value = Number(value).toLocaleString("ko-KR")
    })

    // 목표 금액 저장 버튼 클릭 이벤트
    saveGoalBtn.addEventListener("click", async () => {
        const inputStr = goalInput.value
        const validation = validateGoalValue(inputStr)

        if (!validation.valid) {
            alert(validation.message)
            return
        }

        try {
            const res = await axios.post("/api/dashboard/target-assets", {
                memberId: memberId,
                targetAssets: validation.value,
            })

            if (res.data === true) {
                alert("✅ 목표금액이 설정되었습니다.")
                goalModal.style.display = "none"
                location.reload()
            } else {
                alert("저장 실패 😢")
            }
        } catch (err) {
            alert("에러 발생: " + err)
        }
    })

    // 모달 열기 버튼
    goalButton.addEventListener("click", async () => {
        goalModal.style.display = "block"
        await fetchTotalAssets()
        goalInput.value = ""
    })

    // 닫기 버튼
    closeModalBtn.addEventListener("click", () => {
        goalModal.style.display = "none"
    })

    // 모달 외부 클릭 시 닫기
    window.addEventListener("click", (event) => {
        if (event.target === goalModal) {
            goalModal.style.display = "none"
        }
    })
})

// 대시보드 초기화 함수 (연도별 수입/지출 통합됨)
function initDashboard() {
    updateDate()

    displayMonthlyIncomeAndExpense()
        .then(({ income, expense }) => {
            drawIncomeSpendingChart(income, expense)
        })
        .catch((err) => {
            console.error("대시보드 초기화 중 오류:", err)
        })

    Promise.all([fetchTodayIncome(), fetchTodayExpense()])
        .then(([incomeList, expenseList]) => {
            renderTodayRecords(incomeList, expenseList)
        })
        .catch((error) => {
            console.error("오늘의 수입/지출 렌더링 중 오류:", error)
        })

    fetchAndDrawTop3Category()

    // 연도별 수입/소비 그래프 데이터 로드 추가
    const requestData = {
        memberId: userData.memberId,
        yearDate: userData.yearDate,
    }

    Promise.all([
        axios.post("/api/dashboard/year-incomes", requestData),
        axios.post("/api/dashboard/year-expenses", requestData),
    ])
        .then(([incomeRes, expenseRes]) => {
            const incomeData = Array(12).fill(0)
            const expenseData = Array(12).fill(0)

            incomeRes.data.forEach((item) => {
                const monthIndex = Number.parseInt(item.accountTradeMonth) - 1
                incomeData[monthIndex] = item.accountTradeMoney
            })

            expenseRes.data.forEach((item) => {
                const monthIndex = Number.parseInt(item.cardTradeMonth) - 1
                expenseData[monthIndex] = item.cardTradeMoney
            })

            drawMonthlyChart(incomeData, expenseData)
        })
        .catch((err) => {
            console.error("연도별 수입/지출 데이터 요청 오류:", err)
        })
}

// DOMContentLoaded 시점에 초기화 실행
document.addEventListener("DOMContentLoaded", () => {
    initDashboard()
    updateDate()
    updateTopCategoryMonth()
    updateYearTitle()
})
