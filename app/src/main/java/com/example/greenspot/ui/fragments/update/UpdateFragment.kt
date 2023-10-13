package com.example.greenspot.ui.fragments.update


import android.os.Bundle
import android.text.TextUtils
import android.view.View
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


class UpdateFragment : Fragment(R.layout.fragment_update) {

    private val gSpotViewModel: GSpotViewModel by viewModels()
    private lateinit var binding: FragmentUpdateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)

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
            }

            inLayoutImageView.btnClose.setOnClickListener {
                inLayoutImageView.layoutImageView.visibility = View.GONE
            }

            btnLocation.setOnClickListener {
                findNavController().navigate(R.id.action_updateFragment_to_mapsFragment)
            }
        }
    }

}