$(document).ready(function() {
    loadUser();
    loadTransactions();
    printChart();
});

let allTransactions = [];
let transactionsList = [];
let donutChart = null;
let currentPage = 1;
const pageSize = 8;

async function loadUser() {
  try {
    const request = await fetch("/api/v1/user", {
      method: "GET",
      headers: getHeaders(),
    });

    const user = await request.json();

    $("#txUserName").text(user.name);
    $("#txAvailableAmount").text(formatMoney(user.availableAmount));
    $("#txSaves").text(formatMoney(user.savings));
  } catch (error) {
    console.error("Error al cargar usuario:", error);
    alert("Tu sesion expiro vuelve a iniciar sesion");
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    window.location.href = "/public/iniciar-sesion";
  }
}

async function loadTransactions() {
  try {
    const request = await fetch("/api/v1/transactions", {
      method: "GET",
      headers: getHeaders(),
    });

    allTransactions = await request.json();
    transactionsList = allTransactions;
    printChart();
    renderTransactionList();
    const months = groupByMonthTransactions(allTransactions);
    const dateNow = new Date().getMonth();
    const currentMonth = months[dateNow];
    const previous = currentMonth === 0 ? 11 : dateNow - 1;
    const prevMonth = months[previous];

    const [totalIncomes, totalExpenses] = calculateTotals(currentMonth);
    const [preTotalIncome, preTotalExpense] = calculateTotals(prevMonth);

    const [ incomeDiff, expenseDiff ] = calculateDifferential(preTotalIncome, totalIncomes, preTotalExpense, totalExpenses);

    $("#txTotalIncomes").text(totalIncomes);
    $("#totalExpenses").text(totalExpenses);
    $("#incomeDifferential").text(incomeDiff + "% vs mes anterior");
    $("#expenseDifferential").text(expenseDiff + "% vs mes anterior");

  } catch (error) {
    console.error("Error al cargar transacciones:", error);
  }
}

function renderTransactionList() {
  const container = $("#transactionsList");
  container.empty();

  if (transactionsList.length === 0) {
    container.html(`
      <div class="alert alert-secondary">Aún no hay registros de datos.</div>
    `);
    $("#pagination").html(""); // borra la paginación
    return;
  }

  // 1. Calcular datos de la página
  const start = (currentPage - 1) * pageSize;
  const end = start + pageSize;
  const pageItems = transactionsList.slice(start, end);

  // 2. Dibujar lista
  const html = pageItems.map(tx => createTransactionHtml(tx)).join("");
  container.html(html);

  // 3. Dibujar paginación
  renderPagination(transactionsList.length);
}

function renderPagination(totalItems) {
  const totalPages = Math.ceil(totalItems / pageSize);
  const pag = $("#pagination");

  // Si solo hay una página, no muestres nada.
  if (totalPages <= 1) {
    pag.html("");
    return;
  }

  let html = `
    <nav>
      <ul class="pagination">
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
          <a class="page-link" href="#" onclick="changePage(${currentPage - 1})">Anterior</a>
        </li>
  `;

  for (let i = 1; i <= totalPages; i++) {
    html += `
      <li class="page-item ${i === currentPage ? "active" : ""}">
        <a class="page-link" href="#" onclick="changePage(${i})">${i}</a>
      </li>
    `;
  }

  html += `
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
          <a class="page-link" href="#" onclick="changePage(${currentPage + 1})">Siguiente</a>
        </li>
      </ul>
    </nav>
  `;

  pag.html(html);
}

function createTransactionHtml(tx) {
  const icon = tx.isIncome
    ? `<i class="bi bi-arrow-up-circle-fill text-success fs-4 me-2"></i>`
    : `<i class="bi bi-arrow-down-circle-fill text-danger fs-4 me-2"></i>`;

  const amountClass = tx.isIncome ? "text-success" : "text-danger";

  return `
    <div class="card mb-2 border-0 shadow-sm rounded-4">
      <div class="card-body d-flex justify-content-between align-items-center">
        <div class="d-flex">
          ${icon}
          <div>
            <b>${tx.category}</b> — ${tx.description}<br>
            <small class="text-secondary">${formatDate(tx.date)}</small>
          </div>
        </div>
        <span class="${amountClass} fw-bold fs-5">$${formatMoney(tx.amount)}</span>
      </div>
    </div>
  `;
}

function printChart() {
  if (!allTransactions || allTransactions.length === 0) return;

  // === LINE CHART: últimos 6 meses ===
  const monthlyData = groupLast6Months(allTransactions);

  const labels = monthlyData.map(m =>
    new Date(m.year, m.month).toLocaleString("es-ES", { month: "short" })
  );

  new Chart(document.getElementById("monthsChart"), {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Ingresos",
          data: monthlyData.map(m => m.income),
          borderColor: "#28a745",
          backgroundColor: "rgba(40,167,69,0.2)",
          borderWidth: 2,
          tension: 0.3
        },
        {
          label: "Gastos",
          data: monthlyData.map(m => m.expense),
          borderColor: "#dc3545",
          backgroundColor: "rgba(220,53,69,0.2)",
          borderWidth: 2,
          tension: 0.3
        }
      ]
    }
  });

  printDonutChart();
}
function printDonutChart() {
  const now = new Date();
  const categoryData = groupByCategoryOfMonth(allTransactions.filter(transaction => transaction.isIncome === false), now.getFullYear(), now.getMonth());

  donutChart = new Chart(document.getElementById("categoryChart"), {
    type: "doughnut",
    data: {
      labels: Object.keys(categoryData),
      datasets: [{
        label: "Mes actual",
        data: Object.values(categoryData),
        backgroundColor: Object.keys(categoryData).map(cat => categoryStyles.gasto[cat]?.color ?? "#6c757d")
      }]
    },
    options: {
      responsive: true
    }
  });
}

document.getElementById("btnPrevMonth").addEventListener("click", () => {
  if (!donutChart) return;

  const now = new Date();
  const prev = new Date(now.getFullYear(), now.getMonth() - 1, 1);

  const prevData = groupByCategoryOfMonth(allTransactions.filter(transaction => transaction.isIncome === false), prev.getFullYear(), prev.getMonth());

  if (Object.keys(prevData).length === 0) {
    alert("No hay datos del mes anterior");
    return;
  }

  donutChart.data.datasets.push({
    label: "Mes anterior",
    data: Object.values(prevData),
    backgroundColor: Object.keys(prevData).map(cat => categoryStyles.gasto[cat]?.color ?? "#6c757d"),
    borderWidth: 1,
    borderColor: "#fff"
  });

  donutChart.update();
});

document.getElementById("btnRemovePrev").addEventListener("click", () => {
  if (!donutChart) return;

  // Dejamos solo el dataset del mes actual (índice 0)
  donutChart.data.datasets = donutChart.data.datasets.slice(0, 1);

  donutChart.update();
});

function changePage(page) {
  currentPage = page;
  renderTransactionList();
}

function incomesTransactionsList() {
  transactionsList = allTransactions.filter(t => t.isIncome);
  currentPage = 1;
  renderTransactionList();
}

function expensesTransactionsList() {
  transactionsList = allTransactions.filter(t => !t.isIncome);
  currentPage = 1;
  renderTransactionList();
}

function allTransactionsList() {
  transactionsList = allTransactions;
  currentPage = 1;
  renderTransactionList();
}