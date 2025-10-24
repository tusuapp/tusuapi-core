//package com.tusuapp.coreapi.services.notifications;
//
//import com.tusuapp.coreapi.services.notifications.EmailService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import jakarta.mail.MessagingException;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//// Use a specific profile (like 'integration-test') if you want to use
//// different properties (e.g., test credentials) for this test class.
//@SpringBootTest
//class EmailServiceIntegrationTest {
//
//    @Autowired
//    private EmailService emailService; // Assuming this is the class containing your sendEmail method
//
//    private File testAttachment;
//    private final String RECIPIENT_EMAIL = "tubeviral88@gmail.com";
//    private final String TEST_SUBJECT = "Integration Test - OTP 1234";
//    private final String TEST_BODY = "Your test code is <b>1234</b>";
//
//    // Method to create a temporary file before each test
//    @BeforeEach
//    public void setup() throws IOException {
//        testAttachment = File.createTempFile("test_attachment_", ".txt");
//        // Write some content so the file isn't empty (optional)
//        java.nio.file.Files.writeString(testAttachment.toPath(), "This is test content.");
//    }
//
//    // Method to ensure the temporary file is deleted even if the test fails
//    @AfterEach
//    public void cleanup() {
//        if (testAttachment != null && testAttachment.exists()) {
//            testAttachment.delete();
//        }
//    }
//
//    /**
//     * Tests sending a real email with an attachment and verifies the file is deleted.
//     * This is a true integration test that relies on a live server connection.
//     */
//    @Test
//    public void testSendEmail_WithRealServerAndAttachment() throws MessagingException, UnsupportedEncodingException {
//
//        // Arrange is done in @BeforeEach
//
//        // Act & Assert (Verification of successful execution)
//
//        // 1. Assert that calling the method does NOT throw an exception.
//        // If the email sends successfully, this is a pass.
//        assertDoesNotThrow(() -> {
//            emailService.sendEmail(RECIPIENT_EMAIL, TEST_SUBJECT, TEST_BODY, testAttachment);
//        }, "Email sending failed; check server connection, credentials, and settings.");
//
//        // 2. Assert the file cleanup logic worked (critical side effect check).
//        assertFalse(testAttachment.exists(), "Test file was not deleted after sending the email.");
//    }
//
//    /**
//     * Tests sending a real email without an attachment.
//     */
//    @Test
//    public void testSendEmail_NoAttachment() throws MessagingException, UnsupportedEncodingException {
//        // Arrange: Use null for the attachment
//
//        // Act & Assert (Verification of successful execution)
//        assertDoesNotThrow(() -> {
//            emailService.sendEmail(RECIPIENT_EMAIL, TEST_SUBJECT, TEST_BODY, null);
//        }, "Email sending (without attachment) failed.");
//    }
//
//    // Note: The original request was to test emailService.sendOTPEmail("...", "1234").
//    // If that method calls sendEmail with hardcoded parameters, you can also test it:
//
//    // @Test
//    // public void testSendOTPEmail_RealServer() throws MessagingException, UnsupportedEncodingException {
//    //     assertDoesNotThrow(() -> {
//    //         emailService.sendOTPEmail("tubeviral88@gmail.com", "1234");
//    //     }, "sendOTPEmail execution failed.");
//    // }
//}