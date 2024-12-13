import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nutlicii.R
import com.example.nutlicii.UI.View.FoodAddActivity
import com.example.nutlicii.data.ViewModel.OcrViewModel
import com.example.nutlicii.data.ViewModel.OcrViewModelFactory
import data.Remote.NutliciiBaseApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class OcrFragment : Fragment(R.layout.ocr_activity) {

    private lateinit var imagePreview: ImageView
    private lateinit var cameraButton: ImageButton
    private lateinit var galleryButton: ImageButton
    private lateinit var ocrViewModel: OcrViewModel

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val GALLERY_REQUEST_CODE = 101
        const val CAMERA_PERMISSION_CODE = 200
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePreview = view.findViewById(R.id.imagePreview)
        cameraButton = view.findViewById(R.id.cameraButton)
        galleryButton = view.findViewById(R.id.galleryButton)
        val apiService = NutliciiBaseApi.getApiService()
        val factory = OcrViewModelFactory(requireContext(), apiService)
        ocrViewModel = ViewModelProvider(this, factory).get(OcrViewModel::class.java)
        cameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
        galleryButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)

        }
        ocrViewModel.uploadResult.observe(viewLifecycleOwner, { success ->
            if (success) {
                Toast.makeText(requireContext(), "Yeah,foto berhasil di prosess", Toast.LENGTH_SHORT).show()
                ocrViewModel.nutritionalInfo.observe(viewLifecycleOwner, { nutritionalInfo ->
                    Log.d("OcrFragment", "Calories: ${nutritionalInfo.calories.value}")
                    Log.d("OcrFragment", "Fat: ${nutritionalInfo.fat.value}")
                    Log.d("OcrFragment", "Salt: ${nutritionalInfo.salt.value}")
                    Log.d("OcrFragment", "Sugar: ${nutritionalInfo.sugar.value}")
                    val intent = Intent(requireContext(), FoodAddActivity::class.java).apply {
                        putExtra("calories", nutritionalInfo.calories.value)
                        putExtra("fat", nutritionalInfo.fat.value)
                        putExtra("salt", nutritionalInfo.salt.value)
                        putExtra("sugar", nutritionalInfo.sugar.value)
                    }
                    startActivity(intent)
                })
            } else {
                Toast.makeText(requireContext(), "duh fotonya gagagl di proses,input manual yaaa!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), FoodAddActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    imagePreview.setImageBitmap(photo)
                    val imageFile = saveBitmapToFile(photo)
                    getUserDataAndUploadImage(imageFile)
                }
                GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    imagePreview.setImageURI(imageUri)
                    val imageFile = getFileFromUri(imageUri!!)
                    getUserDataAndUploadImage(imageFile)
                }
            }
        }
    }
    private fun getUserDataAndUploadImage(imageFile: File) {
        viewLifecycleOwner.lifecycleScope.launch {
            ocrViewModel.getUserDataFromDatabase()?.let { userData ->
                val token = userData.token ?: ""
                val username = userData.username ?: ""
                ocrViewModel.uploadImage(imageFile, token, username)
                Toast.makeText(context, "Tunggu sebentar gambarnya lagi di prosess nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File(requireContext().cacheDir, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    private fun getFileFromUri(uri: android.net.Uri): File {
        val file = File(requireContext().cacheDir, "image_from_gallery_${System.currentTimeMillis()}.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
