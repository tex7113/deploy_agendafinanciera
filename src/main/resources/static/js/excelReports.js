document.getElementById("btnGenerateMonthly").addEventListener("click", async () => {
    const month = document.getElementById("monthSelect").value;
    const year = document.getElementById("yearSelectMonthly").value;

    // Mostrar el spinner de carga
    document.getElementById("loadingSpinner").classList.remove("d-none");

    try {
        const response = await fetch(`/api/v1/transactions/month/report?year=${year}&month=${month}`, {
            method: 'GET',
            headers: getHeaders(),
        });

        if (!response.ok) throw new Error("Error al generar el reporte");

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `reporte_mensual_${year}_${month}.xlsx`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);

    } catch (err) {
        console.error(err);
        alert("No se pudo generar el reporte.");
    } finally {
        // Ocultar el spinner de carga después de completar el proceso
        document.getElementById("loadingSpinner").classList.add("d-none");
    }
});

document.getElementById("btnGenerateYearly").addEventListener("click", async () => {
    const year = document.getElementById("yearSelectYearly").value;

    // Mostrar el spinner de carga
    document.getElementById("loadingSpinnerYearly").classList.remove("d-none");

    try {
        const response = await fetch(`/api/v1/transactions/year/report?year=${year}`, {
            method: 'GET',
            headers: getHeaders(),
        });

        if (!response.ok) throw new Error("Error al generar el reporte");

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `reporte_anual_${year}.xlsx`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);

    } catch (err) {
        console.error(err);
        alert("No se pudo generar el reporte.");
    } finally {
        // Ocultar el spinner de carga después de completar el proceso
        document.getElementById("loadingSpinnerYearly").classList.add("d-none");
    }
});