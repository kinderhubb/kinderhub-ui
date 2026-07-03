package com.kinderhub.ui.screens.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChildFormData(
    val id: String = "",
    val firstName: String = "",
    val birthDay: String = "",
    val birthMonth: String = "",
    val birthYear: String = "",
    val firstNameError: String? = null,
    val dateError: String? = null,
    val avatarColor: AvatarColor = AvatarColor.Accent
)

data class AddChildUiState(
    val existingChildren: List<Child> = emptyList(),
    val childForms: List<ChildFormData> = listOf(ChildFormData(id = "form_0")),
    val currentStep: Int = 2, // "Step 2 of 2" from design
    val totalSteps: Int = 2,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val editingChildId: String? = null,
)

class AddChildViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddChildUiState())
    val uiState: StateFlow<AddChildUiState> = _uiState.asStateFlow()

    private val avatarColors = listOf(
        AvatarColor.Accent,
        AvatarColor.Sunshine,
        AvatarColor.Primary,
        AvatarColor.Success
    )

    fun updateChildFirstName(formId: String, name: String) {
        _uiState.update { state ->
            state.copy(
                childForms = state.childForms.map { form ->
                    if (form.id == formId) {
                        form.copy(firstName = name, firstNameError = null)
                    } else form
                }
            )
        }
    }

    fun updateChildBirthDay(formId: String, day: String) {
        if (day.length <= 2 && day.all { it.isDigit() }) {
            _uiState.update { state ->
                state.copy(
                    childForms = state.childForms.map { form ->
                        if (form.id == formId) {
                            form.copy(birthDay = day, dateError = null)
                        } else form
                    }
                )
            }
        }
    }

    fun updateChildBirthMonth(formId: String, month: String) {
        if (month.length <= 2 && month.all { it.isDigit() }) {
            _uiState.update { state ->
                state.copy(
                    childForms = state.childForms.map { form ->
                        if (form.id == formId) {
                            form.copy(birthMonth = month, dateError = null)
                        } else form
                    }
                )
            }
        }
    }

    fun updateChildBirthYear(formId: String, year: String) {
        if (year.length <= 4 && year.all { it.isDigit() }) {
            _uiState.update { state ->
                state.copy(
                    childForms = state.childForms.map { form ->
                        if (form.id == formId) {
                            form.copy(birthYear = year, dateError = null)
                        } else form
                    }
                )
            }
        }
    }

    fun addAnotherChild() {
        _uiState.update { state ->
            val nextIndex = state.childForms.size
            val nextColor = avatarColors[nextIndex % avatarColors.size]
            state.copy(
                childForms = state.childForms + ChildFormData(
                    id = "form_$nextIndex",
                    avatarColor = nextColor
                )
            )
        }
    }

    fun removeChild(formId: String) {
        _uiState.update { state ->
            state.copy(
                childForms = state.childForms.filter { it.id != formId }
            )
        }
    }

    fun editExistingChild(childId: String) {
        _uiState.update { it.copy(editingChildId = childId) }
    }

    fun saveChildren(onComplete: () -> Unit) {
        val state = _uiState.value

        // Validate all forms
        var hasErrors = false
        val validatedForms = state.childForms.map { form ->
            var updatedForm = form

            if (form.firstName.isBlank()) {
                updatedForm = updatedForm.copy(firstNameError = "First name is required")
                hasErrors = true
            }

            if (form.birthDay.isBlank() || form.birthMonth.isBlank() || form.birthYear.isBlank()) {
                updatedForm = updatedForm.copy(dateError = "Complete date of birth is required")
                hasErrors = true
            } else {
                // Validate date
                val day = form.birthDay.toIntOrNull() ?: 0
                val month = form.birthMonth.toIntOrNull() ?: 0
                val year = form.birthYear.toIntOrNull() ?: 0

                if (day < 1 || day > 31 || month < 1 || month > 12 || year < 2000 || year > 2025) {
                    updatedForm = updatedForm.copy(dateError = "Please enter a valid date")
                    hasErrors = true
                }
            }

            updatedForm
        }

        if (hasErrors) {
            _uiState.update { it.copy(childForms = validatedForms) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // Simulate API call
            delay(1000)

            // Convert forms to children
            val children = state.childForms.mapIndexed { index, form ->
                val year = form.birthYear.toInt()
                val currentYear = 2024 // Would use actual current year
                val age = currentYear - year

                Child(
                    id = "child_${Clock.System.now().toEpochMilliseconds()}_$index",
                    firstName = form.firstName.trim(),
                    dateOfBirth = "${form.birthYear}-${form.birthMonth.padStart(2, '0')}-${form.birthDay.padStart(2, '0')}",
                    age = age,
                    avatarColor = form.avatarColor
                )
            }

            _uiState.update {
                it.copy(
                    isSaving = false,
                    existingChildren = it.existingChildren + children
                )
            }

            onComplete()
        }
    }
}
