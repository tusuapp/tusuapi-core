package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "tutor_bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TutorBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "tutor_id")
    private Long tutorId;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "slot_number", nullable = false)
    private String slotNumber;

    @Column(name = "subject")
    private String subject;

    @Column(name = "notes")
    private String notes;

    @Column(name = "rescheduled")
    private Boolean rescheduled;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "booking_no", nullable = false)
    private String bookingNo;

    @Column(name = "bigbluebutton")
    private Integer bigBlueButton;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "booking_date_time")
    private String bookingDateTime; // assuming it’s stored as a formatted string

    @Column(name = "utc_offset")
    private String utcOffset;

    @Column(name = "status_notes", columnDefinition = "LONGTEXT")
    private String statusNotes;

    @Column(name = "is_rescheduled")
    private Boolean isRescheduled;

    @Column(name = "tutor_online_status")
    private String tutorOnlineStatus;

    @Column(name = "student_online_status")
    private String studentOnlineStatus;

    @Column(name = "settled_status")
    private String settledStatus;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "booking_request_id", nullable = false)
    private Long bookingRequestId;
}
