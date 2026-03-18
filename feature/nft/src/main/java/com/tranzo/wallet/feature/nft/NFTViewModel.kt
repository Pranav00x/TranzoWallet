package com.tranzo.wallet.feature.nft

import androidx.lifecycle.ViewModel
import com.tranzo.wallet.domain.model.NFT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NFTGalleryState(
    val isLoading: Boolean = false,
    val nfts: List<NFT> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class NFTViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NFTGalleryState())
    val state: StateFlow<NFTGalleryState> = _state.asStateFlow()

    init {
        // NFT fetching is not yet implemented.
        // In production, this would call an NFT repository to load owned NFTs.
        _state.value = NFTGalleryState(isLoading = false, nfts = emptyList())
    }

    fun refresh() {
        // Placeholder for future NFT refresh logic
        _state.value = NFTGalleryState(isLoading = false, nfts = emptyList())
    }
}
