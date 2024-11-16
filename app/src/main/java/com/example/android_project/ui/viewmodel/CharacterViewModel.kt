import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.domain.CharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.android_project.data.model.Character
import com.example.android_project.data.repository.CharacterRepository
import com.example.android_project.data.store.SettingsDataStore

class CharacterViewModel : ViewModel() {

    private val useCase = CharacterUseCase(CharacterRepository())

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    private val _characterDetails = MutableStateFlow<Character?>(null)
    val characterDetails: StateFlow<Character?> = _characterDetails

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        _loading.value = true
        viewModelScope.launch {
            try {
                _characters.value = useCase.getCharacterList()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchCharacterDetails(characterId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _characterDetails.value = useCase.getCharacterDetails(characterId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applyFilters(context: Context) {
        viewModelScope.launch {
            val settings = SettingsDataStore(context).loadFilters()
            _characters.value = useCase.getCharacterList().filter { character ->
                (settings.gender.isEmpty() || character.details["Gender"] == settings.gender) &&
                        (settings.minAge <= (character.details["Age"]?.toIntOrNull() ?: 0))
            }
        }
    }

}
