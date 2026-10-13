package com.team3.EOManagementApp.service

import com.team3.EOManagementApp.dto.EquipmentRequest
import com.team3.EOManagementApp.dto.EquipmentResponse
import com.team3.EOManagementApp.dto.SimpleCrewResponse
import com.team3.EOManagementApp.entity.Equipment
import com.team3.EOManagementApp.exception.ResourceNotFoundException
import com.team3.EOManagementApp.repository.EquipmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** Logika inventaris alat dan perhitungan stok tersedia. */
@Service
class EquipmentService(
    private val equipmentRepository: EquipmentRepository
) {

    /** Mengambil alat sesuai filter kategori dan pencarian nama yang diisi. */
    @Transactional(readOnly = true)
    fun getAllEquipment(category: String?, search: String?): List<EquipmentResponse> {
        val equipmentList = when {
            !category.isNullOrBlank() && !search.isNullOrBlank() ->
                equipmentRepository.findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category, search)
            !category.isNullOrBlank() ->
                equipmentRepository.findByCategoryContainingIgnoreCase(category)
            !search.isNullOrBlank() ->
                equipmentRepository.findByNameContainingIgnoreCase(search)
            else ->
                equipmentRepository.findAll()
        }
        return equipmentList.map { toEquipmentResponse(it) }
    }

    /** Mengambil alat berdasarkan ID; gagal jika alat tidak ditemukan. */
    @Transactional(readOnly = true)
    fun getEquipmentById(id: Long): EquipmentResponse {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan") }
        return toEquipmentResponse(equipment)
    }

    /** Menyimpan alat baru dari request dan mengembalikan data hasilnya. */
    @Transactional
    fun createEquipment(request: EquipmentRequest): EquipmentResponse {
        val equipment = Equipment(
            name = request.name,
            category = request.category,
            description = request.description,
            totalQuantity = request.totalQuantity,
            conditionStatus = request.conditionStatus ?: "Bagus"
        )
        val saved = equipmentRepository.save(equipment)
        return toEquipmentResponse(saved)
    }

    /** Memperbarui data alat berdasarkan ID dan isi request. */
    @Transactional
    fun updateEquipment(id: Long, request: EquipmentRequest): EquipmentResponse {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan") }

        equipment.name = request.name
        equipment.category = request.category
        equipment.description = request.description
        equipment.totalQuantity = request.totalQuantity
        equipment.conditionStatus = request.conditionStatus ?: "Bagus"

        val updated = equipmentRepository.save(equipment)
        return toEquipmentResponse(updated)
    }

    /** Menghapus alat berdasarkan ID; gagal jika alat tidak ditemukan. */
    @Transactional
    fun deleteEquipment(id: Long) {
        if (!equipmentRepository.existsById(id)) {
            throw ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan")
        }
        equipmentRepository.deleteById(id)
    }

    /** Mengubah alat menjadi respons; stok tersedia dihitung dari total dikurangi pinjaman aktif. */
    fun toEquipmentResponse(equipment: Equipment): EquipmentResponse {
        val borrowedQty = equipment.id?.let { equipmentRepository.countBorrowedQuantityInActiveSchedules(it) } ?: 0
        val availableQty = (equipment.totalQuantity - borrowedQty).coerceAtLeast(0)

        val assignedCrews = equipment.crews.map { crew ->
            SimpleCrewResponse(
                id = crew.id ?: 0,
                name = crew.name,
                phone = crew.phone,
                role = crew.role
            )
        }

        return EquipmentResponse(
            id = equipment.id ?: 0,
            name = equipment.name,
            category = equipment.category,
            description = equipment.description,
            totalQuantity = equipment.totalQuantity,
            availableQuantity = availableQty,
            conditionStatus = equipment.conditionStatus,
            assignedCrews = assignedCrews
        )
    }
}
