package cat.copernic.frontend.core.models.enums.utils

fun String.toCatalanValidation(): String = when (this) {
    "VALIDATED" -> "Validat"
    "INVALIDATED" -> "Invalidat"
    "NOT_CHECKED" -> "No verificat"
    else -> "Desconegut"
}