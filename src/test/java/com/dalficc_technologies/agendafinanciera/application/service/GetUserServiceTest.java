//package com.dalficc_technologies.agendafinanciera.application.service;
//
//import com.dalficc_technologies.agendafinanciera.domain.model.User;
//import com.dalficc_technologies.agendafinanciera.domain.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class GetUserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private GetUserService getUserService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetUser_ReturnsUserSuccessfully() throws Exception {
//        // Arrange
//        String userId = "123";
//        User mockUser = new User();
//        mockUser.setName("John Doe");
//        mockUser.setEmail("john@example.com");
//
//        CompletableFuture<User> mockFuture = CompletableFuture.completedFuture(mockUser);
//        when(userRepository.getUserById(userId)).thenReturn(mockFuture);
//
//        // Act
//        User result = getUserService.getUser(userId).get();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("John Doe", result.getName());
//        assertEquals("john@example.com", result.getEmail());
//        verify(userRepository, times(1)).getUserById(userId);
//    }
//
//    @Test
//    void testGetUser_ReturnsNullWhenNotFound() throws Exception {
//        // Arrange
//        String userId = "not_found";
//        when(userRepository.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(null));
//
//        // Act
//        User result = getUserService.getUser(userId).get();
//
//        // Assert
//        assertNull(result);
//        verify(userRepository, times(1)).getUserById(userId);
//    }
//
//    @Test
//    void testGetUser_ThrowsException() {
//        // Arrange
//        String userId = "error";
//        CompletableFuture<User> failedFuture = new CompletableFuture<>();
//        failedFuture.completeExceptionally(new RuntimeException("Firebase error"));
//        when(userRepository.getUserById(userId)).thenReturn(failedFuture);
//
//        // Act & Assert
//        assertThrows(Exception.class, () -> getUserService.getUser(userId).get());
//        verify(userRepository, times(1)).getUserById(userId);
//    }
//}
