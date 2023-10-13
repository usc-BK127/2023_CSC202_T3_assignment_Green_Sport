package com.example.greenspot.ui.fragments.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenspot.R
import com.example.greenspot.repository.SelectSpot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        var location = LatLng(-34.0, 151.0)
        var title = "Marker in Sydney"

        if(SelectSpot.gSpot != null){
            location = LatLng(SelectSpot.gSpot!!.lat, SelectSpot.gSpot!!.lon)
            title = SelectSpot.gSpot!!.title
        }
        googleMap.addMarker(MarkerOptions().position(location).title(title))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location, 12.0f
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}