package dev.rocli.android.webdav.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.rocli.android.webdav.R
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


abstract class SimpleWebViewDialog protected constructor(@StringRes private val title: Int) : DialogFragment() {
    protected abstract fun getContent(context: Context): String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_web_view, null)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setView(view)
            .setPositiveButton(R.string.ok, null)
            .show()

        val content = getContent(requireContext())
        val webView: WebView = view.findViewById(R.id.web_view)
        webView.loadData(content, "text/html", "UTF-8")
        return dialog
    }

    protected val backgroundColor: String
        get() {
            val color: Int = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorSurfaceContainerHigh,
                javaClass.getCanonicalName()
            )
            return colorToCSS(color)
        }

    protected val textColor: String
        get() {
            val color: Int = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSurface,
                javaClass.getCanonicalName()
            )
            return colorToCSS(0xFFFFFF and color)
        }

    companion object {
        @SuppressLint("DefaultLocale")
        private fun colorToCSS(color: Int): String {
            return String.format(
                "rgb(%d, %d, %d)",
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
        }

        @JvmStatic
        protected fun readAssetAsString(context: Context, name: String): String {
            try {
                context.assets.open(name).use { inStream ->
                    InputStreamReader(inStream, StandardCharsets.UTF_8).use { reader ->
                        return reader.readText()
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}
