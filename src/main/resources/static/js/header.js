document.addEventListener("DOMContentLoaded", function() {
    const headerAuth = document.getElementById("headerAuthContainer");

    const token = localStorage.getItem("token");
    const email = localStorage.getItem("email");

    if (token) {
        headerAuth.innerHTML = `
            <div class="dropdown">
                <a class="btn btn-dark rounded-pill text-white d-flex align-items-center justify-content-center"
                   href="#" role="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                   ${email}
                   <i class="bi bi-person-fill px-2"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                    <li><a class="dropdown-item" href="/panel-de-control">Ir al Panel de Control</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#" id="logoutBtn">Cerrar Sesión</a></li>
                </ul>
            </div>
        `;

        document.getElementById("logoutBtn").addEventListener("click", function(e) {
            e.preventDefault();
            localStorage.removeItem("token");
            localStorage.removeItem("email");
            window.location.href = "/public/iniciar-sesion";
        });
    }
});
