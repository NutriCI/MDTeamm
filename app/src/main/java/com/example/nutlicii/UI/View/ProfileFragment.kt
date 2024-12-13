package com.example.nutlicii.UI.View

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nutlicii.R
import com.example.nutlicii.data.Repository.UserProfileRepository
import com.example.nutlicii.data.ViewModel.AuthViewModel
import com.example.nutlicii.data.ViewModel.AuthViewModelFactory
import com.example.nutlicii.data.ViewModel.UserProfileViewModel
import com.example.nutlicii.data.ViewModel.UserProfileViewModelFactory
import com.example.nutlicii.data.model.UserProfile
import com.example.nutlicii.data.repository.AuthRepository
import data.Remote.NutliciiBaseApi
import data.local.dao.UserDao
import data.local.db.AppDatabase
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var profilePicture: ImageView
    private lateinit var editProfileIcon: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var activitySpinner: Spinner
    private lateinit var ageEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    private lateinit var genderAdapter: ArrayAdapter<CharSequence>
    private lateinit var activityAdapter: ArrayAdapter<CharSequence>
    private var selectedProfileImageFile: File? = null
    private val REQUEST_GALLERY = 1
    private val REQUEST_CAMERA = 2
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        val database = AppDatabase.getDatabase(requireContext())
        val apiService = NutliciiBaseApi.getApiService()
        val authRepository = AuthRepository(apiService, database)

        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        val userDao = database.userDao()
        val userProfileRepository = UserProfileRepository(apiService, userDao)
        val userProfileViewModelFactory = UserProfileViewModelFactory(userProfileRepository)
        userProfileViewModel = ViewModelProvider(this, userProfileViewModelFactory).get(UserProfileViewModel::class.java)

        profilePicture = view.findViewById(R.id.profilePicture)
        editProfileIcon = view.findViewById(R.id.editProfileIcon)
        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        genderSpinner = view.findViewById(R.id.genderSpinner)
        activitySpinner = view.findViewById(R.id.activitySpinner)
        ageEditText = view.findViewById(R.id.ageEditText)
        heightEditText = view.findViewById(R.id.heightEditText)
        weightEditText = view.findViewById(R.id.weightEditText)
        saveButton = view.findViewById(R.id.saveButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        // Load user data and populate the profile
        loadUserProfile(userDao)
        genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_array, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        genderSpinner.adapter = genderAdapter

        activityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_array, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        activitySpinner.adapter = activityAdapter

        userProfileViewModel.userProfile.observe(viewLifecycleOwner, { profile ->
            profile?.let {
                populateUserProfile(it)
            } ?: run {
                Toast.makeText(context, "Update Profile Dulu gak sih?", Toast.LENGTH_SHORT).show()
            }
        })

        userProfileViewModel.loadUserProfile()
        saveButton.setOnClickListener {
            selectedProfileImageFile?.let { imageFile ->
                userProfileViewModel.uploadProfilePicture(imageFile)
            }

            val updatedProfile = getUserProfileFromInput()
            userProfileViewModel.updateUserProfile(updatedProfile)

            userProfileViewModel.updateProfileSuccess.observe(viewLifecycleOwner, { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Yeah,profile berhasil di update!!!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "hemmm ada yang salah nih", Toast.LENGTH_SHORT).show()
                }
            })
            userProfileViewModel.uploadProfilePictureSuccess.observe(viewLifecycleOwner, { success ->
                if (success) {
                    Toast.makeText(requireContext(), "berhasil update foto kamu!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "hmmmm,ada yang salah nihhh", Toast.LENGTH_SHORT).show()
                }
            })
        }
        logoutButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val user = userDao.getUser()
                val name = user?.name ?: "Guest"
                authViewModel.Logout(name)
                val sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        profilePicture.setOnClickListener {
            checkStoragePermission()
        }
        return view
    }
    private fun populateUserProfile(profile: UserProfile) {
        ageEditText.setText(profile.age.toString())
        heightEditText.setText(profile.height.toString())
        weightEditText.setText(profile.weight.toString())
        genderSpinner.setSelection(genderAdapter.getPosition(profile.gender))
        nameEditText.setText(profile.name)
        emailEditText.setText(profile.email)
        Glide.with(this)
            .load(profile.photoUrl)
            .placeholder(R.drawable.ic_profile_placeholder)
            .error(R.drawable.ic_profile_placeholder)
            .into(profilePicture)
    }

    private fun getUserProfileFromInput(): UserProfile {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val age = ageEditText.text.toString().toIntOrNull() ?: 0
        val height = heightEditText.text.toString().toIntOrNull() ?: 0
        val weight = weightEditText.text.toString().toIntOrNull() ?: 0
        val gender = genderSpinner.selectedItem.toString()
        return UserProfile(
            email = email,
            name = name,
            age = age,
            gender = gender,
            height = height,
            weight = weight
        )
    }
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            showImageSourceDialog()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageSourceDialog()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("mau selfie?", "Pilih dari Galeri?")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, android.os.Environment.DIRECTORY_PICTURES)
                }
            }
            val uri: Uri? = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(takePictureIntent, REQUEST_CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    data?.data?.let { uri ->
                        profilePicture.setImageURI(uri)
                        val filePath = getRealPathFromURI(uri)
                        selectedProfileImageFile = filePath?.let { File(it) }
                    }
                }
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    profilePicture.setImageBitmap(bitmap)
                }
            }
        }
    }
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        return cursor?.let {
            it.moveToFirst()
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = it.getString(columnIndex)
            it.close()
            filePath
        }
    }
    private fun loadUserProfile(userDao: UserDao) {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = userDao.getUser()
            user?.let {
                nameEditText.setText(it.name)
                emailEditText.setText(it.email)
            } ?: run {
                Toast.makeText(context, "User not found, please login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
