package com.team3.EOManagementApp.repository

import com.team3.EOManagementApp.entity.Crew
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/** Akses database untuk data kru. */
@Repository
interface CrewRepository : JpaRepository<Crew, Long> {
    /** Mencari kru berdasarkan sebagian jabatan tanpa membedakan huruf besar. */
    fun findByRoleContainingIgnoreCase(role: String): List<Crew>
    /** Mencari kru berdasarkan sebagian nama tanpa membedakan huruf besar. */
    fun findByNameContainingIgnoreCase(name: String): List<Crew>
    /** Mengambil kru yang memiliki penugasan pada equipmentId. */
    fun findByEquipmentsId(equipmentId: Long): List<Crew>
}
