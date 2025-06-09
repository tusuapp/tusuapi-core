package com.tusuapp.coreapi.models.dtos.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("student")
    private UserDto student;

    @JsonProperty("tutor")
    private UserDto tutor;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @JsonProperty("status")
    private String status;

    @JsonProperty("is_rescheduled")
    private boolean isRescheduled;

    @JsonProperty("parent_booking_id")
    private Long parentBookingId;

    @JsonProperty("reschedule_reason")
    private String rescheduleReason;

    @JsonProperty("rescheduled_at")
    private LocalDateTime rescheduledAt;

    @JsonProperty("is_paid")
    private boolean isPaid;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("slot_id")
    private Long slotId;

    public static BookingRequestDto fromBookingRequest(BookingRequest request, UserDto student, UserDto tutor) {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setId(request.getId());
        dto.setStudent(student);
        dto.setTutor(tutor);
        dto.setSubjectId(request.getSubjectId());
        dto.setStartTime(request.getStartTime());
        dto.setEndTime(request.getEndTime());
        dto.setStatus(request.getStatus());
        dto.setRescheduled(request.getIsRescheduled());
        dto.setParentBookingId(request.getParentBookingId());
        dto.setRescheduleReason(request.getRescheduleReason());
        dto.setRescheduledAt(request.getRescheduledAt());
        dto.setPaid(request.getIsPaid());
        dto.setTransactionId(request.getTransactionId());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        dto.setSlotId(request.getSlotId());
        return dto;
    }

}
