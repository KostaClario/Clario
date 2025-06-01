
    document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const formatted = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}/${days[today.getDay()]}`;
    document.getElementById("today-date").textContent = formatted;

    //버튼 1개만 유지
    function setExclusiveActive(button, containerId) {
    const container = document.getElementById(containerId);
    const buttons = container.querySelectorAll("button");
    buttons.forEach(btn => btn.classList.remove("active"));
    button.classList.add("active");
}
    //변수 초기화
    let selectedType = null;
    let selectedDate = null;
    let selectedCategory = null;
    let selectedCard = null;

//변수 선언
    const selection = document.getElementById("selection");
    const incomeTable = document.getElementById("income-table");
    const expenseTable = document.getElementById("expense-table");
    const categoryBtn = document.getElementById("toggleCategory");
    const cardBtn = document.getElementById("cardChoice");
    const incomeBtn = document.getElementById("incomeBtn");
    const expenseBtn = document.getElementById("expenseBtn");
    const toggleBtn = document.getElementById("toggleBtn");
    const yearList = document.getElementById("yearList");
    const monthList = document.getElementById("monthList");
    const currentYear = new Date().getFullYear();

    //소비 선택시 액션
    function initializeSelection() {
    const selected = selection.value;
    selectedType = selected;
    //스크롤 초기화
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
    //날짜 선택
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
    fetchFilteredResults()
};
    monthList.appendChild(btn);
}
}

    function fetchFilteredResults() {
    console.log("필터링 함수 호출됨", selectedType, selectedDate);
}
    initializeSelection();

    // 카테고리 선택
    document.getElementById("toggleCategory").addEventListener("click", () => {
    if(categoryList.style.display === "none" || categoryList.style.display === ""){
    categoryList.style.display = "block";
    cardList.style.display = "none";
    loadHardcodedCategories();
    // loadCategoriesFromDB();
}else{
    categoryList.style.display = "none";
}
});

//카테고리 선택 하드코딩
    function loadHardcodedCategories(){
    categoryList.innerHTML = "";
    const expenseCategories = ["식비","교통비","여가비","고정비","유흥비","건강비","생활비","기타"];
    expenseCategories.forEach(name => {
    const btn = document.createElement("button");
    btn.textContent = name;
    btn.onclick = () => {
    selectedCategory = name;
    categoryList.style.display = "none";
    fetchFilteredResults();
};
    categoryList.appendChild(btn);
});
    adjustScrollBoxHeight("categoryList");
}
    //카테고리 db연결
    /*function  loadCategoriesDB(){
        fetch()
         .then(res => res.json())
         .then(date => {
         categoryList.innerHTML = "";
         data.forEach(name => {
         const btn =document. createElement("button");
         btn.textContent = name;
           btn.onclick = () => {
             selectedCategory = name;
             categoryList.style.display = "none";
             fetchFilteredResults();
         }
          categoryList.appendChild(btn);
         });
          adjustScrollBoxHeight("categoryList");
         })
        .catch(err => console.error("카테고리 로딩 실패", err));

    } */
    //카드 선택
    document.getElementById("cardChoice").addEventListener("click",() => {
    if(cardList.style.display === "none" || cardList.style.display === ""){
    cardList.style.display = "block";
    categoryList.style.display = "none";
    loadHardcodedCards();
    //loadCardsDB()
}else{
    cardList.style.display ="none";
}
})

    //카드 선택 하드코딩
    function  loadHardcodedCards(){
    cardList.innerHTML = "";
    const cards = ["현대카드", "신한카드", "삼성카드"];
    cards.forEach(card => {
    const btn =document.createElement("button");
    btn.textContent = card;
    btn.onclick = () => {
    selectedCard = card;
    cardList.style.display = "none";
    fetchFilteredResults();
};
    cardList.appendChild(btn);
});
    adjustScrollBoxHeight("cardList");
}
    //카드 선택 db 가져오기
    /*function loadCardsDB(){
      fetch()
       .then(res => res.json())
       .then(data => {
       cardList.innerHTML = "";
       data.forEach(card => {
       const btn = document.createElement("button");
       btn.textContent = card;
       btn.onclick = () => {
         selectedCard = card;
         cardList.style.display = "none";
         fetchFilteredResults();
       };
       cardList.appendChild(btn);
        });
         adjustScrollBoxHeight("categoryList");
      })
     .catch(err => console.error("카드 로딩 실패",err));
    }*/
    //테이블 액션
    selection.addEventListener("change", function () {
    initializeSelection(); // 테이블 표시 등은 initializeSelection 내부에서만 처리
});


    //스크롤 창 자동 높이 설정
    function adjustScrollBoxHeight(boxId) {
    const box = document.getElementById(boxId);
    const buttons = box.querySelectorAll("button");
    if (buttons.length === 0) {
    box.style.height = "0px";
    return;
}

    // 각 버튼 높이 계산
    const totalHeight = [...buttons].reduce((sum, btn) => {
    const style = window.getComputedStyle(btn);
    const margin = parseInt(style.marginBottom) || 0;
    return sum + btn.offsetHeight + margin;
}, 0);

    box.style.height = totalHeight + "px";
}
    //입금 DB에 저장
    /*  function saveIncome() {
        const data = {
          date: document.getElementById("incomeDate").value,
          account: document.getElementById("incomeAccount").value,
          source: document.getElementById("incomeSource").value,
          amount: document.getElementById("incomeAmount").value
        };

        fetch("/api/income", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data)
        }).then(res => {
          if (res.ok) {
            alert("입금 저장 완료");
            closeModal("incomeModal");
            fetchFilteredResults();
          } else {
            alert("저장 실패");
          }
        });
      }*/

    //결제 DB에 저장
    /*function saveExpense() {
      const data = {
        date: document.getElementById("expenseDate").value,
        card: document.getElementById("expenseCard").value,
        store: document.getElementById("expenseStore").value,
        amount: document.getElementById("expenseAmount").value
      };

      fetch("/api/expense", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      }).then(res => {
        if (res.ok) {
          alert("결제 저장 완료");
          closeModal("expenseModal");
          fetchFilteredResults();
        } else {
          alert("저장 실패");
        }
      });
    }*/
    /**
     fetch(`/api/detail?id=${id}')
     .then(respose => {
     if(!respose.ok) throw new Error("오류");
     return respose.json();
     })
     .then(data => {
     document.getElementById("detail-card-name").value = data.cardName || "";
     document.getElementById("detail-card-type").value = data.cardType || "";
     document.getElementById("detail-card-number").value = data.cardNumber || "";
     document.getElementById("detail-date").value = data.date || "";
     document.getElementById("detail-cancel-date").value = data.cancelDate || "";
     document.getElementById("detail-amount").value = data.amount || "";
     document.getElementById("detail-store").value = data.store || "";
     document.getElementById("detail-category").value = data.category || "";
     document.getElementById("detail-biznum").value = data.biznum || "";
     })
     .catch(error => {
     console.error("조회 실패", error);
     alert("서버 조회 실패");
     })**/
});

    //상세보기 모달
    function detailModal(id){
    document.getElementById("detailModal").style.display = "flex";

    document.getElementById("detail-card-name").value = "국민카드";
    document.getElementById("detail-card-type").value = "신용카드";
    document.getElementById("detail-card-number").value = "5555-****-4444-4444";
    document.getElementById("detail-date").value = "2025-05-24";
    document.getElementById("detail-cancel-date").value = "";
    document.getElementById("detail-amount").value = "3,000";
    document.getElementById("detail-store").value = "빽다방";
    document.getElementById("detail-category").value = "식비";
    document.getElementById("detail-biznum").value = "123-45-67890"

}
    function closeModal(modalId){
    document.getElementById(modalId).style.display = "none";
}
    //입금, 결제 모달 버튼 동작
    document.getElementById("incomeBtn").addEventListener("click",() =>{
    document.getElementById("incomeModal").style.display = "flex";
});

    document.getElementById("expenseBtn").addEventListener("click",() =>{
    document.getElementById("expenseModal").style.display = "flex";
});
    function saveIncome() {
}

    function saveExpense() {
}
