package com.tranzo.wallet.domain.model

data class Network(
    val chainId: Long,
    val name: String,
    val symbol: String,
    val rpcUrl: String,
    val explorerUrl: String,
    val iconUrl: String = "",
    val isTestnet: Boolean = false
) {
    companion object {
        val ETHEREUM = Network(1, "Ethereum", "ETH", "https://cloudflare-eth.com", "https://etherscan.io")
        val POLYGON = Network(137, "Polygon", "MATIC", "https://polygon-rpc.com", "https://polygonscan.com")
        val BSC = Network(56, "BNB Chain", "BNB", "https://bsc-dataseed.binance.org", "https://bscscan.com")
        val BASE = Network(8453, "Base", "ETH", "https://mainnet.base.org", "https://basescan.org")
        val ARBITRUM = Network(42161, "Arbitrum", "ETH", "https://arb1.arbitrum.io/rpc", "https://arbiscan.io")
        val OPTIMISM = Network(10, "Optimism", "ETH", "https://mainnet.optimism.io", "https://optimistic.etherscan.io")
        val SEPOLIA = Network(11155111, "Sepolia", "ETH", "https://rpc.sepolia.org", "https://sepolia.etherscan.io", isTestnet = true)

        val DEFAULT_NETWORKS = listOf(ETHEREUM, POLYGON, BSC, BASE, ARBITRUM, OPTIMISM, SEPOLIA)
    }
}
