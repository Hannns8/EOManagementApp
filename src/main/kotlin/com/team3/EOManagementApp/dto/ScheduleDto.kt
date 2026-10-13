package com.team3.EOManagementApp.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.team3.EOManagementApp.entity.ScheduleStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalTime

/** ID alat dan jumlah yang diminta untuk dipinjam pada jadwal. */
data class EquipmentBorrowRequest(
    val equipmentId: Long,
    val quantityBorrowed: Int
)

/** Data input jadwal event beserta pilihan alat dan kru. */
data class ScheduleRequest(
    @field:NotBlank(message = "Nama event tidak boleh kosong")
    val eventName: String,

    val description: String? = null,

    @field:NotBlank(message = "Lokasi tidak boleh kosong")
    val location: String,

    @field:NotNull(message = "Tanggal mulai tidak boleh kosong")
    @field:JsonFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,

    @field:NotNull(message = "Tanggal selesai tidak boleh kosong")
    @field:JsonFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate,

    @field:JsonFormat(pattern = "HH:mm:ss")
    val startTime: LocalTime? = null,

    @field:JsonFormat(pattern = "HH:mm:ss")
    val endTime: LocalTime? = null,

    val status: ScheduleStatus? = ScheduleStatus.UPCOMING,

    val equipmentBorrowings: List<EquipmentBorrowRequest> = emptyList(),

    val crewIds: List<Long> = emptyList()
)

/** Rincian alat dan jumlah pinjaman dalam respons jadwal. */
data class ScheduleEquipmentResponse(
    val equipmentId: Long,
    val equipmentName: String,
    val category: String,
    val quantityBorrowed: Int
)

/** Data jadwal lengkap beserta alat dan kru yang dikirim melalui API. */
data class ScheduleResponse(
    val id: Long,
    val eventName: String,
    val description: String?,
    val location: String,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate,

    @field:JsonFormat(pattern = "HH:mm:ss")
    val startTime: LocalTime?,

    @field:JsonFormat(pattern = "HH:mm:ss")
    val endTime: LocalTime?,

    val status: ScheduleStatus,
    val equipments: List<ScheduleEquipmentResponse> = emptyList(),
    val crews: List<SimpleCrewResponse> = emptyList()
)
