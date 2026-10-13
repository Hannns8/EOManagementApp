package com.team3.EOManagementApp.repository

import com.team3.EOManagementApp.entity.Equipment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/** Akses database untuk inventaris alat dan jumlah pinjaman. */
@Repository
interface EquipmentRepository : JpaRepository<Equipment, Long> {
    /** Mencari alat berdasarkan sebagian kategori tanpa membedakan huruf besar. */
    fun findByCategoryContainingIgnoreCase(category: String): List<Equipment>
    /** Mencari alat berdasarkan sebagian nama tanpa membedakan huruf besar. */
    fun findByNameContainingIgnoreCase(name: String): List<Equipment>
    /** Mencari alat yang cocok dengan kategori dan nama tanpa membedakan huruf besar. */
    fun findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category: String, name: String): List<Equipment>

    /** Menjumlahkan alat yang dipinjam pada jadwal UPCOMING atau ONGOING. */
    @Query("""
        SELECT COALESCE(SUM(se.quantityBorrowed), 0)
        FROM ScheduleEquipment se
        JOIN se.schedule s
        WHERE se.equipment.id = :equipmentId
        AND s.status IN ('UPCOMING', 'ONGOING')
    """)
    fun countBorrowedQuantityInActiveSchedules(@Param("equipmentId") equipmentId: Long): Int
}
