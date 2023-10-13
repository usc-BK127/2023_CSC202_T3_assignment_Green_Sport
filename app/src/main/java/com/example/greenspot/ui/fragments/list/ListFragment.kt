package com.example.greenspot.ui.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.greenspot.R
import com.example.greenspot.databinding.FragmentListBinding
import com.example.greenspot.adapter.ListAdapter
import com.example.greenspot.model.GSpot
import com.example.greenspot.repository.SelectSpot
import com.example.greenspot.viewmodel.GSpotViewModel

class ListFragment: Fragment(R.layout.fragment_list) {

    private val gSpotViewModel: GSpotViewModel by viewModels()
    private lateinit var binding: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        //        Recyclerview
        val adapter = ListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter


        gSpotViewModel.readAllGSpot.observe(viewLifecycleOwner) { gSpot ->
            adapter.setData(gSpot)
        }

        adapter.btnEditClick = {
            SelectSpot.gSpot = it
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_icon){
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
            //deleteAllNotes()
        } else if(item.itemId == R.id.info_icon){
            findNavController().navigate(R.id.action_listFragment_to_webViewFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            gSpotViewModel.deleteAllNotes()
            Toast.makeText(
                requireContext(),
                "Successfully removed Everything",
                Toast.LENGTH_SHORT
            )
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete Everything?")
        builder
            .setMessage("Are you sure you want to delete Everything")
        builder.create().show()
    }
}