async function iniciarSesion() {
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const messageDiv = document.getElementById('loginMessage');
    const loginButton = document.getElementById('loginButton');
    const loginSpinner = document.getElementById('loginSpinner');
    const loginButtonText = document.getElementById('loginButtonText');

    messageDiv.textContent = '';

    if (!email || !password) {
        messageDiv.textContent = "Por favor completa todos los campos.";
        return;
    }

    loginSpinner.classList.remove('d-none');
    loginButtonText.textContent = "Iniciando...";

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        loginSpinner.classList.add('d-none');
        loginButtonText.textContent = "Iniciar Sesión";

        if (response.status === 401) {
            // Usuario/contraseña incorrectos
            const text = await response.text();
            messageDiv.textContent = text;
            return;
        }

        if (response.status === 500) {
            messageDiv.textContent = "Ocurrió un error inesperado. Intenta más tarde.";
            return;
        }

        if (!response.ok) {
            messageDiv.textContent = "Error desconocido. Intenta nuevamente.";
            return;
        }

        // Login exitoso
        const data = await response.json();

        if (data.token) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('email', email);
            window.location.href = '/panel-de-control';
        } else {
            messageDiv.textContent = "No se recibió token del servidor.";
        }
    } catch (error) {
        console.error("Error al iniciar sesión:", error);
        loginSpinner.classList.add('d-none');
        loginButtonText.textContent = "Iniciar Sesión";
        messageDiv.textContent = "Ocurrió un error de conexión.";
    }
}
