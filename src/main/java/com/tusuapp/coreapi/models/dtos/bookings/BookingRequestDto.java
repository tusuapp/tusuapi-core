package com.tusuapp.coreapi.models.dtos.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    private Double totalAmount;
    private Long id;
    private UserDto student;
    private UserDto tutor;
    private Long subjectId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private boolean isRescheduled;
    private Long parentBookingId;
    private String rescheduleReason;
    private LocalDateTime rescheduledAt;
    private boolean isPaid;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long slotId;
    private double hourlyFee;


    public BookingRequestDto() {

    }

    public BookingRequestDto(BookingRequest request) {
        this.id = request.getId();
        this.subjectId = request.getSubjectId();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.status = request.getStatus();
        this.isRescheduled = request.getIsRescheduled();
        this.parentBookingId = request.getParentBookingId();
        this.rescheduleReason = request.getRescheduleReason();
        this.rescheduledAt = request.getRescheduledAt();
        this.isPaid = request.getIsPaid();
        this.transactionId = request.getTransactionId();
        this.createdAt = request.getCreatedAt();
        this.updatedAt = request.getUpdatedAt();
        this.slotId = request.getSlotId();
        this.student = UserDto.fromUser(request.getStudent());
        this.tutor = UserDto.fromUser(request.getTutor());
        this.hourlyFee = request.getHourlyCharge();
        this.totalAmount = request.getTotalAmount();
    }

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
        dto.setHourlyFee(request.getHourlyCharge());
        dto.setTotalAmount(request.getTotalAmount());
        return dto;
    }

    public static BookingRequestDto fromBookingRequest(BookingRequest request) {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setId(request.getId());
        dto.setStudent(UserDto.fromUser(request.getStudent()));
        dto.setTutor(UserDto.fromUser(request.getTutor()));
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
        dto.setHourlyFee(request.getHourlyCharge());
        dto.setTotalAmount(request.getTotalAmount());
        return dto;
    }

}
