package com.team3.EOManagementApp.service

import com.team3.EOManagementApp.dto.*
import com.team3.EOManagementApp.entity.Schedule
import com.team3.EOManagementApp.entity.ScheduleEquipment
import com.team3.EOManagementApp.entity.ScheduleStatus
import com.team3.EOManagementApp.exception.ResourceNotFoundException
import com.team3.EOManagementApp.repository.CrewRepository
import com.team3.EOManagementApp.repository.EquipmentRepository
import com.team3.EOManagementApp.repository.ScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/** Logika jadwal event, peminjaman alat, dan penugasan kru. */
@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val equipmentRepository: EquipmentRepository,
    private val crewRepository: CrewRepository
) {

    /** Mengambil jadwal; rentang tanggal diprioritaskan jika kedua tanggal diisi. */
    @Transactional(readOnly = true)
    fun getAllSchedules(startDate: LocalDate?, endDate: LocalDate?, status: ScheduleStatus?): List<ScheduleResponse> {
        val schedules = when {
            startDate != null && endDate != null -> scheduleRepository.findSchedulesInDateRange(startDate, endDate)
            status != null -> scheduleRepository.findByStatus(status)
            else -> scheduleRepository.findAll()
        }
        return schedules.map { toScheduleResponse(it) }
    }

    /** Mengambil jadwal berdasarkan ID; gagal jika jadwal tidak ditemukan. */
    @Transactional(readOnly = true)
    fun getScheduleById(id: Long): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }
        return toScheduleResponse(schedule)
    }

    /** Menyimpan jadwal baru beserta peminjaman alat dan penugasan kru. */
    @Transactional
    fun createSchedule(request: ScheduleRequest): ScheduleResponse {
        val schedule = Schedule(
            eventName = request.eventName,
            description = request.description,
            location = request.location,
            startDate = request.startDate,
            endDate = request.endDate,
            startTime = request.startTime,
            endTime = request.endTime,
            status = request.status ?: ScheduleStatus.UPCOMING
        )

        if (request.equipmentBorrowings.isNotEmpty()) {
            for (borrowReq in request.equipmentBorrowings) {
                val equipment = equipmentRepository.findById(borrowReq.equipmentId)
                    .orElseThrow { ResourceNotFoundException("Equipment dengan ID ${borrowReq.equipmentId} tidak ditemukan") }

                val scheduleEquipment = ScheduleEquipment(
                    schedule = schedule,
                    equipment = equipment,
                    quantityBorrowed = borrowReq.quantityBorrowed
                )
                schedule.scheduleEquipments.add(scheduleEquipment)
            }
        }

        if (request.crewIds.isNotEmpty()) {
            val crews = crewRepository.findAllById(request.crewIds)
            schedule.crews.addAll(crews)
        }

        val saved = scheduleRepository.save(schedule)
        return toScheduleResponse(saved)
    }

    /** Memperbarui jadwal berdasarkan ID serta mengganti daftar alat dan kru. */
    @Transactional
    fun updateSchedule(id: Long, request: ScheduleRequest): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }

        schedule.eventName = request.eventName
        schedule.description = request.description
        schedule.location = request.location
        schedule.startDate = request.startDate
        schedule.endDate = request.endDate
        schedule.startTime = request.startTime
        schedule.endTime = request.endTime

        if (request.status != null) {
            schedule.status = request.status
        }

        schedule.scheduleEquipments.clear()
        if (request.equipmentBorrowings.isNotEmpty()) {
            for (borrowReq in request.equipmentBorrowings) {
                val equipment = equipmentRepository.findById(borrowReq.equipmentId)
                    .orElseThrow { ResourceNotFoundException("Equipment dengan ID ${borrowReq.equipmentId} tidak ditemukan") }

                val scheduleEquipment = ScheduleEquipment(
                    schedule = schedule,
                    equipment = equipment,
                    quantityBorrowed = borrowReq.quantityBorrowed
                )
                schedule.scheduleEquipments.add(scheduleEquipment)
            }
        }

        schedule.crews.clear()
        if (request.crewIds.isNotEmpty()) {
            val crews = crewRepository.findAllById(request.crewIds)
            schedule.crews.addAll(crews)
        }

        val updated = scheduleRepository.save(schedule)
        return toScheduleResponse(updated)
    }

    /** Mengganti status jadwal berdasarkan ID dan mengembalikan data terbaru. */
    @Transactional
    fun updateStatus(id: Long, status: ScheduleStatus): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }

        schedule.status = status
        val updated = scheduleRepository.save(schedule)
        return toScheduleResponse(updated)
    }

    /** Menghapus jadwal berdasarkan ID; gagal jika jadwal tidak ditemukan. */
    @Transactional
    fun deleteSchedule(id: Long) {
        if (!scheduleRepository.existsById(id)) {
            throw ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan")
        }
        scheduleRepository.deleteById(id)
    }

    /** Mengubah entitas jadwal menjadi respons beserta daftar alat dan kru. */
    fun toScheduleResponse(schedule: Schedule): ScheduleResponse {
        val equipmentResponses = schedule.scheduleEquipments.map { se ->
            ScheduleEquipmentResponse(
                equipmentId = se.equipment.id ?: 0,
                equipmentName = se.equipment.name,
                category = se.equipment.category,
                quantityBorrowed = se.quantityBorrowed
            )
        }

        val crewResponses = schedule.crews.map { c ->
            SimpleCrewResponse(
                id = c.id ?: 0,
                name = c.name,
                phone = c.phone,
                role = c.role
            )
        }

        return ScheduleResponse(
            id = schedule.id ?: 0,
            eventName = schedule.eventName,
            description = schedule.description,
            location = schedule.location,
            startDate = schedule.startDate,
            endDate = schedule.endDate,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            status = schedule.status,
            equipments = equipmentResponses,
            crews = crewResponses
        )
    }
}
