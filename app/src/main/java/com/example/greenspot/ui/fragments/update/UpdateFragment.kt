package com.example.greenspot.ui.fragments.update


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.textclassifier.SelectionEvent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.greenspot.R
import com.example.greenspot.databinding.FragmentUpdateBinding
import com.example.greenspot.repository.SelectSpot
import com.example.greenspot.viewmodel.GSpotViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.max
import kotlin.math.min


class UpdateFragment : Fragment(R.layout.fragment_update) {

    private val gSpotViewModel: GSpotViewModel by viewModels()
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f

    // Zooming in and out in a bounded range
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))
            binding.inLayoutImageView.ivZoomImage.scaleX = mScaleFactor
            binding.inLayoutImageView.ivZoomImage.scaleY = mScaleFactor
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)
        mScaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        init()
        actions()
    }

    private fun init(){
        if(SelectSpot.gSpot != null){
            binding.apply {
                etTitle.setText(SelectSpot.gSpot!!.title)
                etPlace.setText(SelectSpot.gSpot!!.place)
                etDate.setText(SelectSpot.gSpot!!.date)
                etLocation.setText(SelectSpot.gSpot!!.location)
                ivImage.setImageURI(SelectSpot.gSpot!!.image.toUri())
                inLayoutImageView.ivZoomImage.setImageURI(SelectSpot.gSpot!!.image.toUri())
            }
        } else{
            Toast.makeText(requireContext(), "Data is not available!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateGSpot(){
        SelectSpot.gSpot!!.title = binding.etTitle.text.toString()
        SelectSpot.gSpot!!.place = binding.etPlace.text.toString()

        if (inputCheck(binding.etTitle.text.toString(), binding.etPlace.text.toString())){
            gSpotViewModel.updateNote(SelectSpot.gSpot!!)
            Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        else{
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }


    private fun inputCheck(title: String, content: String): Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))
    }

    private fun deleteSpot() {
        gSpotViewModel.deleteSpot(SelectSpot.gSpot!!)
        Toast.makeText(requireContext(), "Delete the spot Successfully", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun actions(){
        binding.apply {

            buttonUpdate.setOnClickListener {
                updateGSpot()
            }

            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alert")
                    .setMessage("Do you want to delete this spot")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes") { _, _ ->
                        deleteSpot()
                    }
                    .show()
            }

            ivImage.setOnClickListener {
                inLayoutImageView.layoutImageView.visibility = View.VISIBLE
                btnDelete.visibility = View.GONE
                btnShare.visibility = View.GONE
                buttonUpdate.visibility = View.GONE
            }

            inLayoutImageView.btnClose.setOnClickListener {
                inLayoutImageView.layoutImageView.visibility = View.GONE
                btnDelete.visibility = View.VISIBLE
                btnShare.visibility = View.VISIBLE
                buttonUpdate.visibility = View.VISIBLE
            }

            btnLocation.setOnClickListener {
                findNavController().navigate(R.id.action_updateFragment_to_mapsFragment)
            }

            btnShare.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Spot Name: " + SelectSpot.gSpot!!.title + "\nLocation: " + SelectSpot.gSpot!!.place + "\nCollection Date: " + SelectSpot.gSpot!!.date)
                    type = "text/plain"
                }

                try {
                    startActivity(sendIntent)
                } catch (e: ActivityNotFoundException) {
                    // Define what your app should do if no activity can handle the intent.
                }
            }

            inLayoutImageView.ivZoomImage.setOnTouchListener { _, event ->
                mScaleGestureDetector.onTouchEvent(event)
                true
            }

        }
    }

}