function groupByMonth(transactions) {
  const months = Array(12).fill(0).map(() => ({ income: 0, expense: 0 }));

  transactions.forEach(tx => {
    const date = new Date(tx.date);
    const month = date.getMonth();

    if (tx.isIncome) {
      months[month].income += tx.amount;
    } else {
      months[month].expense += tx.amount;
    }
  });
  return months;
}

function groupByMonthTransactions(transactions) {
  const months = Array(12).fill().map(() => ({ income: [], expense: [] }));

  transactions.forEach(tx => {
    const date = new Date(tx.date);
    const now = new Date();
    const month = date.getMonth();
    const currentYear = now.getFullYear();

    if (tx.isIncome && date.getFullYear() === currentYear) {
      months[month].income.push(tx);
    } else {
      months[month].expense.push(tx);
    }
  });

  return months;
}

function groupLast6Months(transactions) {
  const now = new Date();

  // Crear lista de últimos 6 meses
  const last6Months = [];
  for (let i = 5; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    last6Months.push({ year: d.getFullYear(), month: d.getMonth(), income: 0, expense: 0 });
  }

  // Llenar ingresos y gastos del periodo
  transactions.forEach(tx => {
    const d = new Date(tx.date);
    last6Months.forEach(m => {
      if (d.getFullYear() === m.year && d.getMonth() === m.month) {
        if (tx.isIncome) m.income += tx.amount;
        else m.expense += tx.amount;
      }
    });
  });

  return last6Months;
}

function groupByCategoryCurrentMonth(transactions) {
  const now = new Date();
  const currentMonth = now.getMonth();
  const currentYear = now.getFullYear();

  const categories = {};

  transactions.forEach(tx => {
    const date = new Date(tx.date);

    if (date.getMonth() === currentMonth && date.getFullYear() === currentYear) {
      const cat = tx.category || "Sin categoría";
      if (!categories[cat]) categories[cat] = 0;
      categories[cat] += tx.amount;
    }
  });

  return categories;
}

function groupByCategoryOfMonth(transactions, year, month) {
  const categories = {};

  transactions.forEach(tx => {
    const d = new Date(tx.date);
    if (d.getFullYear() === year && d.getMonth() === month) {
      const cat = tx.category || "Otro";
      if (!categories[cat]) categories[cat] = 0;
      categories[cat] += tx.amount;
    }
  });

  return categories;
}

//-------Calcular Totales
function calculateTotals(transacciones) {
  const totalIncomes = transacciones.income.reduce((sum, tx) => sum + tx.amount, 0);
  const totalExpenses = transacciones.expense.reduce((sum, tx) => sum + tx.amount, 0);
  return [ totalIncomes, totalExpenses ];
}

//-------Calcula el porcentaje diferencial
function calculateDifferential(preIncome, income, preExpense, expense){
    if (preIncome === 0 || preExpense === 0) {
        return income === 0 ? 0 : Infinity;
    }
    const differentialIncomes = preIncome !==0 ? (((income - preIncome)/ preIncome)*100).toFixed(2) : 0;
    const differentialExpense = preExpense !==0 ? (((expense - preExpense)/ preExpense)*100).toFixed(2) : 0;
    return [ differentialIncomes, differentialExpense ];
}

// ------formatearFecha
function formatDate(timestamp) {
  const date = new Date(timestamp);
  return date.toLocaleDateString("es-ES", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

// ------FormatearDouble
function formatMoney(value) {
  return Number(value)
    .toFixed(2)                // 2 decimales
    .replace(',', '.')         // punto → coma en decimales
    .replace(/\B(?=(\d{3})+(?!\d))/g, ','); // separador de miles
}

// ------Headers para solicitudes
function getHeaders() {
  return {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: localStorage.token,
  };
}