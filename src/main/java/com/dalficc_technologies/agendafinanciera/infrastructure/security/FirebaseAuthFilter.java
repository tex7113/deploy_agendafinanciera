//package com.dalficc_technologies.agendafinanciera.infrastructure.security;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class FirebaseAuthFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        // Verificar si viene un token
//        if (authHeader == null || authHeader.isBlank()) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Missing Authorization header");
//            return;
//        }
//
//        try {
//            // Validar el token con Firebase
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authHeader);
//
//            // Agregar el userId al request para que el controlador lo use
//            request.setAttribute("userId", decodedToken.getUid());
//
//            // Pasar al siguiente filtro o controlador
//            filterChain.doFilter(request, response);
//
//        } catch (FirebaseAuthException e) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("Invalid Firebase token: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getRequestURI();
//
//        // 🔹 Rutas públicas (no requieren autenticación)
//        return path.startsWith("/api/auth")  // login o registro
//                || path.startsWith("/panel-de-control")
//                || path.startsWith("/public")    // recursos públicos
//                || path.startsWith("/bootstrap") // estilos locales
//                || path.startsWith("/css")
//                || path.startsWith("/js")
//                || path.startsWith("/img")
//                || path.startsWith("/favicon")
//                || path.startsWith("/webjars")
//                || path.startsWith("/swagger")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/error")
//                || path.equals("/");          // raíz pública
//    }
//}
