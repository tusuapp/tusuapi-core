package com.tusuapp.coreapi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "booking_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private BookingRequest booking;

    @Column(name = "student_bbb_url")
    private String studentBBBUrl;

    @Column(name="tutor_bbb_url")
    private String tutorBBBUrl;

    @Column(name="tutor_pass")
    private String tutorPass;

    @Column(name="student_pass")
    private String studentPass;

    @Column(name="meeting_id")
    private String meetingId;

    @Column(name = "tutor_joined")
    private boolean isTutorJoined;

    @Column(name = "student_joined")
    private boolean isStudentJoined;

    @Column(name = "replay_url")
    private String replayUrl;

}
