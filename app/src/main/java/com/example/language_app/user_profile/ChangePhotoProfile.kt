package com.example.language_app.user_profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.language_app.databases.Initialization.Companion.supabaseClient
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.language_app.base_activities.ActivityBase
import com.example.language_app.databinding.ActChangePhotoBinding
import com.oginotihiro.cropview.CropView
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.io.ByteArrayOutputStream
import java.io.File

class ChangePhotoProfile : ActivityBase<ActChangePhotoBinding>() {

    private val cropView by lazy { findViewById<View>(com.example.language_app.R.id.cropView) as CropView }

    override val screenBinding: ActChangePhotoBinding by lazy {
        ActChangePhotoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenBinding.btnSetPhoto.setOnClickListener {
            lifecycleScope.launch {
                val croppedImage = cropView.output
                val outputStream = ByteArrayOutputStream()
                croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val byteArray = outputStream.toByteArray()
                val user = supabaseClient.auth.currentUserOrNull()
                val id = user?.id

                if (id != null) {
                    supabaseClient.storage
                        .from("UsersPhotos")
                        .list(id)
                        .forEach { file ->
                            supabaseClient.storage.from("UsersPhotos")
                                .delete("$id/${file.name}")
                        }
                }

                val photoFileName =
                    "avatar_${Clock.System.now().toEpochMilliseconds()}.jpeg"

                val path = supabaseClient.storage.from("UsersPhotos")
                    .upload(
                        path = "${id}/$photoFileName",
                        data = byteArray,
                        upsert = true,
                    )
                val fullImageUrl = "https://lsmgztoiwdzwjwsnsjso.supabase.co/storage/v1/s3/$path"
                if (id != null) {
                    setNewImageUrl(id, fullImageUrl)
                }
                finish()
            }

        }

        screenBinding.btnMemorySelection.setOnClickListener {
            startImagePicker()
            screenBinding.CLBottomPhotoSelect.visibility = View.GONE
        }

        screenBinding.btnCameraSelection.setOnClickListener {
            requestCameraPermission()
            screenBinding.CLBottomPhotoSelect.visibility = View.GONE
        }

        setContentView(screenBinding.root)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                cropView.of(uri)
                    .withAspect(0, 0)
                    .withOutputSize(256, 256)
                    .initialize(this)
            }
        }
        else {
            finish()
        }
    }

    private fun startImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private lateinit var photoUri: Uri

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            photoUri.let { uri ->
                cropView.of(uri)
                    .withAspect(0, 0)
                    .withOutputSize(256, 256)
                    .initialize(this)
            }
        } else {
            finish()
        }
    }

    private fun dispatchTakePictureIntent() {
        photoUri = createTempPictureUri("language_app.provider")
        takePictureLauncher.launch(photoUri)
    }

    private suspend fun setNewImageUrl(id: String, url: String) {
        supabaseClient.from("Users_Information").update(
            {
                set("user_photo_url", url)
            }
        ) {
            filter {
                eq("id", id)
            }
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            finish()
        }
    }

    private fun Context.createTempPictureUri(
        authority: String,
        fileName: String = "picture_${System.currentTimeMillis()}",
        fileExtension: String = ".png"
    ): Uri {
        val tempFile = File.createTempFile(
            fileName, fileExtension, cacheDir
        ).apply {
            createNewFile()
        }

        return FileProvider.getUriForFile(applicationContext, authority, tempFile)
    }

}