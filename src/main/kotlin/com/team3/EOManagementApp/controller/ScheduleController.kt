package com.team3.EOManagementApp.controller

import com.team3.EOManagementApp.dto.ScheduleRequest
import com.team3.EOManagementApp.dto.ScheduleResponse
import com.team3.EOManagementApp.entity.ScheduleStatus
import com.team3.EOManagementApp.service.ScheduleService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/** Endpoint HTTP untuk pengelolaan jadwal event. */
@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {

    /** GET /api/schedules: mengembalikan jadwal dengan filter tanggal atau status. */
    @GetMapping
    fun getAllSchedules(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(required = false) status: ScheduleStatus?
    ): ResponseEntity<List<ScheduleResponse>> {
        val schedules = scheduleService.getAllSchedules(startDate, endDate, status)
        return ResponseEntity.ok(schedules)
    }

    /** GET /api/schedules/{id}: mengembalikan detail jadwal berdasarkan ID. */
    @GetMapping("/{id}")
    fun getScheduleById(@PathVariable id: Long): ResponseEntity<ScheduleResponse> {
        val schedule = scheduleService.getScheduleById(id)
        return ResponseEntity.ok(schedule)
    }

    /** POST /api/schedules: membuat jadwal dari request dan mengembalikan HTTP 201. */
    @PostMapping
    fun createSchedule(@Valid @RequestBody request: ScheduleRequest): ResponseEntity<ScheduleResponse> {
        val created = scheduleService.createSchedule(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    /** PUT /api/schedules/{id}: memperbarui jadwal berdasarkan ID dan isi request. */
    @PutMapping("/{id}")
    fun updateSchedule(
        @PathVariable id: Long,
        @Valid @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleResponse> {
        val updated = scheduleService.updateSchedule(id, request)
        return ResponseEntity.ok(updated)
    }

    /** PATCH /api/schedules/{id}/status: mengganti status jadwal berdasarkan ID. */
    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam status: ScheduleStatus
    ): ResponseEntity<ScheduleResponse> {
        val updated = scheduleService.updateStatus(id, status)
        return ResponseEntity.ok(updated)
    }

    /** DELETE /api/schedules/{id}: menghapus jadwal dan mengembalikan pesan hasil. */
    @DeleteMapping("/{id}")
    fun deleteSchedule(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        scheduleService.deleteSchedule(id)
        return ResponseEntity.ok(mapOf("message" to "Jadwal dengan ID $id berhasil dihapus"))
    }
}
