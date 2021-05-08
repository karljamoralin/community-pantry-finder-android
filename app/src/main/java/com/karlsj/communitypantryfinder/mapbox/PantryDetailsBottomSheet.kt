package com.karlsj.communitypantryfinder.mapbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karlsj.communitypantryfinder.databinding.BottomSheetPantryDetailsBinding

class PantryDetailsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPantryDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPantryDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        (arguments?.get(ARG_PANTRY) as Pantry).let { pantry ->
            binding.apply {
                name.text = pantry.name
                streetAddress.text = pantry.streetAddress
                barangay.text = pantry.barangay
                city.text = pantry.city
                province.text = pantry.province
                region.text = pantry.region
                availableSupplies.text = pantry.supplies
                otherDetails.text = pantry.more
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_PANTRY = "pantry"
    }
}