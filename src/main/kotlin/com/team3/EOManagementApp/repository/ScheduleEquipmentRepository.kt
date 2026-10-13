package com.team3.EOManagementApp.repository

import com.team3.EOManagementApp.entity.ScheduleEquipment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/** Akses database untuk rincian alat yang dipinjam pada jadwal. */
@Repository
interface ScheduleEquipmentRepository : JpaRepository<ScheduleEquipment, Long> {
    /** Mengambil rincian peminjaman alat berdasarkan scheduleId. */
    fun findByScheduleId(scheduleId: Long): List<ScheduleEquipment>
    /** Menghapus rincian peminjaman alat berdasarkan scheduleId. */
    fun deleteByScheduleId(scheduleId: Long)
}
