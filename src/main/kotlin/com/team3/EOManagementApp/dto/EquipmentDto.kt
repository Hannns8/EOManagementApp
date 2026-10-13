package com.team3.EOManagementApp.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

/** Data input pembuatan atau perubahan inventaris alat. */
data class EquipmentRequest(
    @field:NotBlank(message = "Nama alat tidak boleh kosong")
    val name: String,

    @field:NotBlank(message = "Kategori tidak boleh kosong")
    val category: String,

    val description: String? = null,

    @field:Min(value = 0, message = "Total jumlah tidak boleh negatif")
    val totalQuantity: Int,

    val conditionStatus: String? = "Bagus"
)

/** Data alat, stok tersedia, dan kru yang dikirim melalui API. */
data class EquipmentResponse(
    val id: Long,
    val name: String,
    val category: String,
    val description: String?,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val conditionStatus: String?,
    val assignedCrews: List<SimpleCrewResponse> = emptyList()
)

/** Ringkasan alat dan stok untuk ditampilkan dalam relasi data lain. */
data class SimpleEquipmentResponse(
    val id: Long,
    val name: String,
    val category: String,
    val totalQuantity: Int,
    val availableQuantity: Int
)
