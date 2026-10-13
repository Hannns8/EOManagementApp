package com.team3.EOManagementApp.repository

import com.team3.EOManagementApp.entity.Schedule
import com.team3.EOManagementApp.entity.ScheduleStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

/** Akses database dan pencarian jadwal event. */
@Repository
interface ScheduleRepository : JpaRepository<Schedule, Long> {
    /** Mengambil jadwal dengan status yang diminta. */
    fun findByStatus(status: ScheduleStatus): List<Schedule>

    /** Mengambil jadwal yang beririsan dengan rentang startDate sampai endDate. */
    @Query("""
        SELECT s FROM Schedule s 
        WHERE (s.startDate <= :endDate AND s.endDate >= :startDate)
    """)
    fun findSchedulesInDateRange(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<Schedule>
}
