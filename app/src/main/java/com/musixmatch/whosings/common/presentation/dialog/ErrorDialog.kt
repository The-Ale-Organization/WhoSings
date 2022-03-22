package com.musixmatch.whosings.common.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.musixmatch.whosings.R
import com.musixmatch.whosings.databinding.ErrorDialogBinding

class ErrorDialog: DialogFragment() {

    private var _binding: ErrorDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ErrorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            dismiss()
        }
        binding.confirmButton.text = getString(R.string.generic_error_accept)
        binding.descriptionTextView.text = getString(R.string.generic_error_description)
        binding.titleTextView.text = getString(R.string.generic_error_title)

    }
}