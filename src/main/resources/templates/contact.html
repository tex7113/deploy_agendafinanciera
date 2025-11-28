<?php
// contact.php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require __DIR__ . '/../vendor/autoload.php';   // Ajusta si tu carpeta vendor está en otra ruta
$config = require __DIR__ . '/../config.php';  // Archivo con tus credenciales SMTP

$alert = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Sanitizar entradas
    $name    = trim(filter_input(INPUT_POST, 'name', FILTER_SANITIZE_STRING));
    $email   = trim(filter_input(INPUT_POST, 'email', FILTER_SANITIZE_EMAIL));
    $subject = trim(filter_input(INPUT_POST, 'subject', FILTER_SANITIZE_STRING));
    $message = trim(filter_input(INPUT_POST, 'message', FILTER_SANITIZE_STRING));

    // Validar campos
    if (empty($name) || empty($email) || empty($subject) || empty($message) || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $alert = '<div class="alert alert-danger mt-3">⚠️ Por favor completa todos los campos correctamente.</div>';
    } else {
        $mail = new PHPMailer(true);
        try {
            // Configuración servidor SMTP
            $mail->isSMTP();
            $mail->Host       = $config['smtp_host'];
            $mail->SMTPAuth   = true;
            $mail->Username   = $config['smtp_user'];
            $mail->Password   = $config['smtp_pass'];
            $mail->SMTPSecure = $config['smtp_secure'];
            $mail->Port       = $config['smtp_port'];

            // Destinatarios
            $mail->setFrom($config['from_email'], $config['from_name']);
            $mail->addAddress($config['to_email']);
            $mail->addReplyTo($email, $name);

            // Contenido del correo
            $mail->isHTML(true);
            $mail->Subject = " Nuevo contacto: " . $subject;
            $mail->Body    = "
                <h2>Nuevo mensaje desde el formulario de contacto</h2>
                <p><strong>Nombre:</strong> {$name}</p>
                <p><strong>Email:</strong> {$email}</p>
                <p><strong>Asunto:</strong> {$subject}</p>
                <p><strong>Mensaje:</strong><br>" . nl2br(htmlspecialchars($message)) . "</p>
            ";
            $mail->AltBody = "Nombre: {$name}\nEmail: {$email}\nAsunto: {$subject}\nMensaje:\n{$message}";


        $mail->send();

        // Redirige a la misma página con un parámetro GET
        header("Location: contact.php?success=1");
        exit;

        } catch (Exception $e) {
            $alert = '<div class="alert alert-danger mt-3"> No se pudo enviar el mensaje. Error: ' . htmlspecialchars($mail->ErrorInfo) . '</div>';
        }
    }
}

require '../Views/Templates/header.php';
?>

<body>

    <!-- Contact Start -->
    <div class="container-fluid">
        <h2 class="text-success display-4 fw-bold text-center mb-4">Contáctanos</h2>
        
        <!-- Alertas -->
        <div class="row justify-content-center">
            <div class="col-lg-8">
            <?php
            if (isset($_GET['success'])) {
                echo '<div class="alert alert-success mt-3"> Tu mensaje fue enviado correctamente. ¡Gracias por contactarnos!</div>';
            } elseif (!empty($alert)) {
                echo $alert;
            }
            ?>

            </div>
        </div>

        <div class="row px-xl-5">
            <div class="col-lg-7 mb-5">
                <div class="contact-form bg-light p-30">
                    <div id="success"></div>
                    <form method="POST" action="contact.php" id="contactForm" novalidate="novalidate">
                        <div class="control-group mb-3">
                            <input type="text" class="form-control" name="name" placeholder="Tu Nombre"
                                required data-validation-required-message="Por favor ingresa tu nombre" />
                        </div>
                        <div class="control-group mb-3">
                            <input type="email" class="form-control" name="email" placeholder="Tu Correo"
                                required data-validation-required-message="Por favor ingresa tu correo" />
                        </div>
                        <div class="control-group mb-3">
                            <input type="text" class="form-control" name="subject" placeholder="Asunto"
                                required data-validation-required-message="Por favor ingresa un asunto" />
                        </div>
                        <div class="control-group mb-3">
                            <textarea class="form-control" rows="8" name="message" placeholder="Escribe tu mensaje"
                                required data-validation-required-message="Por favor escribe un mensaje"></textarea>
                        </div>
                        <div>
                            <button class="btn btn-primary py-2 px-4" type="submit">Enviar Mensaje</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-lg-5 mb-5">
                <div class="bg-light p-30 mb-30">
                    <iframe style="width: 100%; height: 250px;"
                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3811.293488386068!2d-93.01820892506963!3d17.20454598365178!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x85ed06cc929f9fc7%3A0x840c6e5ac8da8c6d!2sUniversidad%20Tecnol%C3%B3gica%20de%20la%20Selva%20%5BUnidad%20Ray%C3%B3n%5D!5e0!3m2!1ses-419!2smx!4v1759022502878!5m2!1ses-419!2smx"
                        frameborder="0" style="border:0;" allowfullscreen="" aria-hidden="false"
                        tabindex="0"></iframe>
                </div>
            </div>
        </div>
    </div>
    <script>
        // Selecciona el alert
        const alertBox = document.querySelector('.alert');
        if(alertBox) {
            // Espera 3 segundos (3000 ms) y luego lo oculta
            setTimeout(() => {
                alertBox.style.transition = "opacity 0.5s ease";
                alertBox.style.opacity = '0';
                setTimeout(() => alertBox.remove(), 500); // Lo elimina del DOM después de la transición
            }, 3000);
        }
    </script>

</body>

<?php
include '../Views/Templates/footer.php';
?>
</html>
