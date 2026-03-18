package com.tranzo.wallet.feature.wallet

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

/**
 * Utility object for generating QR code bitmaps from strings using ZXing.
 */
object QrCodeGenerator {

    /**
     * Generates a QR code [Bitmap] for the given [content] string.
     *
     * @param content The string to encode into a QR code.
     * @param size The width and height in pixels of the resulting bitmap.
     * @param foregroundColor The color of the QR code modules (default: black).
     * @param backgroundColor The background color (default: white).
     * @return A [Bitmap] containing the QR code, or null if generation fails.
     */
    fun generate(
        content: String,
        size: Int = 512,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val hints = mapOf(
                EncodeHintType.MARGIN to 1,
                EncodeHintType.CHARACTER_SET to "UTF-8",
            )

            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)

            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(
                        x, y,
                        if (bitMatrix.get(x, y)) foregroundColor else backgroundColor
                    )
                }
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }
}
