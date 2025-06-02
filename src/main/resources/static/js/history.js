document.addEventListener("DOMContentLoaded", function () {
    // 오늘 날짜 표시
    const today = new Date();
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const formatted = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}/${days[today.getDay()]}`;
    document.getElementById("today-date").textContent = formatted;
    document.getElementById("incomeDate").valueAsDate = new Date();
    document.getElementById("expenseDate").valueAsDate = new Date();


    // 변수 선언
    let selectedType = null;
    let selectedDate = null;
    let selectedCategory = null;
    let selectedCard = null;


    const selection = document.getElementById("selection");
    const incomeTable = document.getElementById("income-table");
    const expenseTable = document.getElementById("expense-table");

    const categoryBtn = document.getElementById("toggleCategory");
    const categoryList = document.getElementById("categoryList");

    const cardBtn = document.getElementById("cardChoice");
    const cardList = document.getElementById("cardList");

    const incomeBtn = document.getElementById("incomeBtn");
    const expenseBtn = document.getElementById("expenseBtn");

    const toggleBtn = document.getElementById("toggleBtn");
    const yearList = document.getElementById("yearList");
    const monthList = document.getElementById("monthList");
    const currentYear = new Date().getFullYear();
    const cardNum = document.getElementById("expenseCard").value;
    // 선택값 초기화 함수
    function initializeSelection() {
        const selected = selection.value;
        selectedType = selected;

        // 숨기기
        yearList.style.display = "none";
        monthList.style.display = "none";
        categoryList.style.display = "none";
        cardList.style.display = "none";

        if (selected === "income") {
            incomeTable.style.display = "table";
            expenseTable.style.display = "none";
            categoryBtn.style.display = "none";
            cardBtn.style.display = "none";
            incomeBtn.style.display = "inline-block";
            expenseBtn.style.display = "none";

        } else if (selected === "expense") {
            incomeTable.style.display = "none";
            expenseTable.style.display = "table";
            categoryBtn.style.display = "inline-block";
            cardBtn.style.display = "inline-block";
            incomeBtn.style.display = "none";
            expenseBtn.style.display = "inline-block";

        } else {
            incomeTable.style.display = "none";
            expenseTable.style.display = "none";
            categoryBtn.style.display = "none";
            cardBtn.style.display = "none";
            incomeBtn.style.display = "none";
            expenseBtn.style.display = "none";
        }

        fetchFilteredResults();
    }

    // 날짜 선택 (연도 → 월)
    toggleBtn.onclick = () => {
        yearList.style.display = "block";
        monthList.style.display = "none";
    };

    for (let i = 0; i < 5; i++) {
        const year = currentYear - i;
        const btn = document.createElement("button");
        btn.textContent = year;

        btn.onclick = () => {
            selectedDate = `${year}`;
            yearList.style.display = "none";
            renderMonths(year);
            monthList.style.display = "block";
        };

        yearList.appendChild(btn);
    }

    function renderMonths(year) {
        monthList.innerHTML = "";
        for (let i = 1; i <= 12; i++) {
            const btn = document.createElement("button");
            btn.textContent = `${i}월`;

            btn.onclick = () => {
                monthList.style.display = "none";
                selectedDate = `${year}-${String(i).padStart(2, '0')}`;
                fetchFilteredResults();
            };

            monthList.appendChild(btn);
        }
    }

    function loadCategoryFromDB() {
        categoryList.innerHTML = "";

        axios.get("/api/category")
            .then(response => {
                const categories = response.data;
                if (!categories || categories.length === 0) {
                    categoryList.innerHTML = "<p>카테고리가 없습니다.</p>";
                    return;
                }

                categories.forEach(category => {
                    const btn = document.createElement("button");
                    btn.textContent = category.category_name; // 또는 category.categoryName
                    btn.onclick = () => {
                        selectedCategory = category.category_name;
                        categoryList.style.display = "none";
                        fetchFilteredResults();
                    };
                    categoryList.appendChild(btn);
                });

                categoryList.style.display = "block";
                adjustScrollBoxHeight("categoryList");
            })
            .catch(error => {
                console.error("카테고리 불러오기 실패", error);
            });
    }

// 클릭 이벤트에서 함수 호출만
    categoryBtn.addEventListener("click", () => {
        if (categoryList.style.display === "block") {
            categoryList.style.display = "none";
            return;
        }
        cardList.style.display = "none";
        loadCategoryFromDB(); // ✅ 호출만
    });
    function loadCardsFromDB() {
        cardList.innerHTML = ""; // 초기화

        axios.get(`/api/card`)
            .then(response => {
                const cards = response.data;
                if (!cards || cards.length === 0) {
                    cardList.innerHTML = "<p>등록된 카드가 없습니다.</p>";
                    return;
                }

                cards.forEach(card => {
                    const btn = document.createElement("button");
                    btn.textContent = card.cardName;   // 카드 이름 표시
                    btn.onclick = () => {
                        selectedCard = card.cardName;  // 필터 조건으로 사용
                        cardList.style.display = "none";
                        fetchFilteredResults();
                    };
                    cardList.appendChild(btn);
                });

                cardList.style.display = "block";
                adjustScrollBoxHeight("cardList");
            })
            .catch(error => {
                console.error("카드 불러오기 실패", error);
            });
    }
    // 카드 버튼 이벤트
    cardBtn.addEventListener("click", () => {
        if (cardList.style.display === "none" || cardList.style.display === "") {
            cardList.style.display = "block";
            categoryList.style.display = "none";
            loadCardsFromDB();  // ✅ 하드코딩 대신 DB 호출
        } else {
            cardList.style.display = "none";
        }
    });
    function fetchFilteredResults() {
        console.log("필터링 호출됨:", selectedType, selectedDate, selectedCategory, selectedCard);
        console.log("🟢 선택된 값 확인", {
            selectedType,
            selectedDate,
            selectedCategory,
            selectedCard
        });

        const params = {};

        if (selectedDate) {
            params.date = selectedDate;
        }
        if (selectedCategory) {
            params.category = selectedCategory;
        }
        if (selectedCard) {
            params.card = selectedCard;
        }

        if (selectedType === "income") {
            axios.get(`/api/incomeHistory`, {params})
                .then(response => {
                    console.log("🟢 수신된 응답:", response.data);
                    const incomeBody = document.getElementById("income-body");
                    incomeBody.innerHTML = "";
                    response.data.forEach(item => {
                        const row = document.createElement("tr");
                        row.innerHTML = `
                        <td>${item.accountDay || "-"}</td>
                        <td>${item.bankName || "-"}</td>
                        <td>${item.source || "-"}</td>
                        <td>${item.accountMoney != null && !isNaN(item.accountMoney) ? item.accountMoney.toLocaleString() : "-"}</td>
                    `;
                        incomeBody.appendChild(row);
                    });
                })
                .catch(error => {
                    console.error("수입 내역 불러오기 실패", error);
                });

        } else if (selectedType === "expense") {
            axios.get(`/api/expenseHistory`, {params})
                .then(response => {
                    const expenseBody = document.getElementById("expense-body");
                    expenseBody.innerHTML = "";
                    response.data.forEach(item => {
                        if (!item) return;
                        const row = document.createElement("tr");
                        row.innerHTML = `
                        <td>${item.cardDay || "-"}</td>
                        <td>${item.categoryName || "-"}</td>
                        <td>${item.cardStoreName || "-"}</td>
                        <td>${item.cardMoney != null ? item.cardMoney.toLocaleString() : "-"}</td>
                        <td><button class="detail-btn" onclick="detailModal(${item.cardTradeId})">상세보기</button></td>
                    `;
                        expenseBody.appendChild(row);
                    });
                })
                .catch(error => {
                    console.error("소비 내역 불러오기 실패", error);
                });
        }
    }

    // 가장 바깥에서 선언 (DOMContentLoaded 바깥 또는 window에 직접 등록)
    window.detailModal = function(cardTradeId) {
        const modal = document.getElementById("detailModal");
        if (!modal) {
            console.error("❌ detailModal 요소가 존재하지 않음");
            return;
        }

        axios.get(`/api/cardDetail/${cardTradeId}`)
            .then(response => {
                const data = response.data;
                console.log("🟢 상세 응답:", response.data);
                modal.style.display = "flex";

                document.getElementById("detail-card-name").value = data.cardName || "-";
                document.getElementById("detail-card-type").value = data.cardType || "-";
                document.getElementById("detail-card-number").value = data.cardNum || "-";
                document.getElementById("detail-date").value = data.cardDay || "-";
                document.getElementById("detail-cancel-date").value = data.cancel_day || "-";
                document.getElementById("detail-amount").value = data.cardMoney != null ? data.cardMoney.toLocaleString() : "-";
                document.getElementById("detail-store").value = data.cardStoreName || "-";
                document.getElementById("detail-category").value = data.categoryName || "-";
                document.getElementById("detail-biznum").value = data.businessNum || "-";
            })
            .catch(error => {
                console.error("상세 내역 불러오기 실패", error);
            });
    };


    function loadAccounts() {
        axios.get("/api/account")  // ❌ params 없이 호출
            .then(response => {
                const select = document.getElementById("bankAccountSelect");
                select.innerHTML = "";
                response.data.forEach(account => {
                    const option = document.createElement("option");
                    option.value = account.bankAccountNum;
                    option.textContent = account.bankAccountName;
                    select.appendChild(option);
                });
            })
            .catch(error => {
                console.error("계좌 목록 불러오기 실패", error);
            });

    }


    document.getElementById("saveIncomeBtn").addEventListener("click", () => {
        const dto = {
            accountDay: document.getElementById("incomeDate").value,
            source: document.getElementById("incomeSource").value,
            accountMoney: parseInt(document.getElementById("incomeAmount").value),
            accountType: '입금',
            bankAccountNum: document.getElementById("bankAccountSelect").value  // <- 계좌번호
        };

        axios.post("/api/income", dto)
            .then(res => {
                alert("입금 완료!");
                document.getElementById("incomeModal").style.display = "none";
                fetchFilteredResults();
            })
            .catch(err => {
                console.error("입금 실패", err);
                alert("입금에 실패했습니다.");
            });
    });
    function generateRandomBusinessNum() {
        const part1 = String(Math.floor(100 + Math.random() * 900)); // 100~999
        const part2 = String(Math.floor(10 + Math.random() * 90));   // 10~99
        const part3 = String(Math.floor(10000 + Math.random() * 90000)); // 10000~99999
        return `${part1}-${part2}-${part3}`;
    }
    function loadCards() {
        // 로그인된 사용자 ID
        axios.get(`/api/card`)
            .then(response => {
                const select = document.getElementById("expenseCard");
                select.innerHTML = ""; // 초기화

                response.data.forEach(card => {
                    const option = document.createElement("option");
                    option.value = card.cardNum;             // 실제 서버에 넘길 값
                    option.textContent = card.cardName;      // 사용자에게 보이는 값
                    select.appendChild(option);
                });
            })
            .catch(error => {
                console.error("카드 목록 불러오기 실패", error);
            });
    }
    expenseBtn.addEventListener("click", () => {
        loadCards();
        loadCateforyModelList();// 카드 목록 로드
        document.getElementById("expenseModal").style.display = "flex";
    });
    window.saveExpense = function () {
        const dto = {
            cardDay: document.getElementById("expenseDate").value,
            cardNum: document.getElementById("expenseCard").value,
            cardStoreName: document.getElementById("expenseStore").value,
            cardMoney: parseInt(document.getElementById("expenseAmount").value),
            businessNum: generateRandomBusinessNum(),
            cardType: "승인",
            categoryName : document.getElementById("categorySelect").value
        };

        axios.post("/api/expense", dto)
            .then(res => {
                alert("결제 완료!");
                closeModal("expenseModal");

                // ✅ 필터 초기화 후 목록 다시 조회
                const today = new Date();
                const year = today.getFullYear();
                const month = String(today.getMonth() + 1).padStart(2, '0');
                selectedDate = `${year}-${month}`;
                selectedCategory = null;
                selectedCard = null;

                document.getElementById("selection").value = "expense";
                initializeSelection();
            })
            .catch(err => {
                console.error("결제 실패", err);
                alert("결제에 실패했습니다.");
            });
    };
    function loadCateforyModelList(){
        axios.get("/api/category")
            .then(response => {
                const select = document.getElementById("categorySelect");
                select.innerHTML = '<option value = "">카테고리 선택</option>'
                response.data.forEach(category => {
                    const option = document.createElement("option");
                    option.value = category.category_name;
                    option.textContent = category.category_name;
                    select.appendChild(option);
                });
            })
            .catch(error => {
                console.error("카테고리 목록 로딩 실패", error);
            });
    }

    // 스크롤 높이 자동 조절
    function adjustScrollBoxHeight(boxId) {
        const box = document.getElementById(boxId);
        const buttons = box.querySelectorAll("button");
        if (buttons.length === 0) {
            box.style.height = "0px";
            return;
        }

        const totalHeight = [...buttons].reduce((sum, btn) => {
            const style = window.getComputedStyle(btn);
            const margin = parseInt(style.marginBottom) || 0;
            return sum + btn.offsetHeight + margin;
        }, 0);

        box.style.height = totalHeight + "px";
    }

    // 테이블 변경 감지
    selection.addEventListener("change", function () {
        initializeSelection();
    });

    // 입금/결제 모달
    incomeBtn.addEventListener("click", () => {
        loadAccounts();
        document.getElementById("incomeModal").style.display = "flex";
    });

    expenseBtn.addEventListener("click", () => {
        document.getElementById("expenseModal").style.display = "flex";
    });


    window.closeModal = function (modalId) {
        document.getElementById(modalId).style.display = "none";
    };


    initializeSelection();
});
