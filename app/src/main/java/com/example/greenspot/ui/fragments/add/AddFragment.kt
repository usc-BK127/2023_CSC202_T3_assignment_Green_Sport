package com.example.greenspot.ui.fragments.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.greenspot.R
import com.example.greenspot.databinding.FragmentAddBinding
import com.example.greenspot.model.GSpot
import com.example.greenspot.viewmodel.GSpotViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val gSpotViewModel: GSpotViewModel by viewModels()
    private var imagePath = ""
    private var lat = 0.0
    private var lon = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)


        actions()
    }
    private  fun addSpotToDatabase() {
        val title = binding.etTitle.text.toString()
        val place = binding.etPlace.text.toString()
        val date = binding.etDate.text.toString()
        val location = binding.etLocation.text.toString()
        val currentTimestamp = System.currentTimeMillis()

        val gSpot = GSpot(currentTimestamp.toInt(), title, place, date, location, lat, lon, imagePath, "0" )
        gSpotViewModel.addGSpot(gSpot)
        Toast.makeText(requireContext(), "Added $title to Green Spot",Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }

    private fun inputCheck(title: String, content: String): Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun actions(){
        binding.apply {

            ivDate.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.addOnPositiveButtonClickListener {
                    val date = Date(it)
                    val myFormat = "dd MMM yyyy"
                    val sdf = SimpleDateFormat(myFormat)
                    etDate.setText(sdf.format(date))
                }

                datePicker.show(parentFragmentManager, "Date")
            }

            // This is the Image upload action
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                -> {
                    btnUpload.setOnClickListener {
                        selectImageRequest.launch(cropImageCameraOptions)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            // This is the Location taking
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                -> {
                    btnLocation.setOnClickListener {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location : Location? ->
                                lat = location?.latitude ?: 0.0
                                lon = location?.longitude ?: 0.0
                                etLocation.setText("Lat: $lat Lon: $lon")
                                Log.d("Location: ", "Lat: $lat Lon: $lon")
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Location is not available in this time!", Toast.LENGTH_SHORT).show()
                                Log.d("Location Error: ", fusedLocationClient.lastLocation.exception.toString())
                            }

                    }
                }
                else -> {
                    requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            btnAddNew.setOnClickListener {
                if(checking()){
                    addSpotToDatabase()
                }
            }
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(requireContext(), "Camera permission is granted",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Camera permission is not granted",Toast.LENGTH_LONG).show()
            }
        }

    private val requestPermissionLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(requireContext(), "Location permission is granted",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Location permission is not granted",Toast.LENGTH_LONG).show()
            }
        }


    /**
     * Image filtering and image capture event
     * those function are working with images
     */
    private val selectImageRequest = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            it.uriContent?.let { it2 -> handleImage(it2) }
        }
    }

    private fun handleImage(uri: Uri) {
        binding.ivImage.setImageURI(uri)
        imagePath = uri.toString()
        Log.d("Image Path: ", imagePath)
    }

    private val cropImageCameraOptions = CropImageContractOptions(
        null,
        CropImageOptions().apply {
            imageSourceIncludeCamera = true
            imageSourceIncludeGallery = false
        })
    //content://com.example.greenspot.cropper.fileprovider/my_images/Pictures/cropped5746722642850833574.jpg
    //content://com.example.greenspot.cropper.fileprovider/my_images/Pictures/cropped2854039891274765770.jpg


    private fun checking(): Boolean {
        binding.apply {
            if (etTitle.text.isNullOrEmpty()) {
                etTitle.error = "Title is not empty"
                etTitle.requestFocus()
            } else if (etPlace.text.isNullOrEmpty()) {
                etPlace.error = "Place is not empty"
                etPlace.requestFocus()
            } else if (etLocation.text.isNullOrEmpty()) {
                etLocation.error = "Location is not empty"
                etLocation.requestFocus()
            } else if (etDate.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Date is not empty", Toast.LENGTH_SHORT).show()
                etDate.requestFocus()
            }  else {
                if (imagePath == "") {
                    Toast.makeText(requireContext(), "Image is not empty", Toast.LENGTH_SHORT).show()
                } else {
                    return true
                }

            }
        }
        return false
    }

}