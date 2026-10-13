package com.team3.EOManagementApp.controller

import com.team3.EOManagementApp.dto.EquipmentRequest
import com.team3.EOManagementApp.dto.EquipmentResponse
import com.team3.EOManagementApp.service.EquipmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/** Endpoint HTTP untuk pengelolaan inventaris alat. */
@RestController
@RequestMapping("/api/equipment")
class EquipmentController(
    private val equipmentService: EquipmentService
) {

    /** GET /api/equipment: mengembalikan alat dengan filter kategori dan pencarian nama. */
    @GetMapping
    fun getAllEquipment(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<List<EquipmentResponse>> {
        val list = equipmentService.getAllEquipment(category, search)
        return ResponseEntity.ok(list)
    }

    /** GET /api/equipment/{id}: mengembalikan detail alat berdasarkan ID. */
    @GetMapping("/{id}")
    fun getEquipmentById(@PathVariable id: Long): ResponseEntity<EquipmentResponse> {
        val equipment = equipmentService.getEquipmentById(id)
        return ResponseEntity.ok(equipment)
    }

    /** POST /api/equipment: membuat alat dari request dan mengembalikan HTTP 201. */
    @PostMapping
    fun createEquipment(@Valid @RequestBody request: EquipmentRequest): ResponseEntity<EquipmentResponse> {
        val created = equipmentService.createEquipment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    /** PUT /api/equipment/{id}: memperbarui alat berdasarkan ID dan isi request. */
    @PutMapping("/{id}")
    fun updateEquipment(
        @PathVariable id: Long,
        @Valid @RequestBody request: EquipmentRequest
    ): ResponseEntity<EquipmentResponse> {
        val updated = equipmentService.updateEquipment(id, request)
        return ResponseEntity.ok(updated)
    }

    /** DELETE /api/equipment/{id}: menghapus alat dan mengembalikan pesan hasil. */
    @DeleteMapping("/{id}")
    fun deleteEquipment(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        equipmentService.deleteEquipment(id)
        return ResponseEntity.ok(mapOf("message" to "Equipment dengan ID $id berhasil dihapus"))
    }
}
