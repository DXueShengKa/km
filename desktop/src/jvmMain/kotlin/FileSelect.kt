import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JLabel

class FileSelect{

    private val fileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
    }

    private val f = JLabel("选择文件")

    suspend fun getFile():File?{
        return withContext(Dispatchers.IO){
            fileChooser.showOpenDialog(f)
            fileChooser.selectedFile
        }
    }
}


@Composable
fun rememberFile():FileSelect{
    return remember { FileSelect() }
}
