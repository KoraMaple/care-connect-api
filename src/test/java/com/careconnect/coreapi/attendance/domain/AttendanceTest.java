package com.careconnect.coreapi.attendance.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AttendanceTest {

    private Attendance attendance;

    @BeforeEach
    void setUp() {
        attendance = new Attendance();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        attendance.setId(id);

        // Then
        assertThat(attendance.getId()).isEqualTo(id);
    }

    @Test
    void setAndGetChildId_ShouldWorkCorrectly() {
        // Given
        UUID childId = UUID.randomUUID();

        // When
        attendance.setChildId(childId);

        // Then
        assertThat(attendance.getChildId()).isEqualTo(childId);
    }

    @Test
    void setAndGetCheckIn_ShouldWorkCorrectly() {
        // Given
        Instant checkIn = Instant.now();

        // When
        attendance.setCheckIn(checkIn);

        // Then
        assertThat(attendance.getCheckIn()).isEqualTo(checkIn);
    }

    @Test
    void setAndGetCheckOut_ShouldWorkCorrectly() {
        // Given
        Instant checkOut = Instant.now();

        // When
        attendance.setCheckOut(checkOut);

        // Then
        assertThat(attendance.getCheckOut()).isEqualTo(checkOut);
    }

    @Test
    void setAndGetStatus_ShouldWorkCorrectly() {
        // Given
        String status = "Present";

        // When
        attendance.setStatus(status);

        // Then
        assertThat(attendance.getStatus()).isEqualTo(status);
    }

    @Test
    void newAttendance_ShouldHaveNullValues() {
        // Given
        Attendance newAttendance = new Attendance();

        // Then
        assertThat(newAttendance.getId()).isNull();
        assertThat(newAttendance.getChildId()).isNull();
        assertThat(newAttendance.getCheckIn()).isNull();
        assertThat(newAttendance.getCheckOut()).isNull();
        assertThat(newAttendance.getStatus()).isNull();
    }
}
